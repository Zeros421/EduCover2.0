package educover.backend;

import educover.backend.*;
import java.io.*;
import java.util.*;

public class DataLoader {

    public static List<Student> loadStudents(String filePath) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                students.add(new Student(
                        p[0], p[1], p[2], p[3], p[4], p[5]
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    public static List<Course> loadCourses(String filePath) {
        List<Course> courses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                courses.add(new Course(
                        p[0], p[1], Integer.parseInt(p[2]),
                        p[3], p[4], Integer.parseInt(p[5])
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    public static List<GradeRecord> loadGrades(String filePath) {
    List<GradeRecord> records = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] p = line.split("\\|");

            String studentID = p[1];
            String courseID = p[2];
            String gradeLetter = p[3];
            String semester = p[4];
            int year = Integer.parseInt(p[5]);

            double gradePoint = convertLetterToPoint(gradeLetter);

            records.add(new GradeRecord(
                    studentID,
                    courseID,
                    gradeLetter,
                    gradePoint,
                    year,
                    semester
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return records;
}

    private static double convertLetterToPoint(String g) {
        return switch (g) {
            case "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            default -> 0.0;  // F or unknown
            };
        }

}
