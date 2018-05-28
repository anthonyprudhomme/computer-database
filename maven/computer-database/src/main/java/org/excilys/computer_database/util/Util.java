package org.excilys.computer_database.util;

public class Util {

  /**
   * Tells if the String is possible to cast into an integer or not.
   * @param s the String to check
   * @return Whether the string is possible to cast or not
   */
  public static boolean isInteger(String s) {
    return isInteger(s, 10);
  }

  /**
   * Tells if the String is possible to cast into an integer or not.
   * @param s the String to check
   * @param radix The range of numbers
   * @return Whether the string is possible to cast or not
   */
  public static boolean isInteger(String s, int radix) {
    if (s == null || s.isEmpty()) {
      return false;
    }
    for (int i = 0; i < s.length(); i++) {
      if (i == 0 && s.charAt(i) == '-') {
        if (s.length() == 1) {
          return false;
        } else {
          continue;
        }
      }
      if (Character.digit(s.charAt(i), radix) < 0) {
        return false;
      }
    }
    return true;
  }

}
