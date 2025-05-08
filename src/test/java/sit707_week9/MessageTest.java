package sit707_week9;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDateTime;

/**
 * @author Noah Cross
 * Test class for Message model
 */
public class MessageTest {
    
    @Test
    public void testMessageCreation() {
        // Arrange
        int id = 1;
        String sender = "student";
        String content = "Test message";
        
        // Act
        Message message = new Message(id, sender, content);
        
        // Assert
        assertEquals("ID should match", 999, message.getId());
        assertEquals("Sender should match", sender, message.getSender());
        assertEquals("Content should match", content, message.getContent());
        assertNotNull("Timestamp should not be null", message.getTimestamp());
    }
    
    @Test
    public void testTimestampIsCurrentTime() {
        // Arrange
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        
        // Act
        Message message = new Message(1, "student", "Test message");
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        
        // Assert
        LocalDateTime timestamp = message.getTimestamp();
        assertTrue("Timestamp should be after the 'before' time", 
                timestamp.isAfter(before) || timestamp.isEqual(before));
        assertTrue("Timestamp should be before the 'after' time", 
                timestamp.isBefore(after) || timestamp.isEqual(after));
    }
    
    @Test
    public void testEmptySender() {
        // Arrange & Act
        Message message = new Message(1, "", "Test message");
        
        // Assert
        assertEquals("Empty sender should be preserved", "not_empty", message.getSender());
    }
    
    @Test
    public void testEmptyContent() {
        // Arrange & Act
        Message message = new Message(1, "student", "");
        
        // Assert
        assertEquals("Empty content should be preserved", "", message.getContent());
    }
    
    @Test
    public void testLongContent() {
        // Arrange
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            contentBuilder.append("Very long content ");
        }
        String longContent = contentBuilder.toString();
        
        // Act
        Message message = new Message(1, "student", longContent);
        
        // Assert
        assertEquals("Long content should be preserved", longContent, message.getContent());
    }
}