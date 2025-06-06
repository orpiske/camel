/* Generated by camel build tools - do NOT edit this file! */
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
package org.apache.camel.builder.endpoint.dsl;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import javax.annotation.processing.Generated;
import org.apache.camel.builder.EndpointConsumerBuilder;
import org.apache.camel.builder.EndpointProducerBuilder;
import org.apache.camel.builder.endpoint.AbstractEndpointBuilder;

/**
 * Extends the mock component by pulling messages from another endpoint on
 * startup to set the expected message bodies.
 * 
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.EndpointDslMojo")
public interface DataSetTestEndpointBuilderFactory {

    /**
     * Builder for endpoint for the DataSet Test component.
     */
    public interface DataSetTestEndpointBuilder
            extends
                EndpointProducerBuilder {
        default AdvancedDataSetTestEndpointBuilder advanced() {
            return (AdvancedDataSetTestEndpointBuilder) this;
        }

        /**
         * Whether the expected messages should arrive in the same order or can
         * be in any order.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param anyOrder the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder anyOrder(boolean anyOrder) {
            doSetProperty("anyOrder", anyOrder);
            return this;
        }
        /**
         * Whether the expected messages should arrive in the same order or can
         * be in any order.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param anyOrder the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder anyOrder(String anyOrder) {
            doSetProperty("anyOrder", anyOrder);
            return this;
        }
        /**
         * Sets a grace period after which the mock will re-assert to ensure the
         * preliminary assertion is still valid. This is used, for example, to
         * assert that exactly a number of messages arrive. For example, if the
         * expected count was set to 5, then the assertion is satisfied when
         * five or more messages arrive. To ensure that exactly 5 messages
         * arrive, then you would need to wait a little period to ensure no
         * further message arrives. This is what you can use this method for. By
         * default, this period is disabled.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Group: producer
         * 
         * @param assertPeriod the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder assertPeriod(long assertPeriod) {
            doSetProperty("assertPeriod", assertPeriod);
            return this;
        }
        /**
         * Sets a grace period after which the mock will re-assert to ensure the
         * preliminary assertion is still valid. This is used, for example, to
         * assert that exactly a number of messages arrive. For example, if the
         * expected count was set to 5, then the assertion is satisfied when
         * five or more messages arrive. To ensure that exactly 5 messages
         * arrive, then you would need to wait a little period to ensure no
         * further message arrives. This is what you can use this method for. By
         * default, this period is disabled.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Group: producer
         * 
         * @param assertPeriod the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder assertPeriod(String assertPeriod) {
            doSetProperty("assertPeriod", assertPeriod);
            return this;
        }
        /**
         * The split delimiter to use when split is enabled. By default the
         * delimiter is new line based. The delimiter can be a regular
         * expression.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: producer
         * 
         * @param delimiter the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder delimiter(String delimiter) {
            doSetProperty("delimiter", delimiter);
            return this;
        }
        /**
         * Specifies the expected number of message exchanges that should be
         * received by this mock. Beware: If you want to expect that 0 messages,
         * then take extra care, as 0 matches when the tests starts, so you need
         * to set a assert period time to let the test run for a while to make
         * sure there are still no messages arrived; for that use the
         * assertPeriod option. If you want to assert that exactly nth message
         * arrives to this mock, then see also the assertPeriod option for
         * further details.
         * 
         * The option is a: <code>int</code> type.
         * 
         * Default: -1
         * Group: producer
         * 
         * @param expectedCount the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder expectedCount(int expectedCount) {
            doSetProperty("expectedCount", expectedCount);
            return this;
        }
        /**
         * Specifies the expected number of message exchanges that should be
         * received by this mock. Beware: If you want to expect that 0 messages,
         * then take extra care, as 0 matches when the tests starts, so you need
         * to set a assert period time to let the test run for a while to make
         * sure there are still no messages arrived; for that use the
         * assertPeriod option. If you want to assert that exactly nth message
         * arrives to this mock, then see also the assertPeriod option for
         * further details.
         * 
         * The option will be converted to a <code>int</code> type.
         * 
         * Default: -1
         * Group: producer
         * 
         * @param expectedCount the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder expectedCount(String expectedCount) {
            doSetProperty("expectedCount", expectedCount);
            return this;
        }
        /**
         * If enabled the messages loaded from the test endpoint will be split
         * using new line delimiters so each line is an expected message. For
         * example to use a file endpoint to load a file where each line is an
         * expected message.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param split the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder split(boolean split) {
            doSetProperty("split", split);
            return this;
        }
        /**
         * If enabled the messages loaded from the test endpoint will be split
         * using new line delimiters so each line is an expected message. For
         * example to use a file endpoint to load a file where each line is an
         * expected message.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param split the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder split(String split) {
            doSetProperty("split", split);
            return this;
        }
        /**
         * The timeout to use when polling for message bodies from the URI.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Default: 2000
         * Group: producer
         * 
         * @param timeout the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder timeout(long timeout) {
            doSetProperty("timeout", timeout);
            return this;
        }
        /**
         * The timeout to use when polling for message bodies from the URI.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Default: 2000
         * Group: producer
         * 
         * @param timeout the value to set
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder timeout(String timeout) {
            doSetProperty("timeout", timeout);
            return this;
        }
    }

    /**
     * Advanced builder for endpoint for the DataSet Test component.
     */
    public interface AdvancedDataSetTestEndpointBuilder
            extends
                EndpointProducerBuilder {
        default DataSetTestEndpointBuilder basic() {
            return (DataSetTestEndpointBuilder) this;
        }

        /**
         * Sets whether to make a deep copy of the incoming Exchange when
         * received at this mock endpoint.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: true
         * Group: producer (advanced)
         * 
         * @param copyOnExchange the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder copyOnExchange(boolean copyOnExchange) {
            doSetProperty("copyOnExchange", copyOnExchange);
            return this;
        }
        /**
         * Sets whether to make a deep copy of the incoming Exchange when
         * received at this mock endpoint.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: true
         * Group: producer (advanced)
         * 
         * @param copyOnExchange the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder copyOnExchange(String copyOnExchange) {
            doSetProperty("copyOnExchange", copyOnExchange);
            return this;
        }
        /**
         * Sets whether assertIsSatisfied() should fail fast at the first
         * detected failed expectation while it may otherwise wait for all
         * expected messages to arrive before performing expectations
         * verifications. Is by default true. Set to false to use behavior as in
         * Camel 2.x.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: true
         * Group: producer (advanced)
         * 
         * @param failFast the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder failFast(boolean failFast) {
            doSetProperty("failFast", failFast);
            return this;
        }
        /**
         * Sets whether assertIsSatisfied() should fail fast at the first
         * detected failed expectation while it may otherwise wait for all
         * expected messages to arrive before performing expectations
         * verifications. Is by default true. Set to false to use behavior as in
         * Camel 2.x.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: true
         * Group: producer (advanced)
         * 
         * @param failFast the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder failFast(String failFast) {
            doSetProperty("failFast", failFast);
            return this;
        }
        /**
         * Whether the producer should be started lazy (on the first message).
         * By starting lazy you can use this to allow CamelContext and routes to
         * startup in situations where a producer may otherwise fail during
         * starting and cause the route to fail being started. By deferring this
         * startup to be lazy then the startup failure can be handled during
         * routing messages via Camel's routing error handlers. Beware that when
         * the first message is processed then creating and starting the
         * producer may take a little time and prolong the total processing time
         * of the processing.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param lazyStartProducer the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder lazyStartProducer(boolean lazyStartProducer) {
            doSetProperty("lazyStartProducer", lazyStartProducer);
            return this;
        }
        /**
         * Whether the producer should be started lazy (on the first message).
         * By starting lazy you can use this to allow CamelContext and routes to
         * startup in situations where a producer may otherwise fail during
         * starting and cause the route to fail being started. By deferring this
         * startup to be lazy then the startup failure can be handled during
         * routing messages via Camel's routing error handlers. Beware that when
         * the first message is processed then creating and starting the
         * producer may take a little time and prolong the total processing time
         * of the processing.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param lazyStartProducer the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder lazyStartProducer(String lazyStartProducer) {
            doSetProperty("lazyStartProducer", lazyStartProducer);
            return this;
        }
        /**
         * To turn on logging when the mock receives an incoming message. This
         * will log only one time at INFO level for the incoming message. For
         * more detailed logging, then set the logger to DEBUG level for the
         * org.apache.camel.component.mock.MockEndpoint class.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param log the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder log(boolean log) {
            doSetProperty("log", log);
            return this;
        }
        /**
         * To turn on logging when the mock receives an incoming message. This
         * will log only one time at INFO level for the incoming message. For
         * more detailed logging, then set the logger to DEBUG level for the
         * org.apache.camel.component.mock.MockEndpoint class.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param log the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder log(String log) {
            doSetProperty("log", log);
            return this;
        }
        /**
         * A number that is used to turn on throughput logging based on groups
         * of the size.
         * 
         * The option is a: <code>int</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param reportGroup the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder reportGroup(int reportGroup) {
            doSetProperty("reportGroup", reportGroup);
            return this;
        }
        /**
         * A number that is used to turn on throughput logging based on groups
         * of the size.
         * 
         * The option will be converted to a <code>int</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param reportGroup the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder reportGroup(String reportGroup) {
            doSetProperty("reportGroup", reportGroup);
            return this;
        }
        /**
         * Sets the minimum expected amount of time the assertIsSatisfied() will
         * wait on a latch until it is satisfied.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param resultMinimumWaitTime the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder resultMinimumWaitTime(long resultMinimumWaitTime) {
            doSetProperty("resultMinimumWaitTime", resultMinimumWaitTime);
            return this;
        }
        /**
         * Sets the minimum expected amount of time the assertIsSatisfied() will
         * wait on a latch until it is satisfied.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param resultMinimumWaitTime the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder resultMinimumWaitTime(String resultMinimumWaitTime) {
            doSetProperty("resultMinimumWaitTime", resultMinimumWaitTime);
            return this;
        }
        /**
         * Sets the maximum amount of time the assertIsSatisfied() will wait on
         * a latch until it is satisfied.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param resultWaitTime the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder resultWaitTime(long resultWaitTime) {
            doSetProperty("resultWaitTime", resultWaitTime);
            return this;
        }
        /**
         * Sets the maximum amount of time the assertIsSatisfied() will wait on
         * a latch until it is satisfied.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param resultWaitTime the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder resultWaitTime(String resultWaitTime) {
            doSetProperty("resultWaitTime", resultWaitTime);
            return this;
        }
        /**
         * Specifies to only retain the first nth number of received Exchanges.
         * This is used when testing with big data, to reduce memory consumption
         * by not storing copies of every Exchange this mock endpoint receives.
         * Important: When using this limitation, then the getReceivedCounter()
         * will still return the actual number of received message. For example
         * if we have received 5000 messages and have configured to only retain
         * the first 10 Exchanges, then the getReceivedCounter() will still
         * return 5000 but there is only the first 10 Exchanges in the
         * getExchanges() and getReceivedExchanges() methods. When using this
         * method, then some of the other expectation methods is not supported,
         * for example the expectedBodiesReceived(Object...) sets a expectation
         * on the first number of bodies received. You can configure both
         * retainFirst and retainLast options, to limit both the first and last
         * received.
         * 
         * The option is a: <code>int</code> type.
         * 
         * Default: -1
         * Group: producer (advanced)
         * 
         * @param retainFirst the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder retainFirst(int retainFirst) {
            doSetProperty("retainFirst", retainFirst);
            return this;
        }
        /**
         * Specifies to only retain the first nth number of received Exchanges.
         * This is used when testing with big data, to reduce memory consumption
         * by not storing copies of every Exchange this mock endpoint receives.
         * Important: When using this limitation, then the getReceivedCounter()
         * will still return the actual number of received message. For example
         * if we have received 5000 messages and have configured to only retain
         * the first 10 Exchanges, then the getReceivedCounter() will still
         * return 5000 but there is only the first 10 Exchanges in the
         * getExchanges() and getReceivedExchanges() methods. When using this
         * method, then some of the other expectation methods is not supported,
         * for example the expectedBodiesReceived(Object...) sets a expectation
         * on the first number of bodies received. You can configure both
         * retainFirst and retainLast options, to limit both the first and last
         * received.
         * 
         * The option will be converted to a <code>int</code> type.
         * 
         * Default: -1
         * Group: producer (advanced)
         * 
         * @param retainFirst the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder retainFirst(String retainFirst) {
            doSetProperty("retainFirst", retainFirst);
            return this;
        }
        /**
         * Specifies to only retain the last nth number of received Exchanges.
         * This is used when testing with big data, to reduce memory consumption
         * by not storing copies of every Exchange this mock endpoint receives.
         * Important: When using this limitation, then the getReceivedCounter()
         * will still return the actual number of received message. For example
         * if we have received 5000 messages and have configured to only retain
         * the last 20 Exchanges, then the getReceivedCounter() will still
         * return 5000 but there is only the last 20 Exchanges in the
         * getExchanges() and getReceivedExchanges() methods. When using this
         * method, then some of the other expectation methods is not supported,
         * for example the expectedBodiesReceived(Object...) sets a expectation
         * on the first number of bodies received. You can configure both
         * retainFirst and retainLast options, to limit both the first and last
         * received.
         * 
         * The option is a: <code>int</code> type.
         * 
         * Default: -1
         * Group: producer (advanced)
         * 
         * @param retainLast the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder retainLast(int retainLast) {
            doSetProperty("retainLast", retainLast);
            return this;
        }
        /**
         * Specifies to only retain the last nth number of received Exchanges.
         * This is used when testing with big data, to reduce memory consumption
         * by not storing copies of every Exchange this mock endpoint receives.
         * Important: When using this limitation, then the getReceivedCounter()
         * will still return the actual number of received message. For example
         * if we have received 5000 messages and have configured to only retain
         * the last 20 Exchanges, then the getReceivedCounter() will still
         * return 5000 but there is only the last 20 Exchanges in the
         * getExchanges() and getReceivedExchanges() methods. When using this
         * method, then some of the other expectation methods is not supported,
         * for example the expectedBodiesReceived(Object...) sets a expectation
         * on the first number of bodies received. You can configure both
         * retainFirst and retainLast options, to limit both the first and last
         * received.
         * 
         * The option will be converted to a <code>int</code> type.
         * 
         * Default: -1
         * Group: producer (advanced)
         * 
         * @param retainLast the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder retainLast(String retainLast) {
            doSetProperty("retainLast", retainLast);
            return this;
        }
        /**
         * Allows a sleep to be specified to wait to check that this mock really
         * is empty when expectedMessageCount(int) is called with zero value.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param sleepForEmptyTest the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder sleepForEmptyTest(long sleepForEmptyTest) {
            doSetProperty("sleepForEmptyTest", sleepForEmptyTest);
            return this;
        }
        /**
         * Allows a sleep to be specified to wait to check that this mock really
         * is empty when expectedMessageCount(int) is called with zero value.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Group: producer (advanced)
         * 
         * @param sleepForEmptyTest the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder sleepForEmptyTest(String sleepForEmptyTest) {
            doSetProperty("sleepForEmptyTest", sleepForEmptyTest);
            return this;
        }
        /**
         * Maximum number of messages to keep in memory available for browsing.
         * Use 0 for unlimited.
         * 
         * The option is a: <code>int</code> type.
         * 
         * Default: 100
         * Group: advanced
         * 
         * @param browseLimit the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder browseLimit(int browseLimit) {
            doSetProperty("browseLimit", browseLimit);
            return this;
        }
        /**
         * Maximum number of messages to keep in memory available for browsing.
         * Use 0 for unlimited.
         * 
         * The option will be converted to a <code>int</code> type.
         * 
         * Default: 100
         * Group: advanced
         * 
         * @param browseLimit the value to set
         * @return the dsl builder
         */
        default AdvancedDataSetTestEndpointBuilder browseLimit(String browseLimit) {
            doSetProperty("browseLimit", browseLimit);
            return this;
        }
    }

    public interface DataSetTestBuilders {
        /**
         * DataSet Test (camel-dataset)
         * Extends the mock component by pulling messages from another endpoint
         * on startup to set the expected message bodies.
         * 
         * Category: core,testing
         * Since: 1.3
         * Maven coordinates: org.apache.camel:camel-dataset
         * 
         * Syntax: <code>dataset-test:name</code>
         * 
         * Path parameter: name (required)
         * Name of endpoint to lookup in the registry to use for polling
         * messages used for testing
         * 
         * @param path name
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder datasetTest(String path) {
            return DataSetTestEndpointBuilderFactory.endpointBuilder("dataset-test", path);
        }
        /**
         * DataSet Test (camel-dataset)
         * Extends the mock component by pulling messages from another endpoint
         * on startup to set the expected message bodies.
         * 
         * Category: core,testing
         * Since: 1.3
         * Maven coordinates: org.apache.camel:camel-dataset
         * 
         * Syntax: <code>dataset-test:name</code>
         * 
         * Path parameter: name (required)
         * Name of endpoint to lookup in the registry to use for polling
         * messages used for testing
         * 
         * @param componentName to use a custom component name for the endpoint
         * instead of the default name
         * @param path name
         * @return the dsl builder
         */
        default DataSetTestEndpointBuilder datasetTest(String componentName, String path) {
            return DataSetTestEndpointBuilderFactory.endpointBuilder(componentName, path);
        }

    }
    static DataSetTestEndpointBuilder endpointBuilder(String componentName, String path) {
        class DataSetTestEndpointBuilderImpl extends AbstractEndpointBuilder implements DataSetTestEndpointBuilder, AdvancedDataSetTestEndpointBuilder {
            public DataSetTestEndpointBuilderImpl(String path) {
                super(componentName, path);
            }
        }
        return new DataSetTestEndpointBuilderImpl(path);
    }
}