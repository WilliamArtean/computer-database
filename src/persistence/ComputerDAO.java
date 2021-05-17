package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import model.Computer;
import java.sql.Types;

public class ComputerDAO {
	
	private DBConnectionManager dbManager;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ComputerMapper mapper = new ComputerMapper();
	
	private final String queryGetByID = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id=?";
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name=?";
	private final String queryGetAll = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id=?";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name=?";
	private final String queryCreate = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private final String queryUpdate = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE name=?";
	
	public void setDatabaseManager(DBConnectionManager databaseManager) {
		this.dbManager = databaseManager;
	}
	
	public Optional<Computer> getByID(long id) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetByID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		Optional<Computer> computer = mapper.mapToComputer(rs);
		
		rs.close();
		ps.close();
		co.close();
		return computer;
	}
	
	public Optional<Computer> getByName(String name) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetByName);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		
		Optional<Computer> computer = mapper.mapToComputer(rs);
		
		rs.close();
		ps.close();
		co.close();
		return computer;
	}
	
	public ArrayList<Computer> getAll() throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Computer> computers = mapper.mapToComputerArray(rs);
		
		rs.close();
		ps.close();
		co.close();
		return computers;
	}
	
	public void create(Computer computer) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryCreate);
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
		co.close();
	}
	
	/**
	 * Updates all the field in the chosen computer with the one from the updated computer.
	 * If the name of the updated computer is null, the previous value will remain
	 * @param computerName the name of the computer to update
	 * @param updatedComputer the updated computer containing the new fields
	 * @throws SQLException
	 */
	public void update(String computerName, Computer updatedComputer) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryUpdate);
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
		co.close();
	}
	
	public void delete(long id) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryDeleteByID);
		ps.setLong(1, id);
		ps.executeUpdate();
		ps.close();
		co.close();
	}
	public void delete(String name) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryDeleteByName);
		ps.setString(1, name);
		ps.executeUpdate();
		ps.close();
		co.close();
	}
	
}
