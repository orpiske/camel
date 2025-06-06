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
package org.apache.camel.component.kubernetes.producer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.api.model.ServiceAccountListBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.apache.camel.BindToRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kubernetes.KubernetesConstants;
import org.apache.camel.component.kubernetes.KubernetesTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableKubernetesMockClient
public class KubernetesServiceAccountsProducerTest extends KubernetesTestSupport {

    KubernetesMockServer server;
    NamespacedKubernetesClient client;

    @BindToRegistry("kubernetesClient")
    public KubernetesClient getClient() {
        return client;
    }

    @Test
    void listTest() {
        server.expect().withPath("/api/v1/serviceaccounts")
                .andReturn(200,
                        new ServiceAccountListBuilder().addNewItem().and().addNewItem().and().addNewItem().and().build())
                .once();
        server.expect().withPath("/api/v1/namespaces/test/serviceaccounts")
                .andReturn(200, new ServiceAccountListBuilder().addNewItem().and().addNewItem().and().build())
                .once();
        List<?> result = template.requestBody("direct:list", "", List.class);
        assertEquals(3, result.size());

        Exchange ex = template.request("direct:list",
                exchange -> exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "test"));
        assertEquals(2, ex.getMessage().getBody(List.class).size());
    }

    @Test
    void listByLabelsTest() throws Exception {
        Map<String, String> labels = Map.of(
                "key1", "value1",
                "key2", "value2");

        String urlEncodedLabels = toUrlEncoded(labels.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(",")));

        server.expect().withPath("/api/v1/serviceaccounts?labelSelector=" + urlEncodedLabels)
                .andReturn(200,
                        new ServiceAccountListBuilder().addNewItem().and().addNewItem().and().addNewItem().and().build())
                .once();
        server.expect().withPath("/api/v1/namespaces/test/serviceaccounts?labelSelector=" + urlEncodedLabels)
                .andReturn(200, new ServiceAccountListBuilder().addNewItem().and().addNewItem().and().build())
                .once();
        Exchange ex = template.request("direct:listByLabels",
                exchange -> exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNTS_LABELS, labels));

        assertEquals(3, ex.getMessage().getBody(List.class).size());

        ex = template.request("direct:listByLabels", exchange -> {
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNTS_LABELS, labels);
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "test");
        });

        assertEquals(2, ex.getMessage().getBody(List.class).size());
    }

    @Test
    void createServiceAccount() {
        ServiceAccount sa1 = new ServiceAccountBuilder().withNewMetadata().withName("sa1").withNamespace("test").and().build();
        server.expect().post().withPath("/api/v1/namespaces/test/serviceaccounts").andReturn(200, sa1).once();

        Exchange ex = template.request("direct:create", exchange -> {
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "test");
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNT, sa1);
        });

        ServiceAccount result = ex.getMessage().getBody(ServiceAccount.class);

        assertEquals("test", result.getMetadata().getNamespace());
        assertEquals("sa1", result.getMetadata().getName());
    }

    @Test
    void updateServiceAccount() {
        ServiceAccount sa1 = new ServiceAccountBuilder().withNewMetadata().withName("sa1").withNamespace("test").and().build();
        server.expect().get().withPath("/api/v1/namespaces/test/serviceaccounts/sa1").andReturn(200, sa1).once();
        server.expect().put().withPath("/api/v1/namespaces/test/serviceaccounts/sa1").andReturn(200, sa1).once();

        Exchange ex = template.request("direct:update", exchange -> {
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "test");
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNT, sa1);
        });

        ServiceAccount result = ex.getMessage().getBody(ServiceAccount.class);

        assertEquals("test", result.getMetadata().getNamespace());
        assertEquals("sa1", result.getMetadata().getName());
    }

    @Test
    void deleteServiceAccount() {
        ServiceAccount sa1 = new ServiceAccountBuilder().withNewMetadata().withName("sa1").withNamespace("test").and().build();

        server.expect().withPath("/api/v1/namespaces/test/serviceaccounts/sa1").andReturn(200, sa1).once();
        Exchange ex = template.request("direct:delete", exchange -> {
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "test");
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNT_NAME, "sa1");
        });

        boolean secDeleted = ex.getMessage().getBody(Boolean.class);

        assertTrue(secDeleted);
    }

    @Test
    public void getServiceAccount() {
        final String name = "sa1";
        final String ns = "test";
        ServiceAccount sa = new ServiceAccountBuilder().withNewMetadata().withName(name).withNamespace(ns).and().build();
        server.expect().get().withPath("/api/v1/namespaces/" + ns + "/serviceaccounts/" + name).andReturn(200, sa).once();

        Exchange ex = template.request("direct:get", exchange -> {
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, ns);
            exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_SERVICE_ACCOUNT_NAME, name);
        });

        ServiceAccount result = ex.getMessage().getBody(ServiceAccount.class);

        assertEquals(ns, result.getMetadata().getNamespace());
        assertEquals(name, result.getMetadata().getName());
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:list")
                        .to("kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=listServiceAccounts");
                from("direct:listByLabels").to(
                        "kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=listServiceAccountsByLabels");
                from("direct:create").to(
                        "kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=createServiceAccount");
                from("direct:update").to(
                        "kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=updateServiceAccount");
                from("direct:delete").to(
                        "kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=deleteServiceAccount");
                from("direct:get")
                        .to("kubernetes-service-accounts:///?kubernetesClient=#kubernetesClient&operation=getServiceAccount");
            }
        };
    }
}
