package org.example.websocket;

import jakarta.websocket.server.ServerEndpoint;

import javax.websocket.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatEndpoint {

    // Thread-safe set to store all connected client sessions
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        broadcast("A new user has joined the chat! Current users: " + sessions.size());
    }

    @OnMessage
    public void onMessage(String message, Session senderSession) {
        // Broadcast the received message to all clients except the sender
        broadcast("User " + senderSession.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        broadcast("A user has left the chat! Current users: " + sessions.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error in session " + session.getId() + ": " + throwable.getMessage());
    }

    private void broadcast(String message) {
        // Synchronize to avoid ConcurrentModificationException
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

