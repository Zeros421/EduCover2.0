/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author aizik
 */
public class UpdateProfile {
    private static final String LectureFilePath = "src/Data/Lecture_information.txt";
    public static void updateProfile(){
        String id = UserSession.userID;
        String newName = UserSession.userName;
        String newEmail = UserSession.userEmail;
        String newPassword = UserSession.userPassword;
        String userType = UserSession.userType;
        
        File inputFile = new File(LectureFilePath);
        File tempFile = new File("lecture_information_temp.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile))){
            String line;
            while ((line = br.readLine()) !=null){
            String[] parts = line.split("\\|");
            
            if(parts[0].equals(id)){
                pw.println(id + "|" + newName + "|" + newEmail + "|" + newPassword + "|" + userType);
                UserSession.userName = newName;
                UserSession.userType = userType;
                UserSession.userPassword = newPassword;
            }else {
                pw.println(line);
            }
           }
        }catch (Exception e){
             JOptionPane.showMessageDialog(null, "Error updating profile!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        inputFile.delete();
        tempFile.renameTo(inputFile);
        
        
        
    }
}
