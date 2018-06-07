package org.excilys.computer_database.validator;

import org.excilys.computer_database.dto.ComputerDto;
import org.excilys.computer_database.util.Util;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ComputerFormValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return ComputerDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ComputerDto computer = (ComputerDto) target;
    System.out.println("Validating");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.code", "You have to fill the name field.");
    if (computer.getName() == null || computer.getName().isEmpty()) {
      errors.rejectValue("name", "Error.code", "The name of the computer is mandatory and can't be empty.");
    }
    if (computer.getIntroduced() != null && !computer.getIntroduced().isEmpty()) {
      if (!Util.isDateValid(computer.getIntroduced())) {
        errors.rejectValue("introduced", "Error.code", "Invalid date: the date isn't in the right format.");
      }
    }
    if (computer.getDiscontinued() != null && !computer.getDiscontinued().isEmpty()) {
      if (!Util.isDateValid(computer.getDiscontinued())) {
        errors.rejectValue("discontinued", "Error.code", "Invalid date: the date isn't in the right format.");
      }
    }
  }

}
