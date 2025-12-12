/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Fia
 */
public class RecoveryPlan {
        private final String studentId;
    private final List<AcademicTask> tasks = new ArrayList<>();

    public RecoveryPlan(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<AcademicTask> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(AcademicTask task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public int getTotalTasks() {
        return tasks.size();
    }

    public int getCompletedTasks() {
        int count = 0;
        for (AcademicTask t : tasks) {
            if (t.getStatus() == TaskStatus.COMPLETED) {
                count++;
            }
        }
        return count;
    }

    public double getProgressPercentage() {
        if (tasks.isEmpty()) return 0.0;
        return (getCompletedTasks() * 100.0) / tasks.size();
    }
}
