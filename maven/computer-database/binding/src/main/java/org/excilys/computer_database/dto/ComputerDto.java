package org.excilys.computer_database.dto;

import java.sql.Date;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;

public class ComputerDto {

	private int id;
	private String name;
	private String introduced;
	private String discontinued;
	private int companyId;
	private String companyName;

	/**
	 * @param computer Computer you want the DTO from
	 */
	public ComputerDto(Computer computer) {
		this.id = computer.getId();
		this.name = computer.getName();
		if (computer.getIntroduced() != null) {
			this.introduced = computer.getIntroduced().toString();
		} else {
			this.introduced = null;
		}
		if (computer.getDiscontinued() != null) {
			this.discontinued = computer.getDiscontinued().toString();
		} else {
			this.discontinued = null;
		}
		if (computer.getCompany() != null) {
			this.companyId = computer.getCompany().getId();
			this.companyName = computer.getCompany().getName();
		}
	}

	public Computer toComputer() {
		Computer computer = new Computer();
		computer.setId(this.id);
		computer.setName(this.name);
		if (this.introduced != null) {
			computer.setIntroduced(Date.valueOf((this.introduced)));
		}
		if (this.discontinued != null) {
			computer.setDiscontinued(Date.valueOf((this.discontinued)));
		}
		Company company = new Company(this.companyId, this.companyName);
		computer.setCompany(company);
		return computer;
	}
	/**
	 * Empty constructor.
	 */
	public ComputerDto() { }

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getShortToString() {
		return this.id + " " + this.name;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "ComputerDto [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ ", companyId=" + companyId + ", companyName=" + companyName + "]";
	}



}
