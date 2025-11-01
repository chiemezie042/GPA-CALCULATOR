import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Course {

    String title;
    int unit;
    String semester;
    String sessions;
    String courseCode;
    int level;
    String grade;
    String reg_number;

    // ✅ Constructor
    public Course(String title, int unit, String semester, String session,
                  String courseCode, int level, String grade, String reg_number) {
        this.title = title;
        this.unit = unit;
        this.semester = semester;
        this.sessions = session;
        this.courseCode = courseCode;
        this.level = level;
        this.grade = grade;
        this.reg_number = reg_number;
    }

    // ✅ Display method for debugging
    public void display() {
        System.out.println("Reg Number: " + reg_number);
        System.out.println("Course Title: " + title);
        System.out.println("Unit: " + unit);
        System.out.println("Semester: " + semester);
        System.out.println("Session: " + sessions);
        System.out.println("Course Code: " + courseCode);
        System.out.println("Level: " + level);
        System.out.println("Grade: " + grade);
    }

    // ✅ Converts letter grades to points
    public double getGradePoint() {
        switch (grade.toUpperCase()) {
            case "A": return 5.0;
            case "B": return 4.0;
            case "C": return 3.0;
            case "D": return 2.0;
            case "E": return 1.0;
            default:  return 0.0;
        }
    }

    // ✅ Calculates GPA for this course
    public double calculateGPA() {
        return getGradePoint(); // single course GPA = grade point
    }

    // ✅ Save course record to database
    public void saveToDatabase() throws Exception {
        // Connect to DB using your sqlconnector or DatabaseConnection class
        try{
        Connection conn = sqlconnector.connect(); // adjust if your connector class name differs

                if(conn!= null){

        // ✅ SQL insert query (matches your table columns)
        String sql = "INSERT INTO result (reg_number, course_code, course_title, credit, grade, level, semester, gpa) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, reg_number);
        ps.setString(2, courseCode);
        ps.setString(3, title);
        ps.setInt(4, unit);
        ps.setString(5, grade);
        ps.setInt(6, level);
        ps.setString(7, semester);
        ps.setDouble(8, calculateGPA());

        ps.executeUpdate();
        ps.close();
        conn.close();

        System.out.println("✅ Course saved to database successfully!");
    }}catch (SQLException e) {
        e.getMessage();
    }
    }

    // ✅ Getters
    public String getTitle() {
        return title; }
    public int getUnits() {
        return unit; }
    public String getGrade() {
        return grade; }
    public String getCourseCode() { 
        return courseCode; }
    public int getLevel() {
        return level; }
}
