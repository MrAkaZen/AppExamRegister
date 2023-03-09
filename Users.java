package university;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Users {
	
	    private String _role;
	    private String _firstName;
	    private String _lastName;
	    private int _id;
	    private String _pswPortal;
	    private ArrayList<Exam> _examSubjectList = new ArrayList<>();
	    

	    public String getRole() {
	        return _role;
	    }

	    public void setRole(String role) {
	        if (role.equals("student")) {
	            _role = role;
	            _examSubjectList = new ArrayList<>();
	        } else if (role.equals("teacher")) {
	            _role = role;
	        } else {
	            System.out.println("This role is not enabled");
	        }
	    }

	    public String getFirstName() {
	        return _firstName;
	    }

	    public void setFirstName(String firstName) {
	        _firstName = firstName;
	    }

	    public String getLastName() {
	        return _lastName;
	    }

	    public void setLastName(String lastName) {
	        _lastName = lastName;
	    }

	    public int getId() {
	        return _id;
	    }

	    public void setId(int id) {
	        _id = id;
	    }

	    public String getPswPortal() {
	        return _pswPortal;
	    }

	    public void setPswPortal(String pswPortal) {
	        _pswPortal = pswPortal;
	    }

	    public ArrayList<Exam> getExamSubjectList() {
	        return _examSubjectList;
	    }

	    
	    public Users(String role, String firstName, String lastName, int id, String pswPortal) {
	        _role = role;
	        _firstName = firstName;
	        _lastName = lastName;
	        _id = id;
	        _pswPortal = pswPortal;
	        if (_role.equals("student")) {
	            _examSubjectList = new ArrayList<>();
	        }
	    }
	
	    
	    public void addExam(UniversitySystem boot) {
	        try {
	            System.out.println("Enter exam information:");

	            System.out.print("Grade:\t\t");
	            int grade = Integer.parseInt(System.console().readLine());

	            if (grade >= 0 && grade <= 31) {
	                System.out.print("Subject Code:\t");
	                String subjectCode = System.console().readLine();

	                if (boot.getListOfSubjects().stream().anyMatch(subject -> subject.getCodSubj().equals(subjectCode))) {
	                    System.out.print("Student ID:\t");
	                    int studentId = Integer.parseInt(System.console().readLine());

	                    if (boot.getListOfUsers().stream().anyMatch(student -> student.getId() == studentId)) {
	                    	LocalDate date = LocalDate.now();
	                        Exam newExam = new Exam(studentId, subjectCode, grade, date);

	                        if (boot.getListOfExams().stream().anyMatch(exam -> exam.getStudentId() == newExam.getStudentId() && exam.getCodSubj().equals(newExam.getCodSubj()))) {
	                            System.out.println("\nThis exam already exists!\n");
	                            return;
	                        }
	                        boot.getListOfExams().add(newExam);
	                        System.out.printf("\nExam with code %s added successfully\n", subjectCode);

	                        Users student = boot.getListOfUsers().stream().filter(s -> s.getId() == studentId).findFirst().orElse(null);

	                        if (student != null) {
	                            // Add the new exam to the student's exam list
	                            student.getExamSubjectList().add(new Exam(subjectCode, grade, date));
	                        }

	                        // Upgrade the exam file with the new exam information
	                        Exam.upgradeExamFile(newExam);

	                        // This method saves the student's exam in a text file
	                        boot.saveExamResultsToFile(student);
	                    }
	                } else {
	                    System.out.println("The subject entered is not present in the official list");
	                }
	            } else {
	                System.out.println("The grade entered is not valid. The exam could not be added.");
	            }
	        } catch (Exception ex) {
	            System.out.println(ex.toString());
	        }
	    }
	    
	    public void ShowStudentsByExamSubject(UniversitySystem boot) {
	        System.out.println("Enter the subject's code:");
	        String codSubj = new Scanner(System.in).nextLine();
	        	        
	        List<Integer> students = boot.getListOfExams().stream()
	                .filter(e -> e.getCodSubj().equals(codSubj))
	                .map(Exam::getStudentId)
	                .distinct()
	                .collect(Collectors.toList());
	        
	        if (students.size() == 0) {
	            System.out.println("No students found for the exam subject with code: " + codSubj);
	        } else {
	            System.out.println("The students that have to take the exam with subject code " + codSubj + " are:");
	            students.forEach(s -> System.out.println("IdStudent: " + s));
	        }
	    }
	    
	    public void StudentsWith25GradeUpper(UniversitySystem boot) {
	    	List<Integer> studentsWithHighGrades = boot.getListOfExams()
	                .stream()
	                .filter(e -> e.getGrade() > 25)
	                .map(Exam::getStudentId)
	                .distinct()
	                .collect(Collectors.toList());

	System.out.println("Students with a grade above 25 :");
	for (Integer student : studentsWithHighGrades) {
	    System.out.println("IdStudent: " + student);
	}
	    }
	    
	    public void showStudentCareer(UniversitySystem boot) {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Enter the student id: ");
	        int idStudent = scanner.nextInt();
	        List<Exam> studentExams = boot.getListOfExams().stream()
	                .filter(e -> e.getStudentId() == idStudent)
	                .collect(Collectors.toList());

	        if (studentExams.isEmpty()) {
	            System.out.println("No exams found for student with ID " + idStudent);
	            return;
	        }

	        System.out.println("Student career for student with ID " + idStudent + ":");
	        for (Exam exam : studentExams) {
	            System.out.println("Subject code: " + exam.getCodSubj() + ", Grade: " + exam.getGrade());
	        }
	    }
	    
	    public void getCareer(UniversitySystem boot, Users student) {
	    	student.getExamSubjectList().clear();
	    	boot.getListOfUsers().stream().filter(s -> s.getId() == student.getId()).findAny();
	    	boot.loadExamResultsFromDirectory(student);

	    	System.out.println("\nExams achieved: " + student.getExamSubjectList().size());
	    	for (Exam examSubject : student.getExamSubjectList()) {
	    	    System.out.println("Cod. Subject: " + examSubject.getCodSubj() +
	    	                       ", Result: " + examSubject.getGrade() +
	    	                       ", Date: " + examSubject.getDate());
	    	}
	    	System.out.println();

	    	student.getExamSubjectList().clear();
	    }

	    public void getAverageGrade(UniversitySystem boot, Users student) {
	        Users averageGradeTargetStudent = boot.getListOfUsers().stream()
	                .filter(s -> s.getId() == student.getId())
	                .findFirst()
	                .orElse(null);

	        boot.loadExamResultsFromDirectory(averageGradeTargetStudent);

	        double totalScore = 0;
	        int numOfExams = averageGradeTargetStudent.getExamSubjectList().size();
	        for (Exam exam : averageGradeTargetStudent.getExamSubjectList()) {
	            totalScore += exam.getGrade();
	        }
	        double finalAverage = totalScore / numOfExams;

	        System.out.println("Average grades achieved\nAverage: " + finalAverage);
	    }
	    	    
	}

