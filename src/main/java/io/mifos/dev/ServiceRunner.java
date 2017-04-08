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

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import io.mifos.accounting.api.v1.client.LedgerManager;
import io.mifos.anubis.api.v1.domain.Signature;
import io.mifos.core.api.config.EnableApiFactory;
import io.mifos.core.api.context.AutoSeshat;
import io.mifos.core.api.util.ApiConstants;
import io.mifos.core.api.util.ApiFactory;
import io.mifos.core.lang.TenantContextHolder;
import io.mifos.core.lang.security.RsaPublicKeyBuilder;
import io.mifos.core.test.env.TestEnvironment;
import io.mifos.core.test.listener.EventRecorder;
import io.mifos.core.test.servicestarter.ActiveMQForTest;
import io.mifos.core.test.servicestarter.EurekaForTest;
import io.mifos.core.test.servicestarter.IntegrationTestEnvironment;
import io.mifos.core.test.servicestarter.Microservice;
import io.mifos.customer.api.v1.client.CustomerManager;
import io.mifos.identity.api.v1.client.IdentityManager;
import io.mifos.office.api.v1.client.OrganizationManager;
import io.mifos.portfolio.api.v1.client.PortfolioManager;
import io.mifos.provisioner.api.v1.client.Provisioner;
import io.mifos.provisioner.api.v1.domain.Application;
import io.mifos.provisioner.api.v1.domain.AssignedApplication;
import io.mifos.provisioner.api.v1.domain.AuthenticationResponse;
import io.mifos.provisioner.api.v1.domain.CassandraConnectionInfo;
import io.mifos.provisioner.api.v1.domain.DatabaseConnectionInfo;
import io.mifos.provisioner.api.v1.domain.IdentityManagerInitialization;
import io.mifos.provisioner.api.v1.domain.Tenant;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PublicKey;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ServiceRunner {
  private static final String CLIENT_ID = "service-runner";
  private static Microservice<Provisioner> provisionerService;
  private static Microservice<IdentityManager> identityService;
  private static Microservice<OrganizationManager> officeClient;
  private static Microservice<CustomerManager> customerClient;
  private static Microservice<LedgerManager> accountingClient;
  private static Microservice<PortfolioManager> portfolioClient;

  private static DB embeddedMariaDb;

  @Configuration
  @ActiveMQForTest.EnableActiveMQListen
  @EnableApiFactory
  @ComponentScan("io.mifos.dev.listener")
  public static class TestConfiguration {
    public TestConfiguration() {
      super();
    }

    @Bean()
    public Logger logger() {
      return LoggerFactory.getLogger("test-logger");
    }
  }

  @ClassRule
  public static final EurekaForTest EUREKA_FOR_TEST = new EurekaForTest();

  @ClassRule
  public static final ActiveMQForTest ACTIVE_MQ_FOR_TEST = new ActiveMQForTest();

  @ClassRule
  public static final IntegrationTestEnvironment INTEGRATION_TEST_ENVIRONMENT = new IntegrationTestEnvironment("fineract-demo");

  @Autowired
  private ApiFactory apiFactory;

  @Autowired
  private EventRecorder eventRecorder;

  public ServiceRunner() {
    super();
  }

  @Before
  public void before() throws Exception
  {
    // start embedded Cassandra
    EmbeddedCassandraServerHelper.startEmbeddedCassandra(TimeUnit.SECONDS.toMillis(30L));
    // start embedded MariaDB
    ServiceRunner.embeddedMariaDb = DB.newEmbeddedDB(
        DBConfigurationBuilder.newBuilder()
            .setPort(3306)
            .build()
    );
    ServiceRunner.embeddedMariaDb.start();

    ServiceRunner.provisionerService =
        new Microservice<>(Provisioner.class, "provisioner", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    final TestEnvironment provisionerTestEnvironment = provisionerService.getProcessEnvironment();
    provisionerTestEnvironment.addSystemPrivateKeyToProperties();
    provisionerTestEnvironment.setProperty("system.initialclientid", ServiceRunner.CLIENT_ID);
    ServiceRunner.provisionerService.start();
    ServiceRunner.provisionerService.setApiFactory(apiFactory);

    ServiceRunner.identityService =
        new Microservice<>(IdentityManager.class, "identity", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    ServiceRunner.identityService.getProcessEnvironment().setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    ServiceRunner.identityService.start();
    ServiceRunner.identityService.setApiFactory(apiFactory);

    ServiceRunner.officeClient =
        new Microservice<>(OrganizationManager.class, "office", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    ServiceRunner.officeClient.getProcessEnvironment().setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    ServiceRunner.officeClient.start();
    ServiceRunner.officeClient.setApiFactory(apiFactory);

    ServiceRunner.customerClient =
        new Microservice<>(CustomerManager.class, "customer", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    ServiceRunner.customerClient.getProcessEnvironment().setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    ServiceRunner.customerClient.start();
    ServiceRunner.customerClient.setApiFactory(apiFactory);

    ServiceRunner.accountingClient =
        new Microservice<>(LedgerManager.class, "accounting", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    ServiceRunner.accountingClient.getProcessEnvironment().setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    ServiceRunner.accountingClient.start();
    ServiceRunner.accountingClient.setApiFactory(apiFactory);

    ServiceRunner.portfolioClient =
        new Microservice<>(PortfolioManager.class, "portfolio", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
    ServiceRunner.portfolioClient.getProcessEnvironment().setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    ServiceRunner.portfolioClient.start();
    ServiceRunner.portfolioClient.setApiFactory(apiFactory);
  }

  @After
  public void tearDown() throws Exception {
    ServiceRunner.portfolioClient.kill();
    ServiceRunner.accountingClient.kill();
    ServiceRunner.customerClient.kill();
    ServiceRunner.officeClient.kill();
    ServiceRunner.identityService.kill();
    ServiceRunner.provisionerService.kill();

    ServiceRunner.embeddedMariaDb.stop();

    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
  }

  @Test
  public void startDevServer() throws InterruptedException {
    this.provisionAppsViaSeshat();

    System.out.println("Identity Service: " + ServiceRunner.identityService.getProcessEnvironment().serverURI());
    System.out.println("Office Service: " + ServiceRunner.officeClient.getProcessEnvironment().serverURI());
    System.out.println("Customer Service: " + ServiceRunner.customerClient.getProcessEnvironment().serverURI());
    System.out.println("Accounting Service: " + ServiceRunner.accountingClient.getProcessEnvironment().serverURI());
    System.out.println("Portfolio Service: " + ServiceRunner.portfolioClient.getProcessEnvironment().serverURI());

    boolean run = true;

    while (run) {
      final Scanner scanner = new Scanner(System.in);
      final String nextLine = scanner.nextLine();
      if (nextLine != null && nextLine.equals("exit")) {
        run = false;
      }
    }
  }

  public PublicKey getPublicKey() {
    final Signature sig = ServiceRunner.identityService.api().getSignature();

    return new RsaPublicKeyBuilder()
        .setPublicKeyMod(sig.getPublicKeyMod())
        .setPublicKeyExp(sig.getPublicKeyExp())
        .build();
  }

  private void provisionAppsViaSeshat() throws InterruptedException {
    final AuthenticationResponse authenticationResponse =
        ServiceRunner.provisionerService.api().authenticate(ServiceRunner.CLIENT_ID, ApiConstants.SYSTEM_SU, "oS/0IiAME/2unkN1momDrhAdNKOhGykYFH/mJN20");

    try (final AutoSeshat ignored = new AutoSeshat(authenticationResponse.getToken())) {
      final Tenant tenant = this.makeTenant();

      ServiceRunner.provisionerService.api().createTenant(tenant);

      final Application identityApplication = new Application();
      identityApplication.setName(ServiceRunner.identityService.name());
      identityApplication.setHomepage(ServiceRunner.identityService.uri());
      identityApplication.setDescription("Identity Service");
      identityApplication.setVendor("Apache Fineract");
      ServiceRunner.provisionerService.api().createApplication(identityApplication);

      final AssignedApplication assignedApplication = new AssignedApplication();
      assignedApplication.setName(ServiceRunner.identityService.name());

      ServiceRunner.provisionerService.api().assignIdentityManager(tenant.getIdentifier(), assignedApplication);

      this.createApplication(tenant, ServiceRunner.officeClient, io.mifos.office.api.v1.EventConstants.INITIALIZE);
      this.createApplication(tenant, ServiceRunner.customerClient, io.mifos.customer.api.v1.CustomerEventConstants.INITIALIZE);
      this.createApplication(tenant, ServiceRunner.accountingClient, io.mifos.accounting.api.v1.EventConstants.INITIALIZE);
      this.createApplication(tenant, ServiceRunner.portfolioClient, io.mifos.portfolio.api.v1.events.EventConstants.INITIALIZE);
    }
  }

  private void createApplication(final Tenant tenant, final Microservice<?> microservice, final String eventType)
      throws InterruptedException {
    final Application application = new Application();
    application.setName(microservice.name());
    application.setHomepage(microservice.uri());
    application.setVendor("Apache Fineract");

    ServiceRunner.provisionerService.api().createApplication(application);

    final AssignedApplication assignedApplication = new AssignedApplication();
    assignedApplication.setName(microservice.name());
    ServiceRunner.provisionerService.api().assignApplications(tenant.getIdentifier(), Collections.singletonList(assignedApplication));

    Assert.assertTrue(this.eventRecorder.wait(eventType, eventType));
  }

  private Tenant makeTenant() {
    final Tenant tenant = new Tenant();
    tenant.setName("Apache Fineract Demo Server");
    tenant.setIdentifier(TenantContextHolder.checkedGetIdentifier());
    tenant.setDescription("All in one Demo Server");

    final CassandraConnectionInfo cassandraConnectionInfo = new CassandraConnectionInfo();
    cassandraConnectionInfo.setClusterName("Test Cluster");
    cassandraConnectionInfo.setContactPoints("127.0.0.1:9142");
    cassandraConnectionInfo.setKeyspace("fineract_demo");
    cassandraConnectionInfo.setReplicas("3");
    cassandraConnectionInfo.setReplicationType("Simple");
    tenant.setCassandraConnectionInfo(cassandraConnectionInfo);

    final DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();
    databaseConnectionInfo.setDriverClass("org.mariadb.jdbc.Driver");
    databaseConnectionInfo.setDatabaseName("fineract_demo");
    databaseConnectionInfo.setHost("localhost");
    databaseConnectionInfo.setPort("3306");
    databaseConnectionInfo.setUser("root");
    databaseConnectionInfo.setPassword("mysql");
    tenant.setDatabaseConnectionInfo(databaseConnectionInfo);
    return tenant;
  }
}
