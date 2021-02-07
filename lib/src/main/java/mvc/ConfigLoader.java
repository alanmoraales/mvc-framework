package mvc;

import exceptions.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class ConfigLoader {
  public void loadConfiguration(View view) {
    var logger = Logger.getInstance();
    var viewClassName = view.getClass().getSimpleName();

    try {
      logger.log(String.format(" * %s: Reading configuration file", viewClassName));
      var configuration = loadConfigFile(viewClassName);
      var modelClassName = configuration.getProperty("model");
      var controllerClassName = configuration.getProperty("controller");
      checkConfigFileStructure(modelClassName, controllerClassName);
      logger.log(String.format(" * %s: Configuration file read correctly", viewClassName));

      var modelInstance = getModelInstance(modelClassName);
      var controllerInstance = getControllerInstance(controllerClassName);
      logger.log(String.format(" * %s: Create model and controller instances", viewClassName));

      controllerInstance.setModel(modelInstance);
      view.setModel(modelInstance);
      view.setModelController(controllerInstance);
      logger.log(String.format(" * %s: Setting model and controller into view", viewClassName));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Controller getControllerInstance(String controllerClassName)
      throws ControllerNotFoundException, CannotBuildInstanceException {
    try {
      Class<?> modelClass = Class.forName(controllerClassName);
      Constructor<?> controllerConstructor = modelClass.getConstructor();
      return (Controller) controllerConstructor.newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      throw new ControllerNotFoundException(controllerClassName);
    } catch (Exception e) {
      throw new CannotBuildInstanceException(controllerClassName);
    }
  }

  private Model getModelInstance(String modelClassName) throws ModelNotFoundException, CannotBuildInstanceException {
    try {
      Class<?> modelClass = Class.forName(modelClassName);
      Constructor<?> modelConstructor = modelClass.getConstructor();
      return (Model) modelConstructor.newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      throw new ModelNotFoundException(modelClassName);
    } catch (Exception e) {
      throw new CannotBuildInstanceException(modelClassName);
    }
  }

  private Properties loadConfigFile(String viewClassName) throws ConfigFileNotFoundException, IOException {
    var configFileName = String.format("%s.properties", viewClassName);
    var configFile = new Properties();
    var propertiesStream = getClass().getClassLoader().getResourceAsStream(configFileName);

    if (propertiesStream == null) {
      throw new ConfigFileNotFoundException(configFileName);
    }

    configFile.load(propertiesStream);
    propertiesStream.close();
    return configFile;
  }

  private void checkConfigFileStructure(String modelClassName, String controllerClassName)
      throws InvalidConfigFileException {
    if (modelClassName == null || controllerClassName == null) {
      throw new InvalidConfigFileException(
          "Invalid configuration file, make sure you declare model and controller classes");
    }
  }
}
