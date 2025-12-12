/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover;
import educover.backend.AcademicTask;
import educover.backend.LectureRecommendation;
import educover.backend.Milestone;
import educover.backend.TaskStatus;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JTextFieldDateEditor;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Fia
 */
public class TaskDialog extends JDialog {

    private final String studentId;

    private JTextField txtCourseCode;
    private JTextField txtComponentName;
    private JTextField txtWeekRange;
    private JTextField txtDescription;
    private JDateChooser deadlineChooser;
    private JComboBox<String> cmbTaskType;

    private AcademicTask createdTask;

    private boolean isEmpty(JTextField field) {
    return field.getText() == null || field.getText().trim().isEmpty();
}

    public TaskDialog(JFrame parent, String studentId) {
        super(parent, "Add Recovery Task", true);
        this.studentId = studentId;

        setLayout(new GridLayout(7, 2, 8, 8));


        add(new JLabel("Course Code:"));
        txtCourseCode = new JTextField();
        add(txtCourseCode);

        
        add(new JLabel("Failed Component:"));
        txtComponentName = new JTextField();
        add(txtComponentName);

        
        add(new JLabel("Week Range:"));
        txtWeekRange = new JTextField();
        add(txtWeekRange);

        
        add(new JLabel("Task Description:"));
        txtDescription = new JTextField();
        add(txtDescription);

        
        add(new JLabel("Deadline:"));
        deadlineChooser = new JDateChooser();
        deadlineChooser.setDateFormatString("dd/MM/yyyy");

        JTextFieldDateEditor editor = (JTextFieldDateEditor) deadlineChooser.getDateEditor();
        editor.setEditable(false);

        add(deadlineChooser);

 
        add(new JLabel("Task Type:"));
        cmbTaskType = new JComboBox<>(new String[] {
                "MILESTONE", "LECTURE_RECOMMENDATION"
        });
        add(cmbTaskType);


        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> onAddClicked());
        add(btnAdd);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        setSize(420, 320);
        setLocationRelativeTo(parent);
    }

private void onAddClicked() {

    String course = txtCourseCode.getText().trim();
    String comp   = txtComponentName.getText().trim();
    String week   = txtWeekRange.getText().trim();
    String desc   = txtDescription.getText().trim();
    String type   = (String) cmbTaskType.getSelectedItem();


    if (course.isEmpty() || comp.isEmpty() ||
        week.isEmpty()   || desc.isEmpty()) {

        JOptionPane.showMessageDialog(this,
                "All fields must be filled before adding a task.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    Date d = deadlineChooser.getDate();
    if (d == null) {
        JOptionPane.showMessageDialog(this,
                "Please select a deadline.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    String deadline = new SimpleDateFormat("dd/MM/yyyy").format(d);

    String taskId = studentId + "-" + System.currentTimeMillis();
    TaskStatus status = TaskStatus.NOT_STARTED;

    AcademicTask task;
    if ("LECTURE_RECOMMENDATION".equalsIgnoreCase(type)) {
        task = new LectureRecommendation(
                taskId,
                studentId,
                course,
                comp,
                week,
                desc,
                deadline,
                status
        );
    } else {
        task = new Milestone(
                taskId,
                studentId,
                course,
                comp,
                week,
                desc,
                deadline,
                status
        );
    }

    this.createdTask = task;
    dispose();
}


    public AcademicTask getCreatedTask() {
        return createdTask;
    }
}
