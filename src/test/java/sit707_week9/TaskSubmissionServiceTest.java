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
        int studentId = 123456;
        String taskTitle = "Java Programming Assignment";
        String taskContent = "Implement a sorting algorithm";
        
        Task submittedTask = taskService.submitTask(studentId, taskTitle, taskContent);
        
        assertNotNull("Submitted task should not be null", submittedTask);
        assertEquals("Student ID should match", studentId, submittedTask.getStudentId());
        assertEquals("Task title should match", taskTitle, submittedTask.getTitle());
        assertEquals("Task content should match", taskContent, submittedTask.getContent());
        assertEquals("Task status should be SUBMITTED", TaskStatus.SUBMITTED, submittedTask.getStatus());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitTaskWithEmptyContent() {
        int studentId = 123456;
        String taskTitle = "Empty Content Assignment";
        String taskContent = "";
        taskService.submitTask(studentId, taskTitle, taskContent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitTaskWithNullContent() {
        int studentId = 123456;
        String taskTitle = "Null Content Assignment";
        taskService.submitTask(studentId, taskTitle, null);
    }
    
    @Test
    public void testSubmitTaskWithLongContent() {
        int studentId = 123456;
        String taskTitle = "Long Content Assignment";
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            contentBuilder.append("Very long content ");
        }
        String taskContent = contentBuilder.toString();
        
        Task submittedTask = taskService.submitTask(studentId, taskTitle, taskContent);
        
        assertNotNull("Task with long content should be created", submittedTask);
        assertEquals("Long content should be preserved", taskContent, submittedTask.getContent());
    }

    @Test
    public void testGetStudentTasks() {
        int studentId = 123456;
        taskService.submitTask(studentId, "Task 1", "Content 1");
        taskService.submitTask(studentId, "Task 2", "Content 2");
        taskService.submitTask(987654, "Task 3", "Content 3"); 
        
        List<Task> studentTasks = taskService.getStudentTasks(studentId);
        
        assertEquals("Student should have 2 tasks", 2, studentTasks.size());
        assertTrue("All tasks should belong to the student", 
            studentTasks.stream().allMatch(task -> task.getStudentId() == studentId));
    }
    
    @Test
    public void testGetStudentTasksWhenNoTasks() {
        int studentId = 999999; 
        List<Task> studentTasks = taskService.getStudentTasks(studentId);
        assertNotNull("Result should not be null for student with no tasks", studentTasks);
        assertTrue("Result should be empty for student with no tasks", studentTasks.isEmpty());
    }
    
    @Test
    public void testGetTaskById() {
        int studentId = 123456;
        Task submittedTask = taskService.submitTask(studentId, "Test Task", "Test Content");
        int taskId = submittedTask.getId();
        
        Task retrievedTask = taskService.getTaskById(taskId);

        assertNotNull("Retrieved task should not be null", retrievedTask);
        assertEquals("Retrieved task ID should match", taskId, retrievedTask.getId());
        assertEquals("Retrieved task title should match", "Test Task", retrievedTask.getTitle());
    }
    
    @Test
    public void testGetTaskByIdWhenTaskDoesNotExist() {
        int nonExistentTaskId = 99999;
        Task retrievedTask = taskService.getTaskById(nonExistentTaskId);
        assertNull("Task should be null for non-existent ID", retrievedTask);
    }
    
    @Test
    public void testAddFeedback() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String feedback = "Good work, but could improve formatting";
        
        Task updatedTask = taskService.addFeedback(taskId, feedback);
        
        assertNotNull("Updated task should not be null", updatedTask);
        assertEquals("Feedback should be added to the task", feedback, updatedTask.getFeedback());
        assertEquals("Task status should change to FEEDBACK_PROVIDED", 
            TaskStatus.FEEDBACK_PROVIDED, updatedTask.getStatus());
    }
    
    @Test
    public void testAddFeedbackWhenTaskDoesNotExist() {
        int nonExistentTaskId = 99999;
        String feedback = "This feedback should not be added";
        Task updatedTask = taskService.addFeedback(nonExistentTaskId, feedback);
        assertNull("Result should be null when task doesn't exist", updatedTask);
    }
    
    @Test
    public void testOverwriteFeedback() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String initialFeedback = "Initial feedback";
        taskService.addFeedback(taskId, initialFeedback);
        
        String updatedFeedback = "Updated feedback";
        
        Task taskWithUpdatedFeedback = taskService.addFeedback(taskId, updatedFeedback);
        
        assertEquals("Feedback should be updated", updatedFeedback, taskWithUpdatedFeedback.getFeedback());
    }
    
    @Test
    public void testAddMessage() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        String sender = "student";
        String messageContent = "I have a question about the assignment";
        
        Message message = taskService.addMessage(taskId, sender, messageContent);
        
        assertNotNull("Message should not be null", message);
        assertEquals("Sender should match", sender, message.getSender());
        assertEquals("Message content should match", messageContent, message.getContent());
        
        Task updatedTask = taskService.getTaskById(taskId);
        assertEquals("Task should have 1 message", 1, updatedTask.getMessages().size());
    }
    
    @Test
    public void testAddMessageWhenTaskDoesNotExist() {
        int nonExistentTaskId = 99999;
        String sender = "student";
        String messageContent = "This message should not be added";
        Message message = taskService.addMessage(nonExistentTaskId, sender, messageContent);
        assertNull("Result should be null when task doesn't exist", message);
    }
    
    @Test
    public void testAddMultipleMessagesToTask() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();

        taskService.addMessage(taskId, "student", "Message 1");
        taskService.addMessage(taskId, "tutor", "Message 2");
        taskService.addMessage(taskId, "student", "Message 3");

        Task updatedTask = taskService.getTaskById(taskId);
        assertEquals("Task should have 3 messages", 3, updatedTask.getMessages().size());
        assertEquals("First message should be from student", "student", updatedTask.getMessages().get(0).getSender());
        assertEquals("Second message should be from tutor", "tutor", updatedTask.getMessages().get(1).getSender());
    }
    
    @Test
    public void testCompleteTask() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();

        Task completedTask = taskService.completeTask(taskId);

        assertNotNull("Completed task should not be null", completedTask);
        assertEquals("Task status should be COMPLETED", TaskStatus.COMPLETED, completedTask.getStatus());
    }
    
    @Test
    public void testCompleteTaskWhenTaskDoesNotExist() {
        int nonExistentTaskId = 99999;
        Task completedTask = taskService.completeTask(nonExistentTaskId);
        assertNull("Result should be null when task doesn't exist", completedTask);
    }
    
    @Test
    public void testCompleteTaskAfterFeedback() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addFeedback(taskId, "Good work");
        Task completedTask = taskService.completeTask(taskId);
        assertEquals("Task status should be COMPLETED", TaskStatus.COMPLETED, completedTask.getStatus());
        assertEquals("Task feedback should be preserved", "Good work", completedTask.getFeedback());
    }
    
    @Test
    public void testTaskSubmissionDateIsSet() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        assertNotNull("Submission date should not be null", task.getSubmissionDate());
    }
    
    @Test
    public void testSubmitMultipleTasksGeneratesUniqueIds() {
        int studentId = 123456;
        Task task1 = taskService.submitTask(studentId, "Assignment 1", "Content 1");
        Task task2 = taskService.submitTask(studentId, "Assignment 2", "Content 2");
        assertNotEquals("Tasks should have different IDs", task1.getId(), task2.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMessageWithEmptySender() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addMessage(taskId, "", "Test message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMessageWithNullSender() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addMessage(taskId, null, "Test message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMessageWithEmptyContent() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addMessage(taskId, "student", "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMessageWithNullContent() {
        int studentId = 123456;
        Task task = taskService.submitTask(studentId, "Assignment", "Content");
        int taskId = task.getId();
        taskService.addMessage(taskId, "student", null);
    }
}