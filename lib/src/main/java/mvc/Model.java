package mvc;

import java.util.ArrayList;

public abstract class Model {
  private Object data;
  private ArrayList<ModelObserver> observers;

  public Model() {
    this.observers = new ArrayList<>();
  }

  protected void sendUpdateNotification() {
    for(ModelObserver observer : observers) {
      observer.updateData();
    }
  }

  public void setData(Object newData) {
    this.data = newData;
    this.sendUpdateNotification();
  }

  public Object getData() {
    return this.data;
  }

  public void addObserver(ModelObserver observer) {
    this.observers.add(observer);
  }

  public void removeObserver(ModelObserver observer) {
    this.observers.remove(observer);
  }
}
