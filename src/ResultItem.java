// Represents one row of GPA result in the table

public class ResultItem {

    private final String course;
    private final int unit;
    private final String grade;
    private final double gpa;

    public ResultItem(String course, int unit, String grade, double gpa) {
        this.course = course;
        this.unit = unit;
        this.grade = grade;
        this.gpa = gpa;
    }

    public String getCourse() {
        return course;
    }

    public int getUnit() {
        return unit;
    }

    public String getGrade() {
        return grade;
    }

    public double getGpa() {
        return gpa;
    }
}
