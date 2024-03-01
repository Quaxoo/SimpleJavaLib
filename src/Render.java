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
        int highest = -1;
        int lowest = -1;
        for (GraphicObject go: objects){
            go.draw(g, true);
            if(go.enableScroll && (highest > go.position.getY() || highest == -1)){
                highest = go.position.getY();
            }
            if(go.enableScroll && (lowest < go.position.getY() + go.getHeight() || lowest == -1)){
                lowest = go.position.getY() + go.getHeight();
            }
        }
        window.setMaxScroll(Math.max(0, -(window.getHeight() - lowest - 3*window.getOutline())));
        window.setMinScroll(Math.min(0, highest - window.getOutline()));
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
