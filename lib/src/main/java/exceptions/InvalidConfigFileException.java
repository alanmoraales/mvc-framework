package exceptions;

public class InvalidConfigFileException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidConfigFileException(String message) {
    super(message);
  }
}
