package educover.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessControlService {

    private final String gradesFile;
    private final String courseInstructorMappingFile;


    public AccessControlService(String gradesFile, String courseInstructorMappingFile) {
        this.gradesFile = gradesFile;
        this.courseInstructorMappingFile = courseInstructorMappingFile;
    }

    public boolean canLecturerAccess(String lecturerID, String studentID) {
        return getAllowedStudents(lecturerID).contains(studentID);
    }

    public List<String> getAllowedStudents(String lecturerID) {
        List<String> allowedStudents = new ArrayList<>();

        try {

            Set<String> lecturerCourses = getCoursesByLecturerId(lecturerID);

            if (lecturerCourses.isEmpty()) return allowedStudents;

            try (BufferedReader br = new BufferedReader(new FileReader(gradesFile))) {
                String line;
                br.readLine(); 

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split("\\|");
                    if (parts.length < 2) continue;

                    String studentID = parts[0].trim();
                    String courseID  = parts[1].trim();

                    if (lecturerCourses.contains(courseID) && !allowedStudents.contains(studentID)) {
                        allowedStudents.add(studentID);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("AccessControl error: " + e.getMessage());
        }

        return allowedStudents;
    }

    private Set<String> getCoursesByLecturerId(String lecturerID) throws IOException {
        Set<String> courses = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(courseInstructorMappingFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;

                String courseID = parts[0].trim();
                String instructorID = parts[1].trim();

                if (instructorID.equals(lecturerID)) {
                    courses.add(courseID);
                }
            }
        }

        return courses;
    }
}