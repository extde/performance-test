package com.tsystems.mb2b.openshift.appstarter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.availability.ApplicationAvailability;

public class RootResponse {

    private String status;
    private String hostName;
    private int connectionsCount;

    public RootResponse(
            ApplicationAvailability applicationAvailability,
            int connectionsCount) {
        this.connectionsCount = connectionsCount;
        status = applicationAvailability.getReadinessState().name();
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostName = "Unknown";
        }
    }

    public String getStatus(){
        return status;
    }

    public String getHostName() {
        return hostName;
    }

    public int getConnectionsCount() {
        return connectionsCount;
    }
}
