package mvc;

import exceptions.ConfigFileNotFoundException;
import exceptions.InvalidConfigFileException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

public class Logger {
  private int currentFileIndex;
  private boolean activated;
  private int maxFileSize;
  private boolean firstWriteInFile;
  private static Logger instance;

  private Logger() {
    this.activated = true;
    this.currentFileIndex = 0;
    this.firstWriteInFile = true;

    try {
      this.loadConfiguration();
    } catch (ConfigFileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InvalidConfigFileException e) {
      e.printStackTrace();
    }
  }

  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }

    return instance;
  }

  public void log(String message) {
    if (this.activated) {
      var fileName = String.format("log_%d.txt", currentFileIndex);
      var logFile = new File(fileName);

      try {
        logFile.createNewFile();

        if (logFile.length() >= maxFileSize) {
          this.currentFileIndex++;
          fileName = String.format("log_%d.txt", currentFileIndex);
          logFile = new File(fileName);
          logFile.createNewFile();
        }

        FileWriter fileWriter;
        if (firstWriteInFile) {
          fileWriter = new FileWriter(logFile);
          this.firstWriteInFile = false;
        } else {
          fileWriter = new FileWriter(logFile, true);
        }

        var printWriter = new PrintWriter(fileWriter);
        printWriter.println(message);
        printWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println(message);
    }
  }

  private void loadConfiguration() throws ConfigFileNotFoundException, IOException, InvalidConfigFileException {
    var configFileName = "log.properties";
    var configFile = new Properties();
    var propertiesStream = getClass().getClassLoader().getResourceAsStream(configFileName);

    if (propertiesStream == null) {
      throw new ConfigFileNotFoundException(configFileName);
    }

    configFile.load(propertiesStream);
    propertiesStream.close();

    var activatedValue = configFile.getProperty("activated");
    var maxFileSizeValue = configFile.getProperty("maxFileSize");

    if (activatedValue == null || maxFileSizeValue == null) {
      throw new InvalidConfigFileException("Invalid configuration file for logger feature");
    } else {
      this.activated = Boolean.parseBoolean(activatedValue);
      this.maxFileSize = Integer.parseInt(maxFileSizeValue);
    }

    if (!this.activated) {
      System.out.println(" * Logger deactivated");
    }
  }
}
