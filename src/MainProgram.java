import java.io.ByteArrayInputStream;
import java.io.InputStream;

import controller.Controller;
import controller.ProcessorControllerExtension;
import model.ImageProcessorExtension;
import model.ProcessorExtension;

/**
 * This is the main class where the program will be initiated in.
 */
public class MainProgram {

  /**
   * The main method of the program, which utilized to initiate the entire program.
   *
   * @param args Argument from cmd, which will always be empty
   */
  public static void main(String[] args) {
    InputStream input = System.in;
    if (args.length > 0 && args.length != 2) {
      throw new IllegalArgumentException("Invalid Command Line Argument");
    }
    if (args.length == 2) {
      if (args[0].equals("-file")) {
        input = new ByteArrayInputStream(String.format("run %s"
                + System.lineSeparator()
                + "exit", args[1]).getBytes());
      } else {
        throw new IllegalArgumentException("Invalid Command Line Argument");
      }
    }
    ProcessorExtension processor = new ImageProcessorExtension();
    Controller controller = new ProcessorControllerExtension(processor, input, System.out);
    controller.execute();
  }


}
