package exceptions;

public class ConfigFileNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public ConfigFileNotFoundException(String configFileName) {
    super("configuration file '" + configFileName + "' not found in the classpath");
  }
}
