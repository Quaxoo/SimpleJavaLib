import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Window extends JFrame {
    private static final ArrayList<Window> windows = new ArrayList<>();
    private static int defaultWindow = 0;
    private boolean isFocused = false;
    private int vsync;
    private JPanel panel;
    private Dimension size;
    private Render renderThread;
    private Update updateThread;
    private Mouse mouse;
    private Keyboard keyboard;
    private boolean moveable = false;
    private Font font;
    private ArrayList<String> console = new ArrayList<>();

    private int scrollMax = 100, scrollMin = -20;

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

        size = Toolkit.getDefaultToolkit().getScreenSize();
        setupListener();
        setupPanel();
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

        size = getSize();
        setupListener();
        setupPanel();
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

    private void setupPanel(){
        loadFont();
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                BufferedImage content = new BufferedImage(size.width-12,size.height-12, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2D = content.createGraphics();
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2D.setBackground(new Color(0,0,0,0));
                g2D.setColor(new Color(44,44,44));
                g2D.fillRoundRect(0, 0,  size.width - 12, size.height - 12, 30, 30);
                renderThread.renderObjects(g2D);
                g2D.dispose();
                g2D = (Graphics2D) g.create();
                g2D.setColor(new Color(90, 90, 90));
                g2D.fillRoundRect(0, 0,  size.width, size.height, 30, 30);
                g2D.drawImage(content, 6,6,null);
                g2D.dispose();
                writeConsole(g);
            }
        };
        panel.setOpaque(true);
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

    public void loadFont(){
        try {
            File fontFile = new File(System.getProperty("user.dir") + "\\src\\" +"Monocraft.ttf");
            InputStream is = new FileInputStream(fontFile);
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String text){
        console.add(text);
    }
    public void println(int text){
        console.add(String.valueOf(text));
    }
    public void println(double text){
        console.add(String.valueOf(text));
    }
    private void writeConsole(Graphics g){
        g.setFont(font);
        g.setColor(Color.WHITE);
        for (int i = 0; i < console.size(); i++){
            FontMetrics fontMetrics = g.getFontMetrics();
            int y = size.height - (console.size()-i)*(fontMetrics.getHeight()+5) - 10;
            if(y > 20){
                g.drawString(console.get(i), 30, y);
            }else{
                console.remove(i);
            }
        }
    }

    public int getScroll(){
        return mouse.getScroll();
    }

    public void update() {
        updateThread.update();
    }

    public int checkScroll(int scrollDelta){
        if(getScroll() + scrollDelta > scrollMax){
            return scrollMax;
        }
        if(getScroll() + scrollDelta < scrollMin){
            return scrollMin;
        }
        return getScroll() + scrollDelta;
    }
}
