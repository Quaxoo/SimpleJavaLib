import java.awt.*;
import java.io.File;

public class ExitButton extends GraphicObject{
    private Window window;
    public ExitButton(Window window) {
        super(0,0, new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitbutton.png", 0.8f));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitbuttonHovered.png", 0.8f));

        Dimension windowSize = window.getSize();

        moveTo(windowSize.width - getWidth()/2 - windowSize.width/100, getHeight() / 2 + windowSize.height/100);
        setBoundsSimple(true);
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

    }

    @Override
    protected void onMouseUp() {

    }

    @Override
    protected void onMouseClicked() {
        System.exit(0);
    }
}
