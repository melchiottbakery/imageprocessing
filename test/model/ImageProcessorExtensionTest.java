package model;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * This is JUnite test class for the Image Processor Extension.
 */
public class ImageProcessorExtensionTest extends ImageProcessorTest {

  @Test
  public void testSepia() {
    processor.storeMap("test", data, 255);
    processor.colorTransformation("sepia", "test", "testResult");
    data = new int[][][]{
            {{90, 73, 94}, {68, 52, 44}, {95, 46, 36}},
            {{80, 65, 84}, {60, 46, 39}, {85, 41, 32}},
            {{62, 50, 65}, {47, 36, 31}, {66, 32, 25}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }

  }

  @Test
  public void testGreyScaleColorTrans() {
    processor.storeMap("test", data, 255);
    processor.colorTransformation("greyscale", "test", "testResult");
    data = new int[][][]{
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}},
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}},
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }

  @Test
  public void testDither() {
    processor.storeMap("test", data, 255);
    processor.dither("test", "testResult");
    data = new int[][][]{
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }

  @Test
  public void testBlur() {
    processor.storeMap("test", data, 255);
    processor.filter("blur", "test", "testResult");
    data = new int[][][]{
            {{96, 114, 76}, {105, 118, 71}, {61, 60, 27}},
            {{1, 4, 8}, {4, 5, 6}, {8, 6, 4}},
            {{21, 27, 25}, {43, 49, 41}, {57, 67, 49}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }

  @Test
  public void testSharpen() {
    processor.storeMap("test", data, 255);
    processor.filter("sharpen", "test", "testResult");
    data = new int[][][]{
            {{255, 255, 179}, {255, 255, 169}, {119, 125, 0}},
            {{0, 3, 27}, {3, 21, 8}, {26, 8, 8}},
            {{12, 33, 28}, {92, 157, 88}, {157, 184, 122}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }


}