package controller.commandextension;

import java.util.List;

/**
 * This is abstract class where it declares all common structure for a command object.
 */
public abstract class AbstractCommand implements CommandExtension {
  protected String[] command;
  protected List<String> extensionList;

  /**
   * Constructor for the object, takes in the input command.
   *
   * @param command The command to execute.
   */
  public AbstractCommand(String[] command) {
    this.command = command;
  }
}
