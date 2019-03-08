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

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.apache.fineract.cn.accounting.api.v1.client.LedgerManager;
import org.apache.fineract.cn.accounting.importer.AccountImporter;
import org.apache.fineract.cn.accounting.importer.LedgerImporter;
import org.apache.fineract.cn.anubis.api.v1.domain.AllowedOperation;
import org.apache.fineract.cn.api.config.EnableApiFactory;
import org.apache.fineract.cn.api.context.AutoGuest;
import org.apache.fineract.cn.api.context.AutoSeshat;
import org.apache.fineract.cn.api.context.AutoUserContext;
import org.apache.fineract.cn.api.util.ApiConstants;
import org.apache.fineract.cn.api.util.ApiFactory;
import org.apache.fineract.cn.cassandra.util.CassandraConnectorConstants;
import org.apache.fineract.cn.cheque.api.v1.client.ChequeManager;
import org.apache.fineract.cn.customer.api.v1.CustomerEventConstants;
import org.apache.fineract.cn.customer.api.v1.client.CustomerManager;
import org.apache.fineract.cn.deposit.api.v1.client.DepositAccountManager;
import org.apache.fineract.cn.group.api.v1.client.GroupManager;
import org.apache.fineract.cn.identity.api.v1.client.IdentityManager;
import org.apache.fineract.cn.identity.api.v1.domain.Authentication;
import org.apache.fineract.cn.identity.api.v1.domain.Password;
import org.apache.fineract.cn.identity.api.v1.domain.Permission;
import org.apache.fineract.cn.identity.api.v1.domain.Role;
import org.apache.fineract.cn.identity.api.v1.domain.UserWithPassword;
import org.apache.fineract.cn.identity.api.v1.events.ApplicationPermissionEvent;
import org.apache.fineract.cn.identity.api.v1.events.ApplicationPermissionUserEvent;
import org.apache.fineract.cn.identity.api.v1.events.ApplicationSignatureEvent;
import org.apache.fineract.cn.identity.api.v1.events.EventConstants;
import org.apache.fineract.cn.interoperation.api.v1.client.InteroperationManager;
import org.apache.fineract.cn.lang.AutoTenantContext;
import org.apache.fineract.cn.mariadb.util.MariaDBConstants;
import org.apache.fineract.cn.notification.api.v1.client.NotificationManager;
import org.apache.fineract.cn.office.api.v1.client.OrganizationManager;
import org.apache.fineract.cn.payroll.api.v1.client.PayrollManager;
import org.apache.fineract.cn.portfolio.api.v1.client.PortfolioManager;
import org.apache.fineract.cn.provisioner.api.v1.client.Provisioner;
import org.apache.fineract.cn.provisioner.api.v1.domain.Application;
import org.apache.fineract.cn.provisioner.api.v1.domain.AssignedApplication;
import org.apache.fineract.cn.provisioner.api.v1.domain.AuthenticationResponse;
import org.apache.fineract.cn.provisioner.api.v1.domain.IdentityManagerInitialization;
import org.apache.fineract.cn.provisioner.api.v1.domain.Tenant;
import org.apache.fineract.cn.reporting.api.v1.client.ReportManager;
import org.apache.fineract.cn.rhythm.api.v1.client.RhythmManager;
import org.apache.fineract.cn.rhythm.api.v1.events.BeatEvent;
import org.apache.fineract.cn.teller.api.v1.client.TellerManager;
import org.apache.fineract.cn.test.env.ExtraProperties;
import org.apache.fineract.cn.test.listener.EnableEventRecording;
import org.apache.fineract.cn.test.listener.EventRecorder;
import org.apache.fineract.cn.test.servicestarter.ActiveMQForTest;
import org.apache.fineract.cn.test.servicestarter.ArtifactResolver;
import org.apache.fineract.cn.test.servicestarter.DirectoryBasedArtifactResolver;
import org.apache.fineract.cn.test.servicestarter.EurekaForTest;
import org.apache.fineract.cn.test.servicestarter.IntegrationTestEnvironment;
import org.apache.fineract.cn.test.servicestarter.MavenRepositoryBasedArtifactResolver;
import org.apache.fineract.cn.test.servicestarter.Microservice;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.fineract.cn.accounting.api.v1.EventConstants.POST_ACCOUNT;
import static org.apache.fineract.cn.accounting.api.v1.EventConstants.POST_LEDGER;

