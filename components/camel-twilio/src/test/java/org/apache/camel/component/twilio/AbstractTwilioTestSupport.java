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
package org.apache.camel.component.twilio;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.support.PropertyBindingSupport;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.junit5.TestSupport;
import org.junit.jupiter.api.TestInstance;

/**
 * Abstract base class for Twilio Integration tests generated by Camel API component maven plugin.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTwilioTestSupport extends CamelTestSupport {

    private static final String TEST_OPTIONS_PROPERTIES = "/test-options.properties";
    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        // read Twilio component configuration from TEST_OPTIONS_PROPERTIES
        TestSupport.loadExternalPropertiesQuietly(properties, AbstractTwilioTestSupport.class, TEST_OPTIONS_PROPERTIES);

    }

    private static boolean hasCredentials() {
        if (properties.isEmpty()) {
            loadProperties();
        }

        return !properties.getProperty("username", "").isEmpty()
                && !properties.getProperty("password", "").isEmpty();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final CamelContext context = super.createCamelContext();

        Map<String, Object> options = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            options.put(entry.getKey().toString(), entry.getValue().toString().isEmpty() ? "value" : entry.getValue());
        }

        // add TwilioComponent to Camel context
        final TwilioComponent component = new TwilioComponent(context);
        PropertyBindingSupport.bindProperties(context, component, options);
        context.addComponent("twilio", component);

        return context;
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers)
            throws CamelExecutionException {
        return (T) template().requestBodyAndHeaders(endpointUri, body, headers);
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBody(String endpoint, Object body) throws CamelExecutionException {
        return (T) template().requestBody(endpoint, body);
    }
}
