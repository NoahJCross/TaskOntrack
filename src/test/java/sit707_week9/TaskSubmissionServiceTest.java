package sit707_week9;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * @author Noah Cross
 * Test class for TaskSubmissionService
 */
public class TaskSubmissionServiceTest {

    private TaskSubmissionService taskService;
    private TaskRepository taskRepository;

    @Before
    public void setUp() {
        taskRepository = new TaskRepository();
        taskService = new TaskSubmissionService(taskRepository);
    }

    @Test
    public void testSubmitTask() {
        // Arrange
        int studentId = 123456;
        String taskTitle = "Java Programming Assignment";
        String taskContent = "Implement a sorting algorithm";
        
        // Act
        Task submittedTask = taskService.submitTask(studentId, taskTitle, taskContent);
        
        // Assert
        assertNotNull("Submitted task should not be null", submittedTask);
        assertEquals("Student ID should match", studentId, submittedTask.getStudentId());
        assertEquals("Task title should match", taskTitle, submittedTask.getTitle());
        assertEquals("Task content should match", taskContent, submittedTask.getContent());
        assertEquals("Task status should be SUBMITTED", TaskStatus.SUBMITTED, submittedTask.getStatus());
    }
    
    @Test
    public void testSubmitTaskWithEmptyContent() {
        // Arrange
        int studentId = 123456;
        String taskTitle = "Empty Content Assignment";
        String taskContent = "";
        
        // Act
        Task submittedTask = taskService.submitTask(studentId, taskTitle, taskContent);
        
        // Assert
        assertNotNull("Task with empty content should still be created", submittedTask);
        assertEquals("Empty content should be preserved", "", submittedTask.getContent());
    }
    
    @Test
    public void testSubmitTaskWithLongContent() {
        // Arrange
        int studentId = 123456;
        String taskTitle = "Long Content Assignment";
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            contentBuilder.append("Very long content ");
        }
        String taskContent = contentBuilder.toString();
        
        // Act
        Task submittedTask = taskService.submitTask(studentId, taskTitle, taskContent);
        
