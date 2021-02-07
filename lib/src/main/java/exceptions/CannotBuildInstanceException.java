package exceptions;

public class CannotBuildInstanceException extends Exception {
  private static final long serialVersionUID = 1L;

  public CannotBuildInstanceException(String modelClassName) {
    super("There was and error when building " + modelClassName + " instance. Please try again.");
  }
}
