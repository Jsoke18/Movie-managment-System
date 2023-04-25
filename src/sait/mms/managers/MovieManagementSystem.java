package sait.mms.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import sait.mms.contracts.DatabaseDriver;
import sait.mms.drivers.MariaDBDriver;
import sait.mms.problemdomain.Movie;

public class MovieManagementSystem {
	public final int OPTION_ADD = 1;
	public final int OPTION_PRINT_YEAR = 2;
	public final int OPTION_PRINT_RANDOM = 3;
	public final int OPTION_DELETE = 4;
	public final int OPTION_EXIT = 5;
	public int id;
	public int duration;
	public String title;
	public int years;

	public void displayMenu() throws SQLException {
		Scanner in = new Scanner(System.in);
		int option = 0;

		while (option != OPTION_EXIT) {
			System.out.println("Jim's Movie Manager");
			System.out.printf("%-8d%s\n", 1, "Add New Movie");
			System.out.printf("%-8d%s\n", 2, "Print movies releashed in year");
			System.out.printf("%-8d%s\n", 3, "Print random list of movies");
			System.out.printf("%-8d%s\n", 4, "Delete a movie");
			System.out.printf("%-8d%s\n", 5, "Exit");
			System.out.println();
			System.out.print("Enter an Option: ");
			option = in.nextInt();
			in.nextLine();

			if (option == OPTION_ADD) {
				String title;
				int duration;
				int year;
				System.out.print("Enter movie title: ");
				title = in.nextLine();
				System.out.print("Enter duration: ");
				duration = in.nextInt();
				System.out.print("Enter year: ");
				year = in.nextInt();
				addMovie(title, duration, year);

			}

			else if (option == OPTION_PRINT_YEAR) {
				int year;
				System.out.print("Enter year: ");
				year = in.nextInt();
				printMoviesInYears(year);
			}
			else if (option == OPTION_PRINT_RANDOM) {
				int num;
				System.out.print("Enter number of movies: ");
				num = in.nextInt();
				printRandomMovies(num);
			}
			else if (option == OPTION_DELETE) {
				int id;
				System.out.print("Enter the movie ID that you want to delete: ");
				id = in.nextInt();
				deleteMovie(id);
			}

		}
		System.out.println("Goodbye!");

	}

	public void addMovie(String title, int duration, int year) throws SQLException {
		DatabaseDriver driver = new MariaDBDriver();
		driver.connect();

		ResultSet rs = driver.get(
				"INSERT INTO Movies (duration, title, year) VALUES(" + duration + ",\'" + title + "\'," + year + ")");
		driver.disconnect();
		System.out.println("Added movie to database");

	}

	public void printMoviesInYears(int year) throws SQLException {
		int totalMin = 0;
		ArrayList<Movie> movies = new ArrayList<Movie>();
		DatabaseDriver dbd = new MariaDBDriver();
		dbd.connect();

		ResultSet rs = dbd.get("SELECT * FROM Movies WHERE year =" + year);
		ResultSet count = dbd.get("SELECT COUNT(*) FROM Movies WHERE year = " + year);
		boolean hasRow = rs.next();
		count.next();

		for (int i = 0; i < count.getInt(1); i++) {
			if (hasRow) {
				duration = Integer.parseInt(rs.getString(2));
				title = (rs.getString(3));
				years = Integer.parseInt(rs.getString(4).substring(0, 4));
				totalMin += duration;

				movies.add(new Movie(duration, title, years));

			} else {
				System.out.print("No Rows were found!");
			}

			rs.next();
		}
		dbd.disconnect();
		System.out.println("Movie List");
		System.out.printf("%-16s %-8s %s\n", "Duration", "Year", "Title");

		for (Movie m : movies) {
			duration = m.getDuration();
			title = m.getTitle();
			years = m.getYear();

			System.out.printf("%-16d %-8d %s\n", duration, years, title);
			
		}
		System.out.println("\nTotal durations: " + totalMin + " minutes");
	}

	public void printRandomMovies(int num) throws SQLException {
		int totalMin = 0;
		ArrayList<Movie> movies = new ArrayList<Movie>();
		DatabaseDriver driver = new MariaDBDriver();
		driver.connect();
		ResultSet rs = driver.get("SELECT * FROM movies Order By Rand() ");
		boolean hasRow = rs.next();
		for (int i = 0; i < num; i++) {
			if (hasRow) {
				duration = Integer.parseInt(rs.getString(2));
				title = (rs.getString(3));
				years = Integer.parseInt(rs.getString(4).substring(0, 4));
				totalMin += duration;
				movies.add(new Movie(duration, title, years));

			} else {
				System.out.print("Oops! No Rows were found :(");
			}

			rs.next();

		}
		driver.disconnect();
		System.out.println("Movie List");
		System.out.printf("%-16s %-8s %s\n", "Duration", "Year", "Title");
		for (Movie mo : movies) {
			duration = mo.getDuration();
			title = mo.getTitle();
			years = mo.getYear();

			System.out.printf("%-16d %-8d %s\n", duration, years, title);
		}
		System.out.println("\nTotal durations: " + totalMin + " minutes");
	}

	public void deleteMovie(int id) throws SQLException {

		DatabaseDriver driver = new MariaDBDriver();
		driver.connect();

		ResultSet rs = driver.get("DELETE FROM movies WHERE id = " + id);
		driver.disconnect();
		
		System.out.println("Movie " + id +" is deleted");

	}
}
