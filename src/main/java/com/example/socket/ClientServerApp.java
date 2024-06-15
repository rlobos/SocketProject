package com.example.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientServerApp {

    private static final Logger logger = LogManager.getLogger(ClientServerApp.class);
    private static final int DEFAULT_PORT = 10000;

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    private static void startServer() {
        Thread serverThread = new Thread(() -> {
            SocketServer server = new SocketServer();
            server.startServer();
        });
        serverThread.start();
    }

    private static void startClient() {
        Thread clientThread = new Thread(() -> {
            SocketClient client = new SocketClient();
            client.startClient();
        });
        clientThread.start();
    }

    private static void startServerFromProp() {
        int port = readServerPortFromProperties();
        SocketServer server = new SocketServer(port);
        Thread serverThread = new Thread(server::startServer);
        serverThread.start();
    }

    private static void startClientFromProp() {
        String host = readClientHostFromProperties();
        int port = readClientPortFromProperties();
        SocketClient client = new SocketClient(host, port);
        Thread clientThread = new Thread(client::startClient);
        clientThread.start();
    }

    private static int readServerPortFromProperties() {
        try (InputStream input = ClientServerApp.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (Exception e) {
            logger.error("Error al leer el puerto del servidor desde properties: {}", e.getMessage());
            return DEFAULT_PORT; // Puerto por defecto si hay algún error
        }
    }

    private static String readClientHostFromProperties() {
        try (InputStream input = ClientServerApp.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("host");
        } catch (Exception e) {
            logger.error("Error al leer el host del cliente desde properties: {}", e.getMessage());
            return "localhost"; // Host por defecto si hay algún error
        }
    }

    private static int readClientPortFromProperties() {
        try (InputStream input = ClientServerApp.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (Exception e) {
            logger.error("Error al leer el puerto del cliente desde properties: {}", e.getMessage());
            return DEFAULT_PORT; // Puerto por defecto si hay algún error
        }
    }
}
