package educover;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Fia
 */

import educover.backend.Student;
import java.io.*;
import java.util.*;

public class StudentDatabase {

    private final List<Student> students = new ArrayList<>();

    public StudentDatabase() {
        loadStudents();
    }

    private void loadStudents() {
        String path = "src" + File.separator + "Data" + File.separator + "Student_Information.txt";


        
        System.out.println("Trying to load from: " + new File(path).getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;


                String[] data = line.split("\\|");
                if (data.length >= 6) {
                    Student s = new Student(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(), 
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim()  
                    );
                    students.add(s);
                }
            }

            System.out.println("Loaded students: " + students.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Student findByKeyword(String keyword) {
        if (keyword == null) return null;

        String k = keyword.trim();
        if (k.isEmpty()) return null;

        for (Student s : students) {
            if (s.getStudentID().equalsIgnoreCase(k)) return s;
            if (s.getFullName().toLowerCase().contains(k.toLowerCase())) return s;
        }
        return null;
    }
    
    public List<Student> searchByKeyword(String keyword) {
    List<Student> results = new ArrayList<>();
    if (keyword == null || keyword.trim().isEmpty()) return results;

    String k = keyword.toLowerCase();

    for (Student s : students) {
        if (s.getStudentID().equalsIgnoreCase(k) ||
            s.getFullName().toLowerCase().contains(k)) {
            results.add(s);
        }
    }
    return results;
}

}
