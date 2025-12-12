package educover.backend;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Fia
 */
public class Student {
    private String StudentID;
    private String FirstName;
    private String LastName;
    private String Major;
    private String Year;
    private String Email;

    public Student(String studentID, String firstName, String lastName,
                   String major, String year, String email) {
        this.StudentID = studentID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Major = major;
        this.Year = year;
        this.Email = email;
    }

    public String getStudentID() { return StudentID; }
    public String getFullName() { return FirstName + " " + LastName; }
    public String getMajor()    { return Major; }
    public String getYear()     { return Year; }
    public String getEmail()    { return Email; }
}
