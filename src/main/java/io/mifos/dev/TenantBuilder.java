package io.mifos.dev;

import io.mifos.provisioner.api.v1.domain.CassandraConnectionInfo;
import io.mifos.provisioner.api.v1.domain.DatabaseConnectionInfo;
import io.mifos.provisioner.api.v1.domain.Tenant;

class TenantBuilder {


  private TenantBuilder() {
    super();
  }

  static Tenant create(final String identifier, final String name, final String databaseName) {
    final Tenant tenant = new Tenant();
    tenant.setIdentifier(identifier);
    tenant.setName(name);
    tenant.setDescription("All in one Demo Server");

    final DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();

    databaseConnectionInfo.setDriverClass("org.mariadb.jdbc.Driver");
    databaseConnectionInfo.setDatabaseName(databaseName);
    databaseConnectionInfo.setHost("localhost");
    databaseConnectionInfo.setPort("3306");
    databaseConnectionInfo.setUser("root");
    databaseConnectionInfo.setPassword("mysql");
    tenant.setDatabaseConnectionInfo(databaseConnectionInfo);

    final CassandraConnectionInfo cassandraConnectionInfo = new CassandraConnectionInfo();
    cassandraConnectionInfo.setClusterName("Test Cluster");
    cassandraConnectionInfo.setContactPoints("127.0.0.1:9142");
    cassandraConnectionInfo.setKeyspace(databaseName);
    cassandraConnectionInfo.setReplicas("3");
    cassandraConnectionInfo.setReplicationType("Simple");
    tenant.setCassandraConnectionInfo(cassandraConnectionInfo);
    return tenant;
  }
}
