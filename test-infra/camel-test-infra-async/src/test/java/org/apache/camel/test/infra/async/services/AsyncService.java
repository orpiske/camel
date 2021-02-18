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
package org.apache.camel.test.infra.async.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.camel.test.infra.common.services.TestService;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Test infra service for Async
 */
public class AsyncService<T extends TestService> implements BeforeAllCallback, AfterAllCallback {
    private final T service;
    private Future<T> completableFuture;

    public AsyncService(T service) {
        this.service = service;
    }


    @Override
    public void afterAll(ExtensionContext context) {
        service.shutdown();
    }

    public Future<T> asyncInitialize() throws InterruptedException {
        CompletableFuture ret = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            service.initialize();

            ret.complete(service);

            return service;
        });

        return ret;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        try {
            completableFuture = asyncInitialize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public T getService() {
        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();

            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();

            return null;
        }
    }
}
