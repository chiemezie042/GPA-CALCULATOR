
import java.sql.*;
import java.util.Scanner;

/**
 * GPA Calculator Program with MySQL Database Integration
 * ------------------------------------------------------- This class allows you
 * to: - View previous results by registration number - Create a new student
 * profile - Enter course details and calculate GPA - Store results in a MySQL
 * database
 */
public class GPACalculator {

    private final Scanner scanner = new Scanner(System.in);

    // Student details
    private String session, semester, name, reg_number;
    private int level;
    private Student student;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gpadb"; // change to your database
    private static final String DB_USER = "root";  // change if needed
    private static final String DB_PASS = "Chiemezie@123"; // change to your password

    public void start() {
        System.out.println("===== Welcome to the GPA Calculator =====");

        while (true) {
            // Ask user whether to view or calculate GPA
            System.out.println("\n1️⃣  View Previous Results");
            System.out.println("2️⃣  Calculate New GPA");
            System.out.print("Enter your choice (1 or 2): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewResultsOption();
                    break;
                case "2":
                    calculateNewGpaOption();
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1 or 2.");
                    continue;
            }

            // Ask if user wants to continue
            if (!askToContinue()) {
                System.out.println("👋 Thank you for using the GPA Calculator! Goodbye.");
                break;
            }
        }
    }

    // ================= VIEW PREVIOUS RESULTS =================
    private void viewResultsOption() {
        System.out.print("\nEnter Registration Number (yyyy/xxxxxx): ");
        reg_number = getValidRegNo();
        showPreviousResult(reg_number);
    }

    // ================= CALCULATE NEW GPA =================
    private void calculateNewGpaOption() {
        // Create new student record
        System.out.print("Enter Registration Number: ");
        reg_number = getValidRegNo();

        System.out.print("Enter Student Name: ");
        name = scanner.nextLine();

        System.out.print("Enter Level (100, 200, 300, 400): ");
        level = getValidLevel();

        System.out.print("Enter Session (e.g., 2023/2024): ");
        session = getValidSession();

        System.out.print("Enter Semester (first/second): ");
        semester = getValidSemester();

        student = new Student(reg_number, name, level, session, semester);
        student.saveToDatabase();

        System.out.println("\n Student profile saved successfully.");

        int numCourses = getNumberOfCourses();
        double totalCredits = 0;
        double totalGradePoints = 0;

        for (int i = 1; i <= numCourses; i++) {
            Course course = getCourseDetails(i);
            totalCredits += course.getUnits();
            totalGradePoints += course.getUnits() * course.getGradePoint();
            saveResultToDatabase(course);

        }

        // Calculate GPA
        if (totalCredits > 0) {
            double GPA = totalGradePoints / totalCredits;
            System.out.printf("\n Your GPA for this semester is: %.2f%n", GPA);
        } else {
            System.out.println("⚠ No valid credits entered. GPA cannot be calculated.");
        }
    }

    /**
     * Displays previous results for a student, filtered either by
     * level/semester or for all semesters grouped together.
     */
    private void showPreviousResult(String regNo) {
        try (Connection conn = sqlconnector.connect()) {

            // Step 1️⃣: Ask what the user wants to view
            System.out.println("\n📘 What would you like to view?");
            System.out.println("1. View results for a specific LEVEL and SEMESTER");
            System.out.println("2. View all results (grouped by LEVEL & SEMESTER)");
            System.out.print("Enter choice (1 or 2): ");
            int choice = Integer.parseInt(scanner.nextLine());

            // Step 2️⃣: Retrieve student info first
            String studentSql = "SELECT name, level FROM students WHERE reg_number = ?";
            PreparedStatement studentStmt = conn.prepareStatement(studentSql);
            studentStmt.setString(1, regNo);
            ResultSet studentRs = studentStmt.executeQuery();

            if (!studentRs.next()) {
                System.out.println("❌ No student found with registration number: " + regNo);
                return;
            }

            String name = studentRs.getString("name");
            int level = studentRs.getInt("level");

            System.out.println("\n==============================================");
            System.out.println("📖 STUDENT PROFILE");
            System.out.println("==============================================");
            System.out.println("Name: " + name);
            System.out.println("Reg Number: " + regNo);
            System.out.println("Level: " + level);
            System.out.println("==============================================");

            // Step 3️⃣: Handle user’s choice
            switch (choice) {
                case 1:
                    // User wants specific level + semester
                    System.out.print("Enter Level to view (e.g., 100, 200, 300): ");
                    int selectedLevel = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter Semester to view (first/second): ");
                    String selectedSemester = getValidSemester();

                    displayResultsByLevel(conn, regNo, selectedLevel, selectedSemester);
                    break;

                case 2:
                    // User wants to view all results grouped by level & semester
                    String sql = "SELECT DISTINCT level, semester FROM result WHERE reg_number = ? ORDER BY level, semester";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, regNo);
                    ResultSet semestersRs = ps.executeQuery();

                    boolean hasAnyResult = false;
                    while (semestersRs.next()) {
                        hasAnyResult = true;
                        int lvl = semestersRs.getInt("level");
                        String sem = semestersRs.getString("semester");

                        // Display each level’s result
                        System.out.println("\n==============================================");
                        System.out.println("📚 " + lvl + " LEVEL (" + sem.toUpperCase() + " SEMESTER)");
                        System.out.println("==============================================");
                        displayResultsByLevel(conn, regNo, lvl, sem);
                    }

                    if (!hasAnyResult) {
                        System.out.println("⚠ No results found for this student.");
                    }
                    break;

                default:
                    System.out.println("⚠ Invalid choice.");
                    break;
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Please enter a valid number.");
        }
    }

