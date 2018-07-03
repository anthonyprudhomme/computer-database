package org.excilys.computer_database.controllers;


import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.dao.OrderByParams;
import org.excilys.computer_database.dto.ComputerDto;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.ComputerService;
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
@RequestMapping("/computers")
public class ComputerController {

  private final Logger logger = LoggerFactory.getLogger(ComputerController.class);

  private ComputerService computerService;

  /**
   * Constructor for ComputerController.
   * @param computerService the computerService
   * @param companyService the companyService
   */
  public ComputerController(ComputerService computerService) {
    this.computerService = computerService;
  }

  /**
   * Show computers.
   * @param currentPage currentPage
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param search search
   * @param orderBy orderBy
   * @param ascOrDesc ascOrDesc
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<ArrayList<ComputerDto>> getComputers(@RequestParam("currentPage") Integer currentPage, Integer numberOfItemPerPage, String search, String orderBy, String ascOrDesc) {
    logger.debug("List Computers");
    OrderByParams orderByParams = getOrderByParams(orderBy, ascOrDesc);
    ArrayList<Computer> computers =  computerService.getComputersWithParams(numberOfItemPerPage, currentPage, search, orderByParams);
    if (computers.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    ArrayList<ComputerDto> computerDtos = new ArrayList<>();
    computers.forEach(computer -> computerDtos.add(new ComputerDto(computer)));
    return new ResponseEntity<ArrayList<ComputerDto>>(computerDtos, HttpStatus.OK);
  }

  /**
   * Delete computer.
   * @param id to delete
   * @return the status of the request
   */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteComputer(@PathVariable("id") long id) {
    logger.debug("Delete computer: " + id);
    computerService.deleteComputer((int)id);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  /**
   * Validate form and save the computer.
   * @param computerForm the computer to save
   * @param result result of the binding
   * @param model the model
   * @param redirectAttributes the redirecting attributes
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<String> createComputer(@RequestBody ComputerDto computerDto) {
    logger.debug("Create Computer " + computerDto.toString());
    Computer computer = computerDto.toComputer();
    try {
      computerService.createComputer(computer);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (CDBObjectException e) {
      e.printStackTrace();
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   * Update the computer.
   * @param id of the computer to update
   * @param computerDto that contains data to update
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @PutMapping(path = "/{id}")
  public ResponseEntity<?> updateComputer(@PathVariable("id") int id, @RequestBody ComputerDto computerDto) {
    Computer currentComputer = computerDto.toComputer();
    Optional<Computer> computer = computerService.getComputerDetails(id);
    if(computer.isPresent()) {
      Computer oldComputer = computer.get();
      oldComputer.setCompany(currentComputer.getCompany());
      oldComputer.setName(currentComputer.getName());
      oldComputer.setIntroduced(currentComputer.getIntroduced());
      oldComputer.setDiscontinued(currentComputer.getDiscontinued());
      try {
        computerService.updateComputer(oldComputer);
        return new ResponseEntity<>(HttpStatus.OK);
      } catch (CDBObjectException e) {
        e.printStackTrace();
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
      }
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Get the OderByParams object from the model.
   * @param orderBy orderBy param
   * @param ascOrDesc ascOrDesc param
   * @param model model object to add orderByParams attributes
   * @return the OderByParams object
   */
  private OrderByParams getOrderByParams(String orderBy, String ascOrDesc) {
    if (orderBy == null || orderBy.isEmpty()) {
      return null;
    }
    if (ascOrDesc == null || ascOrDesc.isEmpty()) {
      return null;
    }
    return new OrderByParams(orderBy, ascOrDesc);
  }

}