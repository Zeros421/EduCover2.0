/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author aizik
 */



public class login {
    
    private static final String LectureFilePath = "src/Data/Lecture_information.txt";
    
    public String[]  validUser (String userEmail, String userPassword){
        File lectureData = new File(LectureFilePath);
        if (!lectureData.exists()){
            return new String[]{"ERROR_FILE_NOT_FOUND"};
        }else {
            try (BufferedReader br = new BufferedReader(new FileReader(lectureData))){
                String line;
                while ((line = br.readLine())!= null){
                    String[] parts = line.split("\\|");
                    if (parts.length == 5){
                        String fileEmail = parts[2];
                        String filePassword = parts[3];
//                        System.out.println("txtfile:" + fileEmail + " " + "user input:" + userEmail);
                        
                        if (fileEmail.equalsIgnoreCase(userEmail)){
                            if (filePassword.equals(userPassword)){
                            return parts;
                            }else{
                            return new String[]{"ERROR_WRONG_PASSWORD"};
                            }
                        }
                    }
                }
                return new String[]{"ERROR_WRONG_EMAIL"};
            } catch (Exception e){
                return new String[]{"ERROR"};
            }
        }
    }
    
}

        
