/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package educover;

/**
 *
 * @author Fia
 */
import educover.backend.Student;
import educover.StudentDatabase;
import educover.backend.FailedComponent;
import educover.backend.FileFailedComponent;
import educover.backend.CourseComponent;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import educover.backend.RecoveryPlan;
import educover.backend.RecoveryPlanTable;
import educover.backend.TaskStatus;
import educover.backend.LectureRecommendation;
import educover.backend.Milestone;
import educover.backend.AcademicTask;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import educover.backend.AccessControlService;
import educover.backend.CRPRepo;
import educover.backend.UserSession;

public class CourseRecoveryPlan extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CourseRecoveryPlan.class.getName());
    private StudentDatabase studentDB;
    private FailedComponent failedRepo;
    private educover.backend.CRPRepo CRPRepo = new educover.backend.CRPRepo();
    private static final String SEARCH_PLACEHOLDER = "Enter Student ID or Name";
    private RecoveryPlanTable planTableModel;
    private RecoveryPlan currentPlan;
    private static final String RECOVERY_FILE = "src/Data/Recovery.txt"; 
    private AccessControlService accessControl;
    private String currentInstructorId = UserSession.userID;
    
    private String calculateCgpaFromGradesFile(String studentId) {
    String path = "src/Data/grades.txt";
    java.nio.file.Path p = java.nio.file.Paths.get(path);

    if (!java.nio.file.Files.exists(p)) return "-";

    double sum = 0.0;
    int count = 0;

    try {
        for (String line : java.nio.file.Files.readAllLines(p)) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length < 3) continue;

            String id = parts[0].trim();
            if (!id.equalsIgnoreCase(studentId)) continue;

            String gpaStr = parts[2].trim();

            try {
                double gpa = Double.parseDouble(gpaStr);
                sum += gpa;
                count++;
            } catch (NumberFormatException ignored) {
            }
        }
    } catch (Exception ex) {
        System.out.println("Failed to read grades.txt: " + ex.getMessage());
        return "-";
    }

    if (count == 0) return "-";

    double cgpa = sum / count;
    return String.format("%.2f", cgpa);
}


    public CourseRecoveryPlan() {
        initComponents();
        studentDB    = new StudentDatabase();
        failedRepo   = new FileFailedComponent("src/Data/Course_Result.txt");
        accessControl = new AccessControlService(
                "src/Data/grades.txt",
                "src/Data/Course_Information.txt",
                "src/Data/Lecture_Information.txt"
        );

        currentPlan     = new RecoveryPlan("UNKNOWN");
        planTableModel  = new RecoveryPlanTable();
        planTableModel.setPlan(currentPlan);

        CRPTable.setModel(planTableModel);
        CRPTable.getColumnModel()
        .getColumn(4)  
        .setCellEditor(new DateCellChooser());
        
javax.swing.JComboBox<TaskStatus> statusCombo = new javax.swing.JComboBox<>(
        new TaskStatus[] {
                TaskStatus.NOT_STARTED,
                TaskStatus.IN_PROGRESS,
                TaskStatus.COMPLETED
        }
);

SubmitButton.addActionListener(evt -> onSubmitPlan());

CRPTable.getColumnModel()
        .getColumn(5)
        .setCellEditor(new javax.swing.DefaultCellEditor(statusCombo));


        planTableModel.addTableModelListener(e -> updateProgressBarAndGrade());

        AddTask.addActionListener(evt -> onAddTask());
        DeleteTask.addActionListener(evt -> onDeleteTask());
        EditRowButton.addActionListener(evt -> onEditRow());
        SaveButton.addActionListener(evt -> onSavePlan());


        TXTsearch.setText(SEARCH_PLACEHOLDER);
        TXTsearch.setForeground(new java.awt.Color(204, 204, 204));
        TXTsearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (TXTsearch.getText().equals(SEARCH_PLACEHOLDER)) {
                    TXTsearch.setText("");
                    TXTsearch.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (TXTsearch.getText().trim().isEmpty()) {
                    TXTsearch.setText(SEARCH_PLACEHOLDER);
                    TXTsearch.setForeground(new java.awt.Color(204, 204, 204));
                }
            }
        });

        updateProgressBarAndGrade();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        wrapper = new javax.swing.JPanel();
        ProgressBar = new javax.swing.JPanel();
        CRPlabel = new javax.swing.JLabel();
        searchbutton = new javax.swing.JButton();
        studentlabel = new javax.swing.JLabel();
        TXTsearch = new javax.swing.JTextField();
        DeleteTask = new javax.swing.JButton();
        AddTask = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        EditRowButton = new javax.swing.JButton();
        SubmitButton = new javax.swing.JButton();
        FinalGrade = new javax.swing.JLabel();
        percentagelabel = new javax.swing.JLabel();
        ProgressLabel = new javax.swing.JLabel();
        FinalGradeLabel = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        StudentInfoPanel = new javax.swing.JPanel();
        StudentInfoLbl = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        IDLabel = new javax.swing.JLabel();
        CGPALabel = new javax.swing.JLabel();
        ProgramLabel = new javax.swing.JLabel();
        YearLabel = new javax.swing.JLabel();
        StudentName = new javax.swing.JLabel();
        StudentTP = new javax.swing.JLabel();
        CGPA = new javax.swing.JLabel();
        BSCProgram = new javax.swing.JLabel();
        Year = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        FailedSummaryLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CRPTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
        mainPanel.setLayout(new java.awt.BorderLayout());

        wrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));
        wrapper.setMaximumSize(new java.awt.Dimension(1800, 1080));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new java.awt.Dimension(1900, 1034));

        ProgressBar.setBackground(new java.awt.Color(255, 255, 255));
        ProgressBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProgressBar.setMaximumSize(new java.awt.Dimension(1920, 1080));

        CRPlabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        CRPlabel.setForeground(new java.awt.Color(47, 54, 93));
        CRPlabel.setLabelFor(ProgressBar);
        CRPlabel.setText("COURSE RECOVERY PLAN");
        CRPlabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        CRPlabel.setPreferredSize(new java.awt.Dimension(200, 50));

        searchbutton.setBackground(new java.awt.Color(255, 199, 76));
        searchbutton.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        searchbutton.setText("Search");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });
        searchbutton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchbuttonKeyPressed(evt);
            }
        });

        studentlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        studentlabel.setText("Search Student:");

        TXTsearch.setForeground(new java.awt.Color(204, 204, 204));
        TXTsearch.setText("Enter Student ID or Name");
        TXTsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTsearchActionPerformed(evt);
            }
        });

        DeleteTask.setBackground(new java.awt.Color(255, 102, 102));
        DeleteTask.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        DeleteTask.setForeground(new java.awt.Color(255, 255, 255));
        DeleteTask.setText("Delete Task");
        DeleteTask.setMaximumSize(new java.awt.Dimension(84, 23));
        DeleteTask.setMinimumSize(new java.awt.Dimension(84, 23));

        AddTask.setBackground(new java.awt.Color(47, 54, 93));
        AddTask.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        AddTask.setForeground(new java.awt.Color(255, 255, 255));
        AddTask.setText("Add Task");
        AddTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTaskActionPerformed(evt);
            }
        });

        SaveButton.setBackground(new java.awt.Color(109, 223, 130));
        SaveButton.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        EditRowButton.setBackground(new java.awt.Color(204, 204, 204));
        EditRowButton.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        EditRowButton.setText("Edit Row");
        EditRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditRowButtonActionPerformed(evt);
            }
        });

        SubmitButton.setBackground(new java.awt.Color(47, 54, 93));
        SubmitButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SubmitButton.setForeground(new java.awt.Color(255, 255, 255));
        SubmitButton.setText("Submit Plan");
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });

        FinalGrade.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        FinalGrade.setText("A*");

        percentagelabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        percentagelabel.setText("60% Completed");

        ProgressLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ProgressLabel.setText("Progress:");

        FinalGradeLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        FinalGradeLabel.setText("Final Expected Grade:");

        jProgressBar1.setValue(60);
        jProgressBar1.setStringPainted(true);

        StudentInfoPanel.setBackground(new java.awt.Color(250, 250, 250));

        StudentInfoLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        StudentInfoLbl.setText("Student Information");

        NameLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        NameLabel.setText("Name:");

        IDLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        IDLabel.setText("Student ID:");

        CGPALabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        CGPALabel.setText("CGPA:");

        ProgramLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ProgramLabel.setText("Program: ");

        YearLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        YearLabel.setText("Year:");

        StudentName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        StudentName.setText("-");

        StudentTP.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        StudentTP.setText("-");

        CGPA.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        CGPA.setText("-");

        BSCProgram.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        BSCProgram.setText("-");

        Year.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        Year.setText("-");

        javax.swing.GroupLayout StudentInfoPanelLayout = new javax.swing.GroupLayout(StudentInfoPanel);
        StudentInfoPanel.setLayout(StudentInfoPanelLayout);
        StudentInfoPanelLayout.setHorizontalGroup(
            StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                        .addComponent(StudentInfoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                        .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(NameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(IDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(CGPALabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ProgramLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(YearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StudentInfoPanelLayout.createSequentialGroup()
                                .addComponent(StudentTP, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(StudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CGPA, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Year, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BSCProgram, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))))
        );
        StudentInfoPanelLayout.setVerticalGroup(
            StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentInfoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameLabel)
                    .addComponent(StudentName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDLabel)
                    .addComponent(StudentTP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CGPALabel)
                    .addComponent(CGPA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BSCProgram)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Year)
                        .addGap(46, 46, 46))
                    .addGroup(StudentInfoPanelLayout.createSequentialGroup()
                        .addComponent(ProgramLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(YearLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("RECOVERY PLAN TABLE");

        FailedSummaryLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        FailedSummaryLabel.setText("Failed Modules:");

        CRPTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Course Code", "Failed Subjects", "Week Range", "Task", "Deadline"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CRPTable.setRowHeight(30);
        CRPTable.setShowGrid(true);
        jScrollPane2.setViewportView(CRPTable);

        javax.swing.GroupLayout ProgressBarLayout = new javax.swing.GroupLayout(ProgressBar);
        ProgressBar.setLayout(ProgressBarLayout);
        ProgressBarLayout.setHorizontalGroup(
            ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProgressBarLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addComponent(FailedSummaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(StudentInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ProgressBarLayout.createSequentialGroup()
                                .addComponent(studentlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(573, 573, 573)
                                .addComponent(CRPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ProgressBarLayout.createSequentialGroup()
                                .addComponent(TXTsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 1667, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26))))
            .addGroup(ProgressBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ProgressBarLayout.createSequentialGroup()
                                .addComponent(AddTask, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(DeleteTask, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(EditRowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ProgressBarLayout.createSequentialGroup()
                                .addComponent(FinalGradeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FinalGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ProgressBarLayout.createSequentialGroup()
                                .addComponent(ProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(percentagelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        ProgressBarLayout.setVerticalGroup(
            ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProgressBarLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CRPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentlabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXTsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(FailedSummaryLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EditRowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DeleteTask, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddTask, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ProgressBarLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ProgressLabel)
                    .addComponent(percentagelabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProgressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FinalGradeLabel)
                    .addComponent(FinalGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 88, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout wrapperLayout = new javax.swing.GroupLayout(wrapper);
        wrapper.setLayout(wrapperLayout);
        wrapperLayout.setHorizontalGroup(
            wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrapperLayout.createSequentialGroup()
                .addComponent(ProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        wrapperLayout.setVerticalGroup(
            wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wrapperLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104))
        );

        jScrollPane1.setViewportView(wrapper);

        mainPanel.add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SubmitButtonActionPerformed

    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed

        String keyword = TXTsearch.getText().trim();
        System.out.println("INPUT keyword = " + keyword);
        Student found = null;

        if (keyword.isEmpty() || keyword.equals(SEARCH_PLACEHOLDER)) {
            System.out.println("ERROR: Empty keyword.");
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please enter a Student ID or Name.",
                "Input Required",
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Student> matches = studentDB.searchByKeyword(keyword);

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Student not found in database.",
                "No Result",
                JOptionPane.INFORMATION_MESSAGE
            );

            StudentName.setText("-");
            StudentTP.setText("-");
            CGPA.setText("-");
            BSCProgram.setText("-");
            Year.setText("-");
            FailedSummaryLabel.setText("Failed: -");
            currentPlan = new RecoveryPlan("UNKNOWN");
            planTableModel.setPlan(currentPlan);
            updateProgressBarAndGrade();
            return;
        }

        Student selected;

        if (matches.size() == 1) {
            selected = matches.get(0);
        } else {

            String[] options = matches.stream()
            .map(s -> s.getStudentID() + " - " + s.getFullName() + " (" + s.getYear() + ")")
            .toArray(String[]::new);

            String choice = (String) JOptionPane.showInputDialog(
                this,
                "Multiple students found. Please select:",
                "Select Student",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == null) {

                return;
            }

            
            int idx = java.util.Arrays.asList(options).indexOf(choice);
            selected = matches.get(idx);
        }

        found = selected; 

        
        if (found == null) {
            System.out.println("NOT FOUND: No matching student for keyword.");
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Student not found in database.",
                "No Result",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            StudentName.setText("-");
            StudentTP.setText("-");
            CGPA.setText("-");
            BSCProgram.setText("-");
            Year.setText("-");

            currentPlan = new RecoveryPlan("UNKNOWN");
            planTableModel.setPlan(currentPlan);

            System.out.println("Table cleared. currentPlan = UNKNOWN");
            updateProgressBarAndGrade();
            return;
        }

        if (!accessControl.canLecturerAccess(currentInstructorId, found.getStudentID())) {

            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Access denied.\nYou are not assigned to any course taken by this student.",
                "Permission Restricted",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );

            StudentName.setText("-");
            StudentTP.setText("-");
            CGPA.setText("-");
            BSCProgram.setText("-");
            Year.setText("-");
            currentPlan = new educover.backend.RecoveryPlan("UNKNOWN");
            planTableModel.setPlan(currentPlan);
            updateProgressBarAndGrade();

            return;
        }

        System.out.println("FOUND student: " + found.getStudentID() + " -> " + found.getFullName());

        StudentName.setText(found.getFullName());
        StudentTP.setText(found.getStudentID());
        BSCProgram.setText(found.getMajor());
        Year.setText(found.getYear());
        CGPA.setText(calculateCgpaFromGradesFile(found.getStudentID()));

        System.out.println("Trying to load saved recovery plan for student: " + found.getStudentID());

        RecoveryPlan savedPlan =
                CRPRepo.loadRecoveryPlanByStudentId(found.getStudentID(), RECOVERY_FILE);


        if (savedPlan != null) {
            System.out.println("Saved plan loaded. Task count = " + savedPlan.getTasks().size());
        } else {
            System.out.println("Saved plan returned NULL (unexpected).");
        }

        if (savedPlan != null && !savedPlan.getTasks().isEmpty()) {
            System.out.println("Using SAVED plan. Updating table model...");
            this.currentPlan = savedPlan;
            planTableModel.setPlan(savedPlan);
        } else {
            System.out.println("NO saved plan exists → Populating failed components instead.");
            populateFailedComponents(found.getStudentID());
        }

        System.out.println("Table model rows after update = " + planTableModel.getRowCount());

        updateProgressBarAndGrade();
        updateFailedSummaryFromPlan();

        System.out.println("Progress, Grade & Failed Summary updated.");
    }//GEN-LAST:event_searchbuttonActionPerformed

    private void searchbuttonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbuttonKeyPressed

    }//GEN-LAST:event_searchbuttonKeyPressed

    private void TXTsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTsearchActionPerformed
    searchbutton.doClick();
    }//GEN-LAST:event_TXTsearchActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
    ((RecoveryPlanTable) CRPTable.getModel()).stopEdit();
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void EditRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditRowButtonActionPerformed

    }//GEN-LAST:event_EditRowButtonActionPerformed

    private void AddTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddTaskActionPerformed

private void populateFailedComponents(String studentId) {
    RecoveryPlan plan = new RecoveryPlan(studentId);

    List<CourseComponent> failedList = failedRepo.findFailedComponentsByStudent(studentId);

    int counter = 1;
    for (CourseComponent cc : failedList) {
        String taskId = studentId + "-" + counter++;

        AcademicTask task = new Milestone(
                taskId,
                studentId,
                cc.getCourseCode(),
                cc.getComponentName(),
                "",        
                "",        
                "",          
                TaskStatus.NOT_STARTED
        );

        plan.addTask(task);
    }

    this.currentPlan = plan;
    planTableModel.setPlan(plan);
    updateProgressBarAndGrade();
    updateFailedSummaryFromPlan();  

    System.out.println("Loaded " + failedList.size() + " failed components for " + studentId);
}



private void updateFailedSummaryFromPlan() {
    if (currentPlan == null || currentPlan.getTasks().isEmpty()) {
        FailedSummaryLabel.setText("Failed: None");
        System.out.println("[FAILED-SUMMARY] No tasks in plan, set to 'Failed: None'");
        return;
    }

    StringBuilder sb = new StringBuilder("Failed: ");
    java.util.List<AcademicTask> tasks = currentPlan.getTasks();

    for (int i = 0; i < tasks.size(); i++) {
        AcademicTask t = tasks.get(i);
        sb.append(t.getCourseCode())
          .append(" (")
          .append(t.getComponentName())
          .append(")");
        if (i < tasks.size() - 1) {
            sb.append(", ");
        }
    }

    String text = sb.toString();
    FailedSummaryLabel.setText(text);
    System.out.println("[FAILED-SUMMARY] " + text);
}


private void updateProgressBarAndGrade() {
    if (currentPlan == null) {
        jProgressBar1.setValue(0);
        percentagelabel.setText("0% Completed");
        FinalGrade.setText("-");
        return;
    }

    double pct = currentPlan.getProgressPercentage();
    int value = (int) Math.round(pct);

    jProgressBar1.setValue(value);
    percentagelabel.setText(value + "% Completed");
    FinalGrade.setText(calculateExpectedGrade(pct));
}

private String calculateExpectedGrade(double pct) {
    if (pct >= 80) return "A";
    if (pct >= 60) return "B";
    if (pct >= 40) return "C";
    return "D";
}

private void onAddTask() {
    if (currentPlan == null || "UNKNOWN".equals(currentPlan.getStudentId())) {
        JOptionPane.showMessageDialog(this,
                "Please search and select a student first.",
                "No Student Selected",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    TaskDialog dialog = new TaskDialog(this, currentPlan.getStudentId());
    dialog.setVisible(true);

    AcademicTask newTask = dialog.getCreatedTask();
    if (newTask != null) {
        currentPlan.addTask(newTask);
        planTableModel.setPlan(currentPlan);
        updateProgressBarAndGrade();
    }
}

private void onDeleteTask() {
    int row = CRPTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a task to delete.",
                "No Task Selected",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this task?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    currentPlan.removeTask(row);
    planTableModel.setPlan(currentPlan);
    updateProgressBarAndGrade();
}

private void onEditRow() {
    int row = CRPTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a task to edit.",
                "No Task Selected",
                JOptionPane.WARNING_MESSAGE);
        return;
    }
    RecoveryPlanTable m = (RecoveryPlanTable) CRPTable.getModel();
    m.startEditRow(row);
    
    CRPTable.editCellAt(row, 2);
    CRPTable.requestFocusInWindow();
}

private void onSavePlan() {
        if (CRPTable.isEditing()) {
        CRPTable.getCellEditor().stopCellEditing();
    }
    planTableModel.stopEdit();

    if (currentPlan == null || "UNKNOWN".equals(currentPlan.getStudentId())) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "No recovery plan to save.",
                "Nothing To Save",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    CRPRepo.saveRecoveryPlan(currentPlan, RECOVERY_FILE);

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Recovery Plan saved successfully.",
            "Saved",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
}


private void onSubmitPlan() {
    if (currentPlan == null || "UNKNOWN".equals(currentPlan.getStudentId())) {
        JOptionPane.showMessageDialog(
                this,
                "No recovery plan is loaded for a student.",
                "Cannot Submit",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (currentPlan.getTasks().isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "There are no tasks in this recovery plan.",
                "Cannot Submit",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    for (AcademicTask t : currentPlan.getTasks()) {
        if (t.getWeekRange().trim().isEmpty() ||
            t.getDescription().trim().isEmpty() ||
            t.getDeadline().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All tasks must have Week Range, Task, and Deadline before submitting.\n" +
                    "Please complete the missing fields.",
                    "Incomplete Plan",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
    }

    CRPRepo.saveRecoveryPlan(currentPlan, RECOVERY_FILE);

    JOptionPane.showMessageDialog(
            this,
            "Recovery Plan submitted successfully.",
            "Plan Submitted",
            JOptionPane.INFORMATION_MESSAGE
    );
}




    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        java.awt.EventQueue.invokeLater(() -> new CourseRecoveryPlan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddTask;
    private javax.swing.JLabel BSCProgram;
    private javax.swing.JLabel CGPA;
    private javax.swing.JLabel CGPALabel;
    private javax.swing.JTable CRPTable;
    private javax.swing.JLabel CRPlabel;
    private javax.swing.JButton DeleteTask;
    private javax.swing.JButton EditRowButton;
    private javax.swing.JLabel FailedSummaryLabel;
    private javax.swing.JLabel FinalGrade;
    private javax.swing.JLabel FinalGradeLabel;
    private javax.swing.JLabel IDLabel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel ProgramLabel;
    private javax.swing.JPanel ProgressBar;
    private javax.swing.JLabel ProgressLabel;
    private javax.swing.JButton SaveButton;
    private javax.swing.JLabel StudentInfoLbl;
    private javax.swing.JPanel StudentInfoPanel;
    private javax.swing.JLabel StudentName;
    private javax.swing.JLabel StudentTP;
    private javax.swing.JButton SubmitButton;
    private javax.swing.JTextField TXTsearch;
    private javax.swing.JLabel Year;
    private javax.swing.JLabel YearLabel;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel percentagelabel;
    private javax.swing.JButton searchbutton;
    private javax.swing.JLabel studentlabel;
    private javax.swing.JPanel wrapper;
    // End of variables declaration//GEN-END:variables
}
