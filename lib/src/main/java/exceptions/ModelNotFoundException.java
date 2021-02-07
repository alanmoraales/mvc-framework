package exceptions;

public class ModelNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public ModelNotFoundException(String modelClassName) {
    super("Model with class " + modelClassName + " was not found in the classpath");
  }
}
