package controller.commands;

import java.io.IOException;
import java.io.OutputStream;

import model.Processor;

/**
 * This is an interface that represents a single command.
 */
public interface Command {
  /**
   * Method utilized to execute the command on the given processor.
   */
  void executeCommand(Processor processor, OutputStream print) throws IOException;
}
