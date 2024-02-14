package model;

/**
 * This is an interface represents a new version of processor, which additionally supports
 * color transformation, filtering, and dithering, as well as operating and executing load/save
 * operation on different file types.
 */
public interface ProcessorExtension extends Processor {

  /**
   * Create a new image object from a known source with optional mechanism and the
   * given id and add to the storage.
   *
   * @param method The color transformation method that will be utilized
   * @param source the source image
   * @param dest   the destination image
   */
  void colorTransformation(String method, String source, String dest);

  /**
   * Create a new image object from a known source with optional mechanism and the
   * given id and add to the storage.
   *
   * @param method The color transformation method that will be utilized
   * @param source the source image
   * @param dest   the destination image
   */
  void filter(String method, String source, String dest);

  /**
   * Create a new image object from a known source with optional mechanism and the
   * given id and add to the storage.
   *
   * @param source the source image
   * @param dest   the destination image
   */
  void dither(String source, String dest);
}
