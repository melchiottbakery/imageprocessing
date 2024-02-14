package controller.commandextension;

import java.io.IOException;
import java.io.OutputStream;

import model.ProcessorExtension;

/**
 * This is a command object to execute the Color Transformation command.
 */
public class ColorTransformation extends AbstractCommand {
  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public ColorTransformation(String[] command) throws IllegalArgumentException {
    super(command);
    if (command.length != 3) {
      throw new IllegalArgumentException("Invalid Color Transformation Format: The command should "
              + "be in the format of: method sourceID destinationID" + System.lineSeparator());
    }
  }

  @Override
  public void executeCommand(ProcessorExtension processor, OutputStream print) throws IOException {
    try {
      processor.colorTransformation(command[0].toLowerCase(), command[1], command[2]);
      print.write(String.format("Color transformation %s Operation Success%n",
              command[0].toLowerCase()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
