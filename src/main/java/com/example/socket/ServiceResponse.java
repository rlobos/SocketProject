package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServiceResponse implements ResponseCallback {

    private static final Logger logger = LogManager.getLogger(ServiceResponse.class);
    private final DataOutputStream out;

    public ServiceResponse(DataOutputStream out) {
        this.out = out;
    }

    @Override
    public void sendResponse(byte[] response) {
        try {
            out.writeInt(response.length);
            out.write(response);
            logger.info("Respuesta enviada: {}", new String(response));
        } catch (IOException e) {
            logger.error("Error al enviar la respuesta: {}", e.getMessage());
        }
    }
}
