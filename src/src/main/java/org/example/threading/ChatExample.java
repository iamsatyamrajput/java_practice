package org.example.threading;

public class ChatExample {
    public static void main(String[] args) throws InterruptedException {
        // Shared Chat object
        Chat chat = new Chat();

        // Runnable for sending messages
        Runnable sender = () -> {
            for (int i = 0; i < 10; i++) {
                chat.sendMessage(Thread.currentThread().getName() + " Hello, how are you? " + i);
            }
        };

        // Runnable for replying to messages
        Runnable receiver = () -> {
            for (int i = 0; i < 10; i++) {
                //System.out.println("Starting");
                chat.replyMessage(Thread.currentThread().getName() + " Replying to message " + i);
            }
        };

        // Create sender and receiver threads
        Thread senderThread = new Thread(sender, "Sender");
        Thread receiverThread = new Thread(receiver, "Receiver");

        // Start the threads
        senderThread.start();
        receiverThread.start();

        // Ensure the main thread waits for sender and receiver threads to finish
        senderThread.join(); // Ensure the main thread waits for sender to finish
        receiverThread.join(); // Ensure the main thread waits for receiver to finish

        // Print message after both threads complete
        System.out.println("Completed chat interaction.");
    }
}

// Chat class simulates sending and receiving messages with synchronization
class Chat {
    private String message;
    private boolean isMessageSent = false;

    // Method for sending messages (synchronized to ensure mutual exclusion)
    public synchronized void sendMessage(String message) {
        try {
            // Wait until the receiver replies to the previous message
            while (isMessageSent) {
                System.out.println(Thread.currentThread().getName() + " waiting to send message..."); // Debugging message
                wait(); // Wait if the message has already been sent and not yet replied to
            }

            // Set the message and print it
            this.message = "Message: " + message;
            System.out.println(Thread.currentThread().getName() + " sent: " + message);

            // Set the flag to indicate the message is sent
            isMessageSent = true;

            // Notify the receiver that the message has been sent
            System.out.println(Thread.currentThread().getName() + " notifying receiver..."); // Debugging message
            notifyAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for receiving and replying to messages (synchronized to ensure mutual exclusion)
    public synchronized void replyMessage(String message) {
        try {
            // Wait until the sender sends a message
            while (!isMessageSent) {
                System.out.println(Thread.currentThread().getName() + " waiting to receive message..."); // Debugging message
                wait(); // Wait if no message has been sent yet
            }

            // Set the reply message and print it
            this.message = "Reply to: " + message;
            System.out.println(Thread.currentThread().getName() + " received and replied: " + message);

            // Set the flag to indicate the receiver has replied
            isMessageSent = false;

            // Notify the sender that the reply has been sent
            System.out.println(Thread.currentThread().getName() + " notifying sender..."); // Debugging message
            notifyAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
