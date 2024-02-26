import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

public class Window extends JFrame {
    private static ArrayList<Window> windows = new ArrayList<>();
    private static int defaultWindow = 0;

    private boolean isFocused = false;
    private int vsync;
    private JPanel panel;

    public Window(String title){

        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new Keyboard());
        setLocationRelativeTo(null);
        setResizable(true);

        setupListener();
        setupPanel();

        getContentPane().setBackground(new Color(44,44,44));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);
    }

    public Window(String title, int width, int height, boolean rezisable){

        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(width,height);
        setUndecorated(false);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new Keyboard());
        setLocationRelativeTo(null);
        setResizable(rezisable);

        setupListener();
        setupPanel();

        getContentPane().setBackground(new Color(44,44,44));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);
    }

    private void setupListener(){
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                isFocused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                isFocused = false;
                Mouse.clear();
                Keyboard.clear();

            }
        });
    }

    private void setupPanel(){
        panel = new JPanel();
        panel.setOpaque(false);
        setLayout(new BorderLayout());
        Mouse mouse = new Mouse();
        panel.addKeyListener(new Keyboard());
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        panel.addMouseWheelListener(mouse);
        getContentPane().add(panel);
    }

    public static Window getDefault(){
        return windows.get(defaultWindow);
    }
    public static void setDefault(int newDefaultWindow){
        if(newDefaultWindow >= 0 && newDefaultWindow < windows.size()){
            defaultWindow = newDefaultWindow;
        }
    }

    public void allowResize(boolean rezisable){
        setResizable(rezisable);
    }
}
