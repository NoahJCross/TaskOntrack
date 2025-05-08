package sit707_week9;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Noah Cross
 * Represents a student task or assignment in the OnTrack system
 */
public class Task {
    private int id;
    private int studentId;
    private String title;
    private String content;
    private TaskStatus status;
    private LocalDateTime submissionDate;
    private String feedback;
    private List<Message> messages;
    
    public Task(int id, int studentId, String title, String content) {
        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.content = content;
        this.status = TaskStatus.SUBMITTED;
        this.submissionDate = LocalDateTime.now();
        this.messages = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
        this.status = TaskStatus.FEEDBACK_PROVIDED;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void addMessage(Message message) {
        this.messages.add(message);
    }
}