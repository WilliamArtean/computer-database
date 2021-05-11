package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Computer;

public class ComputerDAO {
	
	private Connection co;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	
	public Computer getByName(String name) throws SQLException {
		String getByNameQuery = "SELECT * FROM computer WHERE name = \"" + name + "\";";	//Use PreparedStatement instead?
		Statement st = this.co.createStatement();
		ResultSet res = st.executeQuery(getByNameQuery);
		
		if (!res.isBeforeFirst()) return null;
		
		res.next();
		Computer computer = new Computer(res.getString("name"));
		if (res.getString("introduced") != null) {
			computer.setIntroductionDate(res.getDate("introduced"));
		}
		if (res.getString("discontinued") != null) {
			computer.setDiscontinuationDate(res.getDate("discontinued"));
		}
		if (res.getString("company_id") != null) {
			long companyID = res.getLong("company_id");
			computer.setCompany(CompanyDAO.getInstance().getByID(companyID));
		}
		
		st.close();
		return computer;
	}
	
	public ArrayList<Computer> getAll() throws SQLException {
		String getAllQuery = "SELECT * FROM computer;";
		Statement st = this.co.createStatement();
		ResultSet results = st.executeQuery(getAllQuery);
		
		ArrayList<Computer> computers = new ArrayList<Computer>();
		
		while (results.next()) {
			computers.add(new Computer(results.getString("name")));
		}
		
		st.close();
		return computers;
	}
	
	public void create(Computer computer) throws SQLException {
		StringBuilder sb = new StringBuilder();
		StringBuilder columns = new StringBuilder("name");
		StringBuilder values = new StringBuilder("'").append(computer.getName()).append("'");
		
		if (computer.getIntroductionDate() != null) {
			columns.append(", introduced");
			values.append(", '").append(df.format(computer.getIntroductionDate())).append("'");
		}
		if (computer.getDiscontinuationDate() != null) {
			columns.append(", discontinued");
			values.append(", '").append(df.format(computer.getDiscontinuationDate())).append("'");
		}
		if (computer.getCompany() != null) {
			columns.append(", company_id");
			values.append(", ").append(computer.getCompany().getID());
		}
		sb.append("INSERT INTO computer (").append(columns).append(") VALUES (").append(values).append(");");
		
		String createQuery = sb.toString();
		System.out.println(createQuery);
		
		Statement st = this.co.createStatement();
		st.executeUpdate(createQuery);		
		st.close();
	}
	
	//missing Computer argument
	public void update(long id) {
		
	}
	public void update(String name) {
		
	}
	
	public void delete(long id) {
		
	}
	public void delete(String name) {
		
	}
	
}
