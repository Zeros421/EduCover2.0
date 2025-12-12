/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

/**
 *
 * @author Fia
 */
public class CourseComponent {
    private final String StudentID;
    private final String CourseCode;
    private final String ComponentName;
    private final double Mark;
    private final boolean failed;
    
    public CourseComponent(String studentID,
                           String CourseCode,
                           String ComponentName,
                           double Mark,
                           boolean failed){
    this.StudentID=studentID;
    this.CourseCode=CourseCode;
    this.ComponentName=ComponentName;
    this.Mark=Mark;
    this.failed=failed;}
    
 public String getStudentID(){
    return StudentID;
 }
 public String getCourseCode(){
    return CourseCode;
 }
 public String getComponentName(){
    return ComponentName;
 }
 public double getMark(){
    return Mark;
 }
 public boolean isFailed(){
    return failed;
 }
 @Override
    public String toString() {
        return CourseCode + " - " + ComponentName + (failed ? " (FAILED)" : "");
    }
}
