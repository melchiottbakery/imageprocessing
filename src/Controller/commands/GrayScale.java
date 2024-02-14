package controller.commands;

import java.io.IOException;
import java.io.OutputStream;

import model.Processor;

/**
 * This is a command object to execute the GrayScale command.
 */
public class GrayScale extends AbstractCommand {

  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public GrayScale(String[] command) throws IllegalArgumentException {
    super(command);
    if (command.length != 4) {
      throw new IllegalArgumentException("Invalid Command Format: The command should "
              + "be in the format of: greyscale option sourceID destinationID");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    try {
      command[1] = command[1].toLowerCase();
      processor.toGrayScale(command[1], command[2], command[3]);
      print.write(String.format("GrayScale %s Operation Success%n", command[1]).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
