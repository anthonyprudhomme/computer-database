package org.excilys.computer_database.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.dao.ComputerDaoImpl;
import org.excilys.computer_database.model.Computer;

public class ComputerService {
	
private static ComputerService instance = null;
	
	private ComputerService(){}
	
	public static ComputerService getInstance(){
		if(instance == null){
			instance = new ComputerService();
		}
		return instance;
	}
	
	
	public ArrayList<Computer> getComputers() {
		return ComputerDaoImpl.getInstance().getComputers();
	}

	public Computer getComputerDetails(int id) {
		return ComputerDaoImpl.getInstance().getComputerDetails(id);
	}

	public void createComputer(Computer computer) {
		ComputerDaoImpl.getInstance().createComputer(computer);
	}

	public void updateComputer(Computer computer) {
		ComputerDaoImpl.getInstance().updateComputer(computer);
	}

	public void deleteComputer(int id) {
		ComputerDaoImpl.getInstance().deleteComputer(id);
	}
	
	public boolean checkIdInComputerTable(int id) throws SQLException {
		return ComputerDaoImpl.getInstance().checkIdInComputerTable(id);
	}

}
