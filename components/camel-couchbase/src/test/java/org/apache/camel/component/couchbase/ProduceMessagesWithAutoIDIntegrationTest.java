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
package org.apache.camel.component.couchbase;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.infra.common.SharedNameGenerator;
import org.apache.camel.test.infra.common.TestEntityNameGenerator;
import org.apache.camel.test.infra.couchbase.services.CouchbaseService;
import org.apache.camel.test.infra.couchbase.services.CouchbaseServiceFactory;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class ProduceMessagesWithAutoIDIntegrationTest extends CamelTestSupport {

    @RegisterExtension
    public static final CouchbaseService service = CouchbaseServiceFactory.getService();

    @RegisterExtension
    public static final SharedNameGenerator nameGenerator = new TestEntityNameGenerator();

    @Test
    public void testInsert() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(2);

        template.sendBody("direct:start", "ugol1");
        template.sendBody("direct:start", "ugol2");

        assertMockEndpointsSatisfied();

    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        String bucketName = nameGenerator.getName();

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .toF("couchbase:http://%s:%d/mybucket/mybucket?username=%s&password=%s&autoStartIdForInserts=true&startingIdForInsertsFrom=1000",
                                service.getHostname(), service.getPort(), service.getUsername(), service.getPassword())
                        .to("mock:result");
            }
        };
    }
}
