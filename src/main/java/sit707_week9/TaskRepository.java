package sit707_week9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Noah Cross
 * Repository class for storing and retrieving tasks
 */
public class TaskRepository {
    private Map<Integer, Task> tasks;
    private int nextTaskId;
    private int nextMessageId;
    
    public TaskRepository() {
        this.tasks = new HashMap<>();
        this.nextTaskId = 1;
        this.nextMessageId = 1;
    }
    
    public Task saveTask(int studentId, String title, String content) {
        Task task = new Task(nextTaskId++, studentId, title, content);
        tasks.put(task.getId(), task);
        return task;
    }
    
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }
    
    public List<Task> getTasksByStudentId(int studentId) {
        return tasks.values().stream()
                .filter(task -> task.getStudentId() == studentId)
                .collect(Collectors.toList());
    }
    
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }
    
    public Message addMessageToTask(int taskId, String sender, String content) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return null;
        }
        
        Message message = new Message(nextMessageId++, sender, content);
        task.addMessage(message);
        return message;
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
}