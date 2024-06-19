package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceRequest implements RequestHandler {

    private static final Logger logger = LogManager.getLogger(ServiceRequest.class);

    @Override
    public void onRequestReceived(String responseData, ResponseCallback callback) {

        callback.sendResponse(responseData.getBytes());
    }
}
