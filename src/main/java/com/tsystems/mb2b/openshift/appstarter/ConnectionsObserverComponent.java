package com.tsystems.mb2b.openshift.appstarter;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class ConnectionsObserverComponent {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ApplicationAvailability applicationAvailability;

    private final int maxConnections = 5;

    private AtomicInteger connectionsCount = new AtomicInteger(0);

    public int incConnections() {
        int result = connectionsCount.addAndGet(1);
        if (result > maxConnections) {
            System.out.println("Not ready" + result);
            AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
        } else {
            System.out.println("Ready" + result);
        }
        return result;
    }

    public void decConnections() {
        if (connectionsCount.addAndGet(-1) <= maxConnections
                && applicationAvailability.getReadinessState() != ReadinessState.ACCEPTING_TRAFFIC) {
            AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
        }
    }

}
