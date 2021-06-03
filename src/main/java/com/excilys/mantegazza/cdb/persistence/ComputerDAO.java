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

import com.excilys.mantegazza.cdb.enums.OrderBy;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.mappers.ComputerMapper;

public class ComputerDAO {
	
	private Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private ComputerMapper mapper = new ComputerMapper();
	
	private final String queryGetByID = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id=?";
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name=?";
	private final String queryGetAll = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id";
	private final String queryGetSelection = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id ORDER BY computer.id LIMIT ? OFFSET ?";
	private final String queryGetSelectionOrdered = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id ORDER BY %s %s LIMIT ? OFFSET ?";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id=?";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name=?";
	private final String queryCreate = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private final String queryUpdate = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE name=?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM computer";
	private final String queryOrderedLimitedSearchByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY %s %s LIMIT ? OFFSET ?";
	private final String querySearchByName = "SELECT COUNT(computer.id) AS rowcount FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.id";
	
	
	/**
	 * Execute a SQL query to fetch the computer with the required id.
	 * @param id The id of the computer to return
	 * @return An Optional containing a Computer object matching the id, or an empty Optional if the computer could not be found
	 */
	public Optional<Computer> getByID(long id) {
		Optional<Computer> computer = Optional.empty();
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetByID);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			computer = mapper.mapToComputer(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		if (computer.isPresent()) {
			logger.debug("Retrieved computer with id " + id + " from database.");			
		} else {
			logger.debug("Could not retrieve computer with id " + id + " from database.");
		}
		return computer;
	}
	
	/**
	 * Execute a SQL query to fetch the computer with the required name.
	 * @param name The name of the computer to return
	 * @return An Optional containing a Computer object matching the name, or an empty Optional if the computer could not be found
	 */
	public Optional<Computer> getByName(String name) {
		Optional<Computer> computer = Optional.empty();
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetByName);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			computer = mapper.mapToComputer(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		if (computer.isPresent()) {
			logger.debug("Retrieved computer with name " + name + " from database.");			
		} else {
			logger.debug("Could not retrieve computer with name " + name + " from database.");
		}
		return computer;
	}
	
	/**
	 * Execute a SQL query to fetch all the computers present in the company database.
	 * @return An ArrayList of all the Computer objects in the database
	 */
	public ArrayList<Computer> getAll() {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetAll);
			ResultSet rs = ps.executeQuery();
			computers = mapper.mapToComputerArray(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}		
		logger.debug("Retrieved all computers from database.");
		return computers;
	}
	
	/**
	 * Execute a SQL query to fetch a selected number of computers from the database.
	 * The computers are ordered by their id.
	 * @param numberToReturn The number of computers to fetch from the database
	 * @param offset The index of row from which to start the selection. 0 to start from the first row (included).
	 * @return An ArrayList of Computer objects in the database, of size numberToReturn at most (less if the end of the database has been reached)
	 */
	public ArrayList<Computer> getSelection(int numberToReturn, int offset) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetSelection);
			ps.setInt(1, numberToReturn);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			computers = mapper.mapToComputerArray(rs);
			logger.debug("Retrieved computer selection of maximum size " + numberToReturn + " from database.");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}
	
	public ArrayList<Computer> getSelection(int numberToReturn, int offset, OrderBy orderColumn, boolean ascending) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		
		String orderType = ascending ? "ASC" : "DESC";
		String query = String.format(queryGetSelectionOrdered, mapper.orderColumnToSQL(orderColumn), orderType);
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(query);
			
			ps.setInt(1, numberToReturn);
			ps.setInt(2, offset);
			
			ResultSet rs = ps.executeQuery();
			computers = mapper.mapToComputerArray(rs);
			logger.debug("Retrieved computer selection of maximum size " + numberToReturn + " from database.");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}
	
	/**
	 * Execute a SQL query to insert a Computer object data into the database.
	 * @param computer The Computer object to insert into the database
	 */
	public void create(Computer computer) {
		try (
				Connection co = DataSource.getConnection();
			) {
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
			logger.debug("Computer added to database: " + computer.toString());
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
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryUpdate);
			if (updatedComputer.getName() != null) {
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
			logger.debug("Computer updated in database: " + updatedComputer.toString());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Execute a SQL query to delete a computer from the database.
	 * @param id The id of the computer to delete
	 */
	public void delete(long id) {
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryDeleteByID);
			ps.setLong(1, id);
			ps.executeUpdate();
			logger.debug("Deleted computer in database with id: " + id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	/**
	 * Execute a SQL query to delete a computer from the database.
	 * @param name The name of the computer to delete
	 */
	public void delete(String name) {
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryDeleteByName);
			ps.setString(1, name);
			ps.executeUpdate();
			logger.debug("Deleted computer in database with name: " + name);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void delete(ArrayList<Long> idsToDelete) {
		try (
				Connection co = DataSource.getConnection();
			) {
			co.setAutoCommit(false);
			PreparedStatement ps = co.prepareStatement(queryDeleteByID);
			
			StringBuilder ids = new StringBuilder();
			for (long id : idsToDelete) {
				ps.setLong(1, id);
				ps.addBatch();
				ids.append(id).append(", ");
			}
			
			ps.executeBatch();
			co.commit();
			
			logger.debug("Deleted computers in database with id: " + ids.toString());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Execute a SQL query to fetch the number of Computer objects in the database.
	 * @return An int corresponding to the number of rows in the computer database
	 */
	public int getCount() {
		int count = 0;
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetCount);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt("rowcount");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
	public ArrayList<Computer> searchByName(String name, int limit, int offset) {
		return searchByNameOrdered(name, limit, offset, OrderBy.none, true);
	}
	
	/**
	 * Search for a list of computer, specifying the name to search for, the search boundaries,
	 * as well as how to order the result.
	 * @param name The name of the computer or company to search for
	 * @param limit The number of Computers the search will be returning
	 * @param offset The row starting from which the Computers will be returned
	 * @param orderColumn An enum matching the columns used to order the result
	 * @param ascending If true, results will be ordered by ascending order, and if false, by descending order
	 * @return ArrayList of computers, filtered and ordered according to the search parameters
	 */
	public ArrayList<Computer> searchByNameOrdered(String name, int limit, int offset, OrderBy orderColumn, boolean ascending) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		
		String orderType = ascending ? "ASC" : "DESC";
		String query = String.format(queryOrderedLimitedSearchByName, mapper.orderColumnToSQL(orderColumn), orderType);
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(query);
			ps.setString(1, "%" + name + "%");
			ps.setString(2, "%" + name + "%");
			ps.setInt(3, limit);
			ps.setInt(4, offset);
			
			logger.info("Executed query : {}", ps.toString());
			ResultSet rs = ps.executeQuery();
			
			computers = mapper.mapToComputerArray(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		logger.debug("Retrieved {} computers from database.", computers.size());
		
		return computers;
	}
	
	public int getSearchCount(String name) {
		int count = 0;
		try (
				Connection co = DataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(querySearchByName);
			ps.setString(1, "%" + name + "%");
			ps.setString(2, "%" + name + "%");
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt("rowcount");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
}
