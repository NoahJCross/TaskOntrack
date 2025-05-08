package sit707_week9;

import java.time.LocalDateTime;

/**
 * @author Noah Cross
 * Represents a message in the communication between students and tutors
 */
public class Message {
    private int id;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    
    public Message(int id, String sender, String content) {
        if (sender == null || sender.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    public int getId() {
        return id;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}