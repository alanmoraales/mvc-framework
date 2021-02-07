package exceptions;

public class ControllerNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public ControllerNotFoundException(String controllerClassName) {
    super("Controller with class " + controllerClassName + " was not found in the classpath");
  }
}
