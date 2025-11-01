import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student {
     String reg_number;
     String name;
     int level;
     String session;
    String semester; // 🆕 added field

    // 🆕 Updated constructor to include semester
    public Student(String reg_number, String name, int level, String session, String semester) {
        this.reg_number = reg_number;
        this.name = name;
        this.level = level;
        this.session = session;
        this.semester = semester;
    }

    public void saveToDatabase() {
    String sql = "INSERT INTO students (reg_number, name, level, session, semester) VALUES (?, ?, ?, ?, ?)";

    // ✅ Try to get a connection from sqlconnector
    try{
    Connection conn = sqlconnector.connect();

    // ⚠️ Check if the connection failed
    if (conn != null) {
        //System.out.println("❌ Database connection is NULL. Could not save student.");
        

        PreparedStatement stmt = conn.prepareStatement(sql);
        // ✅ Assign field values to query
        stmt.setString(1, reg_number);
        stmt.setString(2, name);
        stmt.setInt(3, level);
        stmt.setString(4, session);
        stmt.setString(5, semester);

        stmt.executeUpdate();
        System.out.println("✅ Student record saved successfully!");
    } } catch (SQLException e) {
        System.out.println("❌ Error saving student: " + e.getMessage());
    }
}


    // getters
    public String getRegNumber() { return reg_number; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public String getSession() { return session; }
    public String getSemester() { return semester; } // 🆕 getter for semester
}


