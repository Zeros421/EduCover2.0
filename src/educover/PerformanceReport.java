/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package educover;

import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.io.*;
import java.util.*;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.JFrame;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;


public class PerformanceReport extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(PerformanceReport.class.getName());

    // ===== INSTANCE VARIABLES =====
    private List<Student> allStudents;
    private List<Course> allCourses;
    private List<GradeRecord> allGrades;
    private DefaultListModel<String> listModel;
    private Student selectedStudent; 

    // ===== CONSTRUCTOR =====
    public PerformanceReport() {
        initComponents();

        // Load and store all data
        allStudents = loadStudents("src/Data/Student_Information.txt");
        allCourses = loadCourses("src/Data/Course_Information.txt");
        allGrades = loadGrades("src/Data/grades.txt");

         initializeStudentList();
        
        jList1.setModel(listModel);
        
        clearTable();
    }
    
    private void initializeStudentList() {
    listModel = new DefaultListModel<>();
    
    // Add all students to the list
    for (Student s : allStudents) {
        listModel.addElement(s.getStudentID() + " - " + s.getFullName());
    }
    
    // Set the model to the JList
    jList1.setModel(listModel);

    jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            if (!evt.getValueIsAdjusting()) {
                String selected = jList1.getSelectedValue();
                if (selected != null && !selected.trim().isEmpty()) {
                    String studentID = selected.split(" - ")[0].trim();
                    selectedStudent = findStudentByID(studentID);
                    if (selectedStudent != null) {
                        label4.setText("Selected: " + selectedStudent.getFullName());
                    }
                }
            }
        }
    });
    
    label4.setText("Total Students: " + allStudents.size());
}
    
    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }
    
   
    private Course findCourseByID(List<Course> list, String id) {
        for (Course c : list) {
            if (c.getCourseID().equals(id)) return c;
        }
        return null;
    }

    private Student findStudentByID(String studentID) {
        for (Student s : allStudents) {
            if (s.getStudentID().equals(studentID)) {
                return s;
            }
        }
        return null;
    }

    private String convertGPAToLetter(double gpa) {
        if (gpa >= 3.7) return "A";
        if (gpa >= 3.3) return "B+";
        if (gpa >= 3.0) return "B";
        if (gpa >= 2.7) return "B-";
        if (gpa >= 2.3) return "C+";
        if (gpa >= 2.0) return "C";
        if (gpa >= 1.7) return "C-";
        if (gpa >= 1.0) return "D";
        return "F";
    }

    private void filterTableByStudent(String studentID) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        for (GradeRecord g : allGrades) {
            if (g.getStudentID().equals(studentID)) {
                Course c = findCourseByID(allCourses, g.getCourseID());
                if (c != null) {
                    double gpa = g.getGPA();
                    double creditPoint = gpa * c.getCredits();
                    
                    model.addRow(new Object[]{
                        c.getCourseID(),
                        c.getCourseName(),
                        c.getCredits(),
                        convertGPAToLetter(gpa),
                        String.format("%.1f", creditPoint)
                    });
                }
            }
        }

        model.addRow(new Object[]{"", "", "", "", ""});
        
        double cgpa = calculateCGPA(studentID);
        model.addRow(new Object[]{"", "", "", "CGPA:", String.format("%.2f", cgpa)});
    }

    private double calculateCGPA(String studentID) {
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (GradeRecord g : allGrades) {
            if (g.getStudentID().equals(studentID)) {
                Course c = findCourseByID(allCourses, g.getCourseID());
                if (c != null) {
                    totalGradePoints += g.getGPA() * c.getCredits();
                    totalCredits += c.getCredits();
                }
            }
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
    //GET STUDENT EMAIL
    private String getStudentEmail(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/siije/Documents/NetBeansProjects/GroupAssignment/EduCover/src/Data/Student_Information.txt"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].trim().equals(studentID)) {
                    return parts[5].trim(); // Email is 6th column
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        private boolean sendEmailWithAttachment(String toEmail, String studentName, File attachment) {
        // Email configuration - CHANGE THESE TO YOUR SETTINGS
        final String FROM_EMAIL = "Simbaba2606@gmail.com";
        final String EMAIL_PASSWORD = "hyyqghpjsuyezgbl";

        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Academic Performance Report - " + studentName);

            // Create message body
            MimeBodyPart textPart = new MimeBodyPart();
            double cgpa = calculateCGPA(selectedStudent.getStudentID());

            String emailBody = "Dear " + studentName + ",\n\n"
                    + "Please find attached your Academic Performance Report.\n\n"
                    + "Summary:\n"
                    + "Student ID: " + selectedStudent.getStudentID() + "\n"
                    + "Program: " + selectedStudent.getProgram() + "\n"
                    + "Cumulative GPA: " + String.format("%.2f", cgpa) + "\n\n"
                    + "If you have any questions about your report, please contact your academic advisor.\n\n"
                    + "Best regards,\n"
                    + "Academic Office\n"
                    + "EduCover System";

            textPart.setText(emailBody);

            // Attach PDF
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

            // Combine parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send email
            Transport.send(message);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // ===== PDF GENERATION =====
    
    private void generatePDFReport(File file, Student student) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph title = new Paragraph("Academic Performance Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(25);
        document.add(title);
        
        // Student Information
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        
        document.add(new Paragraph("Student Name: " + student.getFullName(), normalFont));
        document.add(new Paragraph("Student ID: " + student.getStudentID(), normalFont));
        document.add(new Paragraph("Program: " + student.getProgram(), normalFont));
        document.add(new Paragraph(" ")); // Spacing
        
        // Course Table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setSpacingAfter(15);
        table.setWidths(new float[]{2f, 3.5f, 1.5f, 1.2f, 1.8f});
        
        // Table Headers
        addPDFTableHeader(table, "Course Code", labelFont);
        addPDFTableHeader(table, "Course Title", labelFont);
        addPDFTableHeader(table, "Credit Hours", labelFont);
        addPDFTableHeader(table, "Grade", labelFont);
        addPDFTableHeader(table, "Credit Point", labelFont);
        
        // Table Data
        for (GradeRecord g : allGrades) {
            if (g.getStudentID().equals(student.getStudentID())) {
                Course c = findCourseByID(allCourses, g.getCourseID());
                if (c != null) {
                    double gpa = g.getGPA();
                    double creditPoint = gpa * c.getCredits();
                    
                    addPDFTableCell(table, c.getCourseID(), normalFont);
                    addPDFTableCell(table, c.getCourseName(), normalFont);
                    addPDFTableCell(table, String.valueOf(c.getCredits()), normalFont);
                    addPDFTableCell(table, convertGPAToLetter(gpa), normalFont);
                    addPDFTableCell(table, String.format("%.1f", creditPoint), normalFont);
                }
            }
        }
        
        document.add(table);
        
        // CGPA
        double cgpa = calculateCGPA(student.getStudentID());
        Paragraph cgpaParagraph = new Paragraph(
            "Cumulative GPA (CGPA): " + String.format("%.2f", cgpa), 
            boldFont
        );
        cgpaParagraph.setSpacingBefore(10);
        document.add(cgpaParagraph);
        
        // Footer
        document.add(new Paragraph(" "));
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
        Paragraph footer = new Paragraph(
            "Report Generated: " + new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 
            footerFont
        );
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);
        
        document.close();
    }

    private void addPDFTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new BaseColor(144, 238, 144)); // Light green
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void addPDFTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    // ===== DATA LOADER METHODS (Built-in) =====
    
    private List<Student> loadStudents(String filepath) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    students.add(new Student(
                            parts[0].trim(), // StudentID
                            parts[1].trim(), // FirstName
                            parts[2].trim(), // LastName
                            parts[3].trim() // Program
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    private List<Course> loadCourses(String filepath) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    courses.add(new Course(
                        parts[0].trim(), 
                        parts[1].trim(), 
                        Integer.parseInt(parts[2].trim())
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error loading courses: " + e.getMessage(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return courses;
    }
    
    private List<GradeRecord> loadGrades(String filepath) {
        List<GradeRecord> grades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    grades.add(new GradeRecord(
                        parts[0].trim(), 
                        parts[1].trim(), 
                        Double.parseDouble(parts[2].trim())
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error loading grades: " + e.getMessage(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return grades;
    }

    // ===== INNER CLASSES (Data Models) =====
    
    class Student {

        private String studentID;
        private String firstName;
        private String lastName;
        private String program;

        public Student(String studentID, String firstName, String lastName, String program) {
            this.studentID = studentID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.program = program;
        }

        public String getStudentID() {
            return studentID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }

        public String getProgram() {
            return program;
        }
    }

    class Course {
        private String courseID;
        private String courseName;
        private int credits;
        
        public Course(String courseID, String courseName, int credits) {
            this.courseID = courseID;
            this.courseName = courseName;
            this.credits = credits;
        }
        
        public String getCourseID() { return courseID; }
        public String getCourseName() { return courseName; }
        public int getCredits() { return credits; }
    }

    class GradeRecord {
        private String studentID;
        private String courseID;
        private double gpa;
        
        public GradeRecord(String studentID, String courseID, double gpa) {
            this.studentID = studentID;
            this.courseID = courseID;
            this.gpa = gpa;
        }
        
        public String getStudentID() { return studentID; }
        public String getCourseID() { return courseID; }
        public double getGPA() { return gpa; }
    }
    
/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        label4 = new java.awt.Label();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1920, 1080));

        label1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        label1.setText("Academic Performance Report");

        label2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        label2.setText("Actions");

        label3.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        label3.setText("Student Name");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Course Code", "Course Title", "Credit Hours", "Grade", "Credit Point"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton1.setText("Print PDF");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton2.setText("View Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        label4.setText("StudentID");

        jButton4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton4.setText("Email to Student");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Back");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(251, 251, 251))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jButton4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(159, 159, 159))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            String selected = jList1.getSelectedValue();
            if (selected != null) {
                String studentID = selected.split(" - ")[0];
                selectedStudent = findStudentByID(studentID);
                if (selectedStudent != null) {
                    label4.setText("Selected: " + selectedStudent.getFullName());
                }
            }
        }
    }
        
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Check if student is selected
        if (selectedStudent == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student from the list first!", 
                "No Selection", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new File(selectedStudent.getStudentID() + "_AcademicReport.pdf"));
        
        if (fileChooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            
            try {
                generatePDFReport(file, selectedStudent);
                
                int result = javax.swing.JOptionPane.showConfirmDialog(this, 
                    "PDF report generated successfully!\n" + file.getAbsolutePath() + "\n\nOpen file?", 
                    "Success", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
                if (result == javax.swing.JOptionPane.YES_OPTION) {
                    try {
                        java.awt.Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        System.out.println("Could not open file: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error generating PDF: " + e.getMessage(), 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Check if student is selected
        if (selectedStudent == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student from the list first!", 
                "No Selection", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Filter table to show only selected student's data
        filterTableByStudent(selectedStudent.getStudentID());
        
        // Calculate and show CGPA
        double cgpa = calculateCGPA(selectedStudent.getStudentID());
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Student: " + selectedStudent.getFullName() + "\n" +
            "Student ID: " + selectedStudent.getStudentID() + "\n" +
            "Program: " + selectedStudent.getProgram() + "\n\n" +
            "Cumulative GPA (CGPA): " + String.format("%.2f", cgpa), 
            "Academic Performance Report", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Check if student is selected
        if (selectedStudent == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a student from the list first!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentEmail = getStudentEmail(selectedStudent.getStudentID());

        if (studentEmail == null || studentEmail.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Student email not found!\nPlease add email to Student_Information.txt",
                    "Email Not Found",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ask for confirmation
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Send academic report to:\n" + selectedStudent.getFullName() + "\n" + studentEmail + "\n\nContinue?",
                "Confirm Email",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        // Show progress
        javax.swing.JOptionPane progressPane = new javax.swing.JOptionPane(
                "Sending email to " + selectedStudent.getFullName() + "...\nPlease wait.",
                javax.swing.JOptionPane.INFORMATION_MESSAGE,
                javax.swing.JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );
        javax.swing.JDialog progressDialog = progressPane.createDialog(this, "Sending Email");

        // Send email in background thread
        new Thread(() -> {
            try {
                // Generate PDF first
                File tempPDF = new File("temp_" + selectedStudent.getStudentID() + "_report.pdf");
                generatePDFReport(tempPDF, selectedStudent);

                // Send email with attachment
                boolean success = sendEmailWithAttachment(
                        studentEmail,
                        selectedStudent.getFullName(),
                        tempPDF
                );

                // Close progress dialog
                progressDialog.dispose();

                if (success) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Email sent successfully to " + selectedStudent.getFullName() + "!",
                            "Success",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    // Delete temp file
                    tempPDF.delete();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Failed to send email. Please check your email configuration.",
                            "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                progressDialog.dispose();
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Email Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }).start();

        progressDialog.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PerformanceReport().setVisible(true);
            }
        });
    }
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        HomePage check = new HomePage();
            check.setVisible(true);
            check.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    // End of variables declaration//GEN-END:variables
}
