package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test class for verifying the correctness of input received by the controller
 * from the user with file type command.
 */
@SuppressWarnings("resource")
public class ProcessorControllerTestRunFile extends AbstractProcessorControllerTest {
  private File testFile;

  @Before
  public void setUp() {
    testFile = new File("test/runFile/testFile.txt");
  }

  protected void writeInputFile(String input) throws IOException {
    FileWriter fw = new FileWriter(testFile, false);
    fw.write(input);
    fw.close();
  }

  @Test
  public void testFileRecursiveCallSelf() throws IOException {
    String fileInput = "run test/runFile/testFile.txt";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid File: "
            + "Recursive call of same file within file" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());

  }

  @Test
  public void testFileLoopCall() throws IOException {

    FileWriter fw = new FileWriter("test/runFile/recursiveRun.txt");
    fw.write("run test/runFile/testFile.txt");
    fw.close();
    String fileInput = "run test/runFile/recursiveRun.txt";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid File: "
            + "Recursive call of same file within file" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());

  }

  @Test
  public void testEmptyFile() throws IOException {
    String fileInput = "";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testInvalidCommand() throws IOException {
    String fileInput = "errorDir/Files/test.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: Command errorDir/Files/test.ppm" +
            " not supported"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }


  @Test
  public void testCommandWithHeadingSpace() throws IOException {
    String fileInput = "     load test/ExtensionFiles/test.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandWithHeadingNewLine() throws IOException {
    String fileInput = System.lineSeparator() + "load test/ExtensionFiles/test.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No Command Detected" + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadOnlyToken() throws IOException {
    String fileInput = "load test/Files/onlyToken.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File either missing " +
            "required field, contains text, or mismatch in given size" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadPixelGreaterThanMax() throws IOException {
    String fileInput = "load test/Files/pixelGreater.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: Pixel Value Greater " +
            "Than Maximum Value List By File Header" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadPixelLessThanMin() throws IOException {
    String fileInput = "load test/Files/pixelSmaller.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: Pixel Value Less " +
            "Than 0" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileWithText() throws IOException {
    String fileInput = "load test/Files/testImageWithText.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File either missing required " +
            "field, contains text, or mismatch in given size" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileEmptyPPM() throws IOException {
    String fileInput = "load test/Files/empty.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: Empty File" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadMaxValueGT255() throws IOException {
    String fileInput = "load test/Files/maxValueGreater.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: Invalid Maximum Pixel " +
            "Value" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadMaxValueLTZ() throws IOException {
    String fileInput = "load test/Files/maxValueLessThanZero.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: Invalid Maximum Pixel " +
            "Value" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadHeightValueLTZ() throws IOException {
    String fileInput = "load test/Files/heightLTZ.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: width or length is less or " +
            "equals to 0" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadWidthValueLTZ() throws IOException {
    String fileInput = "load test/Files/widthLTZ.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: width or length is less or " +
            "equals to 0" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileInvalidToken() throws IOException {
    String fileInput = "load test/Files/invalidToken.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: plain RAW file should " +
            "begin with P3" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadWidthHeightMismatch() throws IOException {
    String fileInput = "load test/Files/widthHeightMisMatchLong.ppm test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File length and given width " +
            "and height mismatch, data is longer than given width and height"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
    fileInput = "load test/Files/widthHeightMisMatchShort.ppm test";
    writeInputFile(fileInput);
    in = new ByteArrayInputStream(input.getBytes());
    out = new ByteArrayOutputStream();
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File either missing required field" +
            ", contains text, or mismatch in given size"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }


  @Test
  public void testCommandLoadSuccess() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension);
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testCommandLoadFileNotExist() throws IOException {
    String fileInput = "load invalid test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("File format not supported"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileFormatNotSupported() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.kkk test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("File format not supported" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileArgumentLengthLess() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.jpg.ppm";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command Format: The command should be in the format " +
            "of: load pathName destinationID" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandLoadFileArgumentLengthGreater() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.jpg.ppm k test 123 43534 213123";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command Format: The command should be in the format " +
            "of: load pathName destinationID" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandCombineValid() throws IOException {
    for (String from : fileExtension) {
      for (String to : fileExtension) {
        String fileInput = String.format("load test/ExtensionFiles/test.%s test", from)
                + System.lineSeparator()
                + String.format("load test/ExtensionFiles/test.%s test2", to)
                + System.lineSeparator()
                + String.format("load test/ExtensionFiles/test.%s test3", from)
                + System.lineSeparator()
                + "rgb-combine result test test2 test3";
        writeInputFile(fileInput);
        input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load") + System.lineSeparator()
                + successMap.get("load") + System.lineSeparator()
                + successMap.get("load") + System.lineSeparator()
                + successMap.get("combine") + System.lineSeparator()
                + successMap.get("run") + System.lineSeparator(), out.toString());
      }
    }
  }

  @Test
  public void testCommandCombineSourceNotFound() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "load test/ExtensionFiles/test.ppm test2" + System.lineSeparator()
            + "rgb-combine result test test2 test3";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + "Invalid Command: No source image found with image ID: test3"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandCombineParameterLonger() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "load test/ExtensionFiles/test.ppm test2" + System.lineSeparator()
            + "load test/ExtensionFiles/test.ppm test3" + System.lineSeparator()
            + "rgb-combine result test test2 test3 test4";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + errorMap.get("combine")
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandCombineParameterShorter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "load test/ExtensionFiles/test.ppm test2" + System.lineSeparator()
            + "load test/ExtensionFiles/test.ppm test3" + System.lineSeparator()
            + "rgb-combine result test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + successMap.get("load") + System.lineSeparator()
            + errorMap.get("combine")
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }


  @Test
  public void testCommandHorizontalFlipArgumentLengthLesser() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "horizontal-flip testTestFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("horizontal-flip") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandHorizontalFlipArgumentLengthGreater() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "horizontal-flip test testFlipped sa dwq qwe21 1";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("horizontal-flip") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandHorizontalFlipValid() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "horizontal-flip test testFlipped";
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("horizontal-flip") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testCommandHorizontalFlipSourceNotExist() throws IOException {
    String fileInput = "horizontal-flip temp testFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No source image found with image ID: " +
            "temp" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandVerticalFlipArgumentLengthLesser() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "vertical-flip testTestFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("vertical-flip") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandVerticalFlipArgumentLengthGreater() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "vertical-flip testTestFlipped 123 32431 21334 5dsf";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("vertical-flip") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandVerticalFlipNotExist() throws IOException {
    String fileInput = "vertical-flip temp testFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No source image found with image " +
            "ID: temp" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandVerticalFlipValid() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "vertical-flip test testFlipped";
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("vertical-flip") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testCommandBrightenDarkenPositiveValue() throws IOException {
    for (int i = 0; i < 2; i++) {
      for (String extension : fileExtension) {
        String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator()
                + String.format("brighten %d test testFlipped", i == 0 ? 10 : -10);
        writeInputFile(fileInput);
        input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load") + System.lineSeparator()
                + successMap.get("brighten") + System.lineSeparator()
                + successMap.get("run") + System.lineSeparator(), out.toString());
      }
    }

  }

  @Test
  public void testCommandBrightenDarkenNonNumerical() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "BRIGHTEN kk test testFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + "Invalid Command Format: pixel value should only contain numerical " +
            "character" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandBrightenDarkenShorterParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "BRIGHTEN kk testFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("brighten") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandBrightenDarkenLongerParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "BRIGHTEN kk testFlipped 2131 sd12";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("brighten") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandBrightenDarkenSourceNotFound() throws IOException {
    String fileInput = "BRIGHTEN 10 kk testFlipped";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No source image found with image ID: kk"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testSplitValid() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "rgb-split test testRed testGreen testBlue";
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("split") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testSplitLongerParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "rgb-split test testRed testGreen testBlue testRed";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("split") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testSplitShorterParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "rgb-split test testRed testGreen";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("split") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testSplitSourceNotFound() throws IOException {
    String fileInput = "rgb-split test testRed testGreen testBlue";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No source image found with image ID: test"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testGrayScaleInvalidOption() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "greyscale component test result";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator() +
            "Invalid Command: Invalid GreyScale Option" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testGrayScaleValid() throws IOException {
    for (String extension : fileExtension) {
      for (String x : grayScaleOptionList) {
        String fileInput = String.format("load test/ExtensionFiles/test.%s test"
                + System.lineSeparator()
                + "greyscale %s-component test result", extension, x);
        writeInputFile(fileInput);
        input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        String result = String.format(successMap.get("load") + System.lineSeparator()
                + "GrayScale %s-component Operation Success" + System.lineSeparator()
                + successMap.get("run") + System.lineSeparator(), x);
        assertEquals(result, out.toString());
      }
    }

  }

  @Test
  public void testGrayScaleSourceNotFound() throws IOException {
    for (String x : grayScaleOptionList) {
      String fileInput = String.format("greyscale %s-component test result", x);
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals("Invalid Command: No source image found with image ID: test"
              + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testGrayScaleLongerParameter() throws IOException {
    for (String x : grayScaleOptionList) {
      String fileInput = String.format("load test/ExtensionFiles/test.ppm test"
              + System.lineSeparator()
              + "greyscale %s-component test result yte12", x);
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + errorMap.get("greyscale")
              + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testGrayScaleComponentSourceShorterParameter() throws IOException {
    for (String x : grayScaleOptionList) {
      String fileInput = String.format("load test/ExtensionFiles/test.ppm test"
              + System.lineSeparator()
              + "greyscale %s-component", x);
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + errorMap.get("greyscale")
              + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testCommandSaveValid() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "save test/runFile/testRunSave.ppm test";
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("save") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testCommandSaveFileNotSupported() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "save test/runFile/testRunSave.kjg test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + "File format not supported" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandSaveFileIllegalFilePath() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "save ../test/runFile/testRunSave test";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + "File format not supported" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandSaveFileLongerParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test"
            + System.lineSeparator()
            + "save test/runFile/testRunSave test weqwdasweq 21312";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("save") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandSaveShorterParameter() throws IOException {
    String fileInput = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator()
            + "save test/runFile/testRunSave";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + errorMap.get("save") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandRunFile() throws IOException {
    String fileInput = "run test/runFile/recursiveRun.txt";
    writeInputFile(fileInput);
    FileWriter fw = new FileWriter("test/runFile/recursiveRun.txt", false);
    fw.write("exit");
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("run") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandRunFileNonText() throws IOException {
    String fileInput = "run test/Files/testCombine2x2.ppm";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: Command P3 not supported" + System.lineSeparator()
            + "Invalid Command: Command 2 not supported" + System.lineSeparator()
            + "Invalid Command: Command 10 not supported" + System.lineSeparator()
            + "Invalid Command: Command 1 not supported" + System.lineSeparator()
            + "Invalid Command: Command 1 not supported" + System.lineSeparator()
            + "Invalid Command: Command 1 not supported" + System.lineSeparator()
            + "Invalid Command: Command 2 not supported" + System.lineSeparator()
            + "Invalid Command: Command 2 not supported" + System.lineSeparator()
            + "Invalid Command: Command 2 not supported" + System.lineSeparator()
            + "Invalid Command: Command 3 not supported" + System.lineSeparator()
            + "Invalid Command: Command 3 not supported" + System.lineSeparator()
            + "Invalid Command: Command 3 not supported" + System.lineSeparator()
            + "Invalid Command: Command 4 not supported" + System.lineSeparator()
            + "Invalid Command: Command 4 not supported" + System.lineSeparator()
            + "Invalid Command: Command 4 not supported" + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandRunFileNotFound() throws IOException {
    String fileInput = "run test/runFile/nonExist.txt";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: File <test/runFile/nonExist.txt> does not exist"
            + System.lineSeparator()
            + successMap.get("run") + System.lineSeparator(), out.toString());
  }

  @Test
  public void testCommandRunTerminateWithinFile() throws IOException {
    String fileInput = "exit";
    writeInputFile(fileInput);
    input = "run test/runFile/testFile.txt" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", out.toString());
  }
}
