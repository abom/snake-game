import java.util.ArrayList;
import java.util.Random;
import java.awt.Dimension;
import java.awt.geom.Point2D;

public class Rat extends GraphicsItem {
    ArrayList<Point2D> positions;
    Random random;
    
    public Rat(String ratImagePath, int panelWidth, int panelHeight) {
        super(ratImagePath);
        positions = createPoints(panelWidth, panelHeight);
        random = new Random();

    } 

    public Rat(int panelWidth, int panelHeight) {
        this("art/rat.png", panelWidth, panelHeight);
    }   

    public Rat(Dimension panelSize) {
        this("art/rat.png", panelSize.width, panelSize.height);
    }   

    public Rat() {
        this("art/rat.png", 0, 0);
    }

    public ArrayList<Point2D> createPoints(int w, int h) {
        ArrayList<Point2D> all = new ArrayList<Point2D>();

        // minus getWidth or getHeight
        // so it will no go out of borders
        for(int x=0; x < w - getWidth(); x+=getWidth()) {
            for(int y=0; y < h - getHeight(); y+=getWidth()) {
                all.add(new Point2D.Double(x, y));
            }
        }

        return all;
    }

    public void escapeFrom(Snake snake) {
        ArrayList<Point2D> current = positions;

        for(SnakePart part: snake.parts()) {
            current.remove(new Point2D.Double(part.getX(), part.getY()));
        }

        int randomPosition = random.nextInt(current.size() - 1);
        
        // DEBUG
        // sometimes it goes out of borders
        // and inside the snake
        // System.out.println(current.get(randomPosition));

        moveTo(current.get(randomPosition));
    }
}