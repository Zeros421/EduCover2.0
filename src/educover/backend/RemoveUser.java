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
import javax.swing.JOptionPane;

/**
 *
 * @author aizik
 */
public class RemoveUser {
    
    private static final String LectureFilePath = "src/Data/Lecture_information.txt";

    public static boolean deleteUserFromFile(String userId) {

        File inputFile = new File(LectureFilePath);
        File tempFile = new File("src/Data/temp.txt");

        boolean deleted = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;

            while ((line = br.readLine()) != null) {

                // Skip user row
                if (line.startsWith(userId + "|")) {
                    deleted = true;
                    continue;
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Replace original file
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            return false;
        }

        return deleted;
    }
}
