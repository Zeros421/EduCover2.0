/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author aizik
 */
public class AddUser {
    private static final String LectureFilePath = "src/Data/Lecture_information.txt";

    public static void addUser() {

        String lastIdForRole = null;
        String line;

        // 1️⃣ Decide prefix based on role
        String role = ProfileInfo.userType;
        String prefix = role.equalsIgnoreCase("Admin") ? "A" : "I";

        // 2️⃣ Read file & find last ID for that prefix
        try (BufferedReader br = new BufferedReader(new FileReader(LectureFilePath))) {

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split("\\|");
                String id = data[0];

                if (id.startsWith(prefix)) {
                    lastIdForRole = id;
                }
            }

        } catch (IOException e) {         
            return;
        }

        // 3️⃣ Generate new ID
        String newId = generateNextId(lastIdForRole, prefix);

        // 4️⃣ Write new user
        String newUser =
                newId + "|" +
                ProfileInfo.userName + "|" +
                ProfileInfo.userEmail + "|" +
                ProfileInfo.userPassword + "|" +
                ProfileInfo.userType + "|" +
                ProfileInfo.userStatus;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LectureFilePath, true))) {
            bw.newLine();
            bw.write(newUser);
        } catch (IOException e) {
        }
    }

    // 🔹 Generate next ID safely
    private static String generateNextId(String lastId, String prefix) {

        if (lastId == null) {
            return prefix + "101"; // first Admin or Instructor
        }

        int number = Integer.parseInt(lastId.substring(1));
        number++;

        return prefix + number;
    }
}
