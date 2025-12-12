/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;
import java.util.List;
/**
 *
 * @author Fia
 */
public class AccessControlService {

    private final CRPRepo repo;

    public AccessControlService(String gradesFile,
                                String courseInfoFile,
                                String lecturerInfoFile) {

        repo = new CRPRepo();
        repo.loadEnrollmentsFromGrades(gradesFile);
        repo.loadCourses(courseInfoFile);
        repo.loadLecturers(lecturerInfoFile);
    }

    private String normalizeName(String name) {
        if (name == null) return "";
        return name.replace(" ", "").trim();
    }

    public boolean canLecturerAccess(String instructorId, String studentId) {

        String lecturerName = repo.getLecturerNameById(instructorId);
        if (lecturerName == null) return false;

        String lecturerKey = normalizeName(lecturerName);

        List<String> studentCourses = repo.getCoursesForStudent(studentId);
        if (studentCourses.isEmpty()) return false;

        for (String courseId : studentCourses) {
            CRPRepo.CourseRecord cr = repo.getCourse(courseId);
            if (cr == null) continue;

            // IMPORTANT: this is NAME from Course_Information.txt
            String courseLecturerKey = normalizeName(cr.instructorName); // (or cr.instructorId if you didn't rename)

            if (courseLecturerKey.equalsIgnoreCase(lecturerKey)) {
                return true;
            }
        }

        return false;
    }
}
