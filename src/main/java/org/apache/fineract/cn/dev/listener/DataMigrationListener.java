package org.apache.fineract.cn.dev.listener;

import org.apache.fineract.cn.datamigration.api.v1.events.DatamigrationEventConstants;
import org.apache.fineract.cn.lang.config.TenantHeaderFilter;
import org.apache.fineract.cn.test.listener.EventRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class DataMigrationListener {

  private final EventRecorder eventRecorder;

  @Autowired
  public DataMigrationListener(final EventRecorder eventRecorder) {
    this.eventRecorder = eventRecorder;
  }

  @JmsListener(
          subscription = DatamigrationEventConstants.DESTINATION,
          destination = DatamigrationEventConstants.DESTINATION,
          selector = DatamigrationEventConstants.SELECTOR_INITIALIZE
  )
  public void onInitialized(@Header(TenantHeaderFilter.TENANT_HEADER) final String tenant,
                            final String payload) {
    this.eventRecorder.event(tenant, DatamigrationEventConstants.INITIALIZE, payload, String.class);
  }
}
