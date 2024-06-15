package com.example.socket;

public interface RequestHandler {
    void onRequestReceived(byte[] request, ResponseCallback callback);
}
