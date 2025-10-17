package company.structure;

import company.empoloyees.Employee;
import company.empoloyees.Manager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Department {
    private final String departmentId;
    private String name;
    private String description;
    private Manager manager;
    private final List<Employee> employees;
    private double budget;

    public Department(String departmentId, String name, String description) {
        validateDepartmentInput(departmentId, name);
        
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.employees = new ArrayList<>();
    }

    // Public methods for department management
    public void setManager(Manager manager) {
        this.manager = Objects.requireNonNull(manager, "Manager cannot be null");
        if (!employees.contains(manager)) {
            employees.add(manager);
        }
    }

    void addEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }

    void removeEmployee(Employee employee) {
        if (employee.equals(manager)) {
            throw new IllegalStateException("Cannot remove department manager");
        }
        employees.remove(employee);
    }

    // Public getters
    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Manager getManager() { return manager; }
    public List<Employee> getEmployees() { return Collections.unmodifiableList(employees); }
    public double getBudget() { return budget; }
    public int getEmployeeCount() { return employees.size(); }

    // Public methods for department management
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setBudget(double newBudget) {
        if (newBudget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.budget = newBudget;
    }

    public double getTotalSalaries() {
        return employees.stream()
                       .mapToDouble(Employee::getSalary)
                       .sum();
    }

    public boolean isWithinBudget() {
        return getTotalSalaries() <= budget;
    }

    // Private validation methods
    private void validateDepartmentInput(String departmentId, String name) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty");
        }
        validateName(name);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", manager=" + (manager != null ? manager.getFullName() : "None") +
                ", employees=" + employees.size() +
                ", budget=" + String.format("%.2f", budget) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return departmentId.equals(that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId);
    }
}
