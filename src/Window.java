import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

public class Window extends JFrame {
    private static ArrayList<Window> windows = new ArrayList<>();
    private static int defaultWindow = 0;

    private boolean isFocused = false;
    private int vsync;
    private JPanel panel;

    private Render renderThread;
    private Update updateThread;
    private Mouse mouse;
    private Keyboard keyboard;

    public Window(String title){

        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new Keyboard());
        setLocationRelativeTo(null);
        setResizable(false);

        setupListener();
        setupPanel();
        setupThreads();

        getContentPane().setBackground(new Color(44,44,44));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);

        updateThread.start();
        renderThread.start();
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
        setupThreads();

        getContentPane().setBackground(new Color(44,44,44));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);

        updateThread.start();
        renderThread.start();
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
                mouse.clear();
                keyboard.clear();

            }
        });
    }

    public void repaint(){
        panel.repaint();
    }

    private void setupPanel(){
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                renderThread.renderObjects(g);
            }
        };
        panel.setOpaque(false);
        setLayout(new BorderLayout());
        mouse = new Mouse();
        keyboard = new Keyboard();
        panel.addKeyListener(keyboard);
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        panel.addMouseWheelListener(mouse);
        getContentPane().add(panel);
    }

    private void setupThreads(){
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        renderThread = new Render(this);
        updateThread = new Update(this);
    }

    public static Window getDefault(){
        return windows.get(defaultWindow);
    }
    public static void setDefault(int newDefaultWindow){
        if(newDefaultWindow >= 0 && newDefaultWindow < windows.size()){
            defaultWindow = newDefaultWindow;
        }
    }

    public int getFramerate(){
        return vsync;
    }

    public void add(GraphicObject graphicObject){
        renderThread.add(graphicObject);
        updateThread.add(graphicObject);
    }



}