    /**
     * Helper method to display results for a given level and semester.
     */
    private void displayResultsByLevel(Connection conn, String regNo, int level, String semester) throws SQLException {
        // Updated SQL: filter by level and semester instead of session
        String sql = "SELECT course_code, course_title, credit, grade, gpa FROM result "
                + "WHERE reg_number = ? AND level = ? AND semester = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, regNo);
        ps.setInt(2, level);
        ps.setString(3, semester);
        ResultSet rs = ps.executeQuery();

        boolean hasRecords = false;
        double totalGradePoints = 0;
        double totalCredits = 0;

        System.out.printf("%-10s %-25s %-10s %-10s %-10s%n", "Code", "Title", "Credit", "Grade", "GP");
        System.out.println("--------------------------------------------------------------");

        while (rs.next()) {
            hasRecords = true;
            String courseCode = rs.getString("course_code");
            String courseTitle = rs.getString("course_title");
            int credit = rs.getInt("credit");
            String grade = rs.getString("grade");
            double gpa = rs.getDouble("gpa");

            System.out.printf("%-10s %-25s %-10d %-10s %-10.2f%n",
                    courseCode, courseTitle, credit, grade, gpa);

            totalCredits += credit;
            totalGradePoints += gpa * credit;
        }

        if (hasRecords) {
            double semesterGPA = totalGradePoints / totalCredits;
            System.out.println("--------------------------------------------------------------");
            System.out.printf("🎯 GPA for Level %d (%s Semester): %.2f%n", level, semester, semesterGPA);
        } else {
            System.out.println("⚠ No results found for this semester.");
        }
    }

    // ================= VALIDATION METHODS =================
    private int getValidLevel() {
        while (true) {
            if (scanner.hasNextInt()) {
                int lvl = scanner.nextInt();
                scanner.nextLine();
                if (lvl == 100 || lvl == 200 || lvl == 300 || lvl == 400) {
                    return lvl;
                }
            } else {
                scanner.next();
            }
            System.out.print("Invalid level. Enter 100, 200, 300, or 400: ");
        }
    }

    private String getValidSemester() {
        while (true) {
            String sem = scanner.nextLine().toLowerCase();
            if (sem.equals("first") || sem.equals("second")) {
                return sem;
            }
            System.out.print("Invalid semester. Enter 'first' or 'second': ");
        }
    }

    private String getValidSession() {
        while (true) {
            String sess = scanner.nextLine();
            if (sess.matches("\\d{4}/\\d{4}")) {
                return sess;
            }
            System.out.print("Invalid session format. Use e.g. 2023/2024: ");
        }
    }

    private int getNumberOfCourses() {
        while (true) {
            System.out.print("Enter number of courses (1–20): ");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num >= 1 && num <= 20) {
                    return num;
                }
            } else {
                scanner.next();
            }
            System.out.println("Invalid number. Try again.");
        }
    }

    private String getValidRegNo() {
        while (true) {
            String reg = scanner.nextLine().trim();
            if (reg.matches("\\d{4}/\\d{6}")) {
                return reg;
            }
            System.out.print("Invalid format. Must be YYYY/XXXXXX: ");
        }
    }

    private Course getCourseDetails(int index) {
        System.out.print("\nEnter Course Title " + index + ": ");
        String courseName = scanner.nextLine();

        int credits = getValidCredits();
        String grade = getValidGrade();

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();

        return new Course(courseName, credits, semester, session, courseCode, level, grade, reg_number);
    }

    private int getValidCredits() {
        while (true) {
            System.out.print("Enter credit units (1–6): ");
            if (scanner.hasNextInt()) {
                int c = scanner.nextInt();
                scanner.nextLine();
                if (c >= 1 && c <= 6) {
                    return c;
                }
            } else {
                scanner.next();
            }
            System.out.println("Invalid input. Try again.");
        }
    }

    private String getValidGrade() {
        while (true) {
            System.out.print("Enter Grade (A–F): ");
            String grade = scanner.next().toUpperCase();
            scanner.nextLine();
            if (grade.matches("[ABCDEF]")) {
                return grade;
            }
            System.out.println("Invalid grade. Try again.");
        }
    }

    private boolean askToContinue() {
        while (true) {
            System.out.print("\nWould you like to continue? (yes/no): ");
            String response = scanner.next().toLowerCase();
            scanner.nextLine();
            if (response.equals("yes")) {
                return true;
            }
            if (response.equals("no")) {
                return false;
            }
            System.out.println("Invalid input. Enter yes or no.");
        }
    }

    // ... other code like connectToDatabase(), etc.
    public void saveResultToDatabase(Course course) {
        String sql = "INSERT INTO result (reg_number, course_code, course_title, credit, grade, level, semester, gpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = sqlconnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reg_number);
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getTitle());
            stmt.setInt(4, course.getUnits());
            stmt.setString(5, course.getGrade());
            stmt.setInt(6, level);
            stmt.setString(7, semester);
            stmt.setDouble(8, course.getGradePoint());
            stmt.executeUpdate();

            System.out.println("✅ Course result saved successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error saving course result: " + e.getMessage());
        }
    }

}
