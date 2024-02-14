package model;

/**
 * This is class that represents an exception if the token of PPM file is not found.
 */
public class IllegalFileException extends RuntimeException {
  /**
   * Constructor for the class, which calls its parent with the message of the exception.
   *
   * @param message Message of the exception
   */
  public IllegalFileException(String message) {
    super(message);
  }
}