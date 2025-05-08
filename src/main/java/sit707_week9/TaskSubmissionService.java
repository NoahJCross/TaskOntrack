package sit707_week9;

import java.util.List;

/**
 * @author Noah Cross
 * Service class handling submission and management of tasks in OnTrack
 */
public class TaskSubmissionService {
    private final TaskRepository taskRepository;
    
    public TaskSubmissionService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    /**
     * Submits a new task for the student
     * 
     * @param studentId The ID of the student submitting the task
     * @param title The title of the task
     * @param content The task content or submission
     * @return The submitted task
     */
    public Task submitTask(int studentId, String title, String content) {
        return taskRepository.saveTask(studentId, title, content);
    }
    
    /**
     * Retrieves all tasks belonging to a specific student
     * 
     * @param studentId The ID of the student
     * @return List of tasks submitted by the student
     */
    public List<Task> getStudentTasks(int studentId) {
        return taskRepository.getTasksByStudentId(studentId);
    }
    
    /**
     * Adds feedback to a task
     * 
     * @param taskId The ID of the task
     * @param feedback The feedback content
     * @return The updated task
     */
    public Task addFeedback(int taskId, String feedback) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            return null;
        }
        
        task.setFeedback(feedback);
        return taskRepository.updateTask(task);
    }
    
    /**
     * Adds a message to the task's communication thread
     * 
     * @param taskId The ID of the task
     * @param sender The sender of the message (student or tutor)
     * @param content The message content
     * @return The new message
     */
    public Message addMessage(int taskId, String sender, String content) {
        return taskRepository.addMessageToTask(taskId, sender, content);
    }
    
    /**
     * Retrieves a task by its ID
     * 
     * @param taskId The ID of the task
     * @return The task object
     */
    public Task getTaskById(int taskId) {
        return taskRepository.getTaskById(taskId);
    }
    
    /**
     * Marks a task as completed
     * 
     * @param taskId The ID of the task to complete
     * @return The updated task with COMPLETED status
     */
    public Task completeTask(int taskId) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            return null;
        }
        
        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.updateTask(task);
    }
}