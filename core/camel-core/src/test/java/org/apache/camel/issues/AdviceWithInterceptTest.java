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
package org.apache.camel.issues;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class AdviceWithInterceptTest extends ContextTestSupport {

    @Test
    public void testAdviceIntercept() throws Exception {
        getMockEndpoint("mock:advice").expectedMessageCount(1);

        AdviceWith.adviceWith(context.getRouteDefinition("main"), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveAddFirst().to("direct:advice");
            }
        });

        template.sendBody("direct:main", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                intercept().log("Intercept ${body}");

                from("direct:advice").log("Advice ${body}").to("mock:advice");

                from("direct:main").routeId("main").log("Main ${body}");
            }
        };
    }

}
