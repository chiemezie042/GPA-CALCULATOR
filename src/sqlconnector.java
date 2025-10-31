import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlconnector {

    // ✅ Database details
    private static final String URL = "jdbc:mysql://localhost:3306/gpadb?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Chiemezie@123";

    // ✅ Keep a single connection reference
    private static Connection conn = null;

    // ✅ Method to get a valid connection (auto-reconnect)
    public static Connection connect() {
        try {
            // ✅ Load MySQL JDBC Driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // If connection doesn’t exist or is closed — create a new one
            if (conn == null || conn.isClosed()) {
                System.out.println("🔄 Connecting to MySQL database...");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connection established successfully!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found! Please add mysql-connector-j to your project.");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }

        return conn;
    }
    public static void main(String[] args) {
    Connection test = sqlconnector.connect();
    if (test != null) {
        System.out.println("✅ Test connection successful!");
    } else {
        System.out.println("❌ Test connection failed!");
    }
}


    // ✅ Optional method to manually close connection when done
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Connection closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error closing connection: " + e.getMessage());
        }
    }
}
