/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author aizik
 */
public class RecordAccess {
    
     private static final String AccessLogFilePath = "src/Data/Access_log.txt";
    
    public static String[] writeAccesslogs() {
        try{
            File logFile = new File(AccessLogFilePath);
            
            boolean fileExists = logFile.exists();
            
            if (!fileExists){
                logFile.getParentFile().mkdir();
                logFile.createNewFile();
                System.out.println("Access_log.txt created.");
                
                BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
                bw.write("UserID|UserName|LoginTime|LogoutTime");
                bw.newLine();
                bw.close();
            }
            
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String userID = UserSession.userID;
            String userName = UserSession.userName;
            String loginTime = UserSession.loginTime;
            String logoutTime = now.format(formatter);

            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(userID + "|" + userName + "|" + loginTime + "|" + logoutTime);
            bw.newLine();
            bw.close();
            return new String[]{"LOGOUT_SUCCESSFUL"};
            
            
            
        }catch(IOException e){
            return new String[]{"ERROR_LOGGING_OUT"};
        }
    }
}
