import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Render extends Loop {

    private Window window;
    private CopyOnWriteArrayList<GraphicObject> objects = new CopyOnWriteArrayList<>();

    public Render(Window window){
        super(window::repaint, window.getFramerate());
        this.window = window;
    }
    public void renderObjects(Graphics g){
        for (GraphicObject go: objects){
            go.draw(g, true);
        }
    }

    public void add(GraphicObject graphicObject){
        objects.add(graphicObject);
    }
    public void remove(GraphicObject graphicObject){
        objects.remove(graphicObject);
    }
    public void restart(){
        restartLoop();
    }
}
