package company.projects;

import company.empoloyees.Employee;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    private final String taskId;
    private String title;
    private String description;
    private final Project project;
    private Employee assignedEmployee;
    private double estimatedHours;
    private double actualHours;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private final List<TimeEntry> timeEntries;

    // Enums for better type safety
    public enum TaskStatus {
        NEW, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }

    public enum TaskPriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public Task(String taskId, String title, Project project, Employee assignedEmployee, LocalDateTime dueDate) {
        validateTaskInput(taskId, title, project, dueDate);
        
        this.taskId = taskId;
        this.title = title;
        this.project = project;
        this.assignedEmployee = assignedEmployee;
        this.dueDate = dueDate;
        this.status = TaskStatus.NEW;
        this.priority = TaskPriority.MEDIUM;
        this.timeEntries = new ArrayList<>();
        this.actualHours = 0;
    }

    // Package-private methods for project management
    void logWork(double hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        this.actualHours += hours;
        project.updateActualHours(hours);
        
        if (this.status == TaskStatus.NEW) {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    void addTimeEntry(TimeEntry entry) {
        timeEntries.add(Objects.requireNonNull(entry, "Time entry cannot be null"));
    }

    // Protected methods for task management
    protected void setStatus(TaskStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "Status cannot be null");
    }

    // Public methods for task information
    public String getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public Employee getAssignedEmployee() { return assignedEmployee; }
    public LocalDateTime getDueDate() { return dueDate; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getActualHours() { return actualHours; }

    public double getProgress() {
        if (estimatedHours == 0) return 0;
        return (actualHours / estimatedHours) * 100;
    }

    // Public methods for task updates
    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
    }

    public void reassign(Employee newEmployee) {
        this.assignedEmployee = Objects.requireNonNull(newEmployee, "Employee cannot be null");
    }

    public void setEstimatedHours(double hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive");
        }
        this.estimatedHours = hours;
    }

    public void updateDueDate(LocalDateTime newDueDate) {
        if (newDueDate == null || newDueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }
        this.dueDate = newDueDate;
    }

    // Private validation methods
    private void validateTaskInput(String taskId, String title, Project project, LocalDateTime dueDate) {
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        if (dueDate == null || dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + taskId + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", progress=" + String.format("%.1f%%", getProgress()) +
                ", assignee=" + (assignedEmployee != null ? assignedEmployee.getFullName() : "Unassigned") +
                '}';
    }
}
