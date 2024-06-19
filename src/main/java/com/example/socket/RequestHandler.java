package com.example.socket;

public interface RequestHandler {
    void onRequestReceived(String request, ResponseCallback callback);
}
