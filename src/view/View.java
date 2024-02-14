package view;

/**
 * This is interface that represents a view object.
 */
public interface View {
  /**
   * Method utilized to display error message with the proper view.
   *
   * @param msg The message to be displayed
   */
  void displayError(String msg);

  /**
   * Method utilized to result of a specific action.
   */
  void displayResult();
}
