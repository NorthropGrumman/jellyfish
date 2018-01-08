package com.ngc.seaside.bootstrap.utilities.console.api;

/**
 * This is the base class for classes that render or display data. Think of this class as a more
 * generic version of a JTable. It follows a similar pattern to that of model-view-controller where
 * the {@link ITableFormat} is the controller and the {@link ITableModel} provides your data.
 *  <br><br>
 * The main purpose of this abstraction is to handle the boilerplate items
 * of the table such as storing the format and model.
 * <br><br>
 * This class will automatically create a listener when the table model is set and on
 * construction. The derived class will only have to implement the {@link #render()} method
 * if they need to know that new data is present.
 *
 * @author justan.provence@ngc.com
 * @see ITableFormat
 * @see ITableModel
 */
public abstract class AbstractTable<T> {

  private ITableFormat<T> format;
  private ITableModel<T> model;
  private ModelListener listener = new ModelListener();

  /**
   * Constructor. There is no default constructor for this class on purpose. The format
   * is required in order to render the objects in the model.
   *
   * @param format the table's format.
   */
  public AbstractTable(ITableFormat<T> format) {
    setTableFormat(format);
    setTableModel(new DefaultTableModel<>());
  }

  /**
   * Provide access to the format. This returns the actual object not a copy.
   *
   * @return the format.
   */
  public ITableFormat<T> getFormat() {
    return format;
  }

  /**
   * Provide access to the model. This returns the actual object. Any mutation of this object
   * will provide a callback to this class to render.
   *
   * @return the table's model.
   */
  public ITableModel<T> getModel() {
    return model;
  }

  /**
   * Set the format. The previous format will be overwritten.
   *
   * @param format the new format.
   */
  public void setTableFormat(ITableFormat<T> format) {
    this.format = format;
  }

  /**
   * Set the table model. This will trigger a render call.
   *
   * @param model the new table model.
   */
  public void setTableModel(ITableModel<T> model) {
    if (this.model != null && listener != null) {
      this.model.removeTableModelListener(listener);
    }
    this.model = model;

    this.model.addTableModelListener(listener);
    render();
  }

  /**
   * Render the table. This class provides an empty method in the event that
   * the derived class doesn't need to render on the model change. This is usually in the event
   * that you already know the data being provided at the time of instantiation.
   */
  protected void render() {

  }

  /**
   * Simple model listener that calls render on change.
   */
  private class ModelListener implements ITableModelListener {

    @Override
    public void modelChanged() {
      render();
    }
  }

}
