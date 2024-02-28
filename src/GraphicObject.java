import java.awt.*;
import java.util.ArrayList;

public abstract class GraphicObject {
    protected Coordinate position;
    protected Coordinate center;
    protected final ArrayList<Sprite> sprites = new ArrayList<>();
    protected Sprite currentSprite;
    protected Window window;
    protected boolean entered = false;
    protected boolean mouseDown = false;
    protected boolean enableClick = true;
    protected boolean enableScroll = false;
    protected boolean doRender = true;
    protected int mouseButton;

    public GraphicObject(int x, int y, String spritePath){
        center = new Coordinate(x, y);
        addSprite(spritePath);
        currentSprite = sprites.get(0);
        position = new Coordinate(x - currentSprite.getWidth()/2, y - currentSprite.getHeight()/2);
        Window.getDefault().add(this);
        window = Window.getDefault();
    }
    public GraphicObject(int x, int y, Sprite sprite){
        center = new Coordinate(x,y);
        addSprite(sprite);
        currentSprite = sprites.get(0);
        position = new Coordinate(x - currentSprite.getWidth()/2, y - currentSprite.getHeight()/2);
        Window.getDefault().add(this);
        window = Window.getDefault();
    }

    protected abstract void update();
    public void refresh(){
        if (!entered && currentSprite.contains(window.getMouse(), position, getScroll())){
            enableClick = !window.isAnyButtonDown();
            onMouseEntered();
            entered = true;
        }else if(entered && !currentSprite.contains(window.getMouse(), position, getScroll())){
            onMouseLeft();
            entered = false;
        }
        if(entered && !enableClick){
            enableClick = !window.isAnyButtonDown();
        }
        if(window.isAnyKeyDown()){
            onKeyDown();
        }
        if (entered && window.isAnyButtonDown() && enableClick){
            mouseDown = true;
            onMouseDown();
            mouseButton = window.getPressedButtons().toArray()[0] != null ? (int) window.getPressedButtons().toArray()[0] : -1 ;
        }else if(mouseDown){
            mouseDown = false;
            onMouseUp();
            if(entered){
                onMouseClicked(mouseButton);
            }
            mouseButton = 0;
        }
        update();
    }

    protected abstract void onMouseEntered();
    protected abstract void onMouseLeft();
    protected abstract void onKeyDown();
    protected abstract void onMouseDown();
    protected abstract void onMouseUp();
    protected abstract void onMouseClicked(int button);

    protected void draw(Graphics g, boolean threadRender){
        if(isRendered() == threadRender)
        g.drawImage(currentSprite.getImage(), center.getX() - currentSprite.getWidth()/2, center.getY() - currentSprite.getHeight()/2 - getScroll(), currentSprite.getWidth(), currentSprite.getHeight(), null);
    }

    protected void addSprite(Sprite sprite){
        sprites.add(sprite);
    }
    protected void addSprite(String path){
        sprites.add(new Sprite(path));
    }
    protected boolean isMouseEntered(){
        return entered;
    }
    protected boolean isMouseDown(){
        return mouseDown;
    }
    void setSprite(int spriteIndex){
        if (spriteIndex >= 0 && spriteIndex < sprites.size()){
            currentSprite = sprites.get(spriteIndex);
        }
    }

    protected void move(int x, int y){
        center.change(x,y);
        position.change(x,y);
    }
    protected void moveTo(int x, int y){
        center.set(x,y);
        position = new Coordinate(x - currentSprite.getWidth()/2, y - currentSprite.getHeight()/2);
    }

    protected boolean isKeyDown(char key){
        return window.getPressedKeys().contains(key);
    }
    protected boolean isButtonDown(int button){
        return window.getPressedButtons().contains(button);
    }
    protected int getWidth(){
        return currentSprite.getWidth();
    }
    protected int getHeight(){
        return currentSprite.getHeight();
    }
    public void setBoundsSimple(boolean simple){
        for (Sprite s: sprites){
            s.setBoundsSimple(simple);
        }
    }

    protected int getScroll(){
        return enableScroll ? window.getScroll() : 0;
    }
    public void isScrollable(boolean scrollable){
        enableScroll = scrollable;
    }

    public void setDoRender(boolean render){
        if(render){
            window.removeRender(this);
        }else{
            window.addRender(this);
        }
        doRender = render;
    }
    public boolean isRendered(){
        return doRender;
    }

}
