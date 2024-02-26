import java.util.ArrayList;

public class Window {
    private static ArrayList<Window> windows = new ArrayList<>();
    private static int defaultWindow = 0;

    public Window(int width, int height){

    }

    public static Window getDefault(){
        return windows.get(defaultWindow);
    }
    public static void setDefault(int newDefaultWindow){
        if(newDefaultWindow >= 0 && newDefaultWindow < windows.size()){
            defaultWindow = newDefaultWindow;
        }
    }
}
