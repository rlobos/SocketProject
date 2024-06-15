package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class SocketServer {

    private static final Logger logger = LogManager.getLogger(SocketServer.class);
    private static final String PROPERTIES_FILE = "server.properties";
    private static final int DEFAULT_PORT = 10000;
    private final int port;

    public SocketServer() {
        this.port = getPortFromProperties();
    }

    public SocketServer(int port) {
        this.port = port;
    }

    public void startServer() {
        ServiceRequest serviceRequest = new ServiceRequest();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Servidor escuchando en el puerto {}", port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

                    logger.info("Cliente conectado: {}", clientSocket.getInetAddress());

                    ServiceResponse serviceResponse = new ServiceResponse(out);

                    while (true) {
                        int length = in.readInt();
                        if (length > 0) {
                            byte[] data = new byte[length];
                            in.readFully(data, 0, length);

                            serviceRequest.onRequestReceived(data, serviceResponse);
                        }
                    }
                } catch (EOFException e) {
                    logger.warn("Cliente desconectado.");
                } catch (IOException e) {
                    logger.error("Error con la conexión del cliente: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error al iniciar el servidor en el puerto {}: {}", port, e.getMessage());
        }
    }

    private static int getPortFromProperties() {
        try (InputStream input = SocketServer.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                return Integer.parseInt(properties.getProperty("port", String.valueOf(DEFAULT_PORT)));
            } else {
                logger.warn("No se encontró el archivo {} en el classpath. Usando puerto por defecto: {}", PROPERTIES_FILE, DEFAULT_PORT);
                return DEFAULT_PORT;
            }
        } catch (IOException e) {
            logger.error("Error al leer el puerto del servidor desde properties: {}", e.getMessage());
            return DEFAULT_PORT; // Puerto por defecto si hay algún error
        }
    }
}

