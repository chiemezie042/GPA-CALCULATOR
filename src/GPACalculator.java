/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;
/**
 *
 * @author USER
 */
public class GPACalculator {
   private Scanner scanner;
   String session,semester;
    public GPACalculator() {
        scanner = new Scanner(System.in);
        
    }

    public void start() {
        System.out.println("Welcome to the GPA Calculator! Let's calculate your GPA.");

        while (true) {
            System.out.println("Enter the session");
     // String session = scanner.nextLine();
      session = getValidSession();
     System.out.println("Enter the semester");
      //String semester = scanner.nextLine()
      semester = getValidSemester();
      
            int numCourses = getNumberOfCourses();
            double totalCredits = 0;
            double totalGradePoints = 0;

            for (int i = 1; i <= numCourses; i++) {
                //Course course = getCourseDetails(i);
                Course course = getCourseDetails(i);
                totalCredits += course.getUnits();
                totalGradePoints += course.getUnits() * course.getGradePoint();
            }
           

            if (totalCredits > 0) {
                double gpa = totalGradePoints / totalCredits;
                System.out.printf("Your GPA is %.2f.%n", gpa);
            } else {
                System.out.println("No valid credits entered. Cannot calculate GPA.");
            }

            if (!askToContinue()) {
                System.out.println("Thank you for using the GPA Calculator! Goodbye.");
                break;
            }
        }
    }
    private String getValidSemester(){
        while (true){
            System.out.print("semester(first/second): ");
            String semester = scanner.nextLine().toLowerCase();
            if (semester.equals("first")||
                    semester.equals("second")){
                return semester;
            }
            System.out.println("invalid semester. Please enter 'First' or 'Second' .");
        }
    }
    private String getValidSession(){
        while (true){
            System.out.print("session(yyyy/yyyy): ");
            String session = scanner.nextLine();
            if (session.matches("\\d{4}/\\d{4}")){
                return session;
            }
            System.out.println("invalid session format. Example: 2023/2024");
        }
    }

    private int getNumberOfCourses() {
        while (true) {
            System.out.print("Enter the number of courses : ");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                if (num >= 1 && num <= 20) {
                    //scanner.nextInt();
                    return num;
                }
            } else {
                scanner.next(); // clear invalid input
                System.out.println("Invalid input. Please enter a number between 1 and 20.");
            }
        }
    }

  private Course getCourseDetails(int courseNumber) {
                scanner.nextLine();
       
       System.out.print("Enter Course Name " + courseNumber + ":");
      String courseName = scanner.nextLine();
      int credits = getValidCredits();
      String grade = getValidGrade();
                      scanner.nextLine();

      
      System.out.println("Enter the courseCode");
      String courseCode = scanner.nextLine();
      System.out.println("Enter the level");
      int level = scanner.nextInt();
      System.out.println("Enter the lecturer");
      String lecturer = scanner.nextLine();
      System.out.println(semester + session);
       return new Course(courseName, credits,semester,session, courseCode,level,grade,lecturer);
   }

    private int getValidCredits() {
        while (true) {
            System.out.print("Enter credit units : ");
            if (scanner.hasNextInt()) {
                int credits = scanner.nextInt();
                if (credits >= 1 && credits <= 6) {
                    return credits;
                }
            } else {
                scanner.next(); // clear invalid input
            }
            System.out.println("Invalid credits. Enter a number between 1 and 5.");
        }
    }

    private String getValidGrade() {
        while (true) {
            System.out.print("Enter grade (A, B, C, D, E, F): ");
            String grade = scanner.next().toUpperCase();
            if (grade.matches("[ABCDEF]")) {
                return grade;
            }
            System.out.println("Invalid grade. Enter A, B, C, D, or F.");
        }
    }

    private boolean askToContinue() {
        while (true) {
            System.out.print("Would you like to calculate another GPA? (yes/no): ");
            String choice = scanner.next().toLowerCase();
            if (choice.equals("yes")) {
                return true;
            } else if (choice.equals("no")) {
                return false;
            }else{
            System.out.println("Invalid input. Enter 'yes' or 'no'.");
            }
            
        }
    }
}