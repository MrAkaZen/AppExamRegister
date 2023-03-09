package university;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UniversitySystem {

    private static UniversitySystem instance = null;
    private List<Users> listOfUsers = new ArrayList<>();
    private List<Subject> listOfSubjects = new ArrayList<>();
    private List<Exam> listOfExams = new ArrayList<>();

    public UniversitySystem() {
    	LoadUsersFromFile();
        LoadSubjectsFromFile();
        LoadExamsFromFile();
    }

    public static UniversitySystem getInstance() {
        if (instance == null) {
            instance = new UniversitySystem();
        }
        return instance;
    }

    public List<Users> getListOfUsers() {
        return listOfUsers;
    }

    public List<Subject> getListOfSubjects() {
        return listOfSubjects;
    }

    public List<Exam> getListOfExams() {
        return listOfExams;
    }
    
    private void LoadUsersFromFile() {
        // This text file is located in the directory to facilitate application launch
        String currentDirectory = System.getProperty("user.dir");
        String filePathTeacher = Paths.get(currentDirectory, "listUsers.txt").toString();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathTeacher));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 5) {
                    continue;
                }

                String role = values[0];
                String firstName = values[1];
                String lastName = values[2];
                int id = Integer.parseInt(values[3]);
                String pswPortal = values[4];

                this.listOfUsers.add(new Users(role, firstName, lastName, id, pswPortal));
            }

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void LoadSubjectsFromFile() {
    	String currentDirectory = System.getProperty("user.dir");
    	String filePathSubj = Paths.get(currentDirectory, "ListOfSubj.txt").toString();
    	try {
    	    BufferedReader reader = new BufferedReader(new FileReader(filePathSubj));
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        String[] values = line.split(",");
    	        String nameSubjs = values[0];
    	        String codSubj = values[1];
    	        listOfSubjects.add(new Subject(nameSubjs, codSubj));
    	    }
    	    reader.close();
    	} catch (FileNotFoundException ex) {
    	    System.out.println("File not found: " + ex.getMessage());
    	} catch (IOException ex) {
    	    System.out.println("Error while reading the file: " + ex.getMessage());
    	}
    }

    private void LoadExamsFromFile() {
        String currentDirectory = System.getProperty("user.dir");
        String path = Paths.get(currentDirectory, "Exams.txt").toString();

        try {
            if (Files.exists(Paths.get(path))) {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length != 4) {
                        continue;
                    }

                    int id;

                    try {
                        id = Integer.parseInt(parts[0]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    String codSubj = parts[1];

                    int grade;

                    try {
                        grade = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    LocalDate date = LocalDate.parse(parts[3]);

                    this.listOfExams.add(new Exam(id, codSubj, grade, date));
                }
                reader.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error while reading the file: " + ex.getMessage());
        }
    }

    public void loadExamResultsFromDirectory(Users student) {
        String currentDirectory = System.getProperty("user.dir");
        String directoryPath = Paths.get(currentDirectory, "ExamResults").toString();
        String studentFilePath = Paths.get(directoryPath, student.getId() + "_examResults.txt").toString();

        if (Files.exists(Paths.get(studentFilePath))) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(studentFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length != 3) {
                        System.out.println("Invalid file format in " + studentFilePath + ".");
                        continue;
                    }

                    String subjectCode = values[0];
                    int examResult;
                    try {
                        examResult = Integer.parseInt(values[1]);
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid exam result format for subject " + subjectCode + " in " + studentFilePath + ".");
                        continue;
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    LocalDate date = LocalDate.parse(values[2]);

                    Exam examSubject = new Exam(subjectCode, examResult, date);
                    student.getExamSubjectList().add(examSubject);
                }
            } catch (IOException ex) {
                System.out.println("Error while reading the file " + studentFilePath + ": " + ex.getMessage());
            }
        } else {
            System.out.println("Exam results file for student " + student.getId() + " not found in directory " + directoryPath + ".");
        }
    }
    
    public void saveExamResultsToFile(Users student) {
        String currentDirectory = System.getProperty("user.dir");
        String directoryPath = currentDirectory + File.separator + "ExamResults";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String filePath = directoryPath + File.separator + student.getId() + "_examResults.txt";

        List<Exam> existingData = new ArrayList<>();
        if (new File(filePath).exists()) {
            try (Scanner scanner = new Scanner(new File(filePath))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length != 3 || !parts[1].matches("\\d+")) {
                        System.out.println("Invalid data in file: " + line);
                        continue;
                    }
                    LocalDate date = LocalDate.parse(parts[2]);
                    Exam result = new Exam(parts[0], Integer.parseInt(parts[1]), date);
                    existingData.add(result);
                }
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }

        for (Exam examSubject : student.getExamSubjectList()) {
            String subjectCode = examSubject.getCodSubj();
            if (existingData.stream().noneMatch(x -> x.getCodSubj().equals(subjectCode))) {
            	Exam result = new Exam(subjectCode, examSubject.getGrade(), examSubject.getDate());
                existingData.add(result);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Exam data : existingData) {
                writer.println(data.getCodSubj() + "," + data.getGrade() + "," + new SimpleDateFormat("yyyy-MM-dd").format(data.getDate()));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
