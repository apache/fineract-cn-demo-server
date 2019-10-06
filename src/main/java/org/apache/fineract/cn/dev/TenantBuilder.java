/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cn.dev;

import org.apache.fineract.cn.cassandra.util.CassandraConnectorConstants;
import org.apache.fineract.cn.postgresql.util.PostgreSQLConstants;
import org.apache.fineract.cn.test.env.TestEnvironment;
import org.apache.fineract.cn.provisioner.api.v1.domain.CassandraConnectionInfo;
import org.apache.fineract.cn.provisioner.api.v1.domain.DatabaseConnectionInfo;
import org.apache.fineract.cn.provisioner.api.v1.domain.Tenant;

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

    databaseConnectionInfo.setDriverClass(PostgreSQLConstants.POSTGRESQL_DRIVER_CLASS_DEFAULT);
    databaseConnectionInfo.setDatabaseName(databaseName);
    databaseConnectionInfo.setHost(testEnvironment.getProperty(PostgreSQLConstants.POSTGRESQL_HOST_PROP));
    databaseConnectionInfo.setPort(testEnvironment.getProperty(PostgreSQLConstants.POSTGRESQL_PORT_PROP));
    databaseConnectionInfo.setUser(testEnvironment.getProperty(PostgreSQLConstants.POSTGRESQL_USER_PROP));
    databaseConnectionInfo.setPassword(testEnvironment.getProperty(PostgreSQLConstants.POSTGRESQL_PASSWORD_PROP));
    tenant.setDatabaseConnectionInfo(databaseConnectionInfo);

    final CassandraConnectionInfo cassandraConnectionInfo = new CassandraConnectionInfo();
    cassandraConnectionInfo.setClusterName(testEnvironment.getProperty(CassandraConnectorConstants.CLUSTER_NAME_PROP));
    cassandraConnectionInfo.setContactPoints(testEnvironment.getProperty(CassandraConnectorConstants.CONTACT_POINTS_PROP));
    cassandraConnectionInfo.setKeyspace(databaseName);
    cassandraConnectionInfo.setReplicas("3");
    cassandraConnectionInfo.setReplicationType("Simple");
    tenant.setCassandraConnectionInfo(cassandraConnectionInfo);
    return tenant;
  }
}
