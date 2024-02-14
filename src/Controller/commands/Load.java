package controller.commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import model.IllegalFileException;
import model.Processor;

/**
 * This is a command object to execute the Load command.
 */
public class Load extends AbstractCommand {
  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public Load(String[] command) {
    super(command);
    if (command.length != 3) {
      throw new IllegalArgumentException("Invalid Command Format: The command"
              + " should be in the format of: load pathName destinationID");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    String extension = getFileExtension(command[1]);
    if (!checkValid(print, extension)) {
      return;
    }
    if (extension.equals("ppm")) {
      readPPM(command[2], command[1], print, processor);
    } else {
      readImageFile(command[2], command[1], print, processor);
    }

  }

  /**
   * Helper method utilized to convert a bufferedImage representation of image data into a 3d array
   * image data.
   *
   * @param image the image to convert
   * @return a 3d array representation of the source image
   * @throws FileNotFoundException exception will be thrown if the file is not found.
   */
  protected int[][][] imageToArray(BufferedImage image) throws FileNotFoundException {
    if (image == null) {
      throw new FileNotFoundException("Invalid File Path");
    }
    int col = image.getWidth();
    int row = image.getHeight();
    int[][][] data = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        Color color = new Color(image.getRGB(j, i));
        data[0][i][j] = color.getRed();
        data[1][i][j] = color.getGreen();
        data[2][i][j] = color.getBlue();
      }
    }
    return data;
  }

  /**
   * Helper method utilized to read an image file.
   * @param id The id of the image
   * @param address The address of the file
   * @param print The output stream to print the success/error message
   * @param processor The processor where the image is stored
   * @throws IllegalFileException Exception will be thrown if the file is illegal
   */
  protected final void readImageFile(String id, String address, OutputStream print,
                                     Processor processor) throws IllegalFileException {
    try {
      try {
        int[][][] data = imageToArray(ImageIO.read(new File(address)));
        processor.storeMap(id, data, 255);
        print.write(("Load Operation Success" + System.lineSeparator()).getBytes());
      } catch (FileNotFoundException e) {
        print.write(String.format("Invalid Command: File Not Found%n").getBytes());
      } catch (IOException e) {
        print.write("Exception During ReadTime".getBytes());
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Helper method utilized to build the string builder which consist of all information of a file.
   *
   * @param sc The scanner object which the file is stored
   * @return The string builder where the result will append to.
   */
  private StringBuilder formBuilder(Scanner sc) {
    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.length() == 0) {
        throw new IllegalFileException("Invalid PPM File: plain RAW file should " +
                "not contain empty line" + System.lineSeparator());
      }
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder;
  }

  /**
   * Helper method for reading a ppm image.
   * @param id The id of the image
   * @param address the address of the file
   * @param print The output stream that writes the success/error message
   * @param processor the processor object where the image will be stored
   * @throws IllegalFileException Exception will be thrown if the ppm file is invalid
   * @throws IOException Exception will throw if there's error in reading.
   */
  protected final void readPPM(String id, String address, OutputStream print, Processor processor)
          throws IllegalFileException, IOException {
    try {
      Scanner sc = new Scanner(new File(address));
      StringBuilder builder = null;
      try {
        builder = formBuilder(sc);
      } catch (IllegalArgumentException e) {
        print.write(e.getMessage().getBytes());
        return;
      }
      sc = new Scanner(builder.toString());
      if (!sc.hasNext()) {
        print.write(("Invalid PPM File: Empty File" + System.lineSeparator()).getBytes());
        return;
      }
      String token;
      token = sc.next();
      if (!token.equals("P3")) {
        print.write(("Invalid PPM File: plain RAW file should begin with P3"
                + System.lineSeparator()).getBytes());
        return;
      }
      try {
        generatePPM(sc, id, processor);
        print.write(("Load Operation Success" + System.lineSeparator()).getBytes());
        sc.close();
      } catch (NoSuchElementException exception) {
        print.write(("Invalid PPM File: File either missing required field, " +
                "contains text, or mismatch in given size" + System.lineSeparator()).getBytes());
      } catch (IllegalFileException e) {
        print.write(e.getMessage().getBytes());
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }


  }

  /**
   * This is a private helper method utilized to generated ppm pixel data.
   *
   * @param sc The scanner what contained the file data from an input stream
   * @param id The id of the image.
   */
  private void generatePPM(Scanner sc, String id, Processor processor) {
    int width = sc.nextInt();
    int height = sc.nextInt();
    if (width <= 0 || height <= 0) {
      throw new IllegalFileException("Invalid PPM File: width or length is less or equals to 0"
              + System.lineSeparator());
    }
    int max = sc.nextInt();
    if (max > 255 || max < 0) {
      throw new IllegalFileException("Invalid PPM File: Invalid Maximum Pixel Value"
              + System.lineSeparator());
    }
    int[][][] data = new int[3][width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        if (r > max || g > max || b > max) {
          throw new IllegalFileException("Invalid PPM File: Pixel Value Greater Than Maximum " +
                  "Value List By File Header" + System.lineSeparator());
        }
        if (r < 0 || g < 0 || b < 0) {
          throw new IllegalFileException("Invalid PPM File: Pixel Value Less Than 0"
                  + System.lineSeparator());
        }
        data[0][i][j] = r;
        data[1][i][j] = g;
        data[2][i][j] = b;
      }
    }
    if (sc.hasNextInt()) {
      throw new IllegalFileException(("Invalid PPM File: File length and given width and " +
              "height mismatch, data is longer than given width and height"
              + System.lineSeparator()));
    }
    processor.storeMap(id, data, max);
  }


}
