package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.commands.BrightenDarken;
import controller.commands.Combine;
import controller.commands.Command;
import controller.commands.GrayScale;
import controller.commands.HorizontalFlip;
import controller.commands.Load;
import controller.commands.Save;
import controller.commands.Split;
import controller.commands.VerticalFlip;
import model.Processor;

/**
 * This is a concrete controller class, consist of all function necessary as a processor controller.
 */
public class ProcessorController implements Controller {
  protected final Processor processor;
  protected final InputStream input;
  protected final PrintStream print;
  protected final List<String> fileList;
  private final Map<String, Function<String[], Command>> commandMap;
  protected boolean continueOperation;

  /**
   * Constructor for the class, initialize the proper variable and function mapping.
   *
   * @param processor The model of the controller
   * @param input     The input source of the user.
   * @param output    The view, or the output source of the controller
   */
  public ProcessorController(Processor processor, InputStream input, OutputStream output) {
    this.processor = processor;
    this.input = input;
    this.continueOperation = true;
    this.print = new PrintStream(output);
    commandMap = new HashMap<>();
    fileList = new ArrayList<>();
    establishCommandMap();
  }

  /**
   * Helper method utilized to create each command.
   */
  private void establishCommandMap() {
    commandMap.put("load", command -> new Load(command));
    commandMap.put("save", command -> new Save(command));
    commandMap.put("horizontal-flip", command -> new HorizontalFlip(command));
    commandMap.put("vertical-flip", command -> new VerticalFlip(command));
    commandMap.put("rgb-split", command -> new Split(command));
    commandMap.put("greyscale", command -> new GrayScale(command));
    commandMap.put("rgb-combine", command -> new Combine(command));
    commandMap.put("brighten", command -> new BrightenDarken(command));
  }


  /**
   * The helper method utilized to execute the run operation.
   *
   * @param command The command obtained
   * @throws IllegalArgumentException Exception will be thrown if there's loop or recursive call
   *                                  to itself.
   */
  protected final void runFile(String[] command) throws IllegalArgumentException {
    if (command.length == 2) {
      try {
        if (fileList.contains(command[1])) {
          throw new IllegalArgumentException("Invalid File: " +
                  "Recursive call of same file within file");
        }
        fileList.add(command[1]);
        Scanner sc = new Scanner(new File(command[1]));
        while (sc.hasNextLine()) {
          executeOperation(sc.nextLine().trim().split(" "));
          if (!continueOperation) {
            return;
          }
        }
        fileList.remove(command[1]);
        print.println("File Execution Complete");
      } catch (FileNotFoundException e) {
        print.printf("Invalid Command: File <%s> does not exist%n", command[1]);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      print.println("Invalid Command Format: The command"
              + " should be in the format of: run pathName");
    }
  }

  /**
   * Helper method utilized to execute the operation.
   *
   * @param commandString The command to be executed
   */
  protected void executeOperation(String[] commandString) throws IOException {
    String headCommand = commandString[0].toLowerCase();
    try {
      if (headCommand.length() == 0) {
        print.println("Invalid Command: No Command Detected");
        return;
      }
      if (headCommand.equals("run")) {
        runFile(commandString);
      } else if (headCommand.equals("exit")) {
        continueOperation = false;
      } else {
        if (commandMap.getOrDefault(headCommand, null) == null) {
          print.printf("Invalid Command: Command %s not supported%n", commandString[0]);
          return;
        }
        commandMap.get(headCommand).apply(commandString).executeCommand(processor, print);
      }
    } catch (IllegalArgumentException e) {
      print.printf(e.getMessage() + System.lineSeparator());
    }


  }

  @Override
  public void execute() {
    Scanner sc = new Scanner(input);
    String command = sc.nextLine().trim();
    while (!command.equalsIgnoreCase("exit")) {
      try {
        executeOperation(command.trim().split(" "));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      if (!continueOperation) {
        return;
      }
      command = sc.nextLine();
    }
  }

}
