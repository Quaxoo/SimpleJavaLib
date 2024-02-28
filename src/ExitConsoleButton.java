import java.awt.*;

public class ExitConsoleButton extends GraphicObject{

    public ExitConsoleButton() {
        super(0, 0, new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitconsolebutton.png", 0.8f));
        addSprite(new Sprite(System.getProperty("user.dir") + "\\src\\" +"exitconsolebuttonHovered.png", 0.8f));

        moveTo(window.getWidth() - getWidth()/2 - 3*window.getOutline(), window.getHeight() - window.getConsoleHeight() + getHeight()/2);
        setBoundsSimple(true);
        setDoRender(false);
    }
    protected void draw(Graphics g, boolean threadRender){
        if (window.doesConsoleExist() && isRendered() == threadRender) {
            g.drawImage(currentSprite.getImage(),
                    center.getX() - currentSprite.getWidth() / 2,
                    center.getY() - currentSprite.getHeight() / 2 - getScroll(),
                    currentSprite.getWidth(),
                    currentSprite.getHeight(), null);
        }
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
        if(window.isConsoleOpen()){
            moveTo(window.getWidth() - getWidth()/2 - 3*window.getOutline(), window.getHeight() - 32  + getHeight()/2);
            window.setConsole(1);
        }else{
            moveTo(window.getWidth() - getWidth()/2 - 3*window.getOutline(), window.getHeight() - window.getConsoleHeight() + getHeight()/2);
            window.setConsole(2);
        }
    }
}
