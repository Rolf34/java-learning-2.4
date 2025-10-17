package company.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Position {
    private final String positionId;
    private String title;
    private String description;
    private final List<String> requiredSkills;
    private double minSalary;
    private double maxSalary;
    private int accessLevel;

    public Position(String positionId, String title, String description, double minSalary, double maxSalary) {
        validatePositionInput(positionId, title, minSalary, maxSalary);
        
        this.positionId = positionId;
        this.title = title;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.accessLevel = 1; // Default access level
        this.requiredSkills = new ArrayList<>();
    }

    // Public methods
    public void setAccessLevel(int level) {
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Access level must be between 1 and 5");
        }
        this.accessLevel = level;
    }

    // Public getters
    public String getPositionId() { return positionId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getRequiredSkills() { return Collections.unmodifiableList(requiredSkills); }
    public double getMinSalary() { return minSalary; }
    public double getMaxSalary() { return maxSalary; }
    public int getAccessLevel() { return accessLevel; }

    // Public methods for position management
    public void updateTitle(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setSalaryRange(double minSalary, double maxSalary) {
        validateSalaryRange(minSalary, maxSalary);
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public void addRequiredSkill(String skill) {
        Objects.requireNonNull(skill, "Skill cannot be null");
        if (!skill.trim().isEmpty() && !requiredSkills.contains(skill)) {
            requiredSkills.add(skill);
        }
    }

    public void removeRequiredSkill(String skill) {
        requiredSkills.remove(skill);
    }

    public boolean isSalaryInRange(double salary) {
        return salary >= minSalary && salary <= maxSalary;
    }

    // Private validation methods
    private void validatePositionInput(String positionId, String title, double minSalary, double maxSalary) {
        if (positionId == null || positionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Position ID cannot be null or empty");
        }
        validateTitle(title);
        validateSalaryRange(minSalary, maxSalary);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Position title cannot be null or empty");
        }
    }

    private void validateSalaryRange(double minSalary, double maxSalary) {
        if (minSalary < 0) {
            throw new IllegalArgumentException("Minimum salary cannot be negative");
        }
        if (maxSalary < minSalary) {
            throw new IllegalArgumentException("Maximum salary must be greater than or equal to minimum salary");
        }
    }

    @Override
    public String toString() {
        return "Position{" +
                "id='" + positionId + '\'' +
                ", title='" + title + '\'' +
                ", accessLevel=" + accessLevel +
                ", salary=" + String.format("%.2f-%.2f", minSalary, maxSalary) +
                ", skills=" + requiredSkills.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return positionId.equals(position.positionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionId);
    }
}
