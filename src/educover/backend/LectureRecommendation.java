/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

/**
 *
 * @author Fia
 */
public class LectureRecommendation extends AcademicTask {
    public LectureRecommendation(String taskId,
                                 String studentId,
                                 String courseCode,
                                 String componentName,
                                 String weekRange,
                                 String description,
                                 String deadline,
                                 TaskStatus status) {
        super(taskId, studentId, courseCode, componentName);
        setWeekRange(weekRange);
        setDescription(description);
        setDeadline(deadline);
        setStatus(status);
    }

    @Override
    public String getTaskType() {
        return "LECTURE_RECOMMENDATION";
    }
}
