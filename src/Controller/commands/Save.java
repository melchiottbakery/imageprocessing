package controller.commands;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import model.Processor;

/**
 * This is a command object to execute the Save command.
 */
public class Save extends AbstractCommand {
  /**
   * Constructor for the object, takes in the command and check the parameter length.
   *
   * @param command The command to execute
   * @throws IllegalArgumentException Exception will be thrown if the command mismatch in parameter
   *                                  length
   */
  public Save(String[] command) {
    super(command);
    if (command.length != 3) {
      throw new IllegalArgumentException("Invalid Command Format: The command should "
              + "be in the format of: save destinationAddress sourceID");
    }
  }

  @Override
  public void executeCommand(Processor processor, OutputStream print) throws IOException {
    try {
      String extension = getFileExtension(command[1]);
      if (!checkValid(print, extension)) {
        return;
      }
      if (extension.equals("ppm")) {
        ppmWriterHelper(new FileOutputStream(command[1]), command[2], processor);
      } else {
        imageWriterHelper(new FileOutputStream(command[1]), command[2], extension, processor);
      }
      print.write(("Save Operation Success" + System.lineSeparator()).getBytes());
    } catch (IllegalArgumentException e) {
      print.write(String.format("Invalid Command: %s%n", e.getMessage()).getBytes());
    } catch (RuntimeException e) {
      print.write(e.getMessage().getBytes());
    }
  }

  /**
   * Helper method for writing an image.
   * @param output The output stream to write to
   * @param id The id of the image
   * @param extension The image extension
   * @param processor The processor where the data is stored.
   */
  protected final void imageWriterHelper(OutputStream output, String id, String extension
          , Processor processor) {
    try {
      ImageIO.write(arrayToImage(processor.getImage(id)), extension, output);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Helper method utilized to convert a 3d array representation of image data into a buffered
   * image data.
   *
   * @param imageData the image data
   * @return A bufferedImage object that represents such image data
   */
  protected BufferedImage arrayToImage(int[][][] imageData) {
    int row = imageData[0].length;
    int col = imageData[0][0].length;
    BufferedImage image = new BufferedImage(col, row, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        Color color = new Color(imageData[0][i][j], imageData[1][i][j], imageData[2][i][j]);
        image.setRGB(j, i, color.getRGB());
      }
    }
    return image;
  }


  /**
   * Method utilized to write a ppm image.
   * @param output The output stream where the success/error message is written
   * @param id The id of the image
   * @param processor The processor object where all image data is stored
   * @throws RuntimeException Exception will be thrown if there's error on the data.
   */
  protected final void ppmWriterHelper(OutputStream output, String id
          , Processor processor) throws RuntimeException {
    try {
      int[][][] data = processor.getImage(id);
      output.write(("P3" + System.lineSeparator()).getBytes());
      output.write(String.format("%s %s" + System.lineSeparator(), data[0].length,
              data[0][0].length).getBytes());
      output.write(String.format("%s" + System.lineSeparator(), processor.getMaxValue(id))
              .getBytes());
      for (int i = 0; i < data[0].length; i++) {
        for (int j = 0; j < data[0][0].length; j++) {
          for (int k = 0; k < 3; k++) {
            if (i == data[0].length - 1 && j == data[0][0].length - 1 && k == 2) {
              output.write(String.format("%s", data[k][i][j]).getBytes());
              continue;
            }
            output.write(String.format("%s" + System.lineSeparator(), data[k][i][j]).getBytes());
          }
        }
      }
      output.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
