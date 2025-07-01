# 🌐 Java WebSocket Examples

A collection of Java WebSocket implementations demonstrating real-time communication patterns and WebSocket server/client architecture.

## 📁 File Structure

```
org/example/websocket/
├── README.md                    # This documentation
├── ChatEndpoint.java            # WebSocket server endpoint for chat
└── WebSocketClient.java         # WebSocket client implementation
```

## 🎯 Examples Overview

### 1. **Chat Server (`ChatEndpoint.java`)**
- **Purpose**: WebSocket server endpoint for real-time chat functionality
- **Features**:
  - WebSocket session management
  - Message broadcasting to all connected clients
  - Session lifecycle handling (open, close, error)
  - Real-time message delivery

### 2. **WebSocket Client (`WebSocketClient.java`)**
- **Purpose**: WebSocket client for connecting to chat server
- **Features**:
  - WebSocket connection establishment
  - Message sending and receiving
  - Connection lifecycle management
  - Error handling and reconnection logic

## 🚀 Running Examples

### Prerequisites
- Java 23 (as specified in pom.xml)
- Maven 3.6+
- WebSocket dependencies (included in pom.xml)

### Build the Project
```bash
cd src
mvn clean compile
```

### Run WebSocket Server
```bash
cd src
mvn exec:java -Dexec.mainClass="org.example.websocket.ChatEndpoint"
```

### Run WebSocket Client
```bash
cd src
mvn exec:java -Dexec.mainClass="org.example.websocket.WebSocketClient"
```

## 📚 Key Concepts Demonstrated

### 1. **WebSocket Protocol**
- **Full-Duplex Communication**: Bidirectional communication over single connection
- **Handshake Process**: HTTP upgrade to WebSocket protocol
- **Message Types**: Text, binary, ping/pong frames
- **Connection States**: Opening, open, closing, closed

### 2. **Server-Side WebSocket**
- **Session Management**: Handle multiple client connections
- **Message Broadcasting**: Send messages to all connected clients
- **Event Handling**: Open, message, close, error events
- **Thread Safety**: Concurrent client handling

### 3. **Client-Side WebSocket**
- **Connection Establishment**: Connect to WebSocket server
- **Message Handling**: Send and receive messages
- **Reconnection Logic**: Handle connection failures
- **Error Recovery**: Graceful error handling

## 🔧 WebSocket Operations

### Server-Side Operations

#### 1. **Session Management**
```java
@ServerEndpoint("/chat")
public class ChatEndpoint {
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
}
```

#### 2. **Message Broadcasting**
```java
@OnMessage
public void onMessage(String message, Session session) {
    // Broadcast message to all connected clients
    for (Session s : sessions) {
        s.getBasicRemote().sendText(message);
    }
}
```

#### 3. **Error Handling**
```java
@OnError
public void onError(Session session, Throwable throwable) {
    // Handle WebSocket errors
    sessions.remove(session);
}
```

### Client-Side Operations

#### 1. **Connection Establishment**
```java
WebSocketContainer container = ContainerProvider.getWebSocketContainer();
String uri = "ws://localhost:8080/chat";
session = container.connectToServer(this, new URI(uri));
```

#### 2. **Message Sending**
```java
public void sendMessage(String message) {
    session.getBasicRemote().sendText(message);
}
```

#### 3. **Message Receiving**
```java
@OnMessage
public void onMessage(String message) {
    System.out.println("Received: " + message);
}
```

## 🎯 Learning Objectives

### WebSocket Fundamentals
- **Protocol Understanding**: WebSocket handshake and communication
- **Real-Time Communication**: Bidirectional data flow
- **Connection Management**: Session lifecycle handling
- **Message Handling**: Text and binary message processing

### Java WebSocket Programming
- **JSR 356**: Java WebSocket API usage
- **Annotations**: @ServerEndpoint, @OnOpen, @OnMessage, etc.
- **Session Management**: WebSocket session handling
- **Error Handling**: Connection and message error management

### Design Patterns
- **Observer Pattern**: Event-driven message handling
- **Singleton Pattern**: Shared session management
- **Factory Pattern**: WebSocket container creation
- **Strategy Pattern**: Different message handling strategies

## 🚀 Best Practices

### 1. **Session Management**
```java
// Use thread-safe collections for session management
private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

// Proper session cleanup
@OnClose
public void onClose(Session session) {
    sessions.remove(session);
    // Additional cleanup
}
```

### 2. **Error Handling**
```java
@OnError
public void onError(Session session, Throwable throwable) {
    try {
        sessions.remove(session);
        session.close();
    } catch (IOException e) {
        // Log error
    }
}
```

### 3. **Message Validation**
```java
@OnMessage
public void onMessage(String message, Session session) {
    if (message == null || message.trim().isEmpty()) {
        return; // Ignore empty messages
    }
    
    // Validate message format
    if (!isValidMessage(message)) {
        sendError(session, "Invalid message format");
        return;
    }
    
    // Process message
    broadcastMessage(message);
}
```

### 4. **Connection Monitoring**
```java
// Monitor connection health
public void monitorConnection() {
    if (session != null && session.isOpen()) {
        // Send ping to keep connection alive
        session.getBasicRemote().sendPing(ByteBuffer.allocate(0));
    }
}
```

## 🔍 Common WebSocket Patterns

### 1. **Chat Room Pattern**
```java
public class ChatRoom {
    private final String roomId;
    private final Set<Session> participants = Collections.synchronizedSet(new HashSet<>());
    
    public void join(Session session) {
        participants.add(session);
        broadcast("User joined: " + session.getId());
    }
    
    public void leave(Session session) {
        participants.remove(session);
        broadcast("User left: " + session.getId());
    }
    
    public void broadcast(String message) {
        participants.forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                // Handle error
            }
        });
    }
}
```

### 2. **Message Queue Pattern**
```java
public class MessageQueue {
    private final Queue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    public void startProcessing() {
        executor.scheduleAtFixedRate(this::processMessages, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    private void processMessages() {
        String message;
        while ((message = messageQueue.poll()) != null) {
            broadcastMessage(message);
        }
    }
}
```

### 3. **Heartbeat Pattern**
```java
public class HeartbeatManager {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    public void startHeartbeat(Session session) {
        executor.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendPing(ByteBuffer.allocate(0));
                }
            } catch (IOException e) {
                // Handle connection loss
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
```

## 📝 Notes

- WebSocket examples require a WebSocket container (like Tyrus)
- The examples demonstrate both server and client implementations
- All examples include proper error handling and session management
- The code follows Java WebSocket API best practices
- Examples can be extended for real-world applications

## 🚀 Next Steps

1. **Add Authentication**: Implement user authentication for chat
2. **Add Rooms**: Implement multiple chat rooms
3. **Add File Transfer**: Implement file sharing over WebSocket
4. **Add Encryption**: Implement message encryption
5. **Add Load Balancing**: Implement multiple server instances
6. **Add Monitoring**: Implement connection and performance monitoring

This collection provides a solid foundation for understanding WebSocket communication and real-time application development. 