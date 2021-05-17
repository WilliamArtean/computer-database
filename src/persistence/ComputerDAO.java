package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exceptions.InconsistentDatesException;
import model.Computer;

public class ComputerDAO {
	
	private Connection co;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private CompanyDAO companyDAO;
	
	private final String queryGetByID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id=?";
	private final String queryGetByName = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name=?";
	private final String queryGetAll = "SELECT id, name, introduced, discontinued, company_id FROM computer";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id=?";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name=?";
	
	public void setConnection(Connection co) {
		this.co = co;
	}
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	
	public Computer getByID(long id) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetByID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Computer computer = new Computer(rs.getString("name"));
		computer.setID(rs.getLong("id"));
		if (rs.getString("introduced") != null) {
			computer.setIntroductionDate(rs.getDate("introduced"));
		}
		if (rs.getString("discontinued") != null) {
			computer.setDiscontinuationDate(rs.getDate("discontinued"));
		}
		if (rs.getString("company_id") != null) {
			long companyID = rs.getLong("company_id");
			computer.setCompany(companyDAO.getByID(companyID));
		}
		
		rs.close();
		ps.close();
		return computer;
	}
	
	public Computer getByName(String name) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetByName);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Computer computer = new Computer(rs.getString("name"));
		computer.setID(rs.getLong("id"));
		if (rs.getString("introduced") != null) {
			computer.setIntroductionDate(rs.getDate("introduced"));
		}
		if (rs.getString("discontinued") != null) {
			computer.setDiscontinuationDate(rs.getDate("discontinued"));
		}
		if (rs.getString("company_id") != null) {
			long companyID = rs.getLong("company_id");
			computer.setCompany(companyDAO.getByID(companyID));
		}
		
		rs.close();
		ps.close();
		return computer;
	}
	
	public ArrayList<Computer> getAll() throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Computer> computers = new ArrayList<Computer>();
		
		while (rs.next()) {			
			Computer computer = new Computer(rs.getString("name"));
			computer.setID(rs.getLong("id"));
			if (rs.getString("introduced") != null) {
				computer.setIntroductionDate(rs.getDate("introduced"));
			}
			if (rs.getString("discontinued") != null) {
				computer.setDiscontinuationDate(rs.getDate("discontinued"));
			}
			if (rs.getString("company_id") != null) {
				long companyID = rs.getLong("company_id");
				computer.setCompany(companyDAO.getByID(companyID));
			}
			computers.add(computer);
		}
		
		rs.close();
		ps.close();
		return computers;
	}
	
	public void create(Computer computer) throws SQLException, InconsistentDatesException {
		if (computer.getIntroductionDate() != null
				&& computer.getDiscontinuationDate() != null
				&& computer.getIntroductionDate().after(computer.getDiscontinuationDate()))
			throw new InconsistentDatesException();
		
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
	
	public void update(long id, Computer updatedComputer) throws SQLException, InconsistentDatesException {
		StringBuilder sb = new StringBuilder("UPDATE computer SET ");
		boolean hasPreviousArgument = false;
		
		if (updatedComputer.getName() != null) {
			hasPreviousArgument = true;
			sb.append("name = '").append(updatedComputer.getName()).append("'");
		}
		if (updatedComputer.getIntroductionDate() != null) {
			if (hasPreviousArgument)
				sb.append(", ");
			hasPreviousArgument = true;
			sb.append("introduced = '").append(df.format(updatedComputer.getIntroductionDate())).append("'");
		}
		if (updatedComputer.getDiscontinuationDate() != null) {
			if (hasPreviousArgument)
				sb.append(", ");
			hasPreviousArgument = true;
			sb.append("discontinued = '").append(df.format(updatedComputer.getDiscontinuationDate())).append("'");
		}
		if (updatedComputer.getCompany() != null) {
			if (hasPreviousArgument)
				sb.append(", ");
			hasPreviousArgument = true;
			sb.append("company_id = '").append(updatedComputer.getCompany().getID()).append("'");
		}
		
		sb.append(" WHERE id = ").append(id).append(";");
		
		Statement st = this.co.createStatement();
		
		String datesQuery = "SELECT introduced, discontinued FROM computer WHERE id=" + id;
		ResultSet rs = st.executeQuery(datesQuery);
		rs.next();
		Date introDate = (updatedComputer.getIntroductionDate() != null) ?
				updatedComputer.getIntroductionDate()
				: rs.getDate("introduced");
		Date disconDate = (updatedComputer.getDiscontinuationDate() != null) ?
				updatedComputer.getDiscontinuationDate()
				: rs.getDate("discontinued");
		if (introDate != null && disconDate != null && introDate.after(disconDate))
			throw new InconsistentDatesException();
		
		String createQuery = sb.toString();
		System.out.println(createQuery);
		st.executeUpdate(createQuery);
		st.close();
	}
	public void update(String computerName, Computer updatedComputer) throws SQLException, InconsistentDatesException {
		String query = "SELECT id FROM computer WHERE name='" + computerName + "';";
		
		Statement st = this.co.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		long id = rs.getLong("id");
		st.close();
		
		update(id, updatedComputer);
	}
	
	public void delete(long id) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryDeleteByID);
		ps.setLong(1, id);
		ps.executeUpdate();
		ps.close();
	}
	public void delete(String name) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryDeleteByName);
		ps.setString(1, name);
		ps.executeUpdate();
		ps.close();
	}
	
}
