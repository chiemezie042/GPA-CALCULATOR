
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student {

    private String reg_number;
    private String name;
    private int level;
    private String session;
    private String semester; // Semester field
    private String department; // 🆕 Optional field for school system

    // ✅ Constructor
    public Student(String reg_number, String name, int level, String session, String semester) {
        this.reg_number = reg_number;
        this.name = name;
        this.level = level;
        this.session = session;
        this.semester = semester;
        this.department = "General"; // default, can be updated later
    }

    // ✅ Constructor with department
    public Student(String reg_number, String name, int level, String session, String semester, String department) {
        this.reg_number = reg_number;
        this.name = name;
        this.level = level;
        this.session = session;
        this.semester = semester;
        this.department = department;
    }

    // ✅ Save student record to database
    public void saveToDatabase() throws Exception {
        String sql = "INSERT INTO student (reg_number, name, level, session, semester, department) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = sqlconnector.getConnection()) { // auto-close
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, reg_number);
                stmt.setString(2, name);
                stmt.setInt(3, level);
                stmt.setString(4, session);
                stmt.setString(5, semester);
                stmt.setString(6, department);

                stmt.executeUpdate();
                System.out.println("✅ Student record saved successfully!");
            }

        } catch (SQLException e) {
            throw new Exception("❌ Failed to save student: " + e.getMessage(), e);
        }
    }

    // ✅ Display for debugging
    public void display() {
        System.out.println("Reg Number: " + reg_number);
        System.out.println("Name: " + name);
        System.out.println("Level: " + level);
        System.out.println("Session: " + session);
        System.out.println("Semester: " + semester);
        System.out.println("Department: " + department);
    }

    // ✅ Getters
    public String getRegNumber() {
        return reg_number;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getSession() {
        return session;
    }

    public String getSemester() {
        return semester;
    }

    public String getDepartment() {
        return department;
    }

    // ✅ Optional setter for department
    public void setDepartment(String department) {
        this.department = department;
    }
}
