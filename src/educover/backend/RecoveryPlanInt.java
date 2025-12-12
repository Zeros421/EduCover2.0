/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;
import educover.backend.CourseComponent;

/**
 *
 * @author Fia
 */
public interface RecoveryPlanInt {
    RecoveryPlan loadByStudentId(String studentId);
    void save(RecoveryPlan plan);
}
