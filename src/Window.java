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
    ExitConsoleButton exitConsoleButton;
    private int consoleState = 0;
    private int scrollMax = 100, scrollMin = -20;
    private int outline = 6;

    private Color background = new Color(44,44,44);
    private Color border = new Color(90,90,90);
    private Color consoleBackground = new Color(15,15,15, (int) (255 * 0.9f));
    private Color opaque = new Color(0,0,0,0);
    private int arc = 30;
    private int closedConsoleHeight = 25;

    public Window(String title){

        super(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        size = Toolkit.getDefaultToolkit().getScreenSize();
        setResizable(false);
        setUp();
    }

    public Window(String title, int width, int height, boolean rezisable){

        super(title);
        setSize(width,height);
        size = getSize();
        setResizable(rezisable);
        setUp();
    }

    public void setUp(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        mouse = new Mouse(this);
        keyboard = new Keyboard();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboard);

        setupListener();
        setupPanel();

        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        renderThread = new Render(this);
        updateThread = new Update(this);
        setBackground(opaque);
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

                BufferedImage content = new BufferedImage(size.width - 2 * outline,size.height - 2 * outline, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2D = content.createGraphics();

                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2D.setBackground(opaque);
                g2D.setColor(background);
                g2D.fillRoundRect(0, 0,  size.width - 2 * outline, size.height - 2 * outline, arc, arc);

                renderThread.renderObjects(g2D);
                g2D.dispose();

                g2D = (Graphics2D) g.create();
                g2D.setColor(border);
                g2D.fillRoundRect(0, 0,  size.width, size.height, arc, arc);
                g2D.drawImage(content, outline,outline,null);
                g2D.dispose();
                if (doesConsoleExist()){
                    g.setColor(consoleBackground);
                    if (isConsoleOpen()){
                        g.fillRoundRect(outline, size.height - outline - getConsoleHeight(),  size.width - 2*outline, getConsoleHeight(), arc, arc);
                        writeConsole(g);
                    }else {
                        g.fillRoundRect(outline, size.height - outline - closedConsoleHeight,  size.width - 2*outline, closedConsoleHeight, arc, arc);
                    }
                    exitConsoleButton.draw(g, false);
                }
            }
        };
        panel.setOpaque(true);
        setLayout(new BorderLayout());
        panel.addKeyListener(keyboard);
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        panel.addMouseWheelListener(mouse);
        panel.setBackground(opaque);
        getContentPane().add(panel);
        panel.requestFocus();
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
    public void removeRender(GraphicObject graphicObject){
        renderThread.remove(graphicObject);
    }
    public void addRender(GraphicObject graphicObject){
        renderThread.add(graphicObject);
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
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String text){
        if (!doesConsoleExist()){
            exitConsoleButton = new ExitConsoleButton();
            setConsole(2);
        }
        console.add(text);
    }
    public void println(int text){
        println(String.valueOf(text));
    }
    public void println(double text){
        println(String.valueOf(text));
    }
    private void writeConsole(Graphics g){
        g.setFont(font);
        g.setColor(Color.WHITE);
        for (int i = 0; i < console.size(); i++){
            FontMetrics fontMetrics = g.getFontMetrics();
            int y = size.height - (console.size()-i) * (fontMetrics.getHeight() + 5);
            if(y > getHeight() - (getConsoleHeight() - fontMetrics.getHeight())){
                g.drawString(console.get(i), 5 * outline, y);
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

    public boolean isConsoleOpen(){
        return consoleState == 2;
    }
    public boolean doesConsoleExist(){
        return consoleState != 0;
    }
    public void setConsole(int state){
        consoleState = state;
    }

    public int getConsoleHeight(){
        return size.height/3;
    }

    public int getWidth(){
        return size.width;
    }
    public int getHeight(){
        return size.height;
    }

    public int getOutline(){
        return outline;
    }

    public int getClosedConsoleHeight() {
        return closedConsoleHeight;
    }
}
