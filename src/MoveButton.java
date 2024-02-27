import java.awt.*;
import java.io.File;

public class MoveButton extends GraphicObject{
    private Window window;
    public MoveButton(Window window) {
        super(0,0, new Sprite(System.getProperty("user.dir") + "\\src\\" +"movebutton.png" ));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" + "movebuttonHovered.png"));

        Dimension windowSize = window.getSize();

        moveTo(windowSize.width - getWidth()*2 - windowSize.width/200, getHeight() / 2 + windowSize.height/100);
        setBoundsSimple(true);
        this.window = window;
    }

    @Override
    protected void update() {

    }

    @Override
    protected void onMouseEntered() {
        setSprite(1);
    }

    @Override
    protected void onMouseLeft() {
        setSprite(0);
    }

    @Override
    protected void onKeyDown() {

    }

    @Override
    protected void onMouseDown() {
        window.setMoveable(true);
    }

    @Override
    protected void onMouseUp() {

    }

    @Override
    protected void onMouseClicked() {

    }
}
