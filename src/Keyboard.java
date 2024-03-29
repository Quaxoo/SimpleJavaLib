import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Keyboard implements KeyEventDispatcher, KeyListener {

    public final Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());

    }

    public boolean isKeyDown(int key){
        return pressedKeys.contains(key);
    }

    public void clear(){
        pressedKeys.clear();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        }else if(e.getID() == KeyEvent.KEY_RELEASED){
            keyReleased(e);
        }
        return false;
    }

    public boolean isAnyKeyDown(){
       return !pressedKeys.isEmpty();
    }

    public Set<Integer> getPressedKeys(){
        return pressedKeys;
    }
}