package company.projects;

import company.empoloyees.Manager;
import company.empoloyees.Employee;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Project {
    private final String projectId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Manager projectManager;
    private final List<Employee> participants;
    private final List<Task> tasks;
    private double estimatedHours;
    private double actualHours;
    private ProjectStatus status;

    public enum ProjectStatus {
        PLANNED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }

    public Project(String projectId, String name, String description, LocalDateTime startDate, LocalDateTime endDate, Manager projectManager) {
        validateProjectInput(projectId, name, startDate, endDate, projectManager);
        
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectManager = projectManager;
        this.participants = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.status = ProjectStatus.PLANNED;
        
        addParticipant(projectManager);
    }

    // Package-private methods for internal project management
    void addParticipant(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (!participants.contains(employee)) {
            participants.add(employee);
        }
    }

    void updateActualHours(double hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative");
        }
        this.actualHours += hours;
        if (status == ProjectStatus.PLANNED) {
            status = ProjectStatus.IN_PROGRESS;
        }
    }

    void addTask(Task task) {
        tasks.add(Objects.requireNonNull(task, "Task cannot be null"));
    }

    // Protected methods for project management
    protected void setStatus(ProjectStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "Status cannot be null");
    }

    // Public methods for project information
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Manager getProjectManager() { return projectManager; }
    public ProjectStatus getStatus() { return status; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getActualHours() { return actualHours; }
    public List<Employee> getParticipants() { return Collections.unmodifiableList(participants); }
    public List<Task> getTasks() { return Collections.unmodifiableList(tasks); }

    public double getProgress() {
        if (estimatedHours == 0) return 0;
        return (actualHours / estimatedHours) * 100;
    }

    // Public methods for project updates
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updateDates(LocalDateTime newStartDate, LocalDateTime newEndDate) {
        validateDates(newStartDate, newEndDate);
        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }

    public void changeProjectManager(Manager newManager) {
        Objects.requireNonNull(newManager, "Project manager cannot be null");
        if (!participants.contains(newManager)) {
            participants.add(newManager);
        }
        this.projectManager = newManager;
    }

    public void setEstimatedHours(double hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive");
        }
        this.estimatedHours = hours;
    }

    public void removeParticipant(Employee employee) {
        if (employee.equals(projectManager)) {
            throw new IllegalStateException("Cannot remove project manager from participants");
        }
        participants.remove(employee);
    }

    // Private validation methods
    private void validateProjectInput(String projectId, String name, LocalDateTime startDate, LocalDateTime endDate, Manager projectManager) {
        if (projectId == null || projectId.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        validateDates(startDate, endDate);
        Objects.requireNonNull(projectManager, "Project manager cannot be null");
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Project dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", manager=" + projectManager.getFullName() +
                ", status=" + status +
                ", progress=" + String.format("%.1f%%", getProgress()) +
                ", participants=" + participants.size() +
                ", tasks=" + tasks.size() +
                '}';
    }
}
