package com.tsystems.mb2b.openshift.appstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @Autowired
    private ApplicationAvailability applicationAvailability;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Autowired
    ConnectionsObserverComponent connectionsObserver;

    @GetMapping("/")
    public RootResponse ok() {
        RootResponse result = null;
        try {
            result = new RootResponse(applicationAvailability, connectionsObserver.incConnections());
            Thread.sleep(100);
            connectionsObserver.decConnections();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/readiness/accepting")
    public String markReadinesAcceptingTraffic() {
        AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
        return "Readiness marked as ACCEPTING_TRAFFIC";
    }

    @GetMapping("/readiness/refuse")
    public String markReadinesRefusingTraffic() {
        AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
        return "Readiness marked as REFUSING_TRAFFIC";
    }
}
