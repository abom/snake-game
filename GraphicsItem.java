import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


// base class for any graphics item with image
// it implements shape for geometry operations

public class GraphicsItem implements Shape {
    private BufferedImage itemImage;
    private Graphics2D itemGraphics, drawingGraphics;
    private AffineTransform itemTransfrom;

    public GraphicsItem(String imagePath, double x, double y) {
        itemImage = ImageHelper.loadImage(imagePath);
        // in case we want to edit the image itself 
        itemGraphics = itemImage.createGraphics();
        itemTransfrom = AffineTransform.getTranslateInstance(x, y);
        moveTo(x, y);
    }

    public GraphicsItem(String imagePath) {
        this(imagePath, 0, 0);
    }

    public double getX() {
        return itemTransfrom.getTranslateX();
    }

    public double getY() {
        return itemTransfrom.getTranslateY();
    }

    public int getWidth() {
        return itemImage.getWidth();
    }

    public int getHeight() {
        return itemImage.getHeight();
    }

    public void moveTo(double x, double y) {
        itemTransfrom.translate(x - getX(), y - getY());
    }

    public void moveTo(Point2D position) {
        itemTransfrom.translate(position.getX() - getX(), position.getY() - getY());
    }

    public boolean collidesWith(Shape other) {
        return this.intersects(other.getBounds2D()) ||
               this.contains(other.getBounds2D());
    }

    public BufferedImage image() {
        return itemImage;
    }

    public Graphics2D graphics() {
        return itemGraphics;
    }

    public AffineTransform transform() {
        return itemTransfrom;
    }


    public void draw(Graphics2D g2d)
    {
        if(g2d == null)
            return;

        if(drawingGraphics == null && drawingGraphics != g2d)
            drawingGraphics = g2d; //to redraw

        g2d.drawImage(itemImage, itemTransfrom, null);
    }

    public void redraw() {
        if(drawingGraphics != null)
            draw(drawingGraphics);
    }

    // Shape related
    // first implement the bounds rect by getting a new rect 
    // and rect 2d form image x, y, w, h
    // then use it in the others
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
    } 

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean contains(double x, double y) {
        return getBounds2D().contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return getBounds2D().contains(x, y, w, h);
    }

    @Override
    public boolean contains(Point2D point) {
        return getBounds2D().contains(point.getX(), point.getY());
    }    

    @Override
    public boolean contains(Rectangle2D r) {
        return getBounds2D().contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform transform) {
        return getBounds2D().getPathIterator(transform);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform transform, double flatness) {
        return getBounds2D().getPathIterator(transform, flatness);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return getBounds2D().intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return getBounds2D().intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

}