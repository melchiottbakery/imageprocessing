package model;

import java.util.HashMap;
import java.util.Map;


/**
 * This is the extension class of Image Processor, which in additional to the old function,
 * supports the loading and saving of different file types, filtering, color transformation, and
 * dithering.
 */
public class ImageProcessorExtension extends ImageProcessor implements ProcessorExtension {
  protected final Map<String, double[][]> filteringMap;
  protected final Map<String, double[][]> transformationMap;

  /**
   * Constructor for the object, calls its super class and modify loader/writer map.
   */
  public ImageProcessorExtension() {
    super();
    filteringMap = new HashMap<>();
    transformationMap = new HashMap<>();
    setFilteringMap();
    setTransformationMap();
  }

  /**
   * Private helper method utilized to set up the transformation matrix mapping.
   */
  private void setTransformationMap() {
    transformationMap.put("sepia", new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    });
    transformationMap.put("greyscale", new double[][]{
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    });
  }

  /**
   * Private helper method utilized to set up the filter matrix mapping.
   */
  private void setFilteringMap() {
    filteringMap.put("blur", new double[][]{
            {1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0},
            {1.0 / 8.0, 1.0 / 4.0, 1.0 / 8.0},
            {1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0}
    });
    filteringMap.put("sharpen", new double[][]{
            {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0},
            {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0},
            {-1.0 / 8.0, 1.0 / 4.0, 1.0, 1.0 / 4.0, -1.0 / 8.0},
            {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0},
            {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0}
    });
  }

  @Override
  public void colorTransformation(String method, String source, String dest)
          throws IllegalArgumentException {
    checkExistence(source);
    if (!transformationMap.containsKey(method)) {
      throw new IllegalArgumentException("Color Transformation Method Not Supported");
    }
    images.put(dest, colorTransformationHelper(images.get(source),
            transformationMap.get(method), source));
    maxValueMap.put(dest, maxValueMap.get(source));
  }

  @Override
  public void filter(String method, String source, String dest) {
    checkExistence(source);
    if (!filteringMap.containsKey(method)) {
      throw new IllegalArgumentException("Filtering Method Not Supported");
    }
    images.put(dest, filtering(images.get(source), filteringMap.get(method), source));
    maxValueMap.put(dest, maxValueMap.get(source));
  }

  @Override
  public void dither(String source, String dest) {
    checkExistence(source);
    images.put(dest, dithering(colorTransformationHelper(images.get(source),
            transformationMap.get("greyscale"), source), source));
    maxValueMap.put(dest, maxValueMap.get(source));
  }

  /**
   * This is a method utilized to execute the filtering sequence on an image data.
   *
   * @param data         The target data
   * @param filterMatrix The kernel matrix.
   * @param id           the id of the source image
   * @return The alternated image data
   */
  protected final int[][][] filtering(int[][][] data, double[][] filterMatrix, String id) {
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] newData = new int[3][row][col];
    for (int j = 0; j < row; j++) {
      for (int k = 0; k < col; k++) {
        int filterMid = filterMatrix.length / 2;
        for (int i = 0; i < 3; i++) {
          int result = 0;
          int filterX = 0;
          int filterY = 0;
          for (int filterRow = j - filterMid; filterRow <= j + filterMid; filterRow++) {
            for (int filterCol = k - filterMid; filterCol <= k + filterMid; filterCol++) {
              if (filterCol >= col) {
                break;
              }
              if (filterRow >= 0 && filterRow < row && filterCol >= 0) {
                result += (int) (data[i][filterRow][filterCol] * filterMatrix[filterX][filterY]);
              }
              filterX++;
            }
            filterX = 0;
            filterY++;
            if (filterRow >= row) {
              break;
            }
          }
          newData[i][j][k] = Math.max(0, Math.min(result, maxValueMap.get(id)));
        }
      }
    }
    return newData;
  }

  /**
   * This is a  method utilized to execute the colorTransformation sequence on an image data.
   *
   * @param data         The target data
   * @param filterMatrix The kernel matrix.
   * @param id           the id of the source matrix
   * @return The alternated image data
   */
  protected final int[][][] colorTransformationHelper(int[][][] data, double[][] filterMatrix
          , String id) {
    int row = data[0].length;
    int col = data[0][0].length;
    int[][][] newData = new int[3][row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        for (int k = 0; k < 3; k++) {
          newData[k][i][j] = Math.min(maxValueMap.get(id), (int) (data[0][i][j] * filterMatrix[k][0]
                  + data[1][i][j] * filterMatrix[k][1] + data[2][i][j] * filterMatrix[k][2]));
        }
      }
    }
    return newData;
  }

  /**
   * This is a  helper method utilized to set a specific pixel data on a matrix.
   *
   * @param data     The matrix
   * @param i        the row
   * @param j        the column
   * @param fraction the fraction such matrix is applied by.
   * @param value    the value of the matrix
   */
  private void setPixel(int[][][] data, int i, int j, double fraction, int value) {
    for (int k = 0; k < 3; k++) {
      data[k][i][j] = data[k][i][j] + (int) (fraction * value);
    }
  }

  /**
   * This is a  method utilized to execute the dithering sequence on an image data.
   *
   * @param greyScaleData The target matrix.
   * @param id            the source id of the image
   * @return The alternated image data
   */
  protected final int[][][] dithering(int[][][] greyScaleData, String id) {
    int row = greyScaleData[0].length;
    int col = greyScaleData[0][0].length;
    int[][][] resultData = deepCopy(greyScaleData);
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int oldColor = resultData[0][i][j];
        int newColor = maxValueMap.get(id) / 2 > oldColor ? 0 : maxValueMap.get(id);
        int error = oldColor - newColor;
        for (int k = 0; k < 3; k++) {
          resultData[k][i][j] = newColor;
        }
        if (j + 1 < col) {
          setPixel(resultData, i, j + 1, (double) 7 / 16, error);
        }
        if (i + 1 < row && j - 1 >= 0) {
          setPixel(resultData, i + 1, j - 1, (double) 3 / 16, error);
        }
        if (i + 1 < row) {
          setPixel(resultData, i + 1, j, (double) 5 / 16, error);
        }
        if (i + 1 < row && j + 1 < col) {
          setPixel(resultData, i + 1, j + 1, (double) 1 / 16, error);
        }
      }
    }
    return resultData;
  }


}
