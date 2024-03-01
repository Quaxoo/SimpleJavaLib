import java.awt.*;

public class ScrollButton extends GraphicObject{

    int offset = -999;
    public ScrollButton() {
        super(300, 300, new Sprite(System.getProperty("user.dir") + "\\src\\" +"scrollButton.png"));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" +"scrollButtonHovered.png"));
        setBoundsSimple(true);
        setDoRender(false);
    }

    @Override
    protected void update() {

    }

    @Override
    protected void onMouseEntered() {

    }

    @Override
    protected void onMouseLeft() {

    }

    @Override
    protected void onKeyDown() {

    }

    @Override
    protected void onMouseDown() {
        if(offset == -999){
            offset = center.getY() - window.getMouse().getY();
        }
        moveTo(center.getX(), window.getMouse().getY() + offset);
    }

    @Override
    protected void onMouseUp() {
        offset = -999;
    }

    @Override
    protected void onMouseClicked(int button) {

    }
}
