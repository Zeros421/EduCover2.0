/*
 * 
 * author @siije
 */
package educover;
//imports
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.io.*;
import java.util.*;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import educover.backend.UserSession;
import javax.swing.JFrame;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Comparator;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;           
import java.util.Set;
import java.util.HashSet;



public class PerformanceReport extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(PerformanceReport.class.getName());
    private List<Student> allStudents;
    private List<Course> allCourses;
    private List<GradeRecord> allGrades;
    private DefaultListModel<String> listModel;
    private Student selectedStudent; 
    private List<Student> filteredStudents;
    private String currentSearchQuery = "";
    private String currentSortOption = "name";    
    private String loggedInInstructorID;       
    private List<String> instructorCourses;      
    private boolean isAdmin;
 
    public PerformanceReport() {
        this(null);  
    }

    public PerformanceReport(String instructorID) {
        this.loggedInInstructorID = instructorID;
        this.isAdmin = (instructorID == null || instructorID.isEmpty());

        initComponents();

        
        allStudents = loadStudents("src/Data/Student_Information.txt");
        allCourses = loadCourses("src/Data/Course_Information.txt");
        allGrades = loadGrades("src/Data/grades.txt");

       
        if (!isAdmin) {
            instructorCourses = loadInstructorCourses(instructorID);
            System.out.println("DEBUG: Instructor " + instructorID + " teaches courses: " + instructorCourses);

          
            allStudents = filterStudentsByInstructor();
            System.out.println("DEBUG: Found " + allStudents.size() + " students for instructor");
        }

        filteredStudents = new ArrayList<>(allStudents);

        initializeStudentList();
        jList1.setModel(listModel);
        clearTable();
    }

    private List<String> loadInstructorCourses(String instructorID) {
        List<String> courses = new ArrayList<>();

        if (instructorID == null || instructorID.isEmpty()) {
            return courses;  
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src/Data/Course_Instructor_Mapping.txt"))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String courseID = parts[0].trim();           
                    String courseInstructorID = parts[1].trim(); 

                 
                    if (courseInstructorID.equals(instructorID)) {
                        courses.add(courseID);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: Could not load Course_Instructor_Mapping.txt");
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error loading instructor courses: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        return courses;
    }
    
    private List<Student> filterStudentsByInstructor() {
        if (isAdmin || instructorCourses.isEmpty()) {
            return new ArrayList<>(allStudents);  
        }

        List<Student> filtered = new ArrayList<>();
        Set<String> studentIDs = new HashSet<>();

      
        for (GradeRecord grade : allGrades) {
            if (instructorCourses.contains(grade.getCourseID())) {
                studentIDs.add(grade.getStudentID());
            }
        }

    
        for (Student student : allStudents) {
            if (studentIDs.contains(student.getStudentID())) {
                filtered.add(student);
            }
        }

        return filtered;
    }
    
    private void initializeStudentList() {
    listModel = new DefaultListModel<>();
    

    for (Student s : allStudents) {
        listModel.addElement(s.getStudentID() + " - " + s.getFullName());
    }
    
  
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

             
                if (!isAdmin && !instructorCourses.contains(g.getCourseID())) {
                    continue;  
                }

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

              
                if (!isAdmin && !instructorCourses.contains(g.getCourseID())) {
                    continue;  
                }

                Course c = findCourseByID(allCourses, g.getCourseID());
                if (c != null) {
                    totalGradePoints += g.getGPA() * c.getCredits();
                    totalCredits += c.getCredits();
                }
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
   
    private String getStudentEmail(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Data/Student_Information.txt"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].trim().equals(studentID)) {
                    return parts[5].trim(); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        private boolean sendEmailWithAttachment(String toEmail, String studentName, File attachment) {
       
        final String FROM_EMAIL = "Simbaba2606@gmail.com";
        final String EMAIL_PASSWORD = "hyyqghpjsuyezgbl";

    
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
         
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Academic Performance Report - " + studentName);

           
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

         
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

           
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

          
            Transport.send(message);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
   
    private void applyFilters(String searchQuery, String sortOption) {
      
        filteredStudents = new ArrayList<>(allStudents);

      
        if (!searchQuery.isEmpty()) {
            filteredStudents = searchStudents(searchQuery);
        }

    
        sortStudents(filteredStudents, sortOption);

    
        updateStudentList(filteredStudents);

      
        if (!searchQuery.isEmpty()) {
            label4.setText("Showing " + filteredStudents.size() + " of " + allStudents.size() + " students");
        } else {
            label4.setText("Total Students: " + filteredStudents.size());
        }
    }
    private List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        String searchLower = query.toLowerCase();

        for (Student s : allStudents) {
           
            if (s.getStudentID().toLowerCase().contains(searchLower)) {
                results.add(s);
                continue;
            }

           
            if (s.getFirstName().toLowerCase().contains(searchLower)) {
                results.add(s);
                continue;
            }

          
            if (s.getLastName().toLowerCase().contains(searchLower)) {
                results.add(s);
                continue;
            }

            
            if (s.getName().toLowerCase().contains(searchLower)) {
                results.add(s);
            }
        }

        return results;
    }


    private void sortStudents(List<Student> students, String sortOption) {
        switch (sortOption) {
            case "Name (A-Z)":
                students.sort(Comparator.comparing(Student::getName));
                break;

            case "Name (Z-A)":
                students.sort(Comparator.comparing(Student::getName).reversed());
                break;

            case "Student ID (Ascending)":
                students.sort(Comparator.comparing(Student::getStudentID));
                break;

            case "Student ID (Descending)":
                students.sort(Comparator.comparing(Student::getStudentID).reversed());
                break;

            default:
                students.sort(Comparator.comparing(Student::getName));
        }
    }


    private void updateStudentList(List<Student> students) {
        listModel.clear();

        for (Student s : students) {
            listModel.addElement(s.getStudentID() + " - " + s.getName());
        }

      
        jList1.clearSelection();
        selectedStudent = null;
        clearTable();
    }
 
    
    private void generatePDFReport(File file, Student student) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

      
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph title = new Paragraph("Academic Performance Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(25);
        document.add(title);

       
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        document.add(new Paragraph("Student Name: " + student.getName(), normalFont));
        document.add(new Paragraph("Student ID: " + student.getStudentID(), normalFont));
        document.add(new Paragraph("Program: " + student.getProgram(), normalFont));

      
        if (!isAdmin) {
            document.add(new Paragraph("Instructor ID: " + loggedInInstructorID, normalFont));
            document.add(new Paragraph("Report Type: Instructor-Specific Courses Only", normalFont));
        } else {
            document.add(new Paragraph("Report Type: Complete Academic Record", normalFont));
        }

        document.add(new Paragraph(" ")); 

     
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setSpacingAfter(15);
        table.setWidths(new float[]{2f, 3.5f, 1.5f, 1.2f, 1.8f});

 
        addPDFTableHeader(table, "Course Code", labelFont);
        addPDFTableHeader(table, "Course Title", labelFont);
        addPDFTableHeader(table, "Credit Hours", labelFont);
        addPDFTableHeader(table, "Grade", labelFont);
        addPDFTableHeader(table, "Credit Point", labelFont);

    
        for (GradeRecord g : allGrades) {
            if (g.getStudentID().equals(student.getStudentID())) {

              
                if (!isAdmin && !instructorCourses.contains(g.getCourseID())) {
                    continue;  
                }

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

  
        double cgpa = calculateCGPA(student.getStudentID());
        Paragraph cgpaParagraph = new Paragraph(
                "Cumulative GPA (CGPA): " + String.format("%.2f", cgpa),
                boldFont
        );
        cgpaParagraph.setSpacingBefore(10);
        document.add(cgpaParagraph);

    
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
        cell.setBackgroundColor(new BaseColor(144, 238, 144)); 
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

   
    
    private List<Student> loadStudents(String filepath) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    students.add(new Student(
                            parts[0].trim(), 
                            parts[1].trim(), 
                            parts[2].trim(), 
                            parts[3].trim() 
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
            br.readLine();
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
            br.readLine(); 
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
        
        public String getName() {
            return firstName + " " + lastName;
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
        label5 = new java.awt.Label();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 1080));

        label1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        label1.setText("Academic Performance Report");

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

        label5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        label5.setForeground(new java.awt.Color(153, 153, 153));
        label5.setText("Generate Students Report");

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton3.setText("Filter");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1393, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)))
                .addGap(0, 286, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jButton5)
                .addGap(19, 19, 19)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4)
                        .addComponent(jButton1)
                        .addComponent(jButton3)
                        .addComponent(jButton2)))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1775, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1190, Short.MAX_VALUE)
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

        if (selectedStudent == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student from the list first!", 
                "No Selection", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        

        filterTableByStudent(selectedStudent.getStudentID());
       
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

    
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Send academic report to:\n" + selectedStudent.getFullName() + "\n" + studentEmail + "\n\nContinue?",
                "Confirm Email",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

     
        javax.swing.JOptionPane progressPane = new javax.swing.JOptionPane(
                "Sending email to " + selectedStudent.getFullName() + "...\nPlease wait.",
                javax.swing.JOptionPane.INFORMATION_MESSAGE,
                javax.swing.JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );
        javax.swing.JDialog progressDialog = progressPane.createDialog(this, "Sending Email");

       
        new Thread(() -> {
            try {
              
                File tempPDF = new File("temp_" + selectedStudent.getStudentID() + "_report.pdf");
                generatePDFReport(tempPDF, selectedStudent);

            
                boolean success = sendEmailWithAttachment(
                        studentEmail,
                        selectedStudent.getFullName(),
                        tempPDF
                );

         
                progressDialog.dispose();

                if (success) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Email sent successfully to " + selectedStudent.getFullName() + "!",
                            "Success",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                
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
        
        if (UserSession.userID.startsWith("A")) {
            AdminHomePage adminHome  = new AdminHomePage();
            adminHome.setVisible(true);
            this.dispose();
            return;
        }
        
        HomePage check = new HomePage();
            check.setVisible(true);
            check.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    
        JDialog filterDialog = new JDialog(this, "Filter Students", true);
        filterDialog.setLayout(new java.awt.BorderLayout(10, 10));
        filterDialog.setSize(400, 250);
        filterDialog.setLocationRelativeTo(this);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

      
        JPanel searchPanel = new JPanel(new java.awt.BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(currentSearchQuery);
        searchPanel.add(searchLabel, java.awt.BorderLayout.WEST);
        searchPanel.add(searchField, java.awt.BorderLayout.CENTER);

      
        JPanel sortPanel = new JPanel(new java.awt.BorderLayout(5, 5));
        JLabel sortLabel = new JLabel("Sort by:");
        JComboBox<String> sortCombo = new JComboBox<>(new String[]{
            "Name (A-Z)",
            "Name (Z-A)",
            "Student ID (Ascending)",
            "Student ID (Descending)"
        });
        sortPanel.add(sortLabel, java.awt.BorderLayout.WEST);
        sortPanel.add(sortCombo, java.awt.BorderLayout.CENTER);

        JLabel infoLabel = new JLabel("Search by name or student ID", JLabel.CENTER);
        infoLabel.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 11));

     
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        JButton applyButton = new JButton("Apply");
        JButton resetButton = new JButton("Reset");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

       
        mainPanel.add(searchPanel);
        mainPanel.add(sortPanel);
        mainPanel.add(infoLabel);
        mainPanel.add(buttonPanel);

        filterDialog.add(mainPanel);

       
        applyButton.addActionListener(e -> {
            currentSearchQuery = searchField.getText().trim();
            String sortOption = (String) sortCombo.getSelectedItem();

         
            applyFilters(currentSearchQuery, sortOption);

            filterDialog.dispose();
        });

    
        resetButton.addActionListener(e -> {
            currentSearchQuery = "";
            searchField.setText("");
            sortCombo.setSelectedIndex(0);

        
            applyFilters("", "Name (A-Z)");

            filterDialog.dispose();
        });

  
        cancelButton.addActionListener(e -> {
            filterDialog.dispose();
        });

        
        filterDialog.setVisible(true);
   
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private java.awt.Label label1;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    // End of variables declaration//GEN-END:variables
}
