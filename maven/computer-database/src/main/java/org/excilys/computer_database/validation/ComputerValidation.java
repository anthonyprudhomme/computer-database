package org.excilys.computer_database.validation;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;

public class ComputerValidation {
	
	private static ComputerValidation instance = null;

	private ComputerValidation(){}

	public static ComputerValidation getInstance(){
		if(instance == null){
			instance = new ComputerValidation();
		}
		return instance;
	}
	
	public boolean validateCompanyId(int id){
		return CompanyService.getInstance().getCompany(id) != null;
	}
	
	public boolean validateDate(Computer computer){
		if(computer.getIntroduced()== null){
			return true;
		}else{
			if(computer.getDiscontinued()== null){
				return true;
			}else{
				return computer.getIntroduced().before(computer.getDiscontinued());
			}
		}
	}

}
