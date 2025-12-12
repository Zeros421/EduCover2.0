/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

/**
 *
 * @author Fia
 */
public abstract class AcademicTask {
        private final String taskId;  
    private final String studentId;   
    private final String courseCode; 
    private final String componentName;

    private String weekRange; 
    private String description;    
    private String deadline;   
    private TaskStatus status;       

    protected AcademicTask(String taskId,
                           String studentId,
                           String courseCode,
                           String componentName) {
        this.taskId = taskId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.componentName = componentName;
        this.status = TaskStatus.NOT_STARTED;
        this.weekRange = "";
        this.description = "";
        this.deadline = "";
    }

    public String getId() {
        return taskId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public abstract String getTaskType();
}
