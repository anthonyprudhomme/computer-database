package org.excilys.computer_database.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.dao.ComputerDaoImpl;
import org.excilys.computer_database.model.Computer;

public class ComputerService {
	
	ComputerDaoImpl computerDaoImpl = new ComputerDaoImpl();
	
	public ArrayList<Computer> getComputers() {
		return computerDaoImpl.getComputers();
	}

	public Computer getComputerDetails(int id) {
		return computerDaoImpl.getComputerDetails(id);
	}

	public void createComputer(Computer computer) {
		computerDaoImpl.createComputer(computer);
	}

	public void updateComputer(Computer computer) {
		computerDaoImpl.updateComputer(computer);
	}

	public void deleteComputer(int id) {
		computerDaoImpl.deleteComputer(id);
	}
	
	public boolean checkIdInComputerTable(int id) throws SQLException {
		return computerDaoImpl.checkIdInComputerTable(id);
	}

}
