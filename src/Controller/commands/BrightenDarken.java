package controller.commands;

import java.io.IOException;
import java.io.OutputStream;

import model.Processor;

/**
 * This is a command object to execute the bright/darken command.
 */
public class BrightenDarken extends AbstractCommand {
  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public BrightenDarken(String[] command) throws IllegalArgumentException {
    super(command);
    if (command.length != 4) {
      throw new IllegalArgumentException("Invalid Command Format: The command should"
              + " be in the format of: brighten pixelValue sourceImageID destinationImageID");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    try {
      processor.brightenDarkenImage(Integer.parseInt(command[1]), command[2], command[3]);
      print.write(("Brighten Operation Success" + System.lineSeparator()).getBytes());
    } catch (NumberFormatException e) {
      print.write(("Invalid Command Format: pixel value should only contain " +
              "numerical character" + System.lineSeparator()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
