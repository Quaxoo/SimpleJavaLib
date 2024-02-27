import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Sprite {
    private BufferedImage image;
    private Polygon cleanBounds;
    private Coordinate startingPointCleanBounds;

    public Sprite(String path){
        try{
            image = ImageIO.read(new File(path));
        }catch(Exception e){
            System.out.println("Could not load image (" + path + ")");
        }
        cleanBounds();
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

    public Polygon getCleanBounds(){
        return cleanBounds;
    }
    public  Coordinate getStartingPointCleanBounds(){return startingPointCleanBounds;}

    public void scale(float scale){
        int newWidth = (int) (getWidth() * scale);
        int newHeight = (int) (getHeight() * scale);

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g = scaledImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();

        image = scaledImage;
    }

    private void cleanBounds(){
        ArrayList<Point> points = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++){
            for (int y = 0; y < getHeight(); y++){
                if (!isTransparent(x,y) && hasTransparentNeighbor(x,y)){
                    if(startingPointCleanBounds == null){
                        startingPointCleanBounds = new Coordinate(x,y);
                    }
                    points.add(new Point(x,y));
                }
            }
        }
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];

        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
        }

        cleanBounds = new Polygon(xPoints, yPoints, points.size());
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
}
