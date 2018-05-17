package org.excilys.computer_database.service;

import java.util.ArrayList;

import org.excilys.computer_database.dao.ComputerDao;
import org.excilys.computer_database.dao.ComputerDaoImpl;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.validation.ComputerValidation;
import org.excilys.computer_database.validation.ComputerValidationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public class ComputerService {

	private static ComputerService instance = null;
	private ComputerDao computerDao = ComputerDaoImpl.getInstance();
	private ComputerService(){}

	final private Logger logger = LoggerFactory.getLogger(ComputerService.class);

	public static ComputerService getInstance(){
		if(instance == null){
			instance = new ComputerService();
		}
		return instance;
	}

	public ArrayList<Computer> getComputers() {
		return computerDao.getComputers();
	}

	public Computer getComputerDetails(int id) {
		return computerDao.getComputerDetails(id);
	}

	public Pair<ComputerValidationStatus,String> createComputer(Computer computer) {
		ComputerValidationStatus status = null;
		String message = "";
		if(ComputerValidation.getInstance().validateDate(computer)){
			if(ComputerValidation.getInstance().validateCompanyId(computer.getCompany().getId())){
				computerDao.createComputer(computer);
				status = ComputerValidationStatus.OK;
				message ="Computer successfully created.";
				logger.info("Computer created: "+ computer.toString());
			}else{
				status = ComputerValidationStatus.COMPANY_ID_ERROR;
				message ="The computer couldn't be created, the id of the company doesn't belong to any company in the database.";
				logger.error("Company id error when trying to update computer: "+ computer.toString());
			}
		}else{
			status = ComputerValidationStatus.DATE_ERROR;
			message = "The computer couldn't be created, the discontinued date must be later than the introduced date.";
			logger.error("Date error when trying to create computer: "+ computer.toString());
		}
		return new Pair<>(status, message);
	}

	public Pair<ComputerValidationStatus,String> updateComputer(Computer computer) {
		ComputerValidationStatus status = null;
		String message = "";
		if(ComputerValidation.getInstance().validateDate(computer)){
			if(ComputerValidation.getInstance().validateCompanyId(computer.getCompany().getId())){
				computerDao.updateComputer(computer);
				status = ComputerValidationStatus.OK;
				message ="Computer successfully updated.";
				logger.info("Computer updated: "+ computer.toString());
			}else{
				status = ComputerValidationStatus.COMPANY_ID_ERROR;
				message ="The computer couldn't be updated, the id of the company doesn't belong to any company in the database.";
				logger.error("Company id error when trying to update computer: "+ computer.toString());
			}
		}else{
			status = ComputerValidationStatus.DATE_ERROR;
			message = "The computer couldn't be updated, the discontinued date must be later than the introduced date.";
			logger.error("Date error when trying to update computer: "+ computer.toString());
		}
		return new Pair<>(status, message);
	}

	public void deleteComputer(int id) {
		computerDao.deleteComputer(id);
	}

}
