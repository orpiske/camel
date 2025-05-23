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

package org.apache.camel.component.pg.replication.slot.integration;

import org.apache.camel.test.infra.postgres.services.PostgresLocalContainerService;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PgReplicationITSupport extends CamelTestSupport {

    @RegisterExtension
    static PostgresLocalContainerService service;

    static {
        PostgreSQLContainer container = new PostgreSQLContainer<>(
                DockerImageName.parse(PostgresLocalContainerService.DEFAULT_POSTGRES_CONTAINER)
                        .asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("camel")
                .withCommand("postgres -c wal_level=logical");

        service = new PostgresLocalContainerService(container);
    }
}
