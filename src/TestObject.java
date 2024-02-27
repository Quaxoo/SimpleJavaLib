import java.util.Set;

public class TestObject extends GraphicObject{
    public TestObject(int x, int y, Sprite sprite) {
        super(x, y, sprite);
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
        if(isKeyDown('W')){
            move(0,-1);
        }
        if(isKeyDown('S')){
            move(0,1);
        }
        if(isKeyDown('A')){
            move(-1,0);
        }
        if(isKeyDown('D')){
            move(1,0);
        }
    }

    @Override
    protected void onMouseDown() {

    }

    @Override
    protected void onMouseUp() {

    }

    @Override
    protected void onMouseClicked() {
        if (isButtonDown(1)){
            System.out.println(1);
        }
        if (isButtonDown(2)){
            System.out.println(2);
        }
    }

}
