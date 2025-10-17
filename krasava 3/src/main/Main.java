package main;

import company.empoloyees.*;
import company.structure.*;
import company.projects.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize department structure
            Department itDep = new Department("D001", "IT", "Information Technology Department");
            Department hrDep = new Department("D002", "HR", "Human Resources Department");

            // Create positions with salary ranges and required skills
            Position devPos = new Position("P001", "Software Developer", "Development of software solutions", 25000, 45000);
            devPos.addRequiredSkill("Java");
            devPos.addRequiredSkill("SQL");
            devPos.setAccessLevel(2);

            Position managerPos = new Position("P002", "Project Manager", "Project and team management", 35000, 55000);
            managerPos.addRequiredSkill("Project Management");
            managerPos.addRequiredSkill("Leadership");
            managerPos.setAccessLevel(3);

            // Create work schedules
            WorkSchedule standardSchedule = new WorkSchedule(
                "WS001",
                "Standard Schedule",
                5,
                8.0,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                Duration.ofHours(1),
                false
            );

            WorkSchedule flexibleSchedule = new WorkSchedule(
                "WS002",
                "Flexible Schedule",
                5,
                8.0,
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                Duration.ofMinutes(45),
                true
            );

            // Create employees
            Manager itManager = createManager("M001", "Ivan", "Petrenko", itDep, managerPos, standardSchedule);
            Manager hrManager = createManager("M002", "Maria", "Kovalenko", hrDep, managerPos, standardSchedule);

            Employee developer1 = createDeveloper("E001", "Dmytro", "Shvaika", itDep, devPos, flexibleSchedule);
            Employee developer2 = createDeveloper("E002", "Anna", "Melnyk", itDep, devPos, flexibleSchedule);

            HRSpecialist hrSpecialist = createHRSpecialist("H001", "Olena", "Lysenko", hrDep, standardSchedule);

            // Set department budgets and managers
            itDep.setBudget(200000);
            hrDep.setBudget(150000);
            itDep.setManager(itManager);
            hrDep.setManager(hrManager);

            // Create and setup project
            Project webPortal = new Project(
                "P001",
                "Company Web Portal",
                "Development of internal web portal",
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                itManager
            );

            // Create tasks
            Task frontendTask = new Task(
                "T001",
                "Frontend Development",
                webPortal,
                developer1,
                LocalDateTime.now().plusWeeks(2)
            );
            frontendTask.setEstimatedHours(80);
            frontendTask.setPriority(Task.TaskPriority.HIGH);

            Task backendTask = new Task(
                "T002",
                "Backend Development",
                webPortal,
                developer2,
                LocalDateTime.now().plusWeeks(4)
            );
            backendTask.setEstimatedHours(120);
            backendTask.setPriority(Task.TaskPriority.HIGH);

            // Log work time
            TimeEntry timeEntry1 = new TimeEntry(
                "TE001",
                developer1,
                webPortal,
                frontendTask,
                LocalDateTime.now().minusHours(4)
            );
            timeEntry1.stopWork(LocalDateTime.now());
            timeEntry1.updateDescription("Implemented user interface components");

            // Print system state
            System.out.println("\n=== Department Information ===");
            System.out.println(itDep);
            System.out.println(hrDep);

            System.out.println("\n=== Project Status ===");
            System.out.println(webPortal);
            System.out.println(frontendTask);
            System.out.println(backendTask);

            System.out.println("\n=== Time Tracking ===");
            System.out.println(timeEntry1);

            System.out.println("\n=== Budget Analysis ===");
            System.out.println("IT Department total salaries: " + itDep.getTotalSalaries());
            System.out.println("IT Department within budget: " + itDep.isWithinBudget());

        } catch (Exception e) {
            System.err.println("Error in system initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Manager createManager(String id, String firstName, String lastName, 
                                      Department dept, Position pos, WorkSchedule schedule) {
        return new Manager(id, firstName, lastName,
                         firstName.toLowerCase() + "." + lastName.toLowerCase() + "@company.com",
                         "+380931234" + id.substring(1),
                         dept, pos, "2023-01-01", 45000, schedule, true, dept);
    }

    private static Employee createDeveloper(String id, String firstName, String lastName,
                                          Department dept, Position pos, WorkSchedule schedule) {
        return new Employee(id, firstName, lastName,
                          firstName.toLowerCase() + "." + lastName.toLowerCase() + "@company.com",
                          "+380931234" + id.substring(1),
                          dept, pos, "2023-01-01", 35000, schedule, true);
    }

    private static HRSpecialist createHRSpecialist(String id, String firstName, String lastName,
                                                 Department dept, WorkSchedule schedule) {
        Position hrPos = new Position("PH001", "HR Specialist", "Human resources management", 30000, 45000);
        return new HRSpecialist(id, firstName, lastName,
                              firstName.toLowerCase() + "." + lastName.toLowerCase() + "@company.com",
                              "+380931234" + id.substring(1),
                              dept, hrPos, "2023-01-01", 35000, schedule);
    }
}
