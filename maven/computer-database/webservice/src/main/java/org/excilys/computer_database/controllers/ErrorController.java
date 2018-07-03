package org.excilys.computer_database.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;


@Controller
public class ErrorController {

  @RequestMapping(value = "/errors", method = RequestMethod.GET)
  public ModelAndView renderErrorPage(HttpServletRequest request) {
    ModelAndView errorPage = new ModelAndView();
    int httpErrorCode = getErrorCode(request);
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    String exceptionMessage = getExceptionMessage(throwable, httpErrorCode);

    switch (httpErrorCode) {

    case 403:
      errorPage.setViewName("403");
      break;

    case 404:
      errorPage.setViewName("404");
      break;

    case 500:
      errorPage.setViewName("500");
      break;

    }
    errorPage.addObject("errorMessage", exceptionMessage);
    return errorPage;
  }
  /**
   * Return the error code.
   * @param httpRequest request
   * @return the error code
   */
  private int getErrorCode(HttpServletRequest httpRequest) {
    return (Integer) httpRequest
        .getAttribute("javax.servlet.error.status_code");
  }
  /**
   * get the exception message.
   * @param throwable exception
   * @param statusCode status code
   * @return the exception message
   */
  private String getExceptionMessage(Throwable throwable, Integer statusCode) {
    if (throwable != null) {
      return Throwables.getRootCause(throwable).getMessage();
    }
    HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
    return httpStatus.getReasonPhrase();
  }
}
