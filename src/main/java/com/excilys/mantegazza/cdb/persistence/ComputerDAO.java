package com.excilys.mantegazza.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.model.Computer;

public class ComputerDAO {
	
	private Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	private DBConnectionManager dbManager;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");// HH:mm:ss");
	private ComputerMapper mapper = new ComputerMapper();
	
	private final String queryGetByID = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id=?";
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name=?";
	private final String queryGetAll = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id";
	private final String queryGetSelection = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id ORDER BY computer.id LIMIT ? OFFSET ?";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id=?";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name=?";
	private final String queryCreate = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private final String queryUpdate = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE name=?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM computer";
	
	public void setDatabaseManager(DBConnectionManager databaseManager) {
		this.dbManager = databaseManager;
	}
	
	public Optional<Computer> getByID(long id) {
		Optional<Computer> computer = Optional.empty();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetByID);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			computer = mapper.mapToComputer(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return computer;
	}
	
	public Optional<Computer> getByName(String name) {
		Optional<Computer> computer = Optional.empty();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetByName);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			computer = mapper.mapToComputer(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return computer;
	}
	
	public ArrayList<Computer> getAll() {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetAll);
			ResultSet rs = ps.executeQuery();
			
			computers = mapper.mapToComputerArray(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		return computers;
	}
	
	public ArrayList<Computer> getSelection(int numberToReturn, int offset) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetSelection);
			ps.setInt(1, numberToReturn);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			computers = mapper.mapToComputerArray(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}
	
	public void create(Computer computer) {
		try {
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
			logger.info("Computer added to database: " + computer.toString());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Updates all the field in the chosen computer with the one from the updated computer.
	 * If the name of the updated computer is null, the previous value will remain
	 * @param computerName the name of the computer to update
	 * @param updatedComputer the updated computer containing the new fields
	 * @throws SQLException
	 */
	public void update(String computerName, Computer updatedComputer) {
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryUpdate);
			if(updatedComputer.getName() != null) {
				ps.setString(1, updatedComputer.getName());			
			} else {
				ps.setNull(1, Types.VARCHAR);
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
			logger.info("Computer updated in database: " + updatedComputer.toString());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void delete(long id) {
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryDeleteByID);
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
			co.close();
			logger.info("Deleted computer in database with id: " + id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	public void delete(String name) {
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryDeleteByName);
			ps.setString(1, name);
			ps.executeUpdate();
			ps.close();
			co.close();
			logger.info("Deleted computer in database with name: " + name);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	public int getCount() {
		int count = 0;
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetCount);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt("rowcount");
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
}
