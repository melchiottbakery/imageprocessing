package controller.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This is abstract class where it declares all common structure for a command object.
 */
public abstract class AbstractCommand implements Command {
  protected String[] command;

  protected List<String> extensionList;

  /**
   * Constructor for the object, takes in the input command.
   *
   * @param command The command to execute.
   */
  public AbstractCommand(String[] command) {
    this.command = command;
    extensionList = new ArrayList<>();
    extensionList.add("bmp");
    extensionList.add("jpg");
    extensionList.add("jpeg");
    extensionList.add("png");
    extensionList.add("ppm");
  }

  /**
   * This is a helper method utilized to get the extension of a file.
   *
   * @param target The target file/address
   * @return the extension of a file if exist
   */
  protected final String getFileExtension(String target) {
    for (int i = target.length() - 1; i >= 0; i--) {
      if (target.charAt(i) == '.') {
        if (i + 1 == target.length() - 1) {
          return "";
        }
        return target.substring(i + 1);
      }
    }
    return "";
  }

  /**
   * Method utilized to check whether the image is supported.
   * @param print The output stream to write the error/success message
   * @param extension The image extension
   * @return A boolean represent if the image is accepted
   * @throws IOException Exception will be thrown if there's issue on message writing
   */
  protected boolean checkValid(OutputStream print, String extension) throws IOException {
    if (!extensionList.contains(extension)) {
      print.write(("File format not supported" + System.lineSeparator()).getBytes());
      return false;
    }
    if (!Files.exists(Paths.get(command[1].substring(0, command[1].lastIndexOf('/'))))) {
      print.write(("Invalid Command: Invalid Path Given" + System.lineSeparator()).getBytes());
      return false;
    }
    return true;
  }

}
