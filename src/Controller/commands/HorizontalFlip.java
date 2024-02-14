package controller.commands;

import java.io.IOException;
import java.io.OutputStream;

import model.Processor;

/**
 * This is a command object to execute the HorizontalFlip command.
 */
public class HorizontalFlip extends AbstractCommand {
  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public HorizontalFlip(String[] command) {
    super(command);
    if (command.length != 3) {
      throw new IllegalArgumentException("Invalid Command Format: The command"
              + " should be in the format of: horizontal-flip sourceID destinationID");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    try {
      processor.flipImage(true, command[1], command[2]);
      print.write(("Horizontal-Flip Operation Success" + System.lineSeparator()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
