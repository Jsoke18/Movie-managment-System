package sait.mms.drivers;

import java.sql.*;

import sait.mms.contracts.DatabaseDriver;

public class MariaDBDriver implements DatabaseDriver {
	private final String URL = "jdbc:mariadb://localhost:3306/cprg251?user=cprg251&password=password";
	Connection connect;

	@Override
	public void connect() throws SQLException {
		connect = DriverManager.getConnection(URL);
	}

	@Override
	public ResultSet get(String query) throws SQLException {
		Statement stmt = connect.createStatement();
		ResultSet result = stmt.executeQuery(query);
		return result;
	}

	@Override
	public int update(String query) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = connect.createStatement();
		int count = stmt.executeUpdate(query);
		return count;
	}

	@Override
	public void disconnect() throws SQLException {
		connect.close();
	}
}
