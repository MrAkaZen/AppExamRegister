package university;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class MenuLogin {

	private Map<String, Runnable> _menu;

	public MenuLogin(UniversitySystem boot) {
		
	    _menu = new HashMap<String, Runnable>();
	    _menu.put("Student", () -> LoginStudent(boot));
	    _menu.put("Teacher", () -> loginTeacher(boot));
	    _menu.put("Exit", () -> Exit());
	}

	public void Start() {
	    Scanner scanner = new Scanner(System.in);
	    while (true) {
	        System.out.println("#####################\nWelcome to your University's application\nFMF edition:\n#####################\n");
	        int i = 1;
	        for (String key : _menu.keySet()) {
	            System.out.println(i++ + ". " + key);
	        }
	        System.out.println();
	        String choice = scanner.nextLine();
	        try {
	            int number = Integer.parseInt(choice);
	            if (number >= 1 && number <= _menu.size()) {
	                String selected = (String) _menu.keySet().toArray()[number-1];
	                _menu.get(selected).run();
	                break;
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid choice.");
	        }
	    }
	    scanner.close();
	}

	private void LoginStudent(UniversitySystem boot) {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("Welcome student!\nEnter your credentials:\n");
	    System.out.print("ID:\t\t");
	    int idStudent = Integer.parseInt(scanner.nextLine());
	    System.out.print("Password:\t");
	    String password = scanner.nextLine();
	    Users loggedStudent = boot.getListOfUsers().stream()
	            .filter(s -> s.getId() == idStudent && s.getPswPortal().equals(password))
	            .findFirst()
	            .orElse(null);
	    if (loggedStudent != null) {
	        Map<String, Runnable> menuStudent = new HashMap<String, Runnable>();
	        menuStudent.put("My career", () -> loggedStudent.getCareer(boot, loggedStudent));
	        menuStudent.put("Show my average grade", () -> loggedStudent.getAverageGrade(boot, loggedStudent));
	        menuStudent.put("Logout", () -> Start());
	        System.out.println("\nWelcome " + loggedStudent.getFirstName() + " " + loggedStudent.getLastName() + "\n");
	        while (true) {
	            System.out.println("Select an operation:\n");
	            String[] menuItems = new String[] { "My career", "Show my average grade", "Logout" };
	            for (int i = 0; i < menuItems.length; i++) {
	                System.out.println((i + 1) + ". " + menuItems[i]);
	            }
	            try {
	                int selection = Integer.parseInt(scanner.nextLine());
	                if (selection >= 1 && selection <= menuItems.length) {
	                    String selectedMenuItem = menuItems[selection - 1];
	                    menuStudent.get(selectedMenuItem).run();
	                    if (selectedMenuItem.equals("Logout")) {
	                        Start();
	                    }
	                } else {
	                    System.out.println("Invalid selection. Please try again.");
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid selection. Please try again.");
	            }
	        }
	    } else {
	        System.out.println("Incorrect Id or Password. Please try again.");
	    }
	    scanner.close();
	}

	private void loginTeacher(UniversitySystem boot) {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("Welcome teacher!\nEnter your credentials:\n");
	    System.out.print("ID:\t\t");
	    int idTeacher = scanner.nextInt();
	    scanner.nextLine();
	    System.out.print("Password:\t");
	    String password = scanner.nextLine();
	    Users loggedTeacher = boot.getListOfUsers().stream()
	            .filter(s -> s.getId() == idTeacher && s.getPswPortal().equals(password))
	            .findFirst()
	            .orElse(null);

	    if (loggedTeacher != null) {
	        //####################################################################################################
	        Map<String, Runnable> menuTeacher = new HashMap<>();
	        menuTeacher.put("Add Exam", () -> loggedTeacher.addExam(boot));
	        menuTeacher.put("Show students by Exam Subject", () -> loggedTeacher.ShowStudentsByExamSubject(boot));
	        menuTeacher.put("Students with 25 Grade Upper", () -> loggedTeacher.StudentsWith25GradeUpper(boot));
	        menuTeacher.put("Show Student Career", () -> loggedTeacher.showStudentCareer(boot));
	        menuTeacher.put("Logout", () -> Start());
	        //####################################################################################################

	        System.out.printf("\nWelcome %s %s\n\n", loggedTeacher.getFirstName(), loggedTeacher.getLastName());

	        while (true) {
	            System.out.println("Select an operation:\n");

	            String[] menuItems = new String[] { "Add Exam", "Show students by Exam Subject", "Students with 25 Grade Upper", "Show Student Career", "Logout" };

	            for (int i = 0; i < menuItems.length; i++) {
	                System.out.printf("%d. %s\n", i + 1, menuItems[i]);
	            }

	            int selection;
	            try {
	                selection = scanner.nextInt();
	                scanner.nextLine();
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid selection. Please try again.");
	                scanner.nextLine();
	                continue;
	            }

	            if (selection >= 1 && selection <= menuItems.length) {
	                String selectedMenuItem = menuItems[selection - 1];
	                menuTeacher.get(selectedMenuItem).run();
	                if (selectedMenuItem.equals("Logout")) {
	                	Start();
	                }
	            } else {
	                System.out.println("Invalid selection. Please try again.");
	            }
	        }
	    } else {
	        System.out.println("Incorrect Id or Password. Please try again.");
	    }
	}

	private void Exit()
    {
		System.out.println("\nGoodbye!");
		System.exit(0);
    }
}
