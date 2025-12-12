/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aizik
 */
public class FillLectureTable {
    public static DefaultTableModel getLectureTableInformation(String filename) throws IOException {
        
        String[] colums = {"InstructorID", "Name", "Email", "Role", "Status"};
        DefaultTableModel model = new DefaultTableModel(colums, 0);
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            br.readLine();
            String line; 
            while((line = br.readLine()) !=null){
                String[] data = line.split("\\|");
                if (data.length >= 6){
                    Object[] row = {data[0],data[1],data[2], data[4], data[5]};
                    model.addRow(row);
                }
            }
            
        }
        
        return model;  
    }
}
