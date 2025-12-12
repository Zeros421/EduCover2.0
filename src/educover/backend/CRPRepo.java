/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

/**
 *
 * @author Fia
 */
import java.io.*;
import java.util.*;

public class CRPRepo {
        public static class CourseRecord {
        public final String id;
        public final String name;
        public final String instructorName;

        public CourseRecord(String id, String name, String instructorName) {
            this.id = id;
            this.name = name;
            this.instructorName = instructorName;
        }
    }

    private final Map<String, CourseRecord> courses = new HashMap<>();

    public void loadCourses(String filePath) {
        courses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length < 5) continue;

                String id = p[0].trim();
                String name = p[1].trim();
                String instructorName = p[4].trim();

                courses.put(id, new CourseRecord(id, name, instructorName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CourseRecord getCourse(String courseId) {
        return courses.get(courseId);
    }

    public String getLecturerNameKeyById(String instructorId) {
    String name = lecturerIdToName.get(instructorId);
    if (name == null) return "";
    return name.replace(" ", "").trim();
}
    
    public RecoveryPlan loadRecoveryPlanByStudentId(String studentId, String filePath) {
    RecoveryPlan plan = new RecoveryPlan(studentId);
    File file = new File(filePath);

    if (!file.exists()) {
        System.out.println("Recovery file not found: " + file.getAbsolutePath());
        return plan;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 9) continue;

            String sId = parts[0].trim();
            if (!sId.equalsIgnoreCase(studentId)) continue;

            String taskId        = parts[1].trim();
            String courseCode    = parts[2].trim();
            String componentName = parts[3].trim();
            String taskType      = parts[4].trim();
            String weekRange     = parts[5].trim();
            String description   = parts[6].trim();
            String deadline      = parts[7].trim();
            String statusStr     = parts[8].trim();

            TaskStatus status;
            try {
                status = TaskStatus.valueOf(statusStr);
            } catch (IllegalArgumentException ex) {
                status = TaskStatus.NOT_STARTED;
            }

            AcademicTask task;
            if ("LECTURE_RECOMMENDATION".equalsIgnoreCase(taskType)) {
                task = new LectureRecommendation(
                        taskId, studentId, courseCode, componentName,
                        weekRange, description, deadline, status
                );
            } else { // default to milestone
                task = new Milestone(
                        taskId, studentId, courseCode, componentName,
                        weekRange, description, deadline, status
                );
            }

            plan.addTask(task);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    System.out.println("CRPRepo.loadRecoveryPlanByStudentId: loaded "
            + plan.getTasks().size() + " tasks for " + studentId);

    return plan;
}

public void saveRecoveryPlan(RecoveryPlan plan, String filePath) {
    File file = new File(filePath);
    Map<String, List<String>> linesByStudent = new LinkedHashMap<>();

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 1) continue;

                String sId = parts[0].trim();
                linesByStudent
                        .computeIfAbsent(sId, k -> new ArrayList<>())
                        .add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> newLines = new ArrayList<>();
    for (AcademicTask t : plan.getTasks()) {
        String line = String.join("|",
                plan.getStudentId(),
                t.getId(),
                t.getCourseCode(),
                t.getComponentName(),
                t.getTaskType(),
                t.getWeekRange(),
                t.getDescription(),
                t.getDeadline(),
                t.getStatus().name()
        );
        newLines.add(line);
    }
    linesByStudent.put(plan.getStudentId(), newLines);

    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
        for (List<String> studentLines : linesByStudent.values()) {
            for (String l : studentLines) pw.println(l);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private final Map<String, List<String>> studentCourses = new HashMap<>();

    public void loadEnrollmentsFromGrades(String gradesFilePath) {
        studentCourses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(gradesFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length < 2) continue;

                String studentId = p[0].trim();
                String courseId = p[1].trim();

                studentCourses.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCoursesForStudent(String studentId) {
        return studentCourses.getOrDefault(studentId, Collections.emptyList());
    }


    private final Map<String, String> lecturerIdToName = new HashMap<>();

    public void loadLecturers(String filePath) {
        lecturerIdToName.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length < 2) continue;

                lecturerIdToName.put(p[0].trim(), p[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLecturerNameById(String instructorId) {
        return lecturerIdToName.get(instructorId);
    }


    private final Map<String, List<String>> lecturerSubjects = new HashMap<>();

    public void loadLecturerSubjects(String filePath) {
        lecturerSubjects.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentLecturer = null;
            List<String> subjects = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (Character.isDigit(line.charAt(0))) {
                    if (currentLecturer != null && subjects != null) {
                        lecturerSubjects.put(currentLecturer, new ArrayList<>(subjects));
                    }
                    currentLecturer = line.substring(line.indexOf('.') + 1).trim();
                    subjects = new ArrayList<>();
                } else {
                    if (subjects != null) subjects.add(line);
                }
            }

            if (currentLecturer != null && subjects != null) {
                lecturerSubjects.put(currentLecturer, subjects);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSubjectsForLecturer(String lecturerName) {
        return lecturerSubjects.getOrDefault(lecturerName, Collections.emptyList());
    }
}
