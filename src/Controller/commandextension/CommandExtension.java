package controller.commandextension;

import java.io.IOException;
import java.io.OutputStream;

import model.ProcessorExtension;

/**
 * This is an interface that represents a single command.
 */
public interface CommandExtension {
  /**
   * Method utilized to execute the command on the given processor.
   */
  void executeCommand(ProcessorExtension processor, OutputStream print) throws IOException;
}
