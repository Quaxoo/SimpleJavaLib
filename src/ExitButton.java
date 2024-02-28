import java.awt.*;
import java.io.File;

public class ExitButton extends GraphicObject{
    public ExitButton(Window window) {
        super(0,0, new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitbutton.png", 0.8f));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitbuttonHovered.png", 0.8f));
        moveTo(window.getWidth() - getWidth()/2 - 3 * window.getOutline(), getHeight() / 2 + window.getOutline());
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
    protected void onMouseClicked(int button) {
        if (button == 1){
            System.exit(0);
        }
    }
}
