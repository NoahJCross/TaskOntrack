package sit707_week9;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDateTime;

/**
 * @author Noah Cross
 * Test class for Task model
 */
public class TaskTest {
    
    @Test
    public void testTaskCreation() {
        int id = 1;
        int studentId = 123456;
        String title = "Test Task";
        String content = "Test Content";
        
        Task task = new Task(id, studentId, title, content);
        
        assertEquals("ID should match", id, task.getId());
        assertEquals("Student ID should match", studentId, task.getStudentId());
        assertEquals("Title should match", title, task.getTitle());
        assertEquals("Content should match", content, task.getContent());
        assertEquals("Initial status should be SUBMITTED", TaskStatus.SUBMITTED, task.getStatus());
        assertNotNull("Submission date should not be null", task.getSubmissionDate());
        assertNull("Feedback should initially be null", task.getFeedback());
        assertNotNull("Messages list should not be null", task.getMessages());
        assertTrue("Messages list should be empty", task.getMessages().isEmpty());
    }
    
    @Test
    public void testSetStatus() {
        Task task = new Task(1, 123456, "Test Task", "Test Content");
        
        task.setStatus(TaskStatus.FEEDBACK_PROVIDED);
        
        assertEquals("Status should be updated", TaskStatus.FEEDBACK_PROVIDED, task.getStatus());
    }
    
    @Test
    public void testSetFeedback() {
        Task task = new Task(1, 123456, "Test Task", "Test Content");
        String feedback = "Good work";
        
        task.setFeedback(feedback);
        
        assertEquals("Feedback should be set", feedback, task.getFeedback());
        assertEquals("Status should be changed to FEEDBACK_PROVIDED", TaskStatus.FEEDBACK_PROVIDED, task.getStatus());
    }
    
    @Test
    public void testAddMessage() {
        Task task = new Task(1, 123456, "Test Task", "Test Content");
        Message message = new Message(1, "student", "Test message");
        
        task.addMessage(message);
        
        assertEquals("Task should have 1 message", 1, task.getMessages().size());
        assertEquals("Message should be the one added", message, task.getMessages().get(0));
    }
    
    @Test
    public void testSubmissionDateIsCurrentTime() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        
        Task task = new Task(1, 123456, "Test Task", "Test Content");
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        
        LocalDateTime submissionDate = task.getSubmissionDate();
        assertTrue("Submission date should be after the 'before' time", 
                submissionDate.isAfter(before) || submissionDate.isEqual(before));
        assertTrue("Submission date should be before the 'after' time", 
                submissionDate.isBefore(after) || submissionDate.isEqual(after));
    }
}