package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Computer;

public class ComputerDAO {
	
	private Connection co;
	private static ComputerDAO instance = new ComputerDAO();
	
	public static ComputerDAO getInstance() {
		return instance;
	}
	
	public void setConnection(Connection co) {
		this.co = co;
	}
	
	
	public Computer getByID(long id) {
		return null;
	}
	
	public Computer getByName(String name) {
		return null;
	}
	
	public ArrayList<Computer> getAll() throws SQLException {
		String getAllQuery = "SELECT * FROM computer";
		Statement st = this.co.createStatement();
		ResultSet results = st.executeQuery(getAllQuery);
		
		ArrayList<Computer> computers = new ArrayList<Computer>();
		
		while (results.next()) {
			computers.add(new Computer(results.getString("name")));
		}
		
		return computers;
	}
	
	public void create(Computer computer) {
		
	}
	
	public void update(long id) {
		
	}
	public void update(String name) {
		
	}
	
	public void delete(long id) {
		
	}
	public void delete(String name) {
		
	}
	
}
