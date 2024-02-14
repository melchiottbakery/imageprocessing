package controller.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import model.Processor;

/**
 * This is a command object to execute the Split command.
 */
public class Split extends AbstractCommand {

  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public Split(String[] command) {
    super(command);
    if (command.length != 5) {
      throw new IllegalArgumentException("Invalid Command Format: The command should "
              + "be in the format of: rbg-split sourceImageID destinationImageIDOne " +
              "destinationImageIDTwo destinationImageIDThree");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    try {
      List<String> list = new LinkedList<>();
      list.add(command[2]);
      list.add(command[3]);
      list.add(command[4]);
      processor.split(command[1], list);
      print.write(("Split Operation Success" + System.lineSeparator()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    }
  }
}
