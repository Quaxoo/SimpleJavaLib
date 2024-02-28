import java.awt.*;
import java.io.File;

public class MoveButton extends GraphicObject{
    private final Window window;
    public MoveButton(Window window) {
        super(0,0, new Sprite(System.getProperty("user.dir") + "\\src\\" +"movebutton.png" ));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" + "movebuttonHovered.png"));

        moveTo(window.getWidth() - 2 * getWidth() - 3 * window.getOutline(), getHeight() / 2 + window.getOutline()/2);

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
        window.setMoveable(window.getPressedButtons().contains(1));
    }

    @Override
    protected void onMouseUp() {

    }

    @Override
    protected void onMouseClicked(int button) {

    }
}
