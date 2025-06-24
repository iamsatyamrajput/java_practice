package org.example.websocket;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {
    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(WebSocketClient.class, URI.create("ws://localhost:8080/chat"));

        // Send messages from this client
        session.getBasicRemote().sendText("Hello from client!");

        // Keep the client alive to receive messages
        Thread.sleep(10000);
        session.close();
        
    }
}
