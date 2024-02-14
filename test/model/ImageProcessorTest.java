package model;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * This is a Junit test class for Model.ImageProcessor class.
 */
public class ImageProcessorTest {

  protected ProcessorExtension processor;
  protected Map<String, int[][][]> greyScaleOptionResult;
  protected int[][][] data;

  @Before
  public void setUp() {
    processor = new ImageProcessorExtension();
    greyScaleOptionResult = new HashMap<>();
    data = new int[][][]{
            {{190, 169, 142}, {160, 133, 100}, {104, 62, 0}},
            {{6, 0, 33}, {0, 0, 0}, {32, 0, 16}},
            {{58, 37, 72}, {28, 1, 30}, {160, 118, 129}}
    };
    setUpGreyScaleResult();
  }

  private void setUpGreyScaleResult() {
    greyScaleOptionResult.put("red-component", new int[][][]{
            {{190, 169, 142}, {160, 133, 100}, {104, 62, 0}},
            {{190, 169, 142}, {160, 133, 100}, {104, 62, 0}},
            {{190, 169, 142}, {160, 133, 100}, {104, 62, 0}},
    });
    greyScaleOptionResult.put("green-component", new int[][][]{
            {{6, 0, 33}, {0, 0, 0}, {32, 0, 16}},
            {{6, 0, 33}, {0, 0, 0}, {32, 0, 16}},
            {{6, 0, 33}, {0, 0, 0}, {32, 0, 16}},
    });
    greyScaleOptionResult.put("blue-component", new int[][][]{
            {{58, 37, 72}, {28, 1, 30}, {160, 118, 129}},
            {{58, 37, 72}, {28, 1, 30}, {160, 118, 129}},
            {{58, 37, 72}, {28, 1, 30}, {160, 118, 129}}
    });
    greyScaleOptionResult.put("luma-component", new int[][][]{
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}},
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}},
            {{48, 38, 58}, {36, 28, 23}, {56, 21, 20}}
    });

    greyScaleOptionResult.put("intensity-component", new int[][][]{
            {{84, 68, 82}, {62, 44, 43}, {98, 60, 48}},
            {{84, 68, 82}, {62, 44, 43}, {98, 60, 48}},
            {{84, 68, 82}, {62, 44, 43}, {98, 60, 48}}
    });
    greyScaleOptionResult.put("value-component", new int[][][]{
            {{190, 169, 142}, {160, 133, 100}, {160, 118, 129}},
            {{190, 169, 142}, {160, 133, 100}, {160, 118, 129}},
            {{190, 169, 142}, {160, 133, 100}, {160, 118, 129}}
    });
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlipImageHorizontalSourceNotExist() {
    try {
      processor.flipImage(true, "test", "test2");
    } catch (IllegalArgumentException exception) {
      assertEquals("No source image found with image ID: test", exception.getMessage());
      throw exception;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlipImageVerticalSourceNotExist() {
    try {
      processor.flipImage(false, "test", "test");
    } catch (IllegalArgumentException exception) {
      assertEquals("No source image found with image ID: test", exception.getMessage());
      throw exception;
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testToGrayScaleGrayScaleOptionNotExist() throws FileNotFoundException {
    try {
      processor.storeMap("test", data, 255);
      processor.toGrayScale("invalidOpt", "test", "testGrayScale");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid GreyScale Option", e.getMessage());
      throw e;
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testToGrayScaleSourceNotFound() {
    try {
      processor.toGrayScale("red", "test", "testFile");
    } catch (IllegalArgumentException e) {
      assertEquals("No source image found with image ID: test", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenDarkenGrayScaleSourceNotFound() {
    try {
      processor.brightenDarkenImage(1, "test", "testFile");
    } catch (IllegalArgumentException e) {
      assertEquals("No source image found with image ID: test", e.getMessage());
      throw e;
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testSplitSourceNonExist() {
    try {
      processor.split("test", new LinkedList<>());
    } catch (IllegalArgumentException e) {
      assertEquals("No source image found with image ID: test", e.getMessage());
      throw e;
    }
  }


  @Test
  public void testCombine() throws FileNotFoundException {
    int[][][] data = new int[][][]{
            {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}},
            {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
            {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}
    };
    processor.storeMap("test", data, 255);
    processor.storeMap("test1", data, 255);
    processor.storeMap("test2", data, 255);

    processor.combine("test", "test", "test1", "test2");
    int[][][] test = processor.getImage("test");
    assertEquals(3, test[0].length);
    assertEquals(3, test[0].length);
    assertEquals(255, processor.getMaxValue("test"));
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < test[0].length; j++) {
        assertArrayEquals(data[i][j], test[i][j]);
      }
    }
  }


  @Test
  public void testCombineNoSourceExist() {
    try {
      processor.combine("resule", "test1", "test3", "test2");
    } catch (IllegalArgumentException e) {
      assertEquals("No source image found with image ID: test1", e.getMessage());
    }
  }

  @Test
  public void testGetImageDataSourceExist() {
    processor.storeMap("test", data, 255);
    int[][][] test = processor.getImage("test");
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < test[0].length; j++) {
        assertArrayEquals(data[i][j], test[i][j]);
      }
    }
  }

  @Test
  public void testGetImageDataSourceNonExist() {
    try {
      processor.getImage("test");
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "No source image found with image ID: test");
    }
  }

  @Test
  public void testLoadImage() {
    processor.storeMap("test", data, 255);
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("test")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("test")[i][j]);
      }
    }
  }

  @Test
  public void testVerticalFlip() {

    processor.storeMap("test", data, 255);
    processor.flipImage(false, "test", "testResult");
    data = new int[][][]{
            {{104, 62, 0}, {160, 133, 100}, {190, 169, 142}},
            {{32, 0, 16}, {0, 0, 0}, {6, 0, 33}},
            {{160, 118, 129}, {28, 1, 30}, {58, 37, 72}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }

  @Test
  public void testHorizontalFlip() {
    processor.storeMap("test", data, 255);
    processor.flipImage(true, "test", "testResult");
    data = new int[][][]{
            {{142, 169, 190}, {100, 133, 160}, {0, 62, 104}},
            {{33, 0, 6}, {0, 0, 0}, {16, 0, 32}},
            {{72, 37, 58}, {30, 1, 28}, {129, 118, 160}}
    };
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
        assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
      }
    }
  }

  @Test
  public void testGreyScaleOption() {
    processor.storeMap("test", data, 255);
    for (String method : greyScaleOptionResult.keySet()) {
      processor.toGrayScale(method, "test", "testResult");
      data = greyScaleOptionResult.get(method);
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < processor.getImage("testResult")[0].length; j++) {
          assertArrayEquals(data[i][j], processor.getImage("testResult")[i][j]);
        }
      }
    }
  }

  @Test
  public void testBrightenDarken() {
    int[][][] brightenData = new int[][][]{
            {{200, 179, 152}, {170, 143, 110}, {114, 72, 10}},
            {{16, 10, 43}, {10, 10, 10}, {42, 10, 26}},
            {{68, 47, 82}, {38, 11, 40}, {170, 128, 139}}
    };
    int[][][] darkenData = new int[][][]{
            {{180, 159, 132}, {150, 123, 90}, {94, 52, 0}},
            {{0, 0, 23}, {0, 0, 0}, {22, 0, 6}},
            {{48, 27, 62}, {18, 0, 20}, {150, 108, 119}}
    };
    processor.storeMap("test", data, 255);
    for (int i = 0; i < 2; i++) {
      processor.brightenDarkenImage(i == 0 ? 10 : -10, "test", "testResult");
      data = (i == 0 ? brightenData : darkenData);
      for (int k = 0; k < 3; k++) {
        for (int l = 0; l < processor.getImage("testResult")[0].length; l++) {
          assertArrayEquals(data[k][l], processor.getImage("testResult")[k][l]);
        }
      }
    }
  }

  @Test
  public void testSplit() {
    processor.storeMap("test", data, 255);
    List<String> child = new LinkedList<>();
    child.add("testRed");
    child.add("testGreen");
    child.add("testBlue");
    processor.split("test", child);
    int[][][] red = processor.getImage("testRed");
    int[][][] green = processor.getImage("testGreen");
    int[][][] blue = processor.getImage("testBlue");
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        assertArrayEquals(red[i][j], data[0][j]);
        assertArrayEquals(green[i][j], data[1][j]);
        assertArrayEquals(blue[i][j], data[2][j]);
      }
    }
  }
}