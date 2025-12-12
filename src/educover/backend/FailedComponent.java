/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;
import educover.backend.CourseComponent;
import java.util.List;
/**
 *
 * @author Fia
 */
public interface FailedComponent {
    List<CourseComponent> findFailedComponentsByStudent(String studentId);
}
