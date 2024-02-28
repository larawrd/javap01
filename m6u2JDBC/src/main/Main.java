package main;

import java.sql.SQLException;

import controller.Controller;

public class Main {

	public static void main(String[] args) throws SQLException {
		Controller controller = Controller.getInstance();
		controller.init();
	}
}
