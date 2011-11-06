package midterm1;

public class BouncingObjectFactory  {

  protected AnimationApplet animator; 
  public BouncingObjectFactory(AnimationApplet animator) {
    this.animator = animator; 
  }

  public BouncingObject makeBouncingObject(String objType) {
    if(objType.equals("circle"))
      return new BouncingCircle(animator);
    else if(objType.equals("square"))
      return new BouncingSquare(animator);
    else if(objType.equals("image"))
        return new BouncingImage(animator);
    else
      return new BouncingCircle(animator);
  }
}
