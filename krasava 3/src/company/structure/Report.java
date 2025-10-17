package company.structure;

import company.empoloyees.Employee;
import company.projects.TimeEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.List;
import java.util.Objects;

public class Report {
    private final String reportId;
    private final String title;
    private final ReportType type;
    private final Employee generatedBy;
    private final LocalDateTime generationDate;
    private final LocalDateTime periodStart;
    private final LocalDateTime periodEnd;
    private String format;
    private ReportStatus status;

    public enum ReportType {
        TIME_TRACKING,
        DEPARTMENT_SUMMARY,
        PROJECT_PROGRESS,
        EMPLOYEE_PERFORMANCE
    }

    public enum ReportStatus {
        DRAFT,
        GENERATED,
        EXPORTED,
        ARCHIVED
    }

    public Report(String reportId, String title, ReportType type, 
                 Employee generatedBy, LocalDateTime periodStart, LocalDateTime periodEnd) {
        // Validate input
        Objects.requireNonNull(reportId, "Report ID cannot be null");
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(type, "Report type cannot be null");
        Objects.requireNonNull(generatedBy, "Generator cannot be null");
        Objects.requireNonNull(periodStart, "Period start cannot be null");
        Objects.requireNonNull(periodEnd, "Period end cannot be null");
        
        if (periodEnd.isBefore(periodStart)) {
            throw new IllegalArgumentException("Period end cannot be before period start");
        }

        this.reportId = reportId;
        this.title = title;
        this.type = type;
        this.generatedBy = generatedBy;
        this.generationDate = LocalDateTime.now();
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.format = "PDF";
        this.status = ReportStatus.DRAFT;
    }

    // Getters
    public String getReportId() { return reportId; }
    public String getTitle() { return title; }
    public ReportType getType() { return type; }
    public Employee getGeneratedBy() { return generatedBy; }
    public LocalDateTime getGenerationDate() { return generationDate; }
    public LocalDateTime getPeriodStart() { return periodStart; }
    public LocalDateTime getPeriodEnd() { return periodEnd; }
    public String getFormat() { return format; }
    public ReportStatus getStatus() { return status; }

    public void generateTimeReport(List<TimeEntry> entries) {
        Objects.requireNonNull(entries, "Entries list cannot be null");
        if (type != ReportType.TIME_TRACKING) {
            throw new IllegalStateException("This report is not configured for time tracking");
        }
        
        System.out.println("Generating time report: " + title);
        // Implementation for time report generation
        status = ReportStatus.GENERATED;
    }

    public void exportReport(String format) {
        Objects.requireNonNull(format, "Format cannot be null");
        if (status != ReportStatus.GENERATED) {
            throw new IllegalStateException("Report must be generated before export");
        }
        
        this.format = format.toUpperCase();
        System.out.println("Exporting report in " + this.format + " format");
        status = ReportStatus.EXPORTED;
    }

    public void archive() {
        if (status != ReportStatus.EXPORTED) {
            throw new IllegalStateException("Report must be exported before archiving");
        }
        status = ReportStatus.ARCHIVED;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + reportId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", period=" + periodStart + " to " + periodEnd +
                ", format='" + format + '\'' +
                '}';
    }
}
