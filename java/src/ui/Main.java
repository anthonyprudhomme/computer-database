package ui;

import java.sql.Date;
import model.Computer;
import persistence.JdbcRequest;

public class Main {
	
	public static void main(String[] args){
		JdbcRequest request = new JdbcRequest();
		request.getComputerList();
		//request.getCompanyList();
		//request.getComputerDetails(574);
		Date introduced = new Date(System.currentTimeMillis());
		Date discontinued = new Date(System.currentTimeMillis()+1000*60*60*24);
		int companyId = 4;
		//request.createComputer("MacBookPro", introduced, discontinued, companyId);
		Computer computer = new Computer(-1,"AlienWare", null, discontinued, companyId);
		//request.createComputer(computer);
		Computer computerUpdated = new Computer(579,"AlienWareUpdated", null, discontinued, 12);
		//request.updateComputer(computerUpdated);
		//request.deleteComputer(579);
		//request.getComputerDetails(579);
		
	}

}