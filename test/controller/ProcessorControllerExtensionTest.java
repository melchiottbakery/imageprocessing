package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is a Junit test class for the processor controller extension class.
 */
public class ProcessorControllerExtensionTest extends AbstractProcessorControllerTest {
  private List<String> threeParamMethod;

  @Before
  public void setUp() {
    threeParamMethod = new LinkedList<>();
    setUpThreeParamMethodList();
  }

  private void setUpThreeParamMethodList() {
    threeParamMethod.add("blur");
    threeParamMethod.add("greyscale");
    threeParamMethod.add("dither");
    threeParamMethod.add("sharpen");
    threeParamMethod.add("sepia");
    threeParamMethod.add("vertical-flip");
    threeParamMethod.add("horizontal-flip");
  }

  @Test
  public void invalidNewLineCommend() {
    String input = System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No Command Detected"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void exitDirectly() {
    String input = "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", out.toString());
  }

  @Test
  public void exitDirectlyWithWhiteSpace() {
    String input = "       exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", out.toString());
  }


  @Test
  public void testInvalidCommandFormat() {
    for (String method : threeParamMethod) {
      String input = method + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(errorMap.get(method)
              + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void invalidNewWhiteSpaceCommend() {
    InputStream in;
    String input = " " + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    OutputStream out = new ByteArrayOutputStream();
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: No Command Detected"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void invalidCommend() {
    String input = "ILoveYou" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: Command ILoveYou not supported"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionInvalidImageHeightLessThanZero() {
    String input = "load test/Files/heightLTZ.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: width or length is less or " +
            "equals to 0"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionInvalidImageWidthHeightMisMatchDataLonger() {
    String input = "load test/Files/widthHeightMisMatchLong.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File length and given width and " +
            "height mismatch, data is longer than given width and height"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionInvalidImageWidthHeightMisMatchDataShorter() {
    String input = "load test/Files/widthHeightMisMatchShort.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File either missing required field" +
            ", contains text, or mismatch in given size"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionInvalidImageWithText() {
    String input = "load test/Files/testImageWithText.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid PPM File: File either missing required " +
            "field, contains text, or mismatch in given size"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionSaveFileDirectoryNotFound() {
    String input = "load test/ExtensionFiles/test.ppm testPpm" + System.lineSeparator() +
            "save test/Error/test.ppm testPpm"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator() +
            "Invalid Command: Invalid Path Given"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionSaveFileNoExtension() {
    String input = "load test/ExtensionFiles/test.ppm testPpm" + System.lineSeparator() +
            "save pts testPpm"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);

    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator() +
            "File format not supported"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void loadCommendFunctionSaveFileWrongExtension() {
    String input = "load test/ExtensionFiles/test.ppm test" + System.lineSeparator() +
            "save pts.txt test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);

    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator() +
            "File format not supported"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void testLoad() {
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s test", extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      out = new ByteArrayOutputStream();
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      assertEquals(successMap.get("load")
              + System.lineSeparator(), out.toString());
    }
  }

  @Test
  public void testThreeParamMethodValid() {
    for (String method : threeParamMethod) {
      for (String extension : fileExtension) {
        String input = String.format("load test/ExtensionFiles/test.%s test", extension) +
                System.lineSeparator() +
                String.format("%s test testMethod", method) + System.lineSeparator() + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        Controller controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load")
                        + System.lineSeparator()
                        + successMap.get(method)
                        + System.lineSeparator()
                , out.toString());
      }
    }
  }

  @Test
  public void testThreeParamMethodSourceNotFound() {
    for (String method : threeParamMethod) {
      for (String extension : fileExtension) {
        String input = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator() +
                String.format("%s testError test", method) + System.lineSeparator() + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        Controller controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load")
                        + System.lineSeparator() + "Invalid Command: No source image "
                        + "found with image ID: testError" + System.lineSeparator()
                , out.toString());
      }
    }
  }

  @Test
  public void fileExtensionTransferValidThreeParam() {
    for (String from : fileExtension) {
      for (String to : fileExtension) {
        for (String method : threeParamMethod) {
          String fromResult = from.substring(0, 1).toUpperCase() + from.substring(1);
          String toResult = to.substring(0, 1).toUpperCase() + to.substring(1);
          String methodResult = method.substring(0, 1).toUpperCase() + method.substring(1);
          String input = String.format("load test/ExtensionFiles/test.%s test", from)
                  + System.lineSeparator() +
                  String.format("%s test test", method) + System.lineSeparator()
                  + String.format("save test/ExtensionFiles/test%sTo%sAfter%s.%s test",
                  fromResult, toResult, methodResult, to)
                  + System.lineSeparator()
                  + String.format("load test/ExtensionFiles/test%sTo%sAfter%s.%s test", fromResult,
                  toResult, methodResult, to) + System.lineSeparator() + "exit";
          in = new ByteArrayInputStream(input.getBytes());
          out = new ByteArrayOutputStream();
          Controller controller = new ProcessorControllerExtension(processor, in, out);
          controller.execute();
          assertEquals(successMap.get("load") + System.lineSeparator()
                          + successMap.get(method) + System.lineSeparator()
                          + successMap.get("save") + System.lineSeparator()
                          + successMap.get("load") + System.lineSeparator()

                  , out.toString());
        }
      }
    }
  }

  @Test
  public void testBrightenDarken() {
    for (int i = 0; i < 2; i++) {
      for (String extension : fileExtension) {
        String input = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator()
                + String.format("brighten %d test test", i == 0 ? 10 : -10)
                + System.lineSeparator()
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        Controller controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load") + System.lineSeparator() +
                successMap.get("brighten") + System.lineSeparator(), out.toString());
      }
    }

  }

  @Test
  public void loadGrayScaleFunction() {
    for (String extension : fileExtension) {
      for (String x : grayScaleOptionList) {
        input = String.format("load test/ExtensionFiles/test.%s test", extension)
                + System.lineSeparator() + String.format("greyscale %s-component test result"
                + System.lineSeparator(), x.toUpperCase())
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        controller = new ProcessorController(processor, in, out);
        controller.execute();
        String result = String.format(successMap.get("load") + System.lineSeparator()
                + "GrayScale %s-component Operation Success" + System.lineSeparator(), x);
        assertEquals(result, out.toString());
      }
    }
  }

  @Test
  public void testSplit() {
    for (String from : fileExtension) {
      for (String to : fileExtension) {

        String input = String.format("load test/ExtensionFiles/test.%s test", from)
                + System.lineSeparator() +
                "rgb-split test test-red test-green test-blue" + System.lineSeparator()
                + String.format("save test/ExtensionFiles/test%sTo%sAfterSplit.%s test",
                from.substring(0, 1).toUpperCase() + from.substring(1),
                to.substring(0, 1).toUpperCase() + to.substring(1),
                to) + System.lineSeparator() +
                "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        Controller controller = new ProcessorController(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load") + System.lineSeparator()
                + "Split Operation Success" + System.lineSeparator()
                + "Save Operation Success" + System.lineSeparator(), out.toString());
      }
    }

  }

  @Test
  public void testReform() {
    for (String from : fileExtension) {
      for (String to : fileExtension) {
        String input = String.format("load test/ExtensionFiles/test.%s test", from)
                + System.lineSeparator()
                + "rgb-split test test-red test-green test-blue " + System.lineSeparator()
                + "rgb-combine testResult test-red test-green test-blue " + System.lineSeparator()
                + String.format("save test/ExtensionFiles/test%sTo%sAfterCombine.%s test",
                from.substring(0, 1).toUpperCase() + from.substring(1)
                , to.substring(0, 1).toUpperCase() + to.substring(1)
                , to) + System.lineSeparator()
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        out = new ByteArrayOutputStream();
        Controller controller = new ProcessorController(processor, in, out);
        controller.execute();
        assertEquals(successMap.get("load") + System.lineSeparator()
                + "Split Operation Success" + System.lineSeparator()
                + "Combine Operation Success" + System.lineSeparator()
                + "Save Operation Success" + System.lineSeparator(), out.toString());
      }
    }
  }

  @Test
  public void brightenCommandFailedWithNonNumber() {
    String input = "load res/winnie.ppm winnie" + System.lineSeparator() +
            "brighten XBB winnie Winnie-brighter " + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator() +
            "Invalid Command Format: pixel value should " +
            "only contain numerical character" + System.lineSeparator(), out.toString());
  }

  @Test
  public void invalidRunCommandFileNotFound() {
    String input = "run testError" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command: File <testError> does not exist"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void invalidRunCommandFormat() {
    String input = "run " + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("Invalid Command Format: The command" +
            " should be in the format of: run pathName"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void saveCommendFunctionFailedNotFound() {
    String input = "load test/Files/testColorImage.ppm test" + System.lineSeparator() +
            "save test/Files/testSave.ppm test1 " + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + "Invalid Command: No source image found with image ID: test1"
            + System.lineSeparator(), out.toString());
  }

  @Test
  public void testGrayScaleFunctionFailedOption() {
    input = "load test/Files/testGrayScaleImage.ppm test" + System.lineSeparator()
            + "greyscale s-component test result" + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    out = new ByteArrayOutputStream();
    controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    String result = successMap.get("load") + System.lineSeparator()
            + "Invalid Command: Invalid GreyScale Option" + System.lineSeparator();
    assertEquals(result, out.toString());

  }

  @Test
  public void saveCommendFunctionFailedFormatNotSupported() {
    String input = "load test/Files/testColorImage.ppm test" + System.lineSeparator() +
            "save test/Files/testSave.kkk test " + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals(successMap.get("load") + System.lineSeparator()
            + "File format not supported"
            + System.lineSeparator(), out.toString());
  }

}