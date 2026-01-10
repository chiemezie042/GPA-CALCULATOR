import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized MySQL database connector.
 * Works without MySQL Workbench.
 * Requires mysql-connector-j (8.4+ recommended).
 */
public final class sqlconnector {

    // ===================== DATABASE CONFIG =====================

    private static final String URL =
            "jdbc:mysql://localhost:3306/gpadb"
          + "?useSSL=false"
          + "&allowPublicKeyRetrieval=true"
          + "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "Chiemezie@123";

    // ===================== CONNECTION HOLDER =====================

    private static Connection connection;

    // Prevent instantiation
    private sqlconnector() {}

    // ===================== PUBLIC API =====================

    /**
     * Returns a valid MySQL connection.Creates a new one if none exists or if it was closed.
     * @return
     */
    public static Connection getConnection() {
        try {
            // Ensure JDBC driver is available
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Connecting to MySQL database...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connection established");
            }

            return connection;

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "MySQL JDBC Driver not found. Add mysql-connector-j to your project.",
                e
            );

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Failed to connect to MySQL database. Check URL, credentials, and server status.",
                e
            );
        }
    }

    /**
     * Closes the database connection safely.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===================== QUICK TEST =====================

    public static void main(String[] args) {
        Connection conn = sqlconnector.getConnection();
        System.out.println(conn != null ? "✅ TEST PASSED" : "❌ TEST FAILED");
        sqlconnector.closeConnection();
    }
}
