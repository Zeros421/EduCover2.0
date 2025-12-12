/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aizik
 */
public class ChangePassword {
    private static final String LectureFilePath = "src/Data/Lecture_information.txt";
    public static void changePassword(String userEmail, String newPassword) {
        File file = new File (LectureFilePath);
        List<String> lines = new ArrayList<>();
        
        try{
            try (BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                while ((line =br.readLine()) !=null){
                if (line.trim().isEmpty()){
                    continue;
                }
                    
                    String[] parts = line.split("\\|");
                if (parts.length >= 4 && parts[2].equalsIgnoreCase(userEmail)) {
                    parts [3] = newPassword;
                    line = String.join("|", parts);
                }
                
                lines.add(line);
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : lines){
                bw.write(l);
                bw.newLine();
                }
            }
            System.out.println("Password update successfully");
            
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
