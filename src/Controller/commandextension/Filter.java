package controller.commandextension;

import java.io.IOException;
import java.io.OutputStream;

import model.ProcessorExtension;

/**
 * This is a command object to execute the filter command.
 */
public class Filter extends AbstractCommand {

  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public Filter(String[] command) throws IllegalArgumentException {
    super(command);
    if (command.length != 3) {
      throw new IllegalArgumentException("Invalid Command Format: The command should "
              + "be in the format of: filter sourceID destinationID" + System.lineSeparator());
    }
  }

  @Override
  public void executeCommand(ProcessorExtension processor, OutputStream print) throws IOException {
    try {
      processor.filter(command[0].toLowerCase(), command[1], command[2]);
      print.write(String.format("%s Operation Success%n",
              command[0].substring(0, 1).toUpperCase()
                      + command[0].substring(1).toLowerCase()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s" + System.lineSeparator(),
              e.getMessage()).getBytes());
    }
  }
}
