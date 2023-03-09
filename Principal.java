package university;

public class Principal {

	static void main(String[] args) {
			
		UniversitySystem runSystem = new UniversitySystem();

		MenuLogin menu = new MenuLogin(runSystem);

		 menu.Start();
	}
}


