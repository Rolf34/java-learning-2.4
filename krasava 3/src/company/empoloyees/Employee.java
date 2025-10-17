package company.empoloyees;

import company.structure.Department;
import company.structure.Position;
import company.structure.WorkSchedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Employee class represents a company employee with all their relevant information.
 * The class implements encapsulation by making fields private and providing controlled access through methods.
 */
public class Employee {
    // Constants for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final double MIN_SALARY = 0.0;

    // Immutable fields
    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final LocalDate hireDate;
    
    // Mutable fields with restricted access
    private String email;
    private String phoneNumber;
    private Department department;
    private Position position;
    private double salary;
    private WorkSchedule workSchedule;
    private boolean isActive;

    /**
     * Creates a new Employee with the specified details.
     * All parameters are validated before assignment.
     *
     * @throws IllegalArgumentException if any validation fails
     * @throws NullPointerException if any required object is null
     */
    public Employee(String employeeId, String firstName, String lastName, String email, 
                   String phoneNumber, Department department, Position position, 
                   String hireDate, double salary, WorkSchedule workSchedule) {
        // Validate all required fields
        this.employeeId = validateEmployeeId(employeeId);
        this.firstName = validateName(firstName, "First name");
        this.lastName = validateName(lastName, "Last name");
        this.email = validateEmail(email);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.position = Objects.requireNonNull(position, "Position cannot be null");
        this.hireDate = validateHireDate(hireDate);
        this.salary = validateSalary(salary);
        this.workSchedule = Objects.requireNonNull(workSchedule, "Work schedule cannot be null");
        this.isActive = true;
    }

    /**
     * Overloaded constructor that allows explicitly setting the active state.
     */
    public Employee(String employeeId, String firstName, String lastName, String email,
                    String phoneNumber, Department department, Position position,
                    String hireDate, double salary, WorkSchedule workSchedule, boolean isActive) {
        this(employeeId, firstName, lastName, email, phoneNumber, department, position, hireDate, salary, workSchedule);
        this.isActive = isActive;
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Department getDepartment() { return department; }
    public Position getPosition() { return position; }
    public LocalDate getHireDate() { return hireDate; }
    public double getSalary() { return salary; }
    public WorkSchedule getWorkSchedule() { return workSchedule; }
    public boolean isActive() { return isActive; }

    // Update methods with validation
    public void updateEmail(String newEmail) {
        this.email = validateEmail(newEmail);
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = validatePhoneNumber(newPhoneNumber);
    }

    public void transferToDepartment(Department newDepartment) {
        this.department = Objects.requireNonNull(newDepartment, "Department cannot be null");
    }

    public void updatePosition(Position newPosition) {
        this.position = Objects.requireNonNull(newPosition, "Position cannot be null");
    }

    public void adjustSalary(double amount) {
        double newSalary = this.salary + amount;
        this.salary = validateSalary(newSalary);
    }

    public void updateWorkSchedule(WorkSchedule newSchedule) {
        this.workSchedule = Objects.requireNonNull(newSchedule, "Work schedule cannot be null");
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void reactivate() {
        this.isActive = true;
    }

    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s', department=%s, position=%s, hireDate=%s, active=%b}",
                employeeId, getFullName(), department.getName(), position.getTitle(), hireDate, isActive);
    }

    // Validation methods
    private static String validateEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        return employeeId.trim();
    }

    private static String validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return name.trim();
    }

    private static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return trimmedEmail;
    }

    private static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String trimmedPhone = phoneNumber.trim();
        if (!PHONE_PATTERN.matcher(trimmedPhone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        return trimmedPhone;
    }

    private static LocalDate validateHireDate(String hireDate) {
        Objects.requireNonNull(hireDate, "Hire date cannot be null");
        try {
            LocalDate date = LocalDate.parse(hireDate, DateTimeFormatter.ISO_DATE);
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Hire date cannot be in the future");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid hire date format. Use ISO date format (YYYY-MM-DD)", e);
        }
    }

    private static double validateSalary(double salary) {
        if (salary < MIN_SALARY) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        return salary;
    }
} 
