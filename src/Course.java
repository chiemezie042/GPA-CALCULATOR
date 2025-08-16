
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */
public class Course {
  String title;
     int unit;
     String semester;
     String sessions;
     String courseCode;
     int level;
     String grade;
     String lecturer;
    
    public Course(String title, int unit, String semester, String sessions, String courseCode,int level, String grade, String lecturer){
       this.title = title;
       this.unit = unit;
       this.semester = semester;
       this.sessions = sessions;
       this.courseCode = courseCode;
       this.level = level;
       this.grade = grade;
       this.lecturer = lecturer;
    
    } 

    //Course(int credits, String grade) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //}
    
 //   public void getCourseDetails(int courseNumber) {
   //b        Scanner scanner = new Scanner(System.in);

     //           scanner.nextLine();

       // System.out.print("Enter Course Name " + courseNumber + ":");
        //String courseName = scanner.nextLine();
        //int credits = getValidCredits();
        //String grade = getValidGrade();
        //return new Course(credits, grade);
    //}
    public void display(){
        System.out.println("course title" + title);
        System.out.println("unit" + unit);
        System.out.println("semester" + semester);
        System.out.println("sessions" + sessions );
        System.out.println("Course Code" + courseCode);
        System.out.println("level" + level);
        System.out.println("grade" + grade);
        System.out.println("lecturer: " + lecturer);
    }
   public double getGradePoint() {
       switch(grade){
           case "A": return 5.0;
           case "B": return 4.0;
           case "C": return 3.0;
           case "D": return 2.0;
           case "E": return 1.0;
           default:  return 0.0;
       }
   }
   public String getCourseTitle(){
       return title;
   }

    //double getCredits() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //}

    //int getGrade() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //}
   
   public int getUnits(){
   return unit;
   }
    
      
}
