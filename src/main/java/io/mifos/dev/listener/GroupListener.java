package io.mifos.dev.listener;

import io.mifos.group.api.v1.EventConstants;
import io.mifos.core.lang.config.TenantHeaderFilter;
import io.mifos.core.test.listener.EventRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Myrle Krantz
 */
@SuppressWarnings("unused")
@Component
public class GroupListener {

    private final EventRecorder eventRecorder;

    @Autowired
    public GroupListener(final EventRecorder eventRecorder) {
        this.eventRecorder = eventRecorder;
    }

    @JmsListener(
            destination = EventConstants.DESTINATION,
            selector = EventConstants.SELECTOR_INITIALIZE,
            subscription = EventConstants.DESTINATION
    )
    public void onInitialization(@Header(TenantHeaderFilter.TENANT_HEADER) final String tenant,
                                 final String payload) {
        this.eventRecorder.event(tenant, EventConstants.INITIALIZE, payload, String.class);
    }
}

