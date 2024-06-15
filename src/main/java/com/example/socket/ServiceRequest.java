package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceRequest implements RequestHandler {

    private static final Logger logger = LogManager.getLogger(ServiceRequest.class);

    @Override
    public void onRequestReceived(byte[] request, ResponseCallback callback) {
        String requestData = new String(request);
        logger.debug("Datos recibidos: {}", requestData);

        String responseData = "Respuesta del servicio a: " + requestData;
        callback.sendResponse(responseData.getBytes());
    }
}
