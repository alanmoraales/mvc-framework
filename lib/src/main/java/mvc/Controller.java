package mvc;

public abstract class Controller {
  protected Model model;

  public void setModel(Model model) {
    this.model = model;
  }

  public void updateModel(Object newModelData) {
    this.model.setData(newModelData);
  }
}
