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
package org.apache.camel.test.junit5.legacy;

import org.apache.camel.CamelContext;

/**
 * An interface containing all the legacy methods for code willing to rely on their own implementation of the
 * CamelTestSupport as existed on Camel 4.8 and older.
 */
public interface LegacyTestSupport {
    /**
     * Use the RouteBuilder or not
     *
     * @return <tt>true</tt> then {@link CamelContext} will be auto started, <tt>false</tt> then {@link CamelContext}
     *         will <b>not</b> be auto started (you will have to start it manually)
     */
    boolean isUseRouteBuilder();

    /**
     * Disables the JMX agent. Must be called before the setup method.
     */
    void disableJMX();

    /**
     * Enables the JMX agent. Must be called before the setup method.
     */
    void enableJMX();

    /**
     * Whether JMX should be used during testing.
     *
     * @return <tt>false</tt> by default.
     */
    boolean useJmx();
}
