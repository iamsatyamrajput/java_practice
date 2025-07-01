# 🗄️ Java Database Examples

A collection of Java database implementations demonstrating key-value storage concepts and database design patterns.

## 📁 File Structure

```
org/example/db/
├── README.md                    # This documentation
├── KeyValueDatabase.java        # Simple key-value database implementation
└── KeyValueDatabaseTest.java    # Test class for the database
```

## 🎯 Examples Overview

### 1. **Key-Value Database (`KeyValueDatabase.java`)**
- **Purpose**: Simple in-memory key-value database implementation
- **Features**:
  - Basic CRUD operations (Create, Read, Update, Delete)
  - Thread-safe operations
  - Simple data persistence concepts
  - Error handling and validation

### 2. **Database Testing (`KeyValueDatabaseTest.java`)**
- **Purpose**: Comprehensive testing of the key-value database
- **Features**:
  - Unit tests for all CRUD operations
  - Edge case testing
  - Performance testing
  - Integration testing

## 🚀 Running Examples

### Compile Database Examples
```bash
cd src
javac -cp . org/example/db/*.java
```

### Run Database Tests
```bash
java org.example.db.KeyValueDatabaseTest
```

### Run from Main Class
```bash
cd src
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## 📚 Key Concepts Demonstrated

### 1. **Database Design Patterns**
- **Repository Pattern**: Encapsulation of data access logic
- **CRUD Operations**: Create, Read, Update, Delete operations
- **Data Validation**: Input validation and error handling
- **Thread Safety**: Concurrent access handling

### 2. **Data Structures**
- **HashMap**: Efficient key-value storage
- **Collections**: Java collections framework usage
- **Generics**: Type-safe data storage

### 3. **Error Handling**
- **Exception Handling**: Proper exception management
- **Input Validation**: Data validation before processing
- **Error Messages**: User-friendly error reporting

## 🔧 Database Operations

### Basic CRUD Operations

#### 1. **Create (Insert)**
```java
KeyValueDatabase db = new KeyValueDatabase();
db.put("key1", "value1");
```

#### 2. **Read (Get)**
```java
String value = db.get("key1");
```

#### 3. **Update**
```java
db.put("key1", "newValue"); // Overwrites existing value
```

#### 4. **Delete**
```java
db.remove("key1");
```

### Advanced Operations

#### 1. **Check Existence**
```java
boolean exists = db.containsKey("key1");
```

#### 2. **Get All Keys**
```java
Set<String> keys = db.getAllKeys();
```

#### 3. **Clear Database**
```java
db.clear();
```

#### 4. **Get Size**
```java
int size = db.size();
```

## 🎯 Learning Objectives

### Database Fundamentals
- **Data Storage**: Understanding how data is stored and retrieved
- **Indexing**: Efficient data lookup mechanisms
- **ACID Properties**: Atomicity, Consistency, Isolation, Durability
- **Transaction Management**: Data consistency and integrity

### Java Database Programming
- **Collections Framework**: Using Java collections for data storage
- **Thread Safety**: Handling concurrent access to shared data
- **Exception Handling**: Proper error management in database operations
- **Performance Optimization**: Efficient data structures and algorithms

### Design Patterns
- **Repository Pattern**: Separation of data access logic
- **Singleton Pattern**: Single database instance management
- **Factory Pattern**: Object creation and management
- **Observer Pattern**: Data change notifications

## 🚀 Best Practices

### 1. **Thread Safety**
```java
// Use synchronized methods or concurrent collections
public synchronized void put(String key, String value) {
    // implementation
}
```

### 2. **Input Validation**
```java
public void put(String key, String value) {
    if (key == null || key.trim().isEmpty()) {
        throw new IllegalArgumentException("Key cannot be null or empty");
    }
    // implementation
}
```

### 3. **Error Handling**
```java
public String get(String key) {
    try {
        return data.get(key);
    } catch (Exception e) {
        logger.error("Error retrieving key: " + key, e);
        throw new DatabaseException("Failed to retrieve value", e);
    }
}
```

### 4. **Performance Optimization**
```java
// Use appropriate data structures
private final Map<String, String> data = new ConcurrentHashMap<>();
```

## 🔍 Common Database Patterns

### 1. **Connection Pooling**
```java
// For real databases, use connection pooling
public class DatabaseConnectionPool {
    private final Queue<Connection> connections;
    
    public Connection getConnection() {
        // Get connection from pool
    }
    
    public void releaseConnection(Connection conn) {
        // Return connection to pool
    }
}
```

### 2. **Transaction Management**
```java
public void executeTransaction(Runnable operation) {
    try {
        beginTransaction();
        operation.run();
        commitTransaction();
    } catch (Exception e) {
        rollbackTransaction();
        throw e;
    }
}
```

### 3. **Data Validation**
```java
public void validateData(String key, String value) {
    if (key == null || key.trim().isEmpty()) {
        throw new ValidationException("Invalid key");
    }
    if (value == null) {
        throw new ValidationException("Value cannot be null");
    }
    // Additional validation rules
}
```

## 📝 Notes

- This is a simplified in-memory database for learning purposes
- Real-world databases would include persistence, indexing, and more complex features
- The examples demonstrate fundamental database concepts
- All examples include proper error handling and validation
- The code follows Java best practices for database programming

## 🚀 Next Steps

1. **Add Persistence**: Implement file-based storage
2. **Add Indexing**: Implement efficient search mechanisms
3. **Add Transactions**: Implement ACID properties
4. **Add Caching**: Implement memory caching for performance
5. **Add Query Language**: Implement simple query parsing
6. **Add Concurrency**: Implement advanced concurrency control

This collection provides a solid foundation for understanding database concepts and Java database programming. 