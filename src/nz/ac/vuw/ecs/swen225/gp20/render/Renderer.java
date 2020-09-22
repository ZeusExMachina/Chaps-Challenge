package nz.ac.vuw.ecs.swen225.gp20.render;

public class Renderer {
  private static Renderer instance = new Renderer();
  private Renderer() {}
  public static Renderer getInstance() {
    return instance;
  }
}
