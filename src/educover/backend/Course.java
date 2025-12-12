package educover.backend;

public class Course {
    private String courseID;
    private String courseName;
    private int credits;
    private String semester;
    private String instructor;
    private int capacity;

    public Course(String courseID, String courseName, int credits, 
                  String semester, String instructor, int capacity) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = instructor;
        this.capacity = capacity;
    }

    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getSemester() { return semester; }
    public String getInstructor() { return instructor; }
    public int getCapacity() { return capacity; }
}
