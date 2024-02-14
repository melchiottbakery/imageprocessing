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
 * from the user with both cmd type and file type command.
 */
public class ProcessorControllerTestHybrid extends AbstractProcessorControllerTest {
  private File testFile;

  @Before
  public void setUp() {
    testFile = new File("test/runFile/testFile.txt");
  }

  private void writeInputFile(String input) throws IOException {
    FileWriter fw = new FileWriter(testFile, false);
    fw.write(input);
    fw.close();
  }

  @Test
  public void testFlipHorizontalTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      StringBuilder sb = new StringBuilder();
      sb.append(("horizontal-flip test result" + System.lineSeparator()).repeat(10000));
      String fileInput = sb + "exit" + System.lineSeparator();

      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator() +
              String.format("load test/ExtensionFiles/test.%s test", extension) +
              "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      sb = new StringBuilder();
      sb.append(successMap.get("load")).append(System.lineSeparator());
      sb.append(("Horizontal-Flip Operation Success" + System.lineSeparator()).repeat(10000));
      assertEquals(sb.toString(), out.toString());
    }
  }

  @Test
  public void testFlipHorizontalHandOverControl() throws IOException {
    for (String extension : fileExtension) {
      StringBuilder sb = new StringBuilder();
      sb.append(("horizontal-flip test result" + System.lineSeparator()).repeat(10000));
      String fileInput = sb.toString();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      sb = new StringBuilder();
      sb.append(successMap.get("load")).append(System.lineSeparator());
      sb.append((successMap.get("horizontal-flip") + System.lineSeparator()).repeat(10000));
      sb.append(successMap.get("run")).append(System.lineSeparator());
      sb.append(successMap.get("load")).append(System.lineSeparator());
      assertEquals(sb.toString(), out.toString());
    }

  }

  @Test
  public void testFlipVerticalTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "vertical-flip test result" + System.lineSeparator()
              + "exit" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator() +
              String.format("load test/ExtensionFiles/test.%s test", extension) +
              "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("vertical-flip") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testFlipVerticalHandOverControl() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "vertical-flip test result" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("vertical-flip") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testCombineTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "rgb-combine test test test2 test3" + System.lineSeparator()
              + "exit" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test2", extension)
              + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test3", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("combine") + System.lineSeparator(), out.toString());
    }

  }

  @Test
  public void testCombineTerminateHandOverControl() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "rgb-combine test test test2 test3" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test2", extension)
              + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test3", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator() +
              "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("combine") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testRunTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      FileWriter fw = new FileWriter("test/runFile/recursiveRun.txt", false);
      fw.write("exit");
      fw.close();
      String fileInput = "run test/runFile/recursiveRun.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension);
      writeInputFile(fileInput);
      input = "run test/runFile/testFile.txt" + System.lineSeparator() +
              String.format("load test/ExtensionFiles/test.%s test", extension) +
              "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals("", out.toString());
    }
  }

  @Test
  public void testRunHandOver() throws IOException {
    for (String extension : fileExtension) {
      FileWriter fw = new FileWriter("test/runFile/recursiveRun.txt", false);
      fw.write(String.format("load test/ExtensionFiles/test.%s test", extension));
      fw.close();
      String fileInput = "run test/runFile/recursiveRun.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension);
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testSplitTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "rgb-split test test test2 test3" + System.lineSeparator()
              + "exit" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt"
              + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("split") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testSplitHandOver() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "rgb-split test test test2 test3" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("split") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testBrightenWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "brighten 10 test result" + System.lineSeparator()
              + "exit" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension) +
              "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("brighten") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testBrightenHandOver() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "brighten 10 test result" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("brighten") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testGrayScaleTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      for (String x : grayScaleOptionList) {
        String fileInput = String.format("greyscale %s-component test result"
                + System.lineSeparator()
                + "exit" + System.lineSeparator(), x);
        writeInputFile(fileInput);
        input = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator()
                + "run test/runFile/testFile.txt" + System.lineSeparator()
                + String.format("load test/ExtensionFiles/test.%s test", extension)
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        String result = String.format(successMap.get("load") + System.lineSeparator()
                + "GrayScale %s-component Operation Success" + System.lineSeparator(), x);
        assertEquals(result, out.toString());
      }
    }
  }

  @Test
  public void testGrayScaleHandOver() throws IOException {
    for (String extension : fileExtension) {
      for (String x : grayScaleOptionList) {
        String fileInput = String.format("greyscale %s-component test result" +
                System.lineSeparator(), x);
        writeInputFile(fileInput);
        input = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator()
                + "run test/runFile/testFile.txt" + System.lineSeparator()
                + String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator()
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        String result = String.format(successMap.get("load") + System.lineSeparator()
                + "GrayScale %s-component Operation Success" + System.lineSeparator()
                + successMap.get("run") + System.lineSeparator()
                + successMap.get("load") + System.lineSeparator(), x);
        assertEquals(result, out.toString());
      }
    }
  }

  @Test
  public void testSaveTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "save test/ExtensionFiles/test.ppm test" + System.lineSeparator()
              + "exit" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension);
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("save") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testSaveHandOver() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = "save test/ExtensionFiles/test.ppm test" + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("save") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testLoadTerminateWithinFile() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "exit" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension);
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testLoadFileHandOverControl() throws IOException {
    for (String extension : fileExtension) {
      String fileInput = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator();
      writeInputFile(fileInput);
      input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator()
              + "run test/runFile/testFile.txt" + System.lineSeparator()
              + String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator()
              + successMap.get("run") + System.lineSeparator()
              + successMap.get("load") + System.lineSeparator(), out.toString());
    }
  }
}
