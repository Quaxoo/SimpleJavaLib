import java.awt.*;
import java.util.ArrayList;

public abstract class GraphicObject {
    protected Coordinate position;
    private final ArrayList<Sprite> sprites = new ArrayList<>();
    private int currentSprite = 0;
    public GraphicObject(int x, int y, String imagePath){
        position = new Coordinate();
        addSprite(imagePath);
        Window.getDefault().add(this);
    }

    public abstract void update();
    protected void draw(Graphics g){
        Sprite sprite = sprites.get(currentSprite);
        g.drawImage(sprite.getImage(), position.getX(), position.getY(), sprite.getWidth(), sprite.getHeight(), null);
        g.setColor(Color.red);
        for (int i = 0; i < sprite.getCleanBounds().npoints; i++){
            g.drawRect(sprite.getCleanBounds().xpoints[i],
                    sprite.getCleanBounds().ypoints[i],
                    1, 1);
        }
    }

    protected void addSprite(Sprite sprite){
        sprites.add(sprite);
    }
    protected void addSprite(String path){
        sprites.add(new Sprite(path));
    }

    void setSprite(int spriteIndex){
        if (spriteIndex >= 0 && spriteIndex < sprites.size()){
            currentSprite = spriteIndex;
        }
    }
}
