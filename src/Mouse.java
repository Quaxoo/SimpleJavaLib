
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final Coordinate position = new Coordinate();
    private final Set<Integer> buttonsDown = new HashSet<>();
    private int scroll = 0;
    private int xOffset,yOffset;
    private Window window;

    Mouse(Window window){
        this.window = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        xOffset = e.getX();
        yOffset = e.getY();
        buttonsDown.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonsDown.remove(e.getButton());
        window.setMoveable(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
        if (window.isMoveable()) {
            int newX = e.getXOnScreen() - xOffset;
            int newY = e.getYOnScreen() - yOffset;
            window.setLocation(newX, newY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        position.set(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = window.checkScroll(e.getScrollAmount() * e.getWheelRotation() * 5);
    }

    public Coordinate get() {
        return position;
    }

    public boolean isMouseDown(int button) {
        return buttonsDown.contains(button);
    }

    public void clear() {
        buttonsDown.clear();
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int s) {
        scroll = s;
    }

    public void clearScroll() {
        scroll = 0;
    }

    public boolean isAnyButtonDown(){
        return !buttonsDown.isEmpty();
    }

    public Set<Integer> getPressedButtons(){
        return buttonsDown;
    }
}
