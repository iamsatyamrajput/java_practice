# 🧵 Java Multi-threading Examples

A comprehensive collection of Java multi-threading examples demonstrating various concepts, patterns, and best practices.

## 📁 File Structure

```
org/example/threading/
├── README.md                    # This documentation
├── ThreadTest.java             # Basic thread creation and lifecycle
├── ThreadMethods.java          # Thread lifecycle methods demonstration
├── Syncronization.java         # Thread synchronization examples
├── Locks.java                  # Lock mechanisms and usage
├── FairnessExample.java        # Fairness in thread scheduling
├── OddEvenPrint.java           # Print odd/even numbers using threads
├── OddEvenPrint2.java          # Alternative odd/even printing approach
├── OddEvenGaurentee.java       # Guaranteed odd/even printing order
├── PrintABC.java               # Print A, B, C in sequence using threads
├── ProducerConsumerExample.java # Producer-consumer pattern implementation
├── ChatExample.java            # Multi-threaded chat simulation
└── ExecutorExample.java        # Executor service examples
```

## 🎯 Examples Overview

### 1. **Basic Threading**
- **`ThreadTest.java`** - Basic thread creation, starting, and joining
- **`ThreadMethods.java`** - Demonstrates thread lifecycle methods (sleep, yield, interrupt)

### 2. **Synchronization & Locks**
- **`Syncronization.java`** - Basic synchronization using `synchronized` keyword
- **`Locks.java`** - Advanced locking mechanisms (ReentrantLock, ReadWriteLock)

### 3. **Thread Coordination**
- **`FairnessExample.java`** - Demonstrates fairness in thread scheduling
- **`OddEvenPrint.java`** - Coordinate threads to print odd/even numbers alternately
- **`OddEvenPrint2.java`** - Alternative approach using different synchronization
- **`OddEvenGaurentee.java`** - Guaranteed order using condition variables
- **`PrintABC.java`** - Print A, B, C in sequence using multiple threads

### 4. **Design Patterns**
- **`ProducerConsumerExample.java`** - Classic producer-consumer pattern with bounded buffer
- **`ChatExample.java`** - Multi-threaded chat simulation with message passing
- **`ExecutorExample.java`** - Using ExecutorService for thread pool management

## 🚀 Running Examples

### Compile All Examples
```bash
cd src
javac -cp . org/example/threading/*.java
```

### Run Individual Examples

#### Basic Threading
```bash
java org.example.threading.ThreadTest
java org.example.threading.ThreadMethods
```

#### Synchronization Examples
```bash
java org.example.threading.Syncronization
java org.example.threading.Locks
java org.example.threading.FairnessExample
```

#### Thread Coordination
```bash
java org.example.threading.OddEvenPrint
java org.example.threading.OddEvenPrint2
java org.example.threading.OddEvenGaurentee
java org.example.threading.PrintABC
```

#### Design Patterns
```bash
java org.example.threading.ProducerConsumerExample
java org.example.threading.ChatExample
java org.example.threading.ExecutorExample
```

## 📚 Learning Path

### Beginner Level
1. **Start with `ThreadTest.java`** - Understand basic thread creation
2. **Progress to `ThreadMethods.java`** - Learn thread lifecycle methods
3. **Study `Syncronization.java`** - Basic synchronization concepts

### Intermediate Level
4. **Practice with `OddEvenPrint.java`** - Thread coordination
5. **Explore `Locks.java`** - Advanced synchronization mechanisms
6. **Understand `ProducerConsumerExample.java`** - Classic design pattern

### Advanced Level
7. **Master `OddEvenGaurentee.java`** - Complex thread coordination
8. **Study `ChatExample.java`** - Real-world multi-threaded application
9. **Learn `ExecutorExample.java`** - Thread pool management

## 🎯 Key Concepts Demonstrated

### Thread Lifecycle
- Thread creation and starting
- Thread joining and waiting
- Thread interruption and termination
- Thread state management

### Synchronization
- `synchronized` keyword usage
- ReentrantLock and ReadWriteLock
- Condition variables
- Fairness in thread scheduling

### Thread Coordination
- Wait/notify mechanism
- CountDownLatch usage
- Thread communication patterns
- Order guarantees

### Design Patterns
- Producer-Consumer pattern
- Thread pool management
- Message passing between threads
- Resource sharing and protection

## 🔧 Common Issues & Solutions

### 1. **Race Conditions**
- **Problem**: Multiple threads accessing shared data simultaneously
- **Solution**: Use proper synchronization (synchronized blocks, locks)

### 2. **Deadlocks**
- **Problem**: Threads waiting for each other indefinitely
- **Solution**: Consistent lock ordering, timeout mechanisms

### 3. **Thread Starvation**
- **Problem**: Some threads never get CPU time
- **Solution**: Fair locks, proper thread priorities

### 4. **Memory Visibility**
- **Problem**: Changes not visible across threads
- **Solution**: Use volatile, synchronized, or atomic variables

## 🚀 Best Practices

### 1. **Always Use Proper Synchronization**
```java
// Good
synchronized(sharedObject) {
    // critical section
}

// Better
private final Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

### 2. **Prefer ExecutorService Over Raw Threads**
```java
// Instead of creating threads manually
ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(() -> {
    // task
});
```

### 3. **Use Thread-Safe Collections**
```java
// Use these instead of regular collections in multi-threaded code
ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
```

### 4. **Handle InterruptedException Properly**
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupt status
    // Handle interruption
}
```

## 📝 Notes

- All examples are self-contained and can be run independently
- Examples demonstrate both correct and incorrect approaches for learning
- Each example includes comments explaining the concepts
- The examples progress from basic to advanced concepts
- All examples follow Java best practices for multi-threading

This collection provides a solid foundation for understanding Java multi-threading concepts and patterns. 