package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This is the concrete class representing the processor.
 */
public class ImageProcessor implements Processor {

  protected final Map<String, int[][][]> images;
  protected final Map<String, Integer> maxValueMap;

  protected Map<String, Function<String[], int[][][]>> grayScaleFunctionMap;

  /**
   * Constructor for the class, initialized all local variable.
   */
  public ImageProcessor() {
    images = new HashMap<>();
    maxValueMap = new HashMap<>();
    grayScaleFunctionMap = new HashMap<>();
    grayScaleFunctionMap.put("red-component", this::visualizeRGB);
    grayScaleFunctionMap.put("green-component", this::visualizeRGB);
    grayScaleFunctionMap.put("blue-component", this::visualizeRGB);
    grayScaleFunctionMap.put("value-component", this::visualizeValue);
    grayScaleFunctionMap.put("intensity-component", this::visualizeIntensity);
    grayScaleFunctionMap.put("luma-component", this::visualizeLuma);
  }


  @Override
  public void flipImage(boolean horizontal, String target, String dest)
          throws IllegalArgumentException {
    checkExistence(target);
    int[][][] sourceData = images.get(target);
    if (horizontal) {
      images.put(dest, flipHorizontal(sourceData));
    } else {
      images.put(dest, flipVertical(sourceData));
    }
    maxValueMap.put(dest, maxValueMap.get(target));
  }

  /**
   * This is a helper method utilized to check the existence of an image object.
   *
   * @param target the target image object
   * @throws IllegalArgumentException Exception will be thrown if the source id is not found.
   */
  protected final void checkExistence(String target) throws IllegalArgumentException {
    if (images.get(target) == null) {
      throw new IllegalArgumentException("No source image found with image ID: " + target);
    }
  }

  @Override
  public void toGrayScale(String option, String source, String dest)
          throws IllegalArgumentException {
    checkExistence(source);
    if (grayScaleFunctionMap.containsKey(option.toLowerCase())) {
      images.put(dest, grayScaleFunctionMap.get(option.toLowerCase())
              .apply(new String[]{option, source}));
      maxValueMap.put(dest, maxValueMap.get(source));
    } else {
      throw new IllegalArgumentException("Invalid GreyScale Option");
    }
  }

  @Override
  public void brightenDarkenImage(int pixel, String source, String dest)
          throws IllegalArgumentException {
    checkExistence(source);
    int[][][] sourceImage = images.get(source);
    int row = sourceImage[0].length;
    int col = sourceImage[0][0].length;
    int[][][] data = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int k = 0; k < 3; k++) {
          int resultPixel = sourceImage[k][i][j] + pixel;
          if (pixel > 0) {
            resultPixel = Math.min(resultPixel, 255);
          } else {
            resultPixel = Math.max(resultPixel, 0);
          }
          data[k][i][j] = resultPixel;
        }
      }
    }
    images.put(dest, data);
    maxValueMap.put(dest, maxValueMap.get(source));
  }

  @Override
  public void split(String source, List<String> childImage)
          throws IllegalArgumentException {
    checkExistence(source);
    List<int[][][]> result = splitHelper(source);
    images.put(childImage.get(0), result.get(0));
    images.put(childImage.get(1), result.get(1));
    images.put(childImage.get(2), result.get(2));
    for (int i = 0; i < 3; i++) {
      maxValueMap.put(childImage.get(i), maxValueMap.get(source));
    }
  }

  @Override
  public void combine(String dest, String first, String sec, String third)
          throws IllegalArgumentException {
    checkExistence(first);
    checkExistence(sec);
    checkExistence(third);
    int[][][] result = combineHelper(images.get(first), images.get(sec), images.get(third));
    images.put(dest, result);
    maxValueMap.put(dest, Math.max(maxValueMap.get(first),
            Math.max(maxValueMap.get(sec), maxValueMap.get(third))));
  }


  @Override
  public int[][][] getImage(String id) throws IllegalArgumentException {
    checkExistence(id);
    return deepCopy(images.get(id));
  }

  @Override
  public int getMaxValue(String id) throws IllegalArgumentException {
    checkExistence(id);
    return maxValueMap.get(id);
  }

  /**
   * Private method utilized to execute the deep copy of image data.
   *
   * @param source The data source
   * @return a deep copy of the data.
   */
  protected final int[][][] deepCopy(int[][][] source) {
    int[][][] result = new int[3][source[0].length][source[0][0].length];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < source[0].length; j++) {
        result[i][j] = Arrays.copyOf(source[i][j], source[0][0].length);
      }
    }
    return result;
  }


  /**
   * Flips the current image data in a horizontal fashion and return a
   * new image data array object that represents the alternated image data.
   *
   * @return A new image data array object that represents the alternated image data.
   */
  protected final int[][][] flipHorizontal(int[][][] data) {
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] flippedPixels = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int c = 0; c < 3; c++) {
          flippedPixels[c][i][col - j - 1] = data[c][i][j];
        }
      }
    }
    return flippedPixels;
  }

  /**
   * Flips the current image data in a vertical fashion and return a
   * new image data array object that represents the alternated image data.
   *
   * @return A new image data array object that represents the alternated image data.
   */
  protected final int[][][] flipVertical(int[][][] data) {
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] flippedPixels = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int c = 0; c < 3; c++) {
          flippedPixels[c][row - i - 1][j] = data[c][i][j];
        }
      }
    }
    return flippedPixels;
  }

  /**
   * Combines the current image data with the data of the input objects, which takes
   * the red layer of the current image, green layer of the green image, blue layer of
   * the blue image.
   *
   * @param green the first image
   * @param blue  the second image
   * @return A new image data array object that represents the alternated image data.
   * @throws IllegalArgumentException exception will be thrown if the images mismatch in size
   */
  protected final int[][][] combineHelper(int[][][] red, int[][][] green, int[][][] blue) {
    if (red[0].length != green[0].length
            || red[0].length != blue[0].length
            || red[0][0].length != green[0][0].length
            || red[0][0].length != blue[0][0].length) {
      throw new IllegalArgumentException("Cannot combine image with different in size");
    }
    int row = red[0].length;
    int col = red[0][0].length;
    int[][][] data = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        data[0][i][j] = red[0][i][j];
        data[1][i][j] = green[1][i][j];
        data[2][i][j] = blue[2][i][j];
      }
    }
    return data;
  }


  /**
   * Alternate a color image and split into three grayscale image data array
   * that represents individual rbg channel.
   *
   * @return A list of grayscale data array that represents the grayscale image generated from
   *          different pixel channel.
   */
  protected final List<int[][][]> splitHelper(String id) {
    int[][][] red = visualizeRGB(new String[]{"red-component", id});
    int[][][] green = visualizeRGB(new String[]{"green-component", id});
    int[][][] blue = visualizeRGB(new String[]{"blue-component", id});
    List<int[][][]> collect = new ArrayList<>();
    collect.add(red);
    collect.add(green);
    collect.add(blue);
    return collect;
  }

  /**
   * Creates a new gray scale image object with its data represented by the intensity
   * of the current image.
   *
   * @return A new gray scale image data array
   */
  protected final int[][][] visualizeIntensity(String[] info) {
    int[][][] source = images.get(info[1]);
    int row = source[0].length;
    int col = source[0][0].length;
    int[][][] valuePixels = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int c = 0; c < 3; c++) {
          valuePixels[c][i][j] = (source[0][i][j] + source[1][i][j] + source[2][i][j]) / 3;
        }
      }
    }
    return valuePixels;
  }

  /**
   * Creates a new gray scale image object with its data represented by the value
   * of the current image.
   *
   * @return A new gray scale image data array
   */
  protected final int[][][] visualizeValue(String[] info) {
    int[][][] source = images.get(info[1]);
    int row = source[0].length;
    int col = source[0][0].length;
    int[][][] intensityPixels = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int c = 0; c < 3; c++) {
          intensityPixels[c][i][j] =
                  Math.max(Math.max(source[0][i][j], source[1][i][j]), source[2][i][j]);
        }
      }
    }
    return intensityPixels;
  }

  /**
   * Creates a new gray scale image object with its data represented by the luma
   * of the current image.
   *
   * @return A new gray scale image data array
   */
  protected final int[][][] visualizeLuma(String[] info) {
    int[][][] data = images.get(info[1]);
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] lumaPixels = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int red = data[0][i][j];
        int green = data[1][i][j];
        int blue = data[2][i][j];

        int curr = (int) (red * 0.2126 + green * 0.7152 + blue * 0.0722);
        for (int c = 0; c < 3; c++) {
          lumaPixels[c][i][j] = curr;
        }
      }
    }
    return lumaPixels;
  }

  /**
   * Creates a new gray scale image object with its data represented by the RGB
   * component of the current image.
   *
   * @return A new gray scale image data array
   */
  protected final int[][][] visualizeRGB(String[] info) {
    int[][][] data = images.get(info[1]);
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] redPixels = new int[3][row][col];
    int layer;
    switch (info[0]) {
      case "red-component":
        layer = 0;
        break;
      case "green-component":
        layer = 1;
        break;
      default:
        layer = 2;
        break;
    }
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int c = 0; c < 3; c++) {
          redPixels[c][i][j] = data[layer][i][j];
        }
      }
    }
    return redPixels;
  }


  @Override
  public void storeMap(String id, int[][][] data, int maxVal) {
    images.put(id, data);
    maxValueMap.put(id, maxVal);
  }
}
