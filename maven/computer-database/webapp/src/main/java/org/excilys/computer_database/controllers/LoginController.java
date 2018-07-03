package org.excilys.computer_database.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
  
  private final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @RequestMapping(value = { "/"}, method = RequestMethod.GET)
  public ModelAndView welcomePage() {
    ModelAndView model = new ModelAndView();
    model.setViewName("dashboard");
    return model;
  }
  
  @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
  public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
  @RequestParam(value = "logout", required = false) String logout) {
    logger.debug("Login page");
    ModelAndView model = new ModelAndView();
    if (error != null) {
      model.addObject("error", "Invalid Credentials provided.");
    }

    if (logout != null) {
      model.addObject("message", "Logged out from JournalDEV successfully.");
    }

    model.setViewName("login");
    return model;
  }

}