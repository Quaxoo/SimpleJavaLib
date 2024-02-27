import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Update extends Loop {
    public static CopyOnWriteArrayList<GraphicObject> objects = new CopyOnWriteArrayList<>();
    private Window window;

    public Update(Window window){
        super(Update::update, 200);
        this.window = window;
    }

    public static void update(){
        for (GraphicObject go: objects){
            go.refresh();
        }
    }

    public void add(GraphicObject graphicObject){
        objects.add(graphicObject);
    }

    public void restart(){
        restartLoop();
    }
}
