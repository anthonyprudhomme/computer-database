package persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Company;
import model.Computer;

public class JdbcRequest {
	
	private void doARequest(String query, RequestName requestName, Computer computer){
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
			preparedStatement = this.prepareRequest(preparedStatement, requestName, computer);
			if (preparedStatement.execute()) {
				this.handleResult(preparedStatement.getResultSet(), requestName);
			}
		}
		catch (SQLException sqlException){
			System.out.println("SQLException: " + sqlException.getMessage());
			System.out.println("SQLState: " + sqlException.getSQLState());
			System.out.println("VendorError: " + sqlException.getErrorCode());
		}
		finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException sqlException) { } // ignore
				resultSet = null;
			}

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException sqlException) { } // ignore
				preparedStatement = null;
			}
		}
	}

	private void doARequest(String query, RequestName requestName){
		this.doARequest(query, requestName, null);
	}

	public void getComputerList(){
		String query = "SELECT * FROM computer";
		this.doARequest(query, RequestName.LIST_COMPUTERS);
	}

	public void getCompanyList(){
		String query = "SELECT * FROM company";
		this.doARequest(query, RequestName.LIST_COMPANIES);
	}

	public void getComputerDetails(int id){
		String query = "SELECT * FROM computer WHERE id="+id;
		this.doARequest(query, RequestName.COMPUTER_DETAILS);
	}

	public void createComputer(Computer computer){
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) "
				+ "VALUES(?,?,?,?)";
		this.doARequest(query, RequestName.CREATE_COMPUTER, computer);
	}
	
	private PreparedStatement prepareRequest(PreparedStatement preparedStatement, RequestName requestName, Computer computer) throws SQLException{
		switch(requestName){

		case COMPUTER_DETAILS:
		case DELETE_COMPUTER:
		case UPDATE_COMPUTER:
		case LIST_COMPANIES:
		case LIST_COMPUTERS:
			break;
		case CREATE_COMPUTER:
			
			preparedStatement.setString(1, computer.getName());		
			preparedStatement.setDate(2, computer.getIntroduced());
			preparedStatement.setDate(3, computer.getDiscontinued());
			preparedStatement.setInt(4, computer.getCompanyId());
			break;
		default:
			break;
			
			
		}
		
		return preparedStatement;
	}

	private void handleResult(ResultSet resultSet, RequestName requestName) throws SQLException{
		switch(requestName){

		case COMPUTER_DETAILS:
		case CREATE_COMPUTER:
		case DELETE_COMPUTER:
		case UPDATE_COMPUTER:
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Date introduced = resultSet.getDate("introduced");
				Date discontinued = resultSet.getDate("discontinued");
				int companyId = resultSet.getInt("company_id");	
				Computer computer = new Computer(id, name, introduced, discontinued, companyId);
				System.out.println(computer.toString());
			}
			break;

		case LIST_COMPANIES:
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Company company = new Company(id, name);
				System.out.println(company.toString());
			}
			break;

		case LIST_COMPUTERS:
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				System.out.println(id + ": " + name);
			}
			break;

		default:
			System.out.println("Undefined request.");
			break;


		}
	}
}
