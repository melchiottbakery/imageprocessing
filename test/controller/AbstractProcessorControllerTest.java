package controller;

import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.ImageProcessorExtension;
import model.ProcessorExtension;

/**
 * This is a abstract controller test class that initialize all common variables.
 */
public abstract class AbstractProcessorControllerTest {
  protected List<String> grayScaleOptionList;
  protected InputStream in;
  protected List<String> fileExtension;
  protected ProcessorExtension processor;
  protected OutputStream out;
  protected Controller controller;
  protected String input;
  protected Map<String, String> errorMap;
  protected Map<String, String> successMap;

  @Before
  public void setUpCommonVariable() {
    processor = new ImageProcessorExtension();
    out = new ByteArrayOutputStream();
    fileExtension = new LinkedList<>();
    grayScaleOptionList = new LinkedList<>();
    successMap = new HashMap<>();
    errorMap = new HashMap<>();
    setUpGreyScaleOption();
    setUpExtension();
    setUpSuccessMap();
    setUpErrorMap();
  }

  private void setUpSuccessMap() {
    successMap.put("blur", "Blur Operation Success");
    successMap.put("load", "Load Operation Success");
    successMap.put("sharpen", "Sharpen Operation Success");
    successMap.put("sepia", "Color transformation sepia Operation Success");
    successMap.put("dither", "Dither Operation Success");
    successMap.put("greyscale", "Color Transformation GrayScale Success");
    successMap.put("vertical-flip", "Vertical-Flip Operation Success");
    successMap.put("horizontal-flip", "Horizontal-Flip Operation Success");
    successMap.put("split", "Split Operation Success");
    successMap.put("combine", "Combine Operation Success");
    successMap.put("save", "Save Operation Success");
    successMap.put("brighten", "Brighten Operation Success");
    successMap.put("run", "File Execution Complete");
  }

  private void setUpExtension() {
    fileExtension.add("ppm");
    fileExtension.add("png");
    fileExtension.add("jpg");
    fileExtension.add("jpeg");
    fileExtension.add("bmp");
  }

  private void setUpGreyScaleOption() {
    grayScaleOptionList.add("red");
    grayScaleOptionList.add("green");
    grayScaleOptionList.add("blue");
    grayScaleOptionList.add("luma");
    grayScaleOptionList.add("intensity");
    grayScaleOptionList.add("value");
  }

  private void setUpErrorMap() {
    errorMap.put("blur", "Invalid Command Format: The command should be in the format of: "
            + "filter sourceID destinationID");
    errorMap.put("sharpen", "Invalid Command Format: The command should be in the format of: "
            + "filter sourceID destinationID");
    errorMap.put("sepia", "Invalid Color Transformation Format: The command should be in the "
            + "format of: method sourceID destinationID");
    errorMap.put("greyscale", "Invalid Command Format: The command should be in the format of: "
            + "greyscale option sourceID destinationID or for color transformation: greyscale "
            + "sourceID destinationID");
    errorMap.put("dither", "Invalid Dithering Format: The command should be in the format of: "
            + "dither sourceID destinationID");
    errorMap.put("horizontal-flip", "Invalid Command Format: The command"
            + " should be in the format of: horizontal-flip sourceID destinationID");
    errorMap.put("vertical-flip", "Invalid Command Format: The command"
            + " should be in the format of: vertical-flip sourceID destinationID");
    errorMap.put("split", "Invalid Command Format: The command should "
            + "be in the format of: rbg-split sourceImageID destinationImageIDOne " +
            "destinationImageIDTwo destinationImageIDThree");
    errorMap.put("load", "Invalid Command Format: The command"
            + " should be in the format of: load pathName destinationID");
    errorMap.put("combine", "Invalid Command Format: The command should "
            + "be in the format of: rbg-combine destinationID sourceOne sourceTwo sourceThree");
    errorMap.put("brighten", "Invalid Command Format: The command should"
            + " be in the format of: brighten pixelValue sourceImageID destinationImageID");
    errorMap.put("save", "Invalid Command Format: The command should "
            + "be in the format of: save destinationAddress sourceID");
  }
}
