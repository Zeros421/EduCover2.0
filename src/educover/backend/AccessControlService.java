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
    private final String courseInfoFile;
    private final String lecturerInfoFile;

    public AccessControlService(String gradesFile, String courseInfoFile, String lecturerInfoFile) {
        this.gradesFile = gradesFile;
        this.courseInfoFile = courseInfoFile;
        this.lecturerInfoFile = lecturerInfoFile;
    }


    public boolean canLecturerAccess(String lecturerID, String studentID) {
        return getAllowedStudents(lecturerID).contains(studentID);
    }

    public List<String> getAllowedStudents(String lecturerID) {
        List<String> allowedStudents = new ArrayList<>();

        try {
            String lecturerName = getLecturerNameById(lecturerID);
            if (lecturerName == null) return allowedStudents;

            Set<String> lecturerCourses = getCoursesByLecturerName(lecturerName);

            try (BufferedReader br = new BufferedReader(new FileReader(gradesFile))) {
                String line;
                br.readLine(); 

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split("\\|");
                    if (parts.length < 2) continue;

                    String studentID = parts[0].trim();
                    String courseID  = parts[1].trim();

                    if (lecturerCourses.contains(courseID)
                            && !allowedStudents.contains(studentID)) {
                        allowedStudents.add(studentID);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("AccessControl error: " + e.getMessage());
        }

        return allowedStudents;
    }


    private String getLecturerNameById(String lecturerID) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(lecturerInfoFile))) {
            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].trim().equals(lecturerID)) {
                    return parts[1].trim(); 
                }
            }
        }
        return null;
    }

    private Set<String> getCoursesByLecturerName(String lecturerName) throws IOException {
        Set<String> courses = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(courseInfoFile))) {
            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String courseID = parts[0].trim();
                    String instructorName = parts[4].trim();

                    if (normalize(instructorName).equals(normalize(lecturerName))) {
                        courses.add(courseID);
                    }
                }
            }
        }

        return courses;
    }

    private String normalize(String s) {
        return s == null ? "" : s.replace(" ", "").toLowerCase();
    }

    private boolean isStudentInCourse(String studentID, String courseID) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(gradesFile));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                String sID = parts[0];
                String cID = parts[1];

                if (sID.equals(studentID) && cID.equals(courseID)) {
                    br.close();
                    return true;
                }
            }
        }
        br.close();
        return false;
    }
}
