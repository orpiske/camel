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

package org.apache.camel.component.google.pubsub;

import java.util.concurrent.TimeUnit;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.camel.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GoogleResourceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleResourceUtils.class);

    private GoogleResourceUtils() {
    }

    private static TransportChannelProvider newTransportChannelProviderForEndpoint(String endpoint) {
        LOG.warn("Creating a new custom channel provider");
        ManagedChannel channel = ManagedChannelBuilder.forTarget(endpoint).usePlaintext().build();

        return FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
    }

    public static TransportChannelProvider getChannelProvider(Endpoint endpoint) {
        return getChannelProvider((GooglePubsubEndpoint) endpoint);
    }

    private static TransportChannelProvider getChannelProvider(GooglePubsubEndpoint endpoint) {
        TransportChannelProvider channelProvider = null;

        if (endpoint.getComponent().requiresCustomTransportChannel()) {
            channelProvider = newTransportChannelProviderForEndpoint(endpoint.getComponent().getEndpoint());
        }

        return channelProvider;
    }

    public static void closeChannelProvider(TransportChannelProvider channelProvider) {
        if (channelProvider != null) {
            try {
                TransportChannel transportChannel = channelProvider.getTransportChannel();

                if (transportChannel != null) {
                    transportChannel.shutdown();

                    if (!transportChannel.awaitTermination(5, TimeUnit.SECONDS)) {

                        LOG.warn("Timed out waiting for the transport channel to shutdown");
                    }

                    LOG.warn("Closing the custom channel provider");
                    transportChannel.close();
                }
            } catch (Exception e) {
                LOG.warn("Error while closing the transport channel: {}", e.getMessage());
            }
        }
    }
}
