package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Computer;

public interface ComputerDao {
	public ArrayList<Computer> getComputers();
	public Computer getComputerDetails(int id);
	public void createComputer(Computer computer);
	public void updateComputer(Computer computer);
	public void deleteComputer(int id);
}
