package mvc;

public abstract class View implements ModelObserver {
  protected Model model;
  protected Controller modelController;

  public View() {
    var loader = new ConfigLoader();
    loader.loadConfiguration(this);
  }

  public void setModel(Model model) {
    model.addObserver(this);
    this.model = model;
  }

  public void setModelController(Controller modelController) {
    this.modelController = modelController;
  }
}