        // Assert
        assertNotNull("Task with long content should be created", submittedTask);
        assertEquals("Long content should be preserved", taskContent, submittedTask.getContent());
    }

    @Test
    public void testGetStudentTasks() {
        // Arrange
        int studentId = 123456;
        taskService.submitTask(studentId, "Task 1", "Content 1");
        taskService.submitTask(studentId, "Task 2", "Content 2");
        taskService.submitTask(987654, "Task 3", "Content 3"); // Different student
        
        // Act
        List<Task> studentTasks = taskService.getStudentTasks(studentId);
        
        // Assert
        assertEquals("Student should have 2 tasks", 2, studentTasks.size());
        assertTrue("All tasks should belong to the student", 
            studentTasks.stream().allMatch(task -> task.getStudentId() == studentId));
    }
    
    @Test
    public void testGetStudentTasksWhenNoTasks() {
        // Arrange
        int studentId = 999999; // Student with no tasks
        
        // Act
        List<Task> studentTasks = taskService.getStudentTasks(studentId);
        
        // Assert
        assertNotNull("Result should not be null for student with no tasks", studentTasks);
        assertTrue("Result should be empty for student with no tasks", studentTasks.isEmpty());
    }
    
    @Test
    public void testGetTaskById() {
        // Arrange
        int studentId = 123456;
        Task submittedTask = taskService.submitTask(studentId, "Test Task", "Test Content");
        int taskId = submittedTask.getId();
        
        // Act
        Task retrievedTask = taskService.getTaskById(taskId);
        
        // Assert
        assertNotNull("Retrieved task should not be null", retrievedTask);
        assertEquals("Retrieved task ID should match", taskId, retrievedTask.getId());
        assertEquals("Retrieved task title should match", "Test Task", retrievedTask.getTitle());
    }
    
    @Test
    public void testGetTaskByIdWhenTaskDoesNotExist() {
        // Arrange
        int nonExistentTaskId = 99999;
        
        // Act
        Task retrievedTask = taskService.getTaskById(nonExistentTaskId);
        
        // Assert
        assertNull("Task should be null for non-existent ID", retrievedTask);
    }
    
    @Test
    public void testAddFeedback() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String feedback = "Good work, but could improve formatting";
        
        // Act
        Task updatedTask = taskService.addFeedback(taskId, feedback);
        
        // Assert
        assertNotNull("Updated task should not be null", updatedTask);
        assertEquals("Feedback should be added to the task", feedback, updatedTask.getFeedback());
        assertEquals("Task status should change to FEEDBACK_PROVIDED", 
            TaskStatus.FEEDBACK_PROVIDED, updatedTask.getStatus());
    }
    
    @Test
    public void testAddFeedbackWhenTaskDoesNotExist() {
        // Arrange
        int nonExistentTaskId = 99999;
        String feedback = "This feedback should not be added";
        
        // Act
        Task updatedTask = taskService.addFeedback(nonExistentTaskId, feedback);
        
        // Assert
        assertNull("Result should be null when task doesn't exist", updatedTask);
    }
    
    @Test
    public void testOverwriteFeedback() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String initialFeedback = "Initial feedback";
        taskService.addFeedback(taskId, initialFeedback);
        
        String updatedFeedback = "Updated feedback";
        
        // Act
        Task taskWithUpdatedFeedback = taskService.addFeedback(taskId, updatedFeedback);
        
        // Assert
        assertEquals("Feedback should be updated", updatedFeedback, taskWithUpdatedFeedback.getFeedback());
    }
    
    @Test
    public void testAddMessage() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String sender = "student";
        String messageContent = "I have a question about the assignment";
        
        // Act
        Message message = taskService.addMessage(taskId, sender, messageContent);
        
        // Assert
        assertNotNull("Message should not be null", message);
        assertEquals("Sender should match", sender, message.getSender());
        assertEquals("Message content should match", messageContent, message.getContent());
        
        // Task's message list should contain the new message
        Task updatedTask = taskService.getTaskById(taskId);
        assertEquals("Task should have 1 message", 1, updatedTask.getMessages().size());
    }
    
    @Test
    public void testAddMessageWhenTaskDoesNotExist() {
        // Arrange
        int nonExistentTaskId = 99999;
        String sender = "student";
        String messageContent = "This message should not be added";
        
        // Act
        Message message = taskService.addMessage(nonExistentTaskId, sender, messageContent);
        
        // Assert
        assertNull("Result should be null when task doesn't exist", message);
    }
    
    @Test
    public void testAddMultipleMessagesToTask() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        
        // Act
        taskService.addMessage(taskId, "student", "Message 1");
        taskService.addMessage(taskId, "tutor", "Message 2");
        taskService.addMessage(taskId, "student", "Message 3");
        
        // Assert
        Task updatedTask = taskService.getTaskById(taskId);
        assertEquals("Task should have 3 messages", 3, updatedTask.getMessages().size());
        assertEquals("First message should be from student", "student", updatedTask.getMessages().get(0).getSender());
        assertEquals("Second message should be from tutor", "tutor", updatedTask.getMessages().get(1).getSender());
    }
    
    @Test
    public void testCompleteTask() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        
        // Act
        Task completedTask = taskService.completeTask(taskId);
        
        // Assert
        assertNotNull("Completed task should not be null", completedTask);
        assertEquals("Task status should be COMPLETED", TaskStatus.COMPLETED, completedTask.getStatus());
    }
    
    @Test
    public void testCompleteTaskWhenTaskDoesNotExist() {
        // Arrange
        int nonExistentTaskId = 99999;
        
        // Act
        Task completedTask = taskService.completeTask(nonExistentTaskId);
        
        // Assert
        assertNull("Result should be null when task doesn't exist", completedTask);
    }
    
    @Test
    public void testCompleteTaskAfterFeedback() {
        // Arrange
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addFeedback(taskId, "Good work");
        
        // Act
        Task completedTask = taskService.completeTask(taskId);
        
        // Assert
        assertEquals("Task status should be COMPLETED", TaskStatus.COMPLETED, completedTask.getStatus());
        assertEquals("Task feedback should be preserved", "Good work", completedTask.getFeedback());
    }
    
    @Test
    public void testTaskSubmissionDateIsSet() {
        // Arrange
        int studentId = 123456;
        
        // Act
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        
        // Assert
        assertNotNull("Submission date should not be null", task.getSubmissionDate());
    }
    
    @Test
    public void testSubmitMultipleTasksGeneratesUniqueIds() {
        // Arrange
        int studentId = 123456;
        
        // Act
        Task task1 = taskService.submitTask(studentId, "Assignment 1", "Content 1");
        Task task2 = taskService.submitTask(studentId, "Assignment 2", "Content 2");
        
        // Assert
        assertNotEquals("Tasks should have different IDs", task1.getId(), task2.getId());
    }
}