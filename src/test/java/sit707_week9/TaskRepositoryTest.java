package sit707_week9;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * @author Noah Cross
 * Test class for TaskRepository
 */
public class TaskRepositoryTest {

    private TaskRepository taskRepository;
    
    @Before
    public void setUp() {
        taskRepository = new TaskRepository();
    }
    
    @Test
    public void testSaveTask() {
        int studentId = 123456;
        String title = "Test Task";
        String content = "Test Content";

        Task savedTask = taskRepository.saveTask(studentId, title, content);

        assertNotNull("Saved task should not be null", savedTask);
        assertEquals("Student ID should match", studentId, savedTask.getStudentId());
        assertEquals("Title should match", title, savedTask.getTitle());
        assertEquals("Content should match", content, savedTask.getContent());
        assertTrue("Task ID should be positive", savedTask.getId() > 0);
    }
    
    @Test
    public void testGetTaskById() {
        Task task = taskRepository.saveTask(123456, "Test Task", "Test Content");
        int taskId = task.getId();
        Task retrievedTask = taskRepository.getTaskById(taskId);
        assertNotNull("Retrieved task should not be null", retrievedTask);
        assertEquals("Task ID should match", taskId, retrievedTask.getId());
    }
    
    @Test
    public void testGetTaskByIdWhenTaskDoesNotExist() {
        Task retrievedTask = taskRepository.getTaskById(999);
        assertNull("Task should be null for non-existent ID", retrievedTask);
    }
    
    @Test
    public void testGetTasksByStudentId() {
        int studentId1 = 123456;
        int studentId2 = 987654;
        
        taskRepository.saveTask(studentId1, "Task 1", "Content 1");
        taskRepository.saveTask(studentId1, "Task 2", "Content 2");
        taskRepository.saveTask(studentId2, "Task 3", "Content 3");

        List<Task> studentTasks = taskRepository.getTasksByStudentId(studentId1);

        assertEquals("Student 1 should have 2 tasks", 2, studentTasks.size());
        for (Task task : studentTasks) {
            assertEquals("Task should belong to student 1", studentId1, task.getStudentId());
        }
    }
    
    @Test
    public void testGetTasksByStudentIdWhenNoTasks() {
        List<Task> studentTasks = taskRepository.getTasksByStudentId(999);
        assertNotNull("Result should not be null", studentTasks);
        assertTrue("Result should be empty", studentTasks.isEmpty());
    }
    
    @Test
    public void testUpdateTask() {
        Task task = taskRepository.saveTask(123456, "Original Title", "Original Content");
        int taskId = task.getId();

        task.setStatus(TaskStatus.FEEDBACK_PROVIDED);
        task.setFeedback("Feedback added");

        Task updatedTask = taskRepository.updateTask(task);

        assertNotNull("Updated task should not be null", updatedTask);
        assertEquals("Status should be updated", TaskStatus.FEEDBACK_PROVIDED, updatedTask.getStatus());
        assertEquals("Feedback should be updated", "Feedback added", updatedTask.getFeedback());

        Task retrievedTask = taskRepository.getTaskById(taskId);
        assertEquals("Status should be updated in the repository", TaskStatus.FEEDBACK_PROVIDED, retrievedTask.getStatus());
        assertEquals("Feedback should be updated in the repository", "Feedback added", retrievedTask.getFeedback());
    }
    
    @Test
    public void testAddMessageToTask() {
        Task task = taskRepository.saveTask(123456, "Test Task", "Test Content");
        int taskId = task.getId();
        String sender = "student";
        String content = "Test message";

        Message message = taskRepository.addMessageToTask(taskId, sender, content);

        assertNotNull("Message should not be null", message);
        assertEquals("Sender should match", sender, message.getSender());
        assertEquals("Content should match", content, message.getContent());

        Task taskWithMessage = taskRepository.getTaskById(taskId);
        assertEquals("Task should have 1 message", 1, taskWithMessage.getMessages().size());
        assertEquals("Message content should match", content, taskWithMessage.getMessages().get(0).getContent());
    }
    
    @Test
    public void testAddMessageToNonExistentTask() {
        Message message = taskRepository.addMessageToTask(999, "student", "Test message");
        assertNull("Message should be null for non-existent task", message);
    }
    
    @Test
    public void testGetAllTasks() {
        taskRepository.saveTask(123, "Task 1", "Content 1");
        taskRepository.saveTask(456, "Task 2", "Content 2");
        taskRepository.saveTask(789, "Task 3", "Content 3");
        List<Task> allTasks = taskRepository.getAllTasks();
        assertEquals("Repository should have 3 tasks", 3, allTasks.size());
    }
    
    @Test
    public void testGetAllTasksWhenEmpty() {
        List<Task> allTasks = taskRepository.getAllTasks();
        assertNotNull("Result should not be null", allTasks);
        assertTrue("Result should be empty", allTasks.isEmpty());
    }
    
    @Test
    public void testTaskIdsAreUnique() {
        Task task1 = taskRepository.saveTask(123, "Task 1", "Content 1");
        Task task2 = taskRepository.saveTask(123, "Task 2", "Content 2");
        Task task3 = taskRepository.saveTask(123, "Task 3", "Content 3");
        assertNotEquals("Tasks should have different IDs", task1.getId(), task2.getId());
        assertNotEquals("Tasks should have different IDs", task2.getId(), task3.getId());
        assertNotEquals("Tasks should have different IDs", task1.getId(), task3.getId());
    }
    
    @Test
    public void testMessageIdsAreUnique() {
        Task task = taskRepository.saveTask(123, "Test Task", "Test Content");
        int taskId = task.getId();
        Message message1 = taskRepository.addMessageToTask(taskId, "student", "Message 1");
        Message message2 = taskRepository.addMessageToTask(taskId, "tutor", "Message 2");
        assertNotEquals("Messages should have different IDs", message1.getId(), message2.getId());
    }
}