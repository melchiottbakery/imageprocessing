package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import controller.commandextension.ColorTransformation;
import controller.commandextension.CommandExtension;
import controller.commandextension.Dither;
import controller.commandextension.Filter;
import controller.commandextension.GrayScale;
import model.ProcessorExtension;

/**
 * This is a class that represents the process controller extension.
 */
public class ProcessorControllerExtension extends ProcessorController {
  private final Map<String, Function<String[], CommandExtension>> commandMap;
  private final ProcessorExtension processor;


  /**
   * Constructor for the class, initialize the proper variable and function mapping.
   *
   * @param processor The model of the controller
   * @param input     The input source of the user.
   * @param output    The view, or the output source of the controller
   */
  public ProcessorControllerExtension(ProcessorExtension processor,
                                      InputStream input, OutputStream output) {
    super(processor, input, output);
    commandMap = new HashMap<>();
    this.processor = processor;
    establishCommandMap();
  }


  /**
   * Helper method utilized to create each command.
   */
  private void establishCommandMap() {
    commandMap.put("blur", command -> new Filter(command));
    commandMap.put("sharpen", command -> new Filter(command));
    commandMap.put("sepia", command -> new ColorTransformation(command));
    commandMap.put("dither", command -> new Dither(command));
    commandMap.put("greyscale", command -> new GrayScale(command));
  }

  /**
   * Helper method utilized to execute the operation.
   *
   * @param commandString The command to be executed
   */
  protected void executeOperation(String[] commandString) throws IOException {
    String headCommand = commandString[0].toLowerCase();
    try {
      if (commandMap.getOrDefault(headCommand, null) == null) {
        super.executeOperation(commandString);
        return;
      }
      commandMap.get(headCommand).apply(commandString).executeCommand(processor, print);
    } catch (RuntimeException e) {
      print.print(e.getMessage());
    }
  }


}
