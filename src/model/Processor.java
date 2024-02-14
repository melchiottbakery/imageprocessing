package model;

import java.util.List;

/**
 * This is an interface that represents a processor and all functionality supported by such object.
 */
public interface Processor {
  /**
   * Create a new image object with the given id and image data of horizontally or vertically
   * flipped known image object and add to storage.
   *
   * @param horizontal A boolean represent whether the operation is flipping horizontally.
   * @param target     The id of the source image
   * @param dest       The id of the final image
   * @throws IllegalArgumentException Exception will be thrown if the source image id is unknown
   */
  void flipImage(boolean horizontal, String target, String dest) throws IllegalArgumentException;

  /**
   * Create a new grayscale image object from a known source with optional mechanism and the
   * given id and add to the storage.
   *
   * @param option the mechanism that will be utilized to do the conversion
   * @param source The id of the source image
   * @param dest   The id of the final image
   * @throws IllegalArgumentException Exception will be thrown if the source image id is unknown
   *                                  or the option is invalid
   */
  void toGrayScale(String option, String source, String dest) throws IllegalArgumentException;

  /**
   * Creates a new image object from a known source where each pixel is brightened or dimmed by the
   * given value, min and max is capped at 0 and 255.
   *
   * @param pixel  The amount of pixel to mutate
   * @param source The id of the source image
   * @param dest   the id of the final image
   * @throws IllegalArgumentException Exception will be thrown if the source image id is unknown
   */
  void brightenDarkenImage(int pixel, String source, String dest) throws IllegalArgumentException;

  /**
   * Creates 3 grayscale image object from a known color image with the given id and store them
   * into the storage.
   *
   * @param source     The id of the source image
   * @param childImage The id of the final images
   * @throws IllegalArgumentException Exception will be thrown if the source image id is unknown
   */
  void split(String source, List<String> childImage) throws IllegalArgumentException;

  /**
   * Create a new image with the given id from individual channel of the
   * three image with the given id and store them into the storage.
   *
   * @param dest  The id of the final image
   * @param red   The id of the image where the red channel will be extracted
   * @param green The id of the image where the green channel will be extracted
   * @param blue  The id of the image where the blue channel will be extracted
   * @throws IllegalArgumentException Exception will be thrown if any of the source id is unknown
   */
  void combine(String dest, String red, String green, String blue) throws IllegalArgumentException;

  /**
   * Retrieve a specific image in a format of 3d array.
   *
   * @param id The id of the target image
   * @return A 3d array representation of the image
   * @throws IllegalArgumentException Exception will be thrown if the id is unknown
   */
  int[][][] getImage(String id) throws IllegalArgumentException;

  /**
   * Retrieve the max value of a specific image.
   *
   * @param id the id of the image
   * @return the image's associated max value
   * @throws IllegalArgumentException exception will be thrown if the id is invalid.
   */
  int getMaxValue(String id) throws IllegalArgumentException;

  /**
   * Method utilized to store the corresponding data into the map.
   *
   * @param id     The id of the image
   * @param data   The image data
   * @param maxVal the max value of the file
   */
  void storeMap(String id, int[][][] data, int maxVal);
}

