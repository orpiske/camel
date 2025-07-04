/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.util.backoff;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

import org.apache.camel.util.function.ThrowingFunction;

public final class BackOffTimerTask implements BackOffTimer.Task, Runnable {
    private final Lock lock = new ReentrantLock();
    private final BackOffTimer timer;
    private final BackOff backOff;
    private final ScheduledExecutorService scheduler;
    private final ThrowingFunction<BackOffTimer.Task, Boolean, Exception> function;
    private final AtomicReference<ScheduledFuture<?>> futureRef;
    private final List<BiConsumer<BackOffTimer.Task, Throwable>> consumers;

    private Status status;
    private long firstAttemptTime;
    private long currentAttempts;
    private long currentDelay;
    private long currentElapsedTime;
    private long lastAttemptTime;
    private long nextAttemptTime;
    private Throwable cause;

    public BackOffTimerTask(BackOffTimer timer, BackOff backOff, ScheduledExecutorService scheduler,
                            ThrowingFunction<BackOffTimer.Task, Boolean, Exception> function) {
        this.timer = timer;
        this.backOff = backOff;
        this.scheduler = scheduler;
        this.status = Status.Active;

        this.currentAttempts = 0;
        this.currentDelay = backOff.getDelay().toMillis();
        this.currentElapsedTime = 0;
        this.firstAttemptTime = BackOff.NEVER;
        this.lastAttemptTime = BackOff.NEVER;
        this.nextAttemptTime = BackOff.NEVER;

        this.function = function;
        this.consumers = new ArrayList<>();
        this.futureRef = new AtomicReference<>();
    }

    // *****************************
    // Properties
    // *****************************

    @Override
    public String getName() {
        return timer.getName();
    }

    @Override
    public BackOff getBackOff() {
        return backOff;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public long getCurrentAttempts() {
        return currentAttempts;
    }

    @Override
    public long getCurrentDelay() {
        return currentDelay;
    }

    @Override
    public long getCurrentElapsedTime() {
        return currentElapsedTime;
    }

    @Override
    public long getFirstAttemptTime() {
        return firstAttemptTime;
    }

    @Override
    public long getLastAttemptTime() {
        return lastAttemptTime;
    }

    @Override
    public long getNextAttemptTime() {
        return nextAttemptTime;
    }

    @Override
    public Throwable getException() {
        return cause;
    }

    @Override
    public void reset() {
        this.currentAttempts = 0;
        this.currentDelay = 0;
        this.currentElapsedTime = 0;
        this.firstAttemptTime = BackOff.NEVER;
        this.lastAttemptTime = BackOff.NEVER;
        this.nextAttemptTime = BackOff.NEVER;
        this.status = Status.Active;
        this.cause = null;
    }

    @Override
    public void cancel() {
        stop();

        ScheduledFuture<?> future = futureRef.get();
        if (future != null) {
            future.cancel(true);
        }

        // signal task completion on cancel.
        complete(null);

        // the task is cancelled and should not be restarted so remove from timer
        if (timer != null) {
            timer.remove(this);
        }
    }

    @Override
    public void whenComplete(BiConsumer<BackOffTimer.Task, Throwable> whenCompleted) {
        lock.lock();
        try {
            if (backOff.isRemoveOnComplete()) {
                timer.remove(this);
            }
            consumers.add(whenCompleted);
        } finally {
            lock.unlock();
        }
    }

    // *****************************
    // Task execution
    // *****************************

    @Override
    public void run() {
        if (status == Status.Active) {
            try {
                lastAttemptTime = System.currentTimeMillis();
                if (firstAttemptTime < 0) {
                    firstAttemptTime = lastAttemptTime;
                }

                if (function.apply(this)) {
                    long delay = next();
                    if (status != Status.Active) {
                        // if the call to next makes the context not more
                        // active, signal task completion.
                        complete(null);
                    } else {
                        nextAttemptTime = lastAttemptTime + delay;

                        // Cache the scheduled future so it can be cancelled
                        // later by Task.cancel()
                        futureRef.lazySet(scheduler.schedule(this, delay, TimeUnit.MILLISECONDS));
                    }
                } else {
                    stop();

                    status = Status.Completed;
                    // if the function return false no more attempts should
                    // be made so stop the context.
                    complete(null);
                }
            } catch (Exception e) {
                stop();

                status = Status.Failed;
                complete(e);
            }
        }
    }

    void stop() {
        this.currentAttempts = 0;
        this.currentDelay = BackOff.NEVER;
        this.currentElapsedTime = 0;
        this.firstAttemptTime = BackOff.NEVER;
        this.lastAttemptTime = BackOff.NEVER;
        this.nextAttemptTime = BackOff.NEVER;
        this.status = Status.Inactive;
    }

    void complete(Throwable throwable) {
        this.cause = throwable;
        lock.lock();
        try {
            consumers.forEach(c -> c.accept(this, throwable));
        } finally {
            lock.unlock();
        }
    }

    // *****************************
    // Impl
    // *****************************

    /**
     * Return the number of milliseconds to wait before retrying the operation or ${@link BackOff#NEVER} to indicate
     * that no further attempt should be made.
     */
    public long next() {
        // A call to next when currentDelay is set to NEVER has no effects
        // as this means that either the timer is exhausted or it has explicit
        // stopped
        if (status == Status.Active) {

            currentAttempts++;

            if (currentAttempts > backOff.getMaxAttempts()) {
                currentDelay = BackOff.NEVER;
                status = Status.Exhausted;
            } else if (currentElapsedTime > backOff.getMaxElapsedTime().toMillis()) {
                currentDelay = BackOff.NEVER;
                status = Status.Exhausted;
            } else {
                if (currentDelay <= backOff.getMaxDelay().toMillis()) {
                    currentDelay = (long) (currentDelay * backOff.getMultiplier());
                }

                currentElapsedTime += currentDelay;
            }
        }

        return currentDelay;
    }

    @Override
    public String toString() {
        return "BackOffTimerTask["
               + "name=" + timer.getName()
               + ", status=" + status
               + ", currentAttempts=" + currentAttempts
               + ", currentDelay=" + currentDelay
               + ", currentElapsedTime=" + currentElapsedTime
               + ", firstAttemptTime=" + firstAttemptTime
               + ", lastAttemptTime=" + lastAttemptTime
               + ", nextAttemptTime=" + nextAttemptTime
               + ']';
    }
}
