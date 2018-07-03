package org.excilys.computer_database.controllers;


import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.dao.OrderByParams;
import org.excilys.computer_database.dto.ComputerDto;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.validator.ComputerFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComputerController {

  private final Logger logger = LoggerFactory.getLogger(ComputerController.class);
  private static final String COMPUTER_URL = "/computers";

  private ComputerService computerService;
  private CompanyService companyService;
  private ComputerFormValidator computerFormValidator;

  /**
   * Bind the form validator.
   * @param binder binder
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(computerFormValidator);
  }

  /**
   * Constructor for ComputerController.
   * @param computerService the computerService
   * @param companyService the companyService
   * @param computerFormValidator validator
   */
  public ComputerController(ComputerService computerService,
      CompanyService companyService,
      ComputerFormValidator computerFormValidator) {
    this.computerService = computerService;
    this.companyService = companyService;
    this.computerFormValidator = computerFormValidator;
  }

  /**
   * Show computers.
   * @param currentPage currentPage
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param search search
   * @param orderBy orderBy
   * @param ascOrDesc ascOrDesc
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping(COMPUTER_URL)
  public ModelAndView showComputers(Integer currentPage, Integer numberOfItemPerPage, String search, String orderBy, String ascOrDesc) {
    logger.debug("showComputers");
    ModelAndView modelAndView = new ModelAndView("dashboard");
    modelAndView = prepareDashboard(modelAndView, currentPage, numberOfItemPerPage, search, orderBy, ascOrDesc);
    return modelAndView;
  }
  /**
   * Add computer page handling.
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping(COMPUTER_URL + "/add")
  public ModelAndView addComputer() {
    logger.debug("addComputer");
    ModelAndView modelAndView = new ModelAndView("addComputer");
    modelAndView = prepareForm(modelAndView);
    return modelAndView;
  }

  /**
   * Edit computer page handling.
   * @param id id of the computer
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping(COMPUTER_URL + "/{id}/update")
  public ModelAndView editComputer(@PathVariable("id") int id) {
    logger.debug("editComputer");
    ModelAndView modelAndView = new ModelAndView("editComputer");
    Optional<Computer> computer = computerService.getComputerDetails(id);
    if (computer.isPresent()) {
      modelAndView.addObject("computerForm", new ComputerDto(computer.get()));
      ArrayList<Company> companies = companyService.getCompanies();
      modelAndView.addObject("companies", companies);
    }
    return modelAndView;
  }
  /**
   * Delete computers.
   * @param selection list of ids
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping(COMPUTER_URL + "/delete")
  public ModelAndView deleteComputer(String selection) {
    logger.debug("deleteComputer:" + selection);
    deleteComputersIfRequired(selection);
    ModelAndView modelAndView = new ModelAndView("dashboard");
    modelAndView = prepareDashboard(modelAndView);
    return modelAndView;
  }
  /**
   * Validate form and save the computer.
   * @param computerForm the computer to save
   * @param result result of the binding
   * @param model the model
   * @param redirectAttributes the redirecting attributes
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping(COMPUTER_URL + "/create")
  public ModelAndView saveComputer(@ModelAttribute("computerForm") @Validated ComputerDto computerForm,
      BindingResult result, Model model,
      final RedirectAttributes redirectAttributes) {
    logger.debug("saveComputer");
    ModelAndView modelAndView = null;
    if (result.hasErrors()) {
      logger.debug("hasErrors");
      modelAndView = new ModelAndView("addComputer");
      ArrayList<Company> companies = companyService.getCompanies();
      modelAndView.addObject("companies", companies);
    } else {
      try {
        computerService.createComputer(computerForm.toComputer());
        modelAndView = new ModelAndView("dashboard");
        modelAndView = prepareDashboard(modelAndView);
      } catch (CDBObjectException exception) {
        modelAndView = new ModelAndView("addComputer");
        ArrayList<Company> companies = companyService.getCompanies();
        modelAndView.addObject("companies", companies);
        result.rejectValue("discontinued", "error.discontinued.time");
        result.rejectValue("introduced", "error.introduced.time");
      }
    }
    return modelAndView;
  }

  /**
   * Validate form and update the computer.
   * @param computerForm the computer to update
   * @param result result of the binding
   * @param model the model
   * @param redirectAttributes the redirecting attributes
   * @return the modelAndView object
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping(COMPUTER_URL + "/update")
  public ModelAndView updateComputer(@ModelAttribute("computerForm") @Validated ComputerDto computerForm,
      BindingResult result, Model model,
      final RedirectAttributes redirectAttributes) {
    logger.debug("updateComputer");
    ModelAndView modelAndView = null;
    if (result.hasErrors()) {
      logger.debug("hasErrors");
      modelAndView = new ModelAndView("editComputer");
      ArrayList<Company> companies = companyService.getCompanies();
      modelAndView.addObject("companies", companies);
    } else {
      try {
        computerService.updateComputer(computerForm.toComputer());
        modelAndView = new ModelAndView("dashboard");
        modelAndView = prepareDashboard(modelAndView);
      } catch (CDBObjectException exception) {
        modelAndView = new ModelAndView("editComputer");
        ArrayList<Company> companies = companyService.getCompanies();
        modelAndView.addObject("companies", companies);
        result.rejectValue("discontinued", "Error.code", "Invalid date: the discontinued date cannot be earlier than the introduced date.");
        result.rejectValue("introduced", "Error.code", "Invalid date: the introduced date cannot be later than the discontinued date.");
      }
    }
    return modelAndView;
  }

  /**
   * Get the OderByParams object from the model.
   * @param orderBy orderBy param
   * @param ascOrDesc ascOrDesc param
   * @param model model object to add orderByParams attributes
   * @return the OderByParams object
   */
  private OrderByParams getOrderByParams(String orderBy, String ascOrDesc, ModelAndView model) {
    if (orderBy == null || orderBy.isEmpty()) {
      return null;
    }
    if (ascOrDesc == null || ascOrDesc.isEmpty()) {
      return null;
    }
    model.addObject("orderBy", orderBy);
    model.addObject("ascOrDesc", ascOrDesc);
    return new OrderByParams(orderBy, ascOrDesc);
  }

  /**
   * Delete the computers defined by the user.
   * @param toDelete String containing ids to delete
   */
  private void deleteComputersIfRequired(String toDelete) {
    String[] idsToDeleteAsString = toDelete.split(",");
    int[] idsToDelete = new int[idsToDeleteAsString.length];
    for (int i = 0; i < idsToDeleteAsString.length; i++) {
      idsToDelete[i] = Integer.valueOf(idsToDeleteAsString[i]);
    }
    computerService.deleteComputers(idsToDelete);
  }
  /**
   * Prepares the dashboard.
   * @param modelAndView The ModelAndView Object
   * @return the modelAndView object
   */
  private ModelAndView prepareDashboard(ModelAndView modelAndView) {
    return prepareDashboard(modelAndView, null, null, null, null, null);
  }

  /**
   * Prepares the dashboard.
   * @param modelAndView The ModelAndView Object
   * @param currentPage currentPage
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param search search
   * @param orderBy orderBy
   * @param ascOrDesc ascOrDesc
   * @return the modelAndView object
   */
  private ModelAndView prepareDashboard(ModelAndView modelAndView, Integer currentPage, Integer numberOfItemPerPage, String search, String orderBy, String ascOrDesc) {
    int numberOfComputers = computerService.countComputers(search);
    modelAndView.addObject("numberOfComputers", numberOfComputers);
    if (search != null) {
      modelAndView.addObject("search", search);
    }

    OrderByParams orderByParams = getOrderByParams(orderBy, ascOrDesc, modelAndView);
    int intPage = (currentPage == null || currentPage == 0) ? 1 : currentPage;
    int intNumberOfItemPerPage = (numberOfItemPerPage == null || numberOfItemPerPage == 0) ? 10 : numberOfItemPerPage;
    int numberOfPages = (int) numberOfComputers / intNumberOfItemPerPage + 1;
    if (numberOfComputers % intNumberOfItemPerPage == 0) {
      numberOfPages = numberOfComputers / intNumberOfItemPerPage;
    }
    modelAndView.addObject("currentPage", intPage);
    modelAndView.addObject("numberOfItemPerPage", intNumberOfItemPerPage);
    modelAndView.addObject("numberOfPages", numberOfPages);
    ArrayList<Computer> computers = computerService.getComputersWithParams(intNumberOfItemPerPage, intPage, search, orderByParams);
    ArrayList<ComputerDto> computerDtos = new ArrayList<>();
    computers.forEach(computer -> computerDtos.add(new ComputerDto(computer)));
    modelAndView.addObject("computers", computerDtos);
    return modelAndView;
  }

  /**
   * Prepare the form.
   * @param modelAndView ModelAndView object.
   * @return the modelAndView object
   */
  private ModelAndView prepareForm(ModelAndView modelAndView) {
    ComputerDto computerDto = new ComputerDto();
    modelAndView.addObject("computerForm", computerDto);
    ArrayList<Company> companies = companyService.getCompanies();
    modelAndView.addObject("companies", companies);
    return modelAndView;
  }

}