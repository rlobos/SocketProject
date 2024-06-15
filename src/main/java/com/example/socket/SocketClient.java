package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class SocketClient {

    private static final Logger logger = LogManager.getLogger(SocketClient.class);
    private static final String PROPERTIES_FILE = "client.properties";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 10000;
    private final String host;
    private final int port;

    public SocketClient() {
        this.host = getHostFromProperties();
        this.port = getPortFromProperties();
    }

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() {
        try (Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            logger.info("Conectado al servidor en {}:{}", host, port);

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                byte[] data = userInput.getBytes();
                out.writeInt(data.length);
                out.write(data);

                int length = in.readInt();
                if (length > 0) {
                    byte[] response = new byte[length];
                    in.readFully(response, 0, length);
                    logger.info("Respuesta del servidor: {}", new String(response));
                }
            }
        } catch (IOException e) {
            logger.error("Error de comunicación con el servidor: {}", e.getMessage());
        }
    }

    private static int getPortFromProperties() {
        try (InputStream input = SocketClient.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port", String.valueOf(DEFAULT_PORT)));
        } catch (IOException e) {
            logger.error("Error al leer el puerto del servidor desde properties: {}", e.getMessage());
            return DEFAULT_PORT; // Puerto por defecto si hay algún error
        }
    }

    private static String getHostFromProperties() {
        try (InputStream input = SocketClient.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("host", DEFAULT_HOST);
        } catch (IOException e) {
            logger.error("Error al leer la dirección del servidor desde properties: {}", e.getMessage());
            return DEFAULT_HOST; // Dirección de host por defecto si hay algún error
        }
    }
}
