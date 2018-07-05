package org.excilys.computer_database.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.dto.CompanyDto;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/companies")
public class CompanyController {

  private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

  private CompanyService companyService;

  /**
   * Constructor for CompanyController.
   * @param companyService the companyService
   */
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  /**
   * Show companies.
   * @param currentPage currentPage
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param search search
   * @param orderBy orderBy
   * @param ascOrDesc ascOrDesc
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<ArrayList<CompanyDto>> getCompanies(@RequestParam("currentPage") Integer currentPage, Integer numberOfItemPerPage, String search, String orderBy, String ascOrDesc) {
    logger.debug("List Companies");
    ArrayList<Company> companies =  companyService.getCompaniesWithParams(currentPage, numberOfItemPerPage, search, orderBy, ascOrDesc);
    if (companies.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    ArrayList<CompanyDto> companyDtos = new ArrayList<>();
    companies.forEach(company -> companyDtos.add(new CompanyDto(company)));
    return new ResponseEntity<ArrayList<CompanyDto>>(companyDtos, HttpStatus.OK);
  }

  /**
   * Delete company.
   * @param id to delete
   * @return the status of the request
   */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<CompanyDto> deleteCompany(@PathVariable("id") int id) {
    logger.debug("Delete company: " + id);
    companyService.deleteCompany(id);
    return new ResponseEntity<CompanyDto>(HttpStatus.OK);
  }

  /**
   * Creates a company.
   * @param companyDto the company dto to create
   * @return the response status.
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping(produces = "application/json")
  public ResponseEntity<String> createCompany(@RequestBody CompanyDto companyDto) {
    logger.debug("Create company " + companyDto.toString());
    Company company = companyDto.toCompany();
    companyService.createCompany(company);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Update the company.
   * @param id of the company to update
   * @param companyDto that contains data to update
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @PutMapping(path = "/{id}")
  public ResponseEntity<?> updateCompany(@PathVariable("id") int id, @RequestBody CompanyDto companyDto) {
    Company currentCompany = companyDto.toCompany();
    Optional<Company> company = companyService.getCompany(id);
    if (company.isPresent()) {
      Company oldCompany = company.get();
      oldCompany.setName(currentCompany.getName());
      companyService.updateCompany(oldCompany);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  
  /**
   * Get a company with given id.
   * @param id of the company
   * @return the company
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping(path = "/{id}")
  public ResponseEntity<CompanyDto> getCompany(@PathVariable("id") long id) {
    logger.debug("List Computers");
    Optional<Company> company = companyService.getCompany((int)id);
    if (company.isPresent()) {
      CompanyDto companyDto = new CompanyDto(company.get());
      return new ResponseEntity<CompanyDto>(companyDto, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
  
  /**
   * Count the number of companies.
   * @return the number of companies
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping(path = "/count")
  public ResponseEntity<Integer> countCompanies() {
    logger.debug("Count Computers");
    return new ResponseEntity<Integer>(companyService.countCompanies(), HttpStatus.OK);
  }

}