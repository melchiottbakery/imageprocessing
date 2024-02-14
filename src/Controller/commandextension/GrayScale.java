package controller.commandextension;

import java.io.IOException;
import java.io.OutputStream;

import model.ProcessorExtension;

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
    if (command.length != 4 && command.length != 3) {
      throw new IllegalArgumentException("Invalid Command Format: The command should "
              + "be in the format of: greyscale option sourceID destinationID or"
              + " for color transformation: greyscale sourceID destinationID"
              + System.lineSeparator());
    }
  }

  @Override
  public void executeCommand(ProcessorExtension processor, OutputStream print) throws IOException {
    try {
      if (command.length == 4) {
        command[1] = command[1].toLowerCase();
        processor.toGrayScale(command[1], command[2], command[3]);
        print.write(String.format("GrayScale %s Operation Success%n", command[1]).getBytes());
      } else {
        processor.colorTransformation("greyscale", command[1], command[2]);
        print.write(String.format("Color Transformation GrayScale Success%n").getBytes());
      }
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
