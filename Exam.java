package university;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Exam {
	
    private int _idStudent;
    private String _codSubj;
    private LocalDate _date;
    private int _grade;

    public int getStudentId() {
        return _idStudent;
    }

    public String getCodSubj() {
        return _codSubj;
    }

    public int getGrade() {
        return _grade;
    }

    public void setGrade(int grade) {
        if (grade >= 0 && grade <= 31) {
            this._grade = grade;
        } else {
            System.out.println("It is not possible to put this grade");
        }
    }

    public LocalDate getDate() {
        return _date;
    }

    // This constructor allows you to create an exam to add to the general list of all exams
    public Exam(int studentId, String codSubj, int grade, LocalDate date) {
        this._idStudent = studentId;
        this._codSubj = codSubj;
        this._grade = grade;
        this._date = date;
    }

    // This constructor allows you to create an exam to add to the relevant student's list
    public Exam(String codSubj, int grade, LocalDate date) {
        this._codSubj = codSubj;
        this._grade = grade;
        this._date = date;
    }
    

	public static void upgradeExamFile(Exam newExam) {
            try {
                Path currentPath = Paths.get("").toAbsolutePath();
                String filePath = currentPath.toString() + "/Exams.txt";

                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                writer.write(newExam.getStudentId() + "," + newExam.getCodSubj() + "," + newExam.getGrade() + "," + newExam.getDate().toString());
                writer.newLine();
                writer.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    
    public static void showListExams(List<Exam> list) {
        try {
            for (Exam exam : list) {
                System.out.println("Student ID: " + exam.getStudentId());
                System.out.println("Subject Code: " + exam.getCodSubj());
                System.out.println("Grade: " + exam.getGrade());
                System.out.println();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public String toString() {
        return "Exam - ID student: " + _idStudent + ", Cod. Subject: " + _codSubj + ", Grade: " + _grade;
    }
}


