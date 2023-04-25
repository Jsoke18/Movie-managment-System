package sait.mms.application;

import java.sql.SQLException;

import sait.mms.managers.MovieManagementSystem;

public class appDriver {
	public static void main(String[] args) throws SQLException {
		MovieManagementSystem manager = new MovieManagementSystem();
		manager.displayMenu();
	}
}
