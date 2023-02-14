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

package org.apache.camel;

import org.apache.camel.spi.AsyncProcessorAwaitManager;
import org.apache.camel.spi.ExchangeFactory;
import org.apache.camel.spi.HeadersMapFactory;

public interface CamelContextExtension {
    AsyncProcessorAwaitManager getAsyncProcessorAwaitManager();

    /**
     * Gets the {@link HeadersMapFactory} to use.
     */
    HeadersMapFactory getHeadersMapFactory();

    /**
     * Whether exchange event notification is applicable (possible). This API is used internally in Camel as
     * optimization.
     * <p>
     * This is <b>only</b> for exchange events as this allows Camel to optimize to avoid preparing exchange events if
     * there are no event listeners that are listening for exchange events.
     */
    boolean isEventNotificationApplicable();

    /**
     * Parses the given text and resolve any property placeholders - using {{key}}.
     * <p/>
     * <b>Important:</b> If resolving placeholders on an endpoint uri, then you SHOULD use
     * EndpointHelper#resolveEndpointUriPropertyPlaceholders instead.
     *
     * @param text                   the text such as an endpoint uri or the likes
     * @param keepUnresolvedOptional whether to keep placeholders that are optional and was unresolved
     * @return the text with resolved property placeholders
     * @throws IllegalArgumentException is thrown if property placeholders was used and there was an error resolving
     *                                  them
     */
    String resolvePropertyPlaceholders(String text, boolean keepUnresolvedOptional);

    /**
     * Gets the exchange factory to use.
     */
    ExchangeFactory getExchangeFactory();

    /**
     * Sets a custom exchange factory to use.
     */
    void setExchangeFactory(ExchangeFactory exchangeFactory);
}
