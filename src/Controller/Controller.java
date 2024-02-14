package controller;

/**
 * This interface represents a Controller.Controller and all function supported by such.
 */
public interface Controller {

  /**
   * This is the method utilized for the controller to start reading command from user and hand
   * off the corresponding command to the model or the view.
   */
  void execute();
}
