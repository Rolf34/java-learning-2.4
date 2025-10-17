package company.empoloyees;

import company.structure.Department;
import company.structure.Position;
import company.structure.WorkSchedule;

public class Admin extends Employee {
    private String adminId;
    private boolean fullAccess;
    private String lastLoginDate;

    public Admin(String employeeId, String firstName, String lastName, String email,
                String phoneNumber, Department department, Position position,
                String hireDate, double salary, WorkSchedule workSchedule,
                boolean isActive, String adminId, boolean fullAccess) {
        super(employeeId, firstName, lastName, email, phoneNumber,
              department, position, hireDate, salary, workSchedule, isActive);
        this.adminId = adminId;
        this.fullAccess = fullAccess;
    }
    void grantAccess(Employee employee, String accessLevel) {
        if (fullAccess) {
            System.out.println("Access granted to " + employee.getFullName() + 
                             " with level: " + accessLevel);
        }
    }
    public boolean hasFullAccess() { return fullAccess; }
    public String getLastLoginDate() { return lastLoginDate; }
}
