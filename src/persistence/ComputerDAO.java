package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Computer;
import java.sql.Types;

public class ComputerDAO {
	
	private Connection co;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private CompanyDAO companyDAO;
	
	private final String queryGetByID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id=?";
	private final String queryGetByName = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name=?";
	private final String queryGetAll = "SELECT id, name, introduced, discontinued, company_id FROM computer";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id=?";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name=?";
	private final String queryCreate = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private final String queryUpdate = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE name=?";
	
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
	
	public void create(Computer computer) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryCreate);
		ps.setString(1, computer.getName());
		if (computer.getIntroductionDate() != null) {
			ps.setString(2, df.format(computer.getIntroductionDate()));
		} else {
			ps.setNull(2, Types.TIMESTAMP);
		}
		if (computer.getDiscontinuationDate() != null) {
			ps.setString(3, df.format(computer.getDiscontinuationDate()));
		} else {
			ps.setNull(3, Types.TIMESTAMP);
		}
		if (computer.getCompany() != null) {
			ps.setLong(4, computer.getCompany().getID());
		} else {
			ps.setNull(4, Types.BIGINT);
		}
		
		ps.executeUpdate();
		ps.close();
	}
	
	public void update(long id, Computer updatedComputer) throws SQLException {
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
		
		String createQuery = sb.toString();
		System.out.println(createQuery);
		st.executeUpdate(createQuery);
		st.close();
	}
	
	/**
	 * Updates all the field in the chosen computer with the one from the updated computer.
	 * If the name of the updated computer is null, the previous value will remain
	 * @param computerName the name of the computer to update
	 * @param updatedComputer the updated computer containing the new fields
	 * @throws SQLException
	 */
	public void update(String computerName, Computer updatedComputer) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryUpdate);
		if(updatedComputer.getName() != null) {
			ps.setString(1, updatedComputer.getName());			
		} else {
			ps.setString(1, computerName);
		}
		if (updatedComputer.getIntroductionDate() != null) {
			ps.setString(2, df.format(updatedComputer.getIntroductionDate()));
		} else {
			ps.setNull(2, Types.TIMESTAMP);
		}
		if (updatedComputer.getDiscontinuationDate() != null) {
			ps.setString(3, df.format(updatedComputer.getDiscontinuationDate()));
		} else {
			ps.setNull(3, Types.TIMESTAMP);
		}
		if (updatedComputer.getCompany() != null) {
			ps.setLong(4, updatedComputer.getCompany().getID());
		} else {
			ps.setNull(4, Types.BIGINT);
		}
		ps.setString(5, computerName);
		
		ps.executeUpdate();
		ps.close();
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
