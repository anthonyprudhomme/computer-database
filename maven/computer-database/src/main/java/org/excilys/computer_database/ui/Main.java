package org.excilys.computer_database.ui;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.model.Page;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;

public class Main {
	
	private static CompanyService companyService = new CompanyService();
	private static ComputerService computerService = new ComputerService();

	public static void main(String[] args){
		
		Scanner scan = new Scanner(System.in);
		int optionInput = 0;
		while(optionInput != 7){

			System.out.println("Welcome to the CLI of Computer Database!");
			System.out.println("Pick one of the following:");
			System.out.println("1 - List all COMPUTERS in the database.");
			System.out.println("2 - List all COMPANIES in the database.");
			System.out.println("3 - View detailed informations about a computer.");
			System.out.println("4 - CREATE a computer in the database.");
			System.out.println("5 - UPDATE informations about a computer.");
			System.out.println("6 - DELETE a computer of the database.");
			System.out.println("7 - I am done, thank you.");

			optionInput = askForInt("Your choice: ", scan, false);

			switch(optionInput){
			case 1:
				System.out.println("Computer list");
				ArrayList<Computer> computers = computerService.getComputers();
				ArrayList<String> linesOfComputers = new ArrayList<String>();
				for(Computer computer: computers){
					linesOfComputers.add(computer.getShortToString());
				}
				new Page(linesOfComputers, scan);
				break;

			case 2:
				System.out.println("Company list");
				ArrayList<Company> companies = companyService.getCompanies();
				ArrayList<String> linesOfCompanies = new ArrayList<String>();
				for(Company company: companies){
					linesOfCompanies.add(company.toString());
				}
				new Page(linesOfCompanies, scan);
				break;

			case 3:
				System.out.println("Details about a computer");
				int idInput = getAndCheckComputerId("Please enter the id of the computer you want details of:", scan);
				Computer computerDetails = computerService.getComputerDetails(idInput);
				System.out.println(computerDetails.toString());
				break;

			case 4:
				System.out.println("Computer creation");
				System.out.println("Please enter the name of the computer you want to create:");
				String computerNameInput = scan.nextLine();
				int companyId = askForInt("Please enter the id of the company that made the computer you want to add to the database. Type \"skip\" to skip.",scan, true);
				Date introduced = askForDate("Please enter the introduced date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
				Date discontinued = askForDate("Please enter the discontinued date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
				Computer computerToCreate = new Computer(-1,computerNameInput, introduced, discontinued, companyId);
				computerService.createComputer(computerToCreate);
				break;

			case 5:
				System.out.println("Update a computer");
				int idToUpdateInput = getAndCheckComputerId("Please enter the id of the computer you want to update:", scan);
				Computer computerToUpdate = computerService.getComputerDetails(idToUpdateInput);
				System.out.println(computerToUpdate.toString());
				
				String updatedComputerName = null;
				Date updatedIntroduced = null;
				Date updatedDiscontinued = null;
				int updatedCompanyId = -1;

				int updateChoice = 0;

				while(updateChoice != 5){
					System.out.println("What do you want to update ?");
					System.out.println("Pick one of the following:");
					System.out.println("1 - Name");
					System.out.println("2 - Introduced date");
					System.out.println("3 - Discontinued date");
					System.out.println("4 - Company id");
					System.out.println("5 - Nothing I'm done");

					updateChoice = askForInt("Your choice: ", scan, false);

					switch(updateChoice){

					case 1:
						System.out.print("Please enter the new name of the computer:");
						updatedComputerName = scan.nextLine();
						break;

					case 2:
						updatedIntroduced = askForDate("Please enter the new introduced date of the computer with the format yyyy-mm-dd.", scan, true);
						break;

					case 3:
						updatedDiscontinued = askForDate("Please enter the new discontinued date of the computer with the format yyyy-mm-dd.", scan, true);
						break;

					case 4:
						updatedCompanyId = askForInt("Please enter the new id of the company that made the computer:",scan, true);
						break;

					case 5:
						break;

					default:
						System.out.println("You didn't enter a valid option");
						break;
					}
				}
				Computer updatedComputer = new Computer(idToUpdateInput,updatedComputerName, updatedIntroduced, updatedDiscontinued, updatedCompanyId);
				computerService.updateComputer(updatedComputer);
				break;

			case 6:
				System.out.println("Delete a computer");
				int idToDelete = getAndCheckComputerId("Please enter the id of the computer you want to delete:", scan);
				computerService.deleteComputer(idToDelete);
				break;

			case 7:
				System.out.println("Thank you for using Computer Database");
				break;

			default:
				System.out.println("You didn't select a valid option.");
				break;
			}
			if(optionInput != 7){
				System.out.println("\n\nPress enter to continue...");
				scan.nextLine();

			}
		}
		scan.close();

	}

	private static int getAndCheckComputerId(String query, Scanner scan) {
		boolean isIdValid = false;
		int userIdInput = -1;
		while(!isIdValid){
			userIdInput = askForInt(query, scan, false);
			try {
				isIdValid = computerService.checkIdInComputerTable(userIdInput);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(!isIdValid){
				System.out.println("The id you typed isn't in the database. Please try an other");
			}
		}
		return userIdInput;
	}

	public static Date askForDate(String query, Scanner scan, boolean skippable){
		Date date = null;
		boolean forcedNull = false;
		while(date == null && !(forcedNull && skippable)){
			try{
				System.out.println(query);
				System.out.print("You can type \"now\" to set the date at today's date.");
				String userDateInput = scan.nextLine();
				if(userDateInput.equalsIgnoreCase("now")){
					date = new Date(System.currentTimeMillis());
				}else if(userDateInput.equalsIgnoreCase("skip")){
					date = null;
					forcedNull = true;
				}else{
					date = Date.valueOf(userDateInput);
				}
			}catch(IllegalArgumentException exception){
				date = null;
				System.out.println("The date you typed is not in the right format!");
			}
		}
		return date;
	}

	public static int askForInt(String query, Scanner scan, boolean skippable){
		int userInt = -1;
		boolean forcedNull = false;
		while(userInt == -1 && !(forcedNull && skippable)){

			System.out.println(query);
			String input = scan.nextLine();
			if(input.equalsIgnoreCase("skip")){
				System.out.println("You skipped this part");
				forcedNull = true;
			}else{
				try{
					userInt = Integer.parseInt(input);
				}catch(NumberFormatException exception){
					userInt = -1;
					System.out.println("The value you typed is not in the right format!");
				}
			}

		}
		return userInt;
	}

}