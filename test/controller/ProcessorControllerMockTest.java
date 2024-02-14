package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

import model.ProcessorExtension;

import static org.junit.Assert.assertEquals;

/**
 * This is JUnit test clas for testing the correctness of input received by the processor from
 * the user.
 */
public class ProcessorControllerMockTest extends AbstractProcessorControllerTest {

  StringBuilder log;

  @Before
  public void setUp() {
    log = new StringBuilder();
    processor = new MockModel(log);
  }

  @Test
  public void loadCommendFunctionSuccessful() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s winnieWith%s"
              , extension, extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = winnieWith%s, MaxVal = 255", extension)
              + System.lineSeparator());
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void loadCommendFunctionSuccessfulWithWhiteSpace() {
    String input = "            load res/winnie.ppm winnie"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("LoadFunction Input: ID = winnie, MaxVal = 255"
            + System.lineSeparator(), log.toString());
  }

  @Test
  public void loadCommendFunctionFailedNotFound() {
    String input = "load test/winnie.ppm winnie"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", log.toString());
  }

  @Test
  public void loadCommendFunctionInvalidFileFormatExtension() {
    String input = "load test/testFiles/test.kkk winnie"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", log.toString());
  }

  @Test
  public void loadCommendFunctionInvalidEmptyFileOnlyToken() {
    String input = "load test/testFiles/onlyToken.ppm winnie"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", log.toString());
  }

  @Test
  public void loadCommendFunctionInvalidEmptyFile() {
    String input = "load test/testFiles/empty.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", log.toString());
  }

  @Test
  public void loadCommendFunctionInvalidToken() {
    String input = "load test/testFiles/invalidToken.ppm test"
            + System.lineSeparator() + "exit";
    in = new ByteArrayInputStream(input.getBytes());
    Controller controller = new ProcessorControllerExtension(processor, in, out);
    controller.execute();
    assertEquals("", log.toString());
  }

  @Test
  public void brighten() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s winnieWith%s"
              , extension, extension)
              + System.lineSeparator() + String.format("brighten 50 winnieWith%s winnie-brighter "
              , extension)
              + System.lineSeparator() + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = winnieWith%s, MaxVal = 255"
              + System.lineSeparator()
              + String.format("BrightenFunction Input: pixel: 50 source: winnieWith%s "
              + "dest: winnie-brighter", extension)
              + System.lineSeparator(), extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void testSplit() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s winnieWith%s"
              , extension, extension)
              + System.lineSeparator()
              + String.format("rgb-split winnieWith%s winnie-red winnie-green winnie-blue "
              + System.lineSeparator(), extension)
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = winnieWith%s, MaxVal = 255"
              + System.lineSeparator()
              + String.format("SplitFunction Input: source: winnieWith%s childImage: ", extension)
              + "winnie-red winnie-green winnie-blue"
              + System.lineSeparator(), extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void combineCommendFunctionSuccessful() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s winnieWith%s"
              , extension, extension)
              + System.lineSeparator()
              + String.format("rgb-split winnieWith%s winnie-red winnie-green winnie-blue "
              + System.lineSeparator(), extension)
              + String.format("rgb-combine winnieWith%s winnie-red winnie-green winnie-blue "
              + System.lineSeparator(), extension)
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = winnieWith%s, MaxVal = 255"
                      + System.lineSeparator()
                      + "SplitFunction Input: source: winnieWith%s childImage: winnie-red "
                      + "winnie-green " + "winnie-blue" + System.lineSeparator()
                      + "CombineFunction Input: dest: winnieWith%s redImage: winnie-red greenImage:"
                      + " winnie-green " + "blueImage: winnie-blue" + System.lineSeparator()
              , extension, extension, extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void saveCommendFunctionSuccessful() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s testWith%s"
              , extension, extension)
              + System.lineSeparator()
              + String.format("save test/ExtensionFiles/testSave.ppm testWith%s "
              + System.lineSeparator() + "exit", extension);
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
              + System.lineSeparator()
              + "GetImage Input: ID = testWith%s"
              + System.lineSeparator(), extension, extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void verticalFlipCommendFunctionSuccessful() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s testWith%s"
              , extension, extension)
              + System.lineSeparator()
              + "vertical-flip test test-vertical " + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = testWith%s, "
              + "MaxVal = 255"
              + System.lineSeparator()
              + "FlipFunction Input: horizontal: false target: test dest: test-vertical"
              + System.lineSeparator(), extension, extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void horizontalFlipCommendFunctionSuccessful() {
    StringBuilder result = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s testWith%s"
              , extension, extension)
              + System.lineSeparator()
              + "horizontal-flip test test-horizontal " + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      result.append(String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
              + System.lineSeparator()
              + "FlipFunction Input: horizontal: true target: test dest: test-horizontal"
              + System.lineSeparator(), extension));
    }
    assertEquals(result.toString(), log.toString());
  }

  @Test
  public void testGrayScaleFunctionSuccessful() {
    for (String extension : fileExtension) {
      for (String x : grayScaleOptionList) {
        String fileInput = String.format("greyscale %s-component testWith%s result"
                + System.lineSeparator(), x, extension);
        input = String.format("load test/ExtensionFiles/test.%s testWith%s"
                + System.lineSeparator()
                + fileInput
                + "exit", extension, extension);
        in = new ByteArrayInputStream(input.getBytes());
        log = new StringBuilder();
        processor = new MockModel(log);
        Controller controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        String result = String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
                + System.lineSeparator()
                + "GrayScaleFunction Input: option: %s-component source: testWith%s dest: result"
                + System.lineSeparator(), extension, x, extension);
        assertEquals(result, log.toString());
      }
    }
  }

  @Test
  public void testColorTrans() {
    StringBuilder builder = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s testWith%s", extension
              , extension)
              + System.lineSeparator()
              + "sepia test test" + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      builder.append(String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
                      + System.lineSeparator()
                      + "Input: Method = %s, Source = %s, Dest = %s" + System.lineSeparator(),
              extension, "sepia", "test", "test"));
    }
    assertEquals(log.toString(), builder.toString());
  }

  @Test
  public void testFilter() {
    StringBuilder builder = new StringBuilder();
    List<String> filterList = new LinkedList<>();
    filterList.add("blur");
    filterList.add("sharpen");
    for (String extension : fileExtension) {
      for (String filter : filterList) {
        String input = String.format("load test/ExtensionFiles/test.%s testWith%s",
                extension, extension)
                + System.lineSeparator()
                + String.format("%s test test", filter) + System.lineSeparator()
                + "exit";
        in = new ByteArrayInputStream(input.getBytes());
        Controller controller = new ProcessorControllerExtension(processor, in, out);
        controller.execute();
        builder.append(String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
                        + System.lineSeparator()
                        + "Input: Method = %s, Source = %s, Dest = %s" + System.lineSeparator()
                , extension, filter, "test", "test"));
      }
    }
    assertEquals(log.toString(), builder.toString());
  }

  @Test
  public void testDither() {
    StringBuilder builder = new StringBuilder();
    for (String extension : fileExtension) {
      String input = String.format("load test/ExtensionFiles/test.%s testWith%s", extension
              , extension)
              + System.lineSeparator()
              + "dither test test" + System.lineSeparator()
              + "exit";
      in = new ByteArrayInputStream(input.getBytes());
      Controller controller = new ProcessorControllerExtension(processor, in, out);
      controller.execute();
      builder.append(String.format("LoadFunction Input: ID = testWith%s, MaxVal = 255"
                      + System.lineSeparator()
                      + "Input: Source = %s, Dest = %s" + System.lineSeparator(),
              extension, "test", "test"));
    }
    assertEquals(log.toString(), builder.toString());
  }

  /**
   * This is a Mock class utilized to represent the model.
   */
  public static class MockModel implements ProcessorExtension {

    private final StringBuilder log;

    /**
     * Constructor for the mock model.
     *
     * @param log Initialize the string builder utilized for comparison.
     */
    public MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void flipImage(boolean horizontal, String target, String dest)
            throws IllegalArgumentException {
      log.append(String.format("FlipFunction Input: horizontal: %s target: %s dest: %s%s",
              horizontal, target, dest, System.lineSeparator()));
    }

    @Override
    public void toGrayScale(String option, String source, String dest)
            throws IllegalArgumentException {
      log.append(String.format("GrayScaleFunction Input: option: %s source: %s dest: %s%s",
              option, source, dest, System.lineSeparator()));
    }

    @Override
    public void brightenDarkenImage(int pixel, String source, String dest)
            throws IllegalArgumentException {

      log.append(String.format("BrightenFunction Input: pixel: %d source: %s dest: %s%s",
              pixel, source, dest, System.lineSeparator()));
    }

    @Override
    public void split(String source, List<String> childImage) throws IllegalArgumentException {
      log.append(String.format("SplitFunction Input: source: %s childImage: %s %s %s%s", source,
              childImage.get(0), childImage.get(1), childImage.get(2), System.lineSeparator()));
    }

    @Override
    public void combine(String dest, String red, String green, String blue)
            throws IllegalArgumentException {

      log.append(String.format("CombineFunction Input: dest: %s redImage: %s greenImage: " +
              "%s blueImage: %s%s", dest, red, green, blue, System.lineSeparator()));
    }

    @Override
    public int[][][] getImage(String id) throws IllegalArgumentException {
      log.append(String.format("GetImage Input: ID = %s"
              + System.lineSeparator(), id));
      return null;
    }

    @Override
    public int getMaxValue(String id) throws IllegalArgumentException {
      log.append(String.format("getMaxValue Input: ID = %s"
              + System.lineSeparator(), id));
      return 0;
    }

    @Override
    public void storeMap(String id, int[][][] data, int maxVal) {
      log.append(String.format("LoadFunction Input: ID = %s, MaxVal = %d"
              + System.lineSeparator(), id, maxVal));
    }

    @Override
    public void colorTransformation(String method, String source, String dest) {
      log.append(String.format("Input: Method = %s, Source = %s, Dest = %s"
              + System.lineSeparator(), method, source, dest));
    }

    @Override
    public void filter(String method, String source, String dest) {
      log.append(String.format("Input: Method = %s, Source = %s, Dest = %s"
              + System.lineSeparator(), method, source, dest));
    }

    @Override
    public void dither(String source, String dest) {
      log.append(String.format("Input: Source = %s, Dest = %s"
              + System.lineSeparator(), source, dest));
    }
  }

}