package com.ngc.seaside.jellyfish.cli.utilities.console.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the Table model backed by a {@link List}
 *
 * @author justan.provence@ngc.com
 */
public class DefaultTableModel<T> implements ITableModel<T> {

  private List<T> items = new ArrayList<>();
  private List<ITableModelListener> listeners = new ArrayList<>();

  @Override
  public void addTableModelListener(ITableModelListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeTableModelListener(ITableModelListener listener) {
    listeners.remove(listener);
  }

  @Override
  public List<T> getItems() {
    return new ArrayList<>(items);
  }

  @Override
  public void addItem(T item) {
    items.add(item);
    notifyListeners();
  }

  @Override
  public void addItems(List<T> items) {
    this.items.addAll(items);
    notifyListeners();
  }

  @Override
  public void removeItem(T item) {
    items.remove(item);
    notifyListeners();
  }

  @Override
  public void removeAll() {
    items.clear();
    notifyListeners();
  }

  /**
   * This method will notify the listeners in a fail-safe way. This means that if one listener
   * was to error it would not cause other listeners to not be notified.
   */
  private void notifyListeners() {
    for(ITableModelListener listener : listeners) {
      try {
        listener.modelChanged();
      } catch (Throwable t) {
        //catch any error the listener might throw
        //this is to protect any subsequent listeners from getting called due to
        //a listener prior to it throwing an error
      }
    }
  }

}
