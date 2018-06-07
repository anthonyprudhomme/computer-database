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
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name.empty");
    if (computer.getIntroduced() != null && !computer.getIntroduced().isEmpty()) {
      if (!Util.isDateValid(computer.getIntroduced())) {
        errors.rejectValue("introduced", "error.date.format");
      }
    }
    if (computer.getDiscontinued() != null && !computer.getDiscontinued().isEmpty()) {
      if (!Util.isDateValid(computer.getDiscontinued())) {
        errors.rejectValue("discontinued", "error.date.format");
      }
    }
  }

}
