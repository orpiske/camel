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

package org.apache.camel.impl.engine;

import org.apache.camel.CamelContextExtension;
import org.apache.camel.spi.AsyncProcessorAwaitManager;
import org.apache.camel.spi.ExchangeFactory;
import org.apache.camel.spi.HeadersMapFactory;

public class ExtendedCamelContextExtension implements CamelContextExtension {
    private final AbstractCamelContext camelContext;

    public ExtendedCamelContextExtension(AbstractCamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public AsyncProcessorAwaitManager getAsyncProcessorAwaitManager() {
        return camelContext.getAsyncProcessorAwaitManager();
    }

    @Override
    public HeadersMapFactory getHeadersMapFactory() {
        return camelContext.getHeadersMapFactory();
    }

    @Override
    public boolean isEventNotificationApplicable() {
        return camelContext.isEventNotificationApplicable();
    }

    @Override
    public String resolvePropertyPlaceholders(String text, boolean keepUnresolvedOptional) {
        return camelContext.resolvePropertyPlaceholders(text, keepUnresolvedOptional);
    }

    @Override
    public ExchangeFactory getExchangeFactory() {
        return camelContext.getExchangeFactory();
    }

    @Override
    public void setExchangeFactory(ExchangeFactory exchangeFactory) {
        camelContext.setExchangeFactory(exchangeFactory);
    }
}
