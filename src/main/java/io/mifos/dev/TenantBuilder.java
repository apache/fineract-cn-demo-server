/*
 * Copyright 2017 The Mifos Initiative.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.dev;

import io.mifos.core.cassandra.util.CassandraConnectorConstants;
import io.mifos.core.mariadb.util.MariaDBConstants;
import io.mifos.core.test.env.TestEnvironment;
import io.mifos.provisioner.api.v1.domain.CassandraConnectionInfo;
import io.mifos.provisioner.api.v1.domain.DatabaseConnectionInfo;
import io.mifos.provisioner.api.v1.domain.Tenant;

class TenantBuilder {


  private TenantBuilder() {
    super();
  }

  static Tenant create(final TestEnvironment testEnvironment, final String identifier, final String name, final String databaseName) {
    final Tenant tenant = new Tenant();
    tenant.setIdentifier(identifier);
    tenant.setName(name);
    tenant.setDescription("All in one Demo Server");

    final DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();

    databaseConnectionInfo.setDriverClass("org.mariadb.jdbc.Driver");
    databaseConnectionInfo.setDatabaseName(databaseName);
    databaseConnectionInfo.setHost(testEnvironment.getProperty(MariaDBConstants.MARIADB_HOST_PROP));
    databaseConnectionInfo.setPort(testEnvironment.getProperty(MariaDBConstants.MARIADB_PORT_PROP));
    databaseConnectionInfo.setUser(testEnvironment.getProperty(MariaDBConstants.MARIADB_USER_PROP));
    databaseConnectionInfo.setPassword(testEnvironment.getProperty(MariaDBConstants.MARIADB_PASSWORD_PROP));
    tenant.setDatabaseConnectionInfo(databaseConnectionInfo);

    final CassandraConnectionInfo cassandraConnectionInfo = new CassandraConnectionInfo();
    cassandraConnectionInfo.setClusterName(CassandraConnectorConstants.CLUSTER_NAME_PROP);
    cassandraConnectionInfo.setContactPoints(testEnvironment.getProperty(CassandraConnectorConstants.CONTACT_POINTS_PROP));
    cassandraConnectionInfo.setKeyspace(databaseName);
    cassandraConnectionInfo.setReplicas("3");
    cassandraConnectionInfo.setReplicationType("Simple");
    tenant.setCassandraConnectionInfo(cassandraConnectionInfo);
    return tenant;
  }
}
