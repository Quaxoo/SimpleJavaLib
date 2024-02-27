import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Window extends JFrame {
    private static final ArrayList<Window> windows = new ArrayList<>();
    private static int defaultWindow = 0;

    private boolean isFocused = false;
    private int vsync;
    private JPanel panel;

    private Render renderThread;
    private Update updateThread;
    private Mouse mouse;
    private Keyboard keyboard;
    private boolean moveable = false;

    public Window(String title){

        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);


        setLocationRelativeTo(null);
        setResizable(false);

        mouse = new Mouse(this);
        keyboard = new Keyboard();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboard);

        setupListener();
        setupPanel(Toolkit.getDefaultToolkit().getScreenSize());
        setupThreads();

        setBackground(new Color(0,0,0,0));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);

        updateThread.start();
        renderThread.start();

        new ExitButton(this);
        new MoveButton(this);
    }

    public Window(String title, int width, int height, boolean rezisable){

        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(width,height);
        setUndecorated(true);

        setLocationRelativeTo(null);
        setResizable(rezisable);

        mouse = new Mouse(this);
        keyboard = new Keyboard();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboard);

        setupListener();
        setupPanel(getSize());
        setupThreads();

        setBackground(new Color(0,0,0,0));

        setVisible(true);
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        windows.add(this);

        updateThread.start();
        renderThread.start();

        new ExitButton(this);
        new MoveButton(this);
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

    private void setupPanel(Dimension size){
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(getForeground());
                graphics.setColor(new Color(44,44,44));
                graphics.fillRoundRect(0, 0,  size.width- 1, size.height - 1, 30, 30);
                graphics.dispose();
                renderThread.renderObjects(g);
            }
        };
        panel.setOpaque(false);
        setLayout(new BorderLayout());
        panel.addKeyListener(keyboard);
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        panel.addMouseWheelListener(mouse);
        panel.setBackground(new Color(0,0,0,0));
        getContentPane().add(panel);
        panel.requestFocus();
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

    public void setDefault(){
        defaultWindow = windows.indexOf(this);
    }

    public int getFramerate(){
        return vsync;
    }

    public void add(GraphicObject graphicObject){
        renderThread.add(graphicObject);
        updateThread.add(graphicObject);
    }

    public boolean isMouseDown(int button){
        return mouse.isMouseDown(button);
    }
    public Coordinate getMouse(){
        return mouse.get();
    }

    public boolean isAnyKeyDown(){
        return keyboard.isAnyKeyDown();
    }

    public Set<Character> getPressedKeys(){
        Set<Character> chars = new HashSet<>();
        for (int x: keyboard.getPressedKeys()){
            chars.add((char) x);
        }
        return chars;
    }

    public boolean isAnyButtonDown(){
        return mouse.isAnyButtonDown();
    }

    public Set<Integer> getPressedButtons(){
        return mouse.getPressedButtons();
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable){
        this.moveable = moveable;
    }

}
