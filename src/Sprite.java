import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Sprite {
    private BufferedImage image;
    private Polygon bounds;
    private Rectangle simpleBounds;

    private boolean simpleBoundsOn = false;

    public Sprite(String path){
        try{
            image = ImageIO.read(new File(path));
        }catch(Exception e){
            System.out.println("Could not load image (" + path + ")");
        }
        cleanBounds();
        simpleBounds = new Rectangle(0,0,getWidth(), getHeight());
    }
    public Sprite(String path, float scale){
        try{
            image = ImageIO.read(new File(path));
        }catch(Exception e){
            System.out.println("Could not load image (" + path + ")");
        }
        scale(scale);
        cleanBounds();
        simpleBounds = new Rectangle(0,0,getWidth(), getHeight());
    }

    public BufferedImage getImage(){
        return image;
    }
    public int getWidth(){
        return image.getWidth();
    }
    public int getHeight() {
        return image.getHeight();
    }

    public Polygon getBounds(){
        return bounds;
    }
    public Rectangle getSimpleBounds(){
        return simpleBounds;
    }
    public void scale(float scale){
        int newWidth = (int) (getWidth() * scale);
        int newHeight = (int) (getHeight() * scale);

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();

        image = scaledImage;
    }

    private void cleanBounds(){
        ArrayList<Coordinate> cords = new ArrayList<>();

        for (int x = 0; x < getWidth(); x++){
            for (int y = 0; y < getHeight(); y++){
                if (!isTransparent(x,y) && hasTransparentNeighbor(x,y)){
                    cords.add(new Coordinate(x,y));
                }
            }
        }

        ArrayList<Coordinate> list = cords;
        ArrayList<Coordinate> sorted = new ArrayList<>();

        int size = list.size();
        if (size == 0){
            return;
        }

        Coordinate current = list.remove(0);
        Coordinate closest = null;

        sorted.add(current);
        double closestDistance;

        for (int n = 0; n < size; n++) {
            closestDistance = -1;
            for (int i = 0; i < list.size(); i++) {

                double d = current.getDistance(list.get(i));

                if (closestDistance > d || closestDistance < 0) {
                    closestDistance = d;
                    closest = list.get(i);
                }
            }
            current = closest;
            list.remove(closest);
            sorted.add(closest);
        }

        cords = sorted;

        int[] xPoints = new int[cords.size()];
        int[] yPoints = new int[cords.size()];

        for (int i = 0; i < cords.size(); i++) {
            xPoints[i] = cords.get(i).getX();
            yPoints[i] = cords.get(i).getY();
        }

        bounds = new Polygon(xPoints, yPoints, cords.size());
    }
    private boolean hasTransparentNeighbor(int x, int y) {
        return isTransparent( x, y - 1) ||
                isTransparent(x, y + 1) ||
                isTransparent(x - 1, y) ||
                isTransparent(x + 1, y);
    }
    private boolean isTransparent(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            int pixel = image.getRGB(x, y);
            int alpha = (pixel >> 24) & 0xff;
            return alpha == 0;
        }
        return false;
    }

    public boolean contains(Coordinate point, Coordinate pos, int scroll){
        if (simpleBoundsOn){
            return simpleBounds.contains(new Point(point.getX() - pos.getX(), point.getY() - pos.getY() + scroll));
        }else {
            return bounds.contains(new Point(point.getX() - pos.getX(), point.getY() - pos.getY() + scroll));
        }
    }

    public void setBoundsSimple(boolean simple){
        simpleBoundsOn = simple;
    }

}