@SuppressWarnings({"SpringAutowiredFieldsWarningInspection", "Duplicates"})
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ServiceRunner {
  private static final String CLIENT_ID = "service-runner";
  private static final String SCHEDULER_USER_NAME = "imhotep";
  private static final String ADMIN_USER_NAME = "antony";
  private static final String TEST_LOGGER = "test-logger";
  private static final String LOAN_INCOME_LEDGER = "1100";

  private static final String NOTIFICATION_USER_PASSWORD = "shingi";
  private static final String NOTIFICATION_ROLE = "notificationAdmin";
  private static final String NOTIFICATION_USER_IDENTIFIER = "wadaadmin";

  private static final String ADMINISTRATOR_USER = "operator";
  private static final String ADMINISTRATOR_ROLE = "orgadmin";
  private static final String ADMINISTRATOR_PASS = "init1@l";

  private static final String INTEROPERATION_USER = "interopUser";
  private static final String INTEROPERATION_ROLE = "interopRole";
  private static final String INTEROPERATION_PASS = "intop@d1";

  private static Microservice<Provisioner> provisionerService;
  private static Microservice<IdentityManager> identityManager;
  private static Microservice<RhythmManager> rhythmManager;
  private static Microservice<OrganizationManager> organizationManager;
  private static Microservice<CustomerManager> customerManager;
  private static Microservice<LedgerManager> ledgerManager;
  private static Microservice<PortfolioManager> portfolioManager;
  private static Microservice<DepositAccountManager> depositAccountManager;
  private static Microservice<TellerManager> tellerManager;
  private static Microservice<ReportManager> reportManager;
  private static Microservice<ChequeManager> chequeManager;
  private static Microservice<InteroperationManager> interoperationManager;
  private static Microservice<PayrollManager> payrollManager;
  private static Microservice<GroupManager> groupManager;
  private static Microservice<NotificationManager> notificationManager;

  private static DB embeddedMariaDb;

  private static final String CUSTOM_PROP_PREFIX = "custom.";
  private boolean runInDebug;

  private static final String JAR_DIR_PROP = "jarsdir";

  @Configuration
  @ActiveMQForTest.EnableActiveMQListen
  @EnableApiFactory
  @EnableEventRecording(maxWait = 60_000)
  @ComponentScan("org.apache.fineract.cn.dev.listener")
  public static class TestConfiguration {
    public TestConfiguration() {
      super();
    }

    @Bean(name = TEST_LOGGER)
    public Logger logger() {
      return LoggerFactory.getLogger(TEST_LOGGER);
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

  @Autowired
  private Environment environment;

  @Autowired
  protected DiscoveryClient discoveryClient;

  @Autowired
  @Qualifier(TEST_LOGGER)
  private Logger logger;

  private boolean isPersistent;
  private boolean shouldProvision;

  /* Enabling lite mode restricts the working set of micro-services to Provisioner, Identity, Rhythm, Organization and Customer
 */
  private boolean liteModeEnabled;

  private static ArtifactResolver artifactResolver;

  public ServiceRunner() {
    super();
  }

  @Before
  public void before() throws Exception
  {
    this.isPersistent = this.environment.containsProperty("demoserver.persistent");
    this.shouldProvision = this.environment.containsProperty("demoserver.provision");
    liteModeEnabled = this.environment.containsProperty("demoserver.lite");

    if (!this.isPersistent) {
      // start embedded Cassandra
      EmbeddedCassandraServerHelper.startEmbeddedCassandra(TimeUnit.SECONDS.toMillis(30L));
      // start embedded MariaDB
      ServiceRunner.embeddedMariaDb = DB.newEmbeddedDB(
          DBConfigurationBuilder.newBuilder()
              .setPort(3306)
              .build()
      );
      ServiceRunner.embeddedMariaDb.start();
    }

    ExtraProperties generalProperties = new ExtraProperties();
    generalProperties.setProperty("server.max-http-header-size", Integer.toString(16 * 1024));
    generalProperties.setProperty("bonecp.partitionCount", "1");
    generalProperties.setProperty("bonecp.maxConnectionsPerPartition", "4");
    generalProperties.setProperty("bonecp.minConnectionsPerPartition", "1");
    generalProperties.setProperty("bonecp.acquireIncrement", "1");
    this.setAdditionalProperties(generalProperties);

    this.setupArtifactResolver(generalProperties);

    ServiceRunner.provisionerService = new Microservice<>(Provisioner.class, "provisioner", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver);
    provisionerService.getProcessEnvironment().addSystemPrivateKeyToProperties();
    provisionerService.getProcessEnvironment().setProperty("system.initialclientid", ServiceRunner.CLIENT_ID);
    startService(generalProperties, provisionerService);

    ServiceRunner.identityManager = new Microservice<>(IdentityManager.class, "identity", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver)
            .addProperties(new ExtraProperties() {{
              setProperty("identity.token.refresh.secureCookie", "false");}});
    startService(generalProperties, identityManager);

    ServiceRunner.rhythmManager = new Microservice<>(RhythmManager.class, "rhythm", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver)
            .addProperties(new ExtraProperties() {{
              setProperty("rhythm.beatCheckRate", Long.toString(TimeUnit.MINUTES.toMillis(10)));
              setProperty("rhythm.user", SCHEDULER_USER_NAME);}});
    startService(generalProperties, rhythmManager);

    ServiceRunner.organizationManager = new Microservice<>(OrganizationManager.class, "office", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver);
    startService(generalProperties, organizationManager);

    ServiceRunner.customerManager = new Microservice<>(CustomerManager.class, "customer", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver);
    startService(generalProperties, customerManager);

    if(!liteModeEnabled) {
      ServiceRunner.ledgerManager = new Microservice<>(LedgerManager.class, "accounting", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ledgerManager);

      ServiceRunner.portfolioManager = new Microservice<>(PortfolioManager.class, "portfolio", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT)
              .addProperties(new ExtraProperties() {{
                setProperty("portfolio.bookLateFeesAndInterestAsUser", SCHEDULER_USER_NAME);
              }});
      startService(generalProperties, portfolioManager);

      ServiceRunner.depositAccountManager = new Microservice<>(DepositAccountManager.class, "deposit-account-management", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, depositAccountManager);

      ServiceRunner.interoperationManager = new Microservice<>(InteroperationManager.class, "interoperation", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT, artifactResolver);
      startService(generalProperties, interoperationManager);

      ServiceRunner.tellerManager = new Microservice<>(TellerManager.class, "teller", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.tellerManager);

      ServiceRunner.reportManager = new Microservice<>(ReportManager.class, "reporting", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.reportManager);

      ServiceRunner.chequeManager = new Microservice<>(ChequeManager.class, "cheques", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.chequeManager);

      ServiceRunner.payrollManager = new Microservice<>(PayrollManager.class, "payroll", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.payrollManager);

      ServiceRunner.groupManager = new Microservice<>(GroupManager.class, "group", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.groupManager);

      ServiceRunner.notificationManager = new Microservice<>(NotificationManager.class, "notification", "0.1.0-BUILD-SNAPSHOT", ServiceRunner.INTEGRATION_TEST_ENVIRONMENT);
      startService(generalProperties, ServiceRunner.notificationManager);
    }
  }

  @After
  public void tearDown() throws Exception {
    if(!liteModeEnabled) {
      ServiceRunner.notificationManager.kill();
      ServiceRunner.groupManager.kill();
      ServiceRunner.payrollManager.kill();
      ServiceRunner.chequeManager.kill();
      ServiceRunner.reportManager.kill();
      ServiceRunner.interoperationManager.kill();
      ServiceRunner.tellerManager.kill();
      ServiceRunner.depositAccountManager.kill();
      ServiceRunner.portfolioManager.kill();
      ServiceRunner.ledgerManager.kill();
    }
    ServiceRunner.customerManager.kill();
    ServiceRunner.organizationManager.kill();
    ServiceRunner.rhythmManager.kill();
    ServiceRunner.identityManager.kill();

    if (!isPersistent) {
      ServiceRunner.embeddedMariaDb.stop();
      EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
  }

  @Test
  public void startDevServer() throws InterruptedException, IOException, ArtifactResolutionException {
    try {
      if (this.shouldProvision) {
        this.provisionAppsViaSeshat();
      } else {
        this.migrateServices();
      }
    }
    finally {
      ServiceRunner.provisionerService.kill();
    }

    System.out.println(identityManager.toString());
    System.out.println(organizationManager.toString());
    System.out.println(customerManager.toString());
    if(!liteModeEnabled) {
      System.out.println(ledgerManager.toString());
      System.out.println(portfolioManager.toString());
      System.out.println(rhythmManager.toString());
      System.out.println(depositAccountManager.toString());
      System.out.println(chequeManager.toString());
      System.out.println(tellerManager.toString());
      System.out.println(reportManager.toString());
      System.out.println(interoperationManager.toString());
      System.out.println(payrollManager.toString());
      System.out.println(groupManager.toString());
      System.out.println(notificationManager.toString());
    }

    System.out.println("-- End of Provision process --");

    boolean run = false;

    while (run) {
      final Scanner scanner = new Scanner(System.in);
      final String nextLine = scanner.nextLine();
      if (nextLine != null && nextLine.equals("exit")) {
        run = false;
      }
      eventRecorder.clear();
    }
  }

  private void setupArtifactResolver(final ExtraProperties properties) {
    if (properties.containsKey(ServiceRunner.JAR_DIR_PROP)) {
      final String jarDir = properties.get(ServiceRunner.JAR_DIR_PROP);
      artifactResolver = new DirectoryBasedArtifactResolver(jarDir);
      logger.info("Deployment based on Jar directory: "+ jarDir);
      return;
    }
    artifactResolver = new MavenRepositoryBasedArtifactResolver(INTEGRATION_TEST_ENVIRONMENT.getMavenRepsotiroyBasedArtifactoryDirectory());
    logger.info("Deployment based on Maven repository.");
  }

  private void startService(ExtraProperties properties, Microservice microservice) throws InterruptedException, IOException, ArtifactResolutionException {
    if (this.runInDebug) {
      microservice.runInDebug();
    }
    microservice.addProperties(properties);
    microservice.start();
    final boolean registered = microservice.waitTillRegistered(discoveryClient);
    logger.info("Service '{}' started and {} with Eureka.", microservice.name(), registered ? "registered" : "not registered");
    if (this.runInDebug) {
      logger.info("Service '{}' started with debug port {}.", microservice.name(), microservice.debuggingPort());
    }
    microservice.setApiFactory(this.apiFactory);

    TimeUnit.SECONDS.sleep(20); //Give it some extra time before the next service...
  }

  private void migrateServices() {
    final AuthenticationResponse authenticationResponse =
        ServiceRunner.provisionerService.api().authenticate(ServiceRunner.CLIENT_ID, ApiConstants.SYSTEM_SU, "oS/0IiAME/2unkN1momDrhAdNKOhGykYFH/mJN20");

    try (final AutoSeshat ignored = new AutoSeshat(authenticationResponse.getToken())) {
      final List<Tenant> tenants = ServiceRunner.provisionerService.api().getTenants();
      tenants.forEach(tenant -> {
        final List<AssignedApplication> assignedApplications = ServiceRunner.provisionerService.api().getAssignedApplications(tenant.getIdentifier());
        assignedApplications.forEach(assignedApplication -> {
          if (assignedApplication.getName().equals(ServiceRunner.identityManager.name())) {
            ServiceRunner.provisionerService.api().assignIdentityManager(tenant.getIdentifier(), assignedApplication);
          } else {
            ServiceRunner.provisionerService.api().assignApplications(tenant.getIdentifier(), Collections.singletonList(assignedApplication));
            try {
              Thread.sleep(5000L);
            } catch (InterruptedException e) {
              //do nothing
            }
          }
        });
      });
    }
  }

  private void provisionAppsViaSeshat() throws InterruptedException, IOException {
    final AuthenticationResponse authenticationResponse =
        ServiceRunner.provisionerService.api().authenticate(ServiceRunner.CLIENT_ID, ApiConstants.SYSTEM_SU, "oS/0IiAME/2unkN1momDrhAdNKOhGykYFH/mJN20");

    final List<Application> applicationsToCreate = Arrays.asList(
        ApplicationBuilder.create(ServiceRunner.identityManager.name(), ServiceRunner.identityManager.uri()),
        ApplicationBuilder.create(ServiceRunner.rhythmManager.name(), ServiceRunner.rhythmManager.uri()),
        ApplicationBuilder.create(ServiceRunner.organizationManager.name(), ServiceRunner.organizationManager.uri()),
        ApplicationBuilder.create(ServiceRunner.customerManager.name(), ServiceRunner.customerManager.uri()),
        ApplicationBuilder.create(ServiceRunner.ledgerManager.name(), ServiceRunner.ledgerManager.uri()),
        ApplicationBuilder.create(ServiceRunner.portfolioManager.name(), ServiceRunner.portfolioManager.uri()),
        ApplicationBuilder.create(ServiceRunner.depositAccountManager.name(), ServiceRunner.depositAccountManager.uri()),
        ApplicationBuilder.create(ServiceRunner.tellerManager.name(), ServiceRunner.tellerManager.uri()),
        ApplicationBuilder.create(ServiceRunner.reportManager.name(), ServiceRunner.reportManager.uri()),
        ApplicationBuilder.create(ServiceRunner.chequeManager.name(), ServiceRunner.chequeManager.uri()),
        ApplicationBuilder.create(ServiceRunner.interoperationManager.name(), ServiceRunner.interoperationManager.uri()),
        ApplicationBuilder.create(ServiceRunner.payrollManager.name(), ServiceRunner.payrollManager.uri()),
        ApplicationBuilder.create(ServiceRunner.groupManager.name(), ServiceRunner.groupManager.uri()),
        ApplicationBuilder.create(ServiceRunner.notificationManager.name(), ServiceRunner.notificationManager.uri())
    );

    // TODO: read from file
    final List<Tenant> tenantsToCreate = Arrays.asList(
            TenantBuilder.create(ServiceRunner.provisionerService.getProcessEnvironment(), "tn01", "Demo Customer Tenant", "tn01"),
            TenantBuilder.create(ServiceRunner.provisionerService.getProcessEnvironment(), "tn02", "Demo Merchant Tenant", "tn02")
    );

    logger.info("create applications");
    try (final AutoSeshat ignored = new AutoSeshat(authenticationResponse.getToken())) {
      applicationsToCreate.forEach(application -> {
        logger.info("Create application: "+ application.getName());
        ServiceRunner.provisionerService.api().createApplication(application);
      });
    }
    for (final Tenant tenant : tenantsToCreate) {
      try (final AutoSeshat ignored = new AutoSeshat(authenticationResponse.getToken())) {
        provisionAppsViaSeshatForTenant(tenant);
      }
    }
  }

  private String provisionAppsViaSeshatForTenant(final Tenant tenant) throws InterruptedException, IOException {
    provisionerService.api().createTenant(tenant);

    try (final AutoTenantContext ignored = new AutoTenantContext(tenant.getIdentifier())) {

      final AssignedApplication isisAssigned = new AssignedApplication();
      isisAssigned.setName(identityManager.name());

      final IdentityManagerInitialization tenantAdminPassword
              = provisionerService.api().assignIdentityManager(tenant.getIdentifier(), isisAssigned);


      //Creation of the schedulerUserRole, and permitting it to create application permission requests are needed in the
      //provisioning of portfolio.  Portfolio asks rhythm for a callback.  Rhythm asks identity for permission to send
      //that call back.  Rhythm needs permission to ask identity directly rather than through the provisioner because
      //the request is made outside of rhythm's initialization.
      final UserWithPassword schedulerUser = createSchedulerUserRoleAndPassword(tenantAdminPassword.getAdminPassword());

      provisionApp(tenant, rhythmManager, org.apache.fineract.cn.rhythm.api.v1.events.EventConstants.INITIALIZE);

      Assert.assertTrue(
              this.eventRecorder.wait(EventConstants.OPERATION_POST_APPLICATION_PERMISSION,
                      new ApplicationPermissionEvent(rhythmManager.name(),
                      org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.APPLICATION_SELF_MANAGEMENT)));

      final Authentication schedulerUserAuthentication;
      try (final AutoGuest ignored2 = new AutoGuest()) {
        enableUser(schedulerUser);
        schedulerUserAuthentication = identityManager.api().login(schedulerUser.getIdentifier(), schedulerUser.getPassword());
      }

      try (final AutoUserContext ignored2 = new AutoUserContext(schedulerUser.getIdentifier(), schedulerUserAuthentication.getAccessToken())) {
        identityManager.api().setApplicationPermissionEnabledForUser(
                rhythmManager.name(),
                org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.APPLICATION_SELF_MANAGEMENT,
                schedulerUser.getIdentifier(),
                true);
        Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_PUT_APPLICATION_PERMISSION_USER_ENABLED, new ApplicationPermissionUserEvent(rhythmManager.name(), org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.APPLICATION_SELF_MANAGEMENT, schedulerUser.getIdentifier())));
      }

      provisionApp(tenant, ServiceRunner.organizationManager, org.apache.fineract.cn.office.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ledgerManager, org.apache.fineract.cn.accounting.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, portfolioManager, org.apache.fineract.cn.portfolio.api.v1.events.EventConstants.INITIALIZE);

      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_PERMITTABLE_GROUP,
              org.apache.fineract.cn.rhythm.spi.v1.PermittableGroupIds.forApplication(portfolioManager.name())));

      for (int i = 0; i < 24; i++) {
        Assert.assertTrue("Beat #" + i,
                eventRecorder.wait(org.apache.fineract.cn.rhythm.api.v1.events.EventConstants.POST_BEAT,
                        new BeatEvent(portfolioManager.name(), "alignment" + i)));
      }

      final Authentication schedulerAuthentication;
      try (final AutoGuest ignored2 = new AutoGuest()) {
        schedulerAuthentication = identityManager.api().login(schedulerUser.getIdentifier(), schedulerUser.getPassword());
      }

      try (final AutoUserContext ignored2 = new AutoUserContext(schedulerUser.getIdentifier(), schedulerAuthentication.getAccessToken())) {
        //Allow rhythm to send a beat to portfolio as the scheduler user.
        identityManager.api().setApplicationPermissionEnabledForUser(
                rhythmManager.name(),
                org.apache.fineract.cn.rhythm.spi.v1.PermittableGroupIds.forApplication(portfolioManager.name()),
                schedulerUser.getIdentifier(),
                true);
        Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_PUT_APPLICATION_PERMISSION_USER_ENABLED,
                new ApplicationPermissionUserEvent(rhythmManager.name(),
                        org.apache.fineract.cn.rhythm.spi.v1.PermittableGroupIds.forApplication(portfolioManager.name()), schedulerUser.getIdentifier())));
      }

      provisionApp(tenant, ServiceRunner.customerManager, CustomerEventConstants.INITIALIZE);

      provisionApp(tenant, depositAccountManager, org.apache.fineract.cn.deposit.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.tellerManager, org.apache.fineract.cn.teller.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.reportManager, org.apache.fineract.cn.reporting.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.chequeManager, org.apache.fineract.cn.cheque.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.payrollManager, org.apache.fineract.cn.payroll.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.groupManager, org.apache.fineract.cn.group.api.v1.EventConstants.INITIALIZE);

      provisionApp(tenant, ServiceRunner.notificationManager, org.apache.fineract.cn.notification.api.v1.events.NotificationEventConstants.INITIALIZE);

      logger.info("Creating users");

      final UserWithPassword orgAdminUserPassword = createOrgAdminRoleAndUser(tenantAdminPassword.getAdminPassword());

      createInteroperationRoleAndUser(tenantAdminPassword.getAdminPassword());
      createNotificationsAdmin(tenantAdminPassword.getAdminPassword());

      createChartOfAccounts(orgAdminUserPassword);

      provisionApp(tenant, interoperationManager, org.apache.fineract.cn.interoperation.api.v1.EventConstants.INITIALIZE);

      return tenantAdminPassword.getAdminPassword();
    }
  }

  private void createChartOfAccounts(final UserWithPassword userWithPassword) throws IOException, InterruptedException {
    final Authentication authentication;
    try (final AutoGuest ignored = new AutoGuest()) {
      authentication = identityManager.api().login(userWithPassword.getIdentifier(), userWithPassword.getPassword());
    }

    // TODO: externalize this thing
    try (final AutoUserContext ignored = new AutoUserContext(userWithPassword.getIdentifier(), authentication.getAccessToken())) {
      final LedgerImporter ledgerImporter = new LedgerImporter(ledgerManager.api(), logger);
      final URL ledgersUrl = ServiceRunner.class.getResource("/standardChartOfAccounts/ledgers.csv");
      ledgerImporter.importCSV(ledgersUrl);
      Assert.assertTrue(this.eventRecorder.wait(POST_LEDGER, LOAN_INCOME_LEDGER));

      final AccountImporter accountImporter = new AccountImporter(ledgerManager.api(), logger);
      final URL accountsUrl = ServiceRunner.class.getResource("/standardChartOfAccounts/accounts.csv");
      logger.info("Load chart of accounts '{}'", accountsUrl);
      accountImporter.importCSV(accountsUrl);
      Assert.assertTrue(this.eventRecorder.wait(POST_ACCOUNT, "9330"));

      identityManager.api().logout();
    }
  }

  private <T> void provisionApp(
          final Tenant tenant,
          final Microservice<T> service,
          final String initialize_event) throws InterruptedException {
    logger.info("Provisioning service '{}', for tenant '{}'.", service.name(), tenant.getName());

    final AssignedApplication assignedApp = new AssignedApplication();
    assignedApp.setName(service.name());

    provisionerService.api().assignApplications(tenant.getIdentifier(), Collections.singletonList(assignedApp));

    Assert.assertTrue(this.eventRecorder.wait(initialize_event, initialize_event));
    Assert.assertTrue(this.eventRecorder.waitForMatch(EventConstants.OPERATION_PUT_APPLICATION_SIGNATURE,
            (ApplicationSignatureEvent x) -> x.getApplicationIdentifier().equals(service.name())));
  }

  private UserWithPassword createSchedulerUserRoleAndPassword(String tenantAdminPassword) throws InterruptedException {
    final Authentication adminAuthentication;
    try (final AutoGuest ignored = new AutoGuest()) {
      adminAuthentication = identityManager.api().login(ADMIN_USER_NAME, tenantAdminPassword);
    }

    final UserWithPassword schedulerUser;
    try (final AutoUserContext ignored = new AutoUserContext(ADMIN_USER_NAME, adminAuthentication.getAccessToken())) {
      final Role schedulerRole = defineSchedulerRole();
      identityManager.api().createRole(schedulerRole);

      schedulerUser = new UserWithPassword();
      schedulerUser.setIdentifier(SCHEDULER_USER_NAME);
      schedulerUser.setPassword(encodePassword("26500BC"));
      schedulerUser.setRole(schedulerRole.getIdentifier());

      identityManager.api().createUser(schedulerUser);
      Assert.assertTrue(eventRecorder.wait(EventConstants.OPERATION_POST_USER, schedulerUser.getIdentifier()));
    }

    try (final AutoGuest ignored = new AutoGuest()) {
      enableUser(schedulerUser);
    }

    return schedulerUser;
  }

  private Role defineSchedulerRole() {
    final Permission permissionRequestionCreationPermission = getPermission(AllowedOperation.CHANGE, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.APPLICATION_SELF_MANAGEMENT);

    final Permission beatPublishToPortfolioPermission = getPermission(AllowedOperation.CHANGE, org.apache.fineract.cn.rhythm.spi.v1.PermittableGroupIds.forApplication(portfolioManager.name()));

    final Role role = new Role();
    role.setIdentifier("scheduler");
    role.setPermissions(Arrays.asList(permissionRequestionCreationPermission /*, beatPublishToPortfolioPermission */)); // TODO: temp fix

    return role;
  }

  private UserWithPassword createOrgAdminRoleAndUser(final String tenantAdminPassword) throws InterruptedException {
    final Authentication adminAuthentication;
    try (final AutoUserContext ignored = new AutoGuest()) {
      adminAuthentication = ServiceRunner.identityManager.api().login(ADMIN_USER_NAME, tenantAdminPassword);
    }

    try (final AutoUserContext ignored = new AutoUserContext(ADMIN_USER_NAME, adminAuthentication.getAccessToken())) {
      final Role fimsAdministratorRole = defineOrgAdministratorRole();

      ServiceRunner.identityManager.api().createRole(fimsAdministratorRole);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_ROLE, fimsAdministratorRole.getIdentifier()));

      final UserWithPassword fimsAdministratorUser = new UserWithPassword();
      fimsAdministratorUser.setIdentifier(ADMINISTRATOR_USER);
      fimsAdministratorUser.setPassword(Base64Utils.encodeToString(ADMINISTRATOR_PASS.getBytes()));
      fimsAdministratorUser.setRole(fimsAdministratorRole.getIdentifier());

      ServiceRunner.identityManager.api().createUser(fimsAdministratorUser);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_USER, fimsAdministratorUser.getIdentifier()));

      ServiceRunner.identityManager.api().logout();

      enableUser(fimsAdministratorUser);
      return fimsAdministratorUser;
    }
  }

  private UserWithPassword createInteroperationRoleAndUser(final String tenantAdminPassword) throws InterruptedException {
    final Authentication adminAuthentication;
    try (final AutoUserContext ignored = new AutoGuest()) {
      adminAuthentication = ServiceRunner.identityManager.api().login(ADMIN_USER_NAME, tenantAdminPassword);
    }

    try (final AutoUserContext ignored = new AutoUserContext(ADMIN_USER_NAME, adminAuthentication.getAccessToken())) {
      final Role role = createInteroperationRole();

      ServiceRunner.identityManager.api().createRole(role);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_ROLE, role.getIdentifier()));

      final UserWithPassword interoperationUser = new UserWithPassword();
      interoperationUser.setIdentifier(INTEROPERATION_USER);
      interoperationUser.setPassword(Base64Utils.encodeToString(INTEROPERATION_PASS.getBytes()));
      interoperationUser.setRole(role.getIdentifier());

      ServiceRunner.identityManager.api().createUser(interoperationUser);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_USER, interoperationUser.getIdentifier()));

      ServiceRunner.identityManager.api().logout();

      enableUser(interoperationUser);
      return interoperationUser;
    }
  }

  private Role defineOrgAdministratorRole() {
    ArrayList<Permission> permissions = new ArrayList<>();

    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.office.api.v1.PermittableGroupIds.EMPLOYEE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.office.api.v1.PermittableGroupIds.OFFICE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.office.api.v1.PermittableGroupIds.SELF_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.IDENTITY_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.ROLE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.SELF_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.deposit.api.v1.PermittableGroupIds.DEFINITION_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.deposit.api.v1.PermittableGroupIds.INSTANCE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_ACCOUNT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_FIN_CONDITION));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_INCOME_STMT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_JOURNAL));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_LEDGER));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_TX_TYPES));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.PRODUCT_LOSS_PROVISIONING_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.PRODUCT_OPERATIONS_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.PRODUCT_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.CASE_DOCUMENT_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.CASE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.CASE_STATUS));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.CATALOG));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.DOCUMENTS));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.IDENTIFICATIONS));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.PORTRAIT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.CUSTOMER));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.customer.PermittableGroupIds.TASK));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.cheque.api.v1.PermittableGroupIds.CHEQUE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.cheque.api.v1.PermittableGroupIds.CHEQUE_TRANSACTION));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.teller.api.v1.PermittableGroupIds.TELLER_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.teller.api.v1.PermittableGroupIds.TELLER_OPERATION));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.interoperation.api.v1.PermittableGroupIds.INTEROPERATION_SINGLE));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.interoperation.api.v1.PermittableGroupIds.INTEROPERATION_BULK));

    final Role role = new Role();
    role.setIdentifier(ADMINISTRATOR_ROLE);
    role.setPermissions(permissions);

    return role;
  }

  private Role createInteroperationRole() {
    ArrayList<Permission> permissions = new ArrayList<>();

    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.office.api.v1.PermittableGroupIds.EMPLOYEE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.office.api.v1.PermittableGroupIds.OFFICE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.IDENTITY_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.ROLE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.identity.api.v1.PermittableGroupIds.SELF_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.deposit.api.v1.PermittableGroupIds.DEFINITION_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.deposit.api.v1.PermittableGroupIds.INSTANCE_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_ACCOUNT));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_JOURNAL));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_LEDGER));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_TX_TYPES));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.PRODUCT_OPERATIONS_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.portfolio.api.v1.PermittableGroupIds.PRODUCT_MANAGEMENT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.customer.PermittableGroupIds.PORTRAIT));
    permissions.add(getPermission(AllowedOperation.READ, org.apache.fineract.cn.customer.PermittableGroupIds.CUSTOMER));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.interoperation.api.v1.PermittableGroupIds.INTEROPERATION_SINGLE));
    permissions.add(getPermission(AllowedOperation.ALL, org.apache.fineract.cn.interoperation.api.v1.PermittableGroupIds.INTEROPERATION_BULK));

    final Role role = new Role();
    role.setIdentifier(INTEROPERATION_ROLE);
    role.setPermissions(permissions);

    return role;
  }

  private Permission getPermission(Set<AllowedOperation> operations, String permittable) {
    final Permission permission = new Permission();
    permission.setAllowedOperations(operations);
    permission.setPermittableEndpointGroupIdentifier(permittable);
    return permission;
  }

  private Permission getPermission(AllowedOperation operation, String permittable) {
    return getPermission(Collections.singleton(operation), permittable);
  }

  private UserWithPassword createNotificationsAdmin(final String tenantAdminPassword) throws InterruptedException {
    final Authentication adminAuthentication;
    try (final AutoUserContext ignored = new AutoGuest()) {
      adminAuthentication = ServiceRunner.identityManager.api().login(ADMIN_USER_NAME, tenantAdminPassword);
    }

    try (final AutoUserContext ignored = new AutoUserContext(ADMIN_USER_NAME, adminAuthentication.getAccessToken())) {
      final Role notificationRole = defineNotificationRole();

      ServiceRunner.identityManager.api().createRole(notificationRole);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_ROLE, notificationRole.getIdentifier()));

      final UserWithPassword notificationUser = new UserWithPassword();
      notificationUser.setIdentifier(NOTIFICATION_USER_IDENTIFIER);
      notificationUser.setPassword(Base64Utils.encodeToString(NOTIFICATION_USER_PASSWORD.getBytes()));
      notificationUser.setRole(notificationRole.getIdentifier());

      ServiceRunner.identityManager.api().createUser(notificationUser);
      Assert.assertTrue(this.eventRecorder.wait(EventConstants.OPERATION_POST_USER, notificationUser.getIdentifier()));

      ServiceRunner.identityManager.api().logout();

      enableUser(notificationUser);
      return notificationUser;
    }
  }

  private Role defineNotificationRole() {
    final Permission customerPermission = getPermission(AllowedOperation.READ, org.apache.fineract.cn.customer.PermittableGroupIds.CUSTOMER);

    final Role role = new Role();
    role.setIdentifier(NOTIFICATION_ROLE);
    role.setPermissions(Arrays.asList(
        customerPermission
        )
    );
    return role;
  }

  private void enableUser(final UserWithPassword userWithPassword) throws InterruptedException {
    final Authentication passwordOnlyAuthentication
            = identityManager.api().login(userWithPassword.getIdentifier(), userWithPassword.getPassword());
    try (final AutoUserContext ignored
                 = new AutoUserContext(userWithPassword.getIdentifier(), passwordOnlyAuthentication.getAccessToken()))
    {
      identityManager.api().changeUserPassword(
              userWithPassword.getIdentifier(), new Password(userWithPassword.getPassword()));
      Assert.assertTrue(eventRecorder.wait(EventConstants.OPERATION_PUT_USER_PASSWORD,
              userWithPassword.getIdentifier()));
    }
  }

  private static String encodePassword(final String password) {
    return Base64Utils.encodeToString(password.getBytes());
  }

  private void setAdditionalProperties(final ExtraProperties properties) {
    if (this.environment.containsProperty(ServiceRunner.JAR_DIR_PROP)) {
      properties.setProperty(ServiceRunner.JAR_DIR_PROP, this.environment.getProperty(ServiceRunner.JAR_DIR_PROP));
    }

    if (this.environment.containsProperty(ServiceRunner.CUSTOM_PROP_PREFIX + CassandraConnectorConstants.CONTACT_POINTS_PROP)) {
      properties.setProperty(CassandraConnectorConstants.CONTACT_POINTS_PROP, this.environment.getProperty(ServiceRunner.CUSTOM_PROP_PREFIX + CassandraConnectorConstants.CONTACT_POINTS_PROP));
    }

    if (this.environment.containsProperty(CassandraConnectorConstants.CLUSTER_USER_PROP)) {
      properties.setProperty(CassandraConnectorConstants.CLUSTER_USER_PROP, this.environment.getProperty(CassandraConnectorConstants.CLUSTER_USER_PROP));
    }

    if (this.environment.containsProperty(CassandraConnectorConstants.CLUSTER_PASSWORD_PROP)) {
      properties.setProperty(CassandraConnectorConstants.CLUSTER_PASSWORD_PROP, this.environment.getProperty(CassandraConnectorConstants.CLUSTER_PASSWORD_PROP));
    }

    if (this.environment.containsProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_HOST_PROP)) {
      properties.setProperty(MariaDBConstants.MARIADB_HOST_PROP, this.environment.getProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_HOST_PROP));
    }

    if (this.environment.containsProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_PORT_PROP)) {
      properties.setProperty(MariaDBConstants.MARIADB_PORT_PROP, this.environment.getProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_PORT_PROP));
    }

    if (this.environment.containsProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_USER_PROP)) {
      properties.setProperty(MariaDBConstants.MARIADB_USER_PROP, this.environment.getProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_USER_PROP));
    }

    if (this.environment.containsProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_PASSWORD_PROP)) {
      properties.setProperty(MariaDBConstants.MARIADB_PASSWORD_PROP, this.environment.getProperty(ServiceRunner.CUSTOM_PROP_PREFIX + MariaDBConstants.MARIADB_PASSWORD_PROP));
    }
  }
}