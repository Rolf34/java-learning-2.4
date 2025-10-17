package company.empoloyees;

import company.structure.Department;
import company.structure.Position;
import company.structure.WorkSchedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HRSpecialist extends Employee {
    private final List<String> accessPermissions;
    private final List<Department> managedDepartments;

    public HRSpecialist(String employeeId, String firstName, String lastName,
                       String email, String phoneNumber, Department department,
                       Position position, String hireDate, double salary,
                       WorkSchedule workSchedule) {
        super(employeeId, firstName, lastName, email, phoneNumber,
              department, position, hireDate, salary, workSchedule, true);
        this.accessPermissions = new ArrayList<>();
        this.managedDepartments = new ArrayList<>();
        addDefaultPermissions();
    }

    // Package-private methods
    void addManagedDepartment(Department department) {
        Objects.requireNonNull(department, "Department cannot be null");
        if (!managedDepartments.contains(department)) {
            managedDepartments.add(department);
        }
    }

    void removeManagedDepartment(Department department) {
        managedDepartments.remove(department);
    }

    void grantPermission(String permission) {
        if (permission != null && !permission.trim().isEmpty() && !accessPermissions.contains(permission)) {
            accessPermissions.add(permission);
        }
    }

    void revokePermission(String permission) {
        accessPermissions.remove(permission);
    }

    // Public methods
    public List<Department> getManagedDepartments() {
        return Collections.unmodifiableList(managedDepartments);
    }

    public List<String> getAccessPermissions() {
        return Collections.unmodifiableList(accessPermissions);
    }

    public boolean canManageDepartment(Department department) {
        return managedDepartments.contains(department);
    }

    public boolean hasPermission(String permission) {
        return accessPermissions.contains(permission);
    }

    public void createEmployeeProfile(String employeeId, String firstName, 
                                    String lastName, Department department) {
        validateEmployeeData(employeeId, firstName, lastName, department);
        // Implementation for creating new employee profiles
        System.out.println("Creating new employee profile: " + firstName + " " + lastName);
    }

    public void updateEmployeeDepartment(Employee employee, Department newDepartment) {
        if (!canManageDepartment(newDepartment)) {
            throw new IllegalStateException("HR Specialist does not have permission to manage this department");
        }
        employee.transferToDepartment(newDepartment);
    }

    // Private helper methods
    private void addDefaultPermissions() {
        accessPermissions.add("VIEW_EMPLOYEE_PROFILES");
        accessPermissions.add("CREATE_EMPLOYEE_PROFILES");
        accessPermissions.add("UPDATE_EMPLOYEE_INFO");
        accessPermissions.add("VIEW_DEPARTMENTS");
    }

    private void validateEmployeeData(String employeeId, String firstName, String lastName, Department department) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        if (!canManageDepartment(department)) {
            throw new IllegalStateException("HR Specialist does not have permission to manage this department");
        }
    }

    @Override
    public String toString() {
        return "HRSpecialist{" +
                "name='" + getFullName() + '\'' +
                ", managedDepartments=" + managedDepartments.size() +
                ", permissions=" + accessPermissions.size() +
                '}';
    }
}
