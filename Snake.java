import java.util.ArrayList;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class Snake {
    //private ArrayList<GraphicsItem> allItems;
    private ArrayList<SnakePart> snakeBody;
    private SnakePart snakeHead;
    private Direction direction;
    private SnakeAction action;
    // int partHeight, partWidth;
    private Rat rat;
    // DEBUG
    // private int collisions;
    // borders
    private Rectangle2D.Double borders;

    public Snake() {
        // default length with 6 parts
        // head + 5 parts
        this(Config.defaultLength);
    }

    // with specific parts
    public Snake(int length) {
        // FIXME: lower/higher bounds for parts number
        // list of parts
        snakeBody = new ArrayList<SnakePart>();
        //allItems = new ArrayList<GraphicsItem>();
        // set defult direction
        direction = Direction.UP;
        // add head
        snakeHead = new SnakePart("art/snake_head.png");
        addPart(snakeHead);

        for(int i=0; i < length; i++)
            addNormalPart();

        // DEBUG
        // collisions = 0;
        // System.out.println(snakeHead.getWidth());
        // System.out.println(snakeHead.getHeight());
    }

    public void setRat(Rat r)
    {
        rat = r;
    }


    public void setSnakeActionListener(SnakeAction act)
    {
        action = act;
    }

    //public void setBorders(HashMap<Direction, Rectangle2D.Double> b)
    public void setBorders(Rectangle2D.Double rect)
    {
        borders = rect;
    }

    // add a given part at any direction (condition)
    // FIXME: add part when stepping have error
    public void addPart(SnakePart part) {
        // add part after the last one
        if(part == null) return;
        if(direction == null) direction = Direction.UP;

        int size = snakeBody.size();
        double lastX = 0, lastY = 0;
        SnakePart lastPart = null;
        
        if (size > 0) {
            lastPart = snakeBody.get(snakeBody.size() - 1);
            lastX = lastPart.getX();
            lastY = lastPart.getY();
        }
        // move to the last coordinates
        part.moveTo(lastX , lastY);
        // place the part at the right location
        // no last part so step with step value of 0
        // for both vertical or horizontal step
        // then reset last direction
        if(lastPart == null) {
            // set default direction reverse
            part.setDirection(reverseDirection(direction));
            part.step(0, 0);
            part.setDirection(direction);
        }
        else {
            // set direction to reverse to add to last
            part.setDirection(reverseDirection(lastPart.getDirection()));
            part.step();
            part.setDirection(lastPart.getDirection());
        }
        // add to his borthers :D
        snakeBody.add(part);
        // debug
        //System.out.println("Part Added at: " + part.getX() + ", " + part.getY());
        //System.out.println("Direction: " + part.getDirection());
    }

    // add a normal snake part
    public void addNormalPart() {
        addPart(new SnakePart());
    }

    public ArrayList<SnakePart> parts() {
        return snakeBody;
    }

    public void setDirection(Direction dir) {
        direction = dir;
    }

    public Direction getDirection() {
        return direction;
    } 

    // the whole snake step
    // FIXME:
    // don't move a parts in the reverse direction
    // (means in the direction of it's previous one)
    public void step() {
        // move the head
        // then every part will replace the next part's place
        double nextX = snakeHead.getX(), nextY = snakeHead.getY();
        // get the curren direction of head
        // and it will be the next direction to others
        Direction nextDirection = snakeHead.getDirection(), currentDirection;
        // set the new head direction
        // which is the whole snake direction
        //System.out.println(nextDirection);
        snakeHead.setDirection(direction);
        // move the head
        snakeHead.step();

        // follow the head
        for(int i = 1; i  < snakeBody.size(); i++) {
            // FIXME:
            // test for collision by the way?
            SnakePart nextPart = snakeBody.get(i);
            // store the current direction
            currentDirection = nextPart.getDirection();
            // set the next direction
            nextPart.setDirection(nextDirection);
            // move to that direction
            nextPart.step();
            // and reset the next to current
            // so the next direction will directed to teh current
            nextDirection = currentDirection;
        }

        checkCollisions();
    }

    public void checkCollisions() {
        // check collision and call a something
        // or make an Event derived class to do so
        // TODO: how to define own events?
        // collisionCallback();

        if(action == null)
            return;

        //test 
        if(snakeHead.collidesWith(rat))
            action.snakeHitsRat();

        //TODO: callback for slef and rat collision
        // test collision with itself and borders 
        // FIXME: test collide with again
        // head collides with others already
        // its previous part has the same coordinates
        for(int i = 1; i  < snakeBody.size(); i++)
            if(snakeHead.collidesWith(snakeBody.get(i)))
                action.snakeHitsItself();


        if(!borders.contains(snakeHead.getBounds()))
            action.snakeHitsBorders();

        for(SnakePart part: snakeBody) {
            if(part == snakeHead)
                continue;

            if(borders.contains(part.getBounds()))
                action.partOutOfBorders(part);
        }
        /*
        Iterator<Direction>  borderIterator = borders.keySet().iterator();
        while(borderIterator.hasNext())
        {
           Direction d = borderIterator.next();
           if(snakeHead.intersects(borders.get(d)))
           {
                //System.out.println(d);
                action.snakeHitsBorderAt(d);
           }
        }
        */
    }

    public Direction reverseDirection(Direction dir) {
        Direction d = Direction.DOWN;

        switch(dir) {
            case UP:
                d = Direction.DOWN;
                break;
            case DOWN:
                d = Direction.UP;
                break;
            case LEFT:
                d = Direction.RIGHT;
                break;
            case RIGHT:
                d = Direction.LEFT;
                break;
        }

        return d;
    }

    // move the whole snake
    public void moveTo(double x, double y) {
        for(SnakePart part: snakeBody) {
            part.moveTo(x + part.getX(), y + part.getY());
        }
    }

    public void draw(Graphics2D g2d)
    {
        for(SnakePart part: snakeBody) {
            part.draw(g2d);
        }
    }

    public void redraw()
    {
        for(SnakePart part: snakeBody) {
            part.redraw();
        }
    }
}