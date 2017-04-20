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
