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
        int id = 1;
        String sender = "student";
        String content = "Test message";
        Message message = new Message(id, sender, content);
        assertEquals("ID should match", id, message.getId());
        assertEquals("Sender should match", sender, message.getSender());
        assertEquals("Content should match", content, message.getContent());
        assertNotNull("Timestamp should not be null", message.getTimestamp());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMessageCreationWithEmptySender() {
        new Message(1, "", "Test message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMessageCreationWithEmptyContent() {
        new Message(1, "student", "");
    }
    
    @Test
    public void testMessageIdsAreUnique() {
        Message message1 = new Message(1, "student", "First message");
        Message message2 = new Message(2, "tutor", "Second message");
        assertNotEquals("Messages should have different IDs", message1.getId(), message2.getId());
    }
    
    @Test
    public void testLongContent() {
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            contentBuilder.append("Very long content ");
        }
        String longContent = contentBuilder.toString();
        Message message = new Message(1, "student", longContent);
        assertEquals("Long content should be preserved", longContent, message.getContent());
    }
    
    @Test
    public void testTimestampIsCurrentTime() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        Message message = new Message(1, "student", "Test message");
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        LocalDateTime timestamp = message.getTimestamp();
        assertTrue("Timestamp should be after the 'before' time", 
                timestamp.isAfter(before) || timestamp.isEqual(before));
        assertTrue("Timestamp should be before the 'after' time", 
                timestamp.isBefore(after) || timestamp.isEqual(after));
    }
}