
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Course {

    private String title;
    private int unit;
    private String semester;
    private String session;
    private String courseCode;
    private int level;
    private String grade;
    private String reg_number;

    // ✅ Constructor
    public Course(String title, int unit, String semester, String session,
            String courseCode, int level, String grade, String reg_number) {
        this.title = title;
        this.unit = unit;
        this.semester = semester;
        this.session = session;
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
        System.out.println("Session: " + session);
        System.out.println("Course Code: " + courseCode);
        System.out.println("Level: " + level);
        System.out.println("Grade: " + grade);
    }

    // ✅ Converts letter grades to points
    public double getGradePoint() {
        switch (grade.toUpperCase()) {
            case "A":
                return 5.0;
            case "B":
                return 4.0;
            case "C":
                return 3.0;
            case "D":
                return 2.0;
            case "E":
                return 1.0;
            default:
                return 0.0;
        }
    }

    // ✅ Calculates GPA for this course
    public double calculateGPA() {
        return getGradePoint(); // single course GPA = grade point
    }

    // ✅ Save course record to database
    public void saveToDatabase() throws Exception {
        String sql = "INSERT INTO result (reg_number, course_code, course_title, credit, grade, level, semester, gpa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = sqlconnector.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, reg_number);
                ps.setString(2, courseCode);
                ps.setString(3, title);
                ps.setInt(4, unit);
                ps.setString(5, grade);
                ps.setInt(6, level);
                ps.setString(7, semester);
                ps.setDouble(8, calculateGPA());

                ps.executeUpdate();
                System.out.println("✅ Course saved to database successfully!");
            }

        } catch (SQLException e) {
            throw new Exception("❌ Failed to save course: " + e.getMessage(), e);
        }
    }

    // ✅ Getters
    public String getTitle() {
        return title;
    }

    public int getUnits() {
        return unit;
    }

    public String getGrade() {
        return grade;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getLevel() {
        return level;
    }

    public String getSemester() {
        return semester;
    }

    public String getSession() {
        return session;
    }

    public String getRegNumber() {
        return reg_number;
    }

    public double getGpa() {
        return calculateGPA();
    }

    // ✅ Optional setters if you want to update values later
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
