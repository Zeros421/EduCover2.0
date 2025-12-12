package educover.backend;

public class GradeRecord {
    private String studentID;
    private String courseID;
    private String gradeLetter;
    private double gradePoint;
    private int year;
    private String semester;

    public GradeRecord(String studentID, String courseID, String gradeLetter,
                       double gradePoint, int year, String semester) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.gradeLetter = gradeLetter;
        this.gradePoint = gradePoint;
        this.year = year;
        this.semester = semester;
    }

    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public String getGradeLetter() { return gradeLetter; }
    public double getGradePoint() { return gradePoint; }
    public int getYear() { return year; }
    public String getSemester() { return semester; }
}
