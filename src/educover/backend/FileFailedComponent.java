/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import educover.backend.FailedComponent;
/**
 *
 * @author Fia
 */
public class FileFailedComponent implements FailedComponent {
    private final String filePath;

    public FileFailedComponent(String filePath) {
        this.filePath = filePath;
    }
     @Override
    public List<CourseComponent> findFailedComponentsByStudent(String studentId) {
        List<CourseComponent> result = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("grades file not found at: " + file.getAbsolutePath());
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;

                String sId   = parts[0].trim();
                String cCode = parts[1].trim();
                String comp  = parts[2].trim();
                String markStr = parts[3].trim();
                String status  = parts[4].trim().toUpperCase();

                if (!sId.equalsIgnoreCase(studentId)) {
                    continue; //
                }

                double mark = -1;
                try {
                    mark = Double.parseDouble(markStr);
                } catch (NumberFormatException ignored) {
                }

                boolean failed = status.equals("FAIL") || status.equals("F");

                if (failed) {
                    CourseComponent cc = new CourseComponent(
                            sId,
                            cCode,
                            comp,
                            mark,
                            true
                    );
                    result.add(cc);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
