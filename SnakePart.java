// multiple key press
// http://stackoverflow.com/questions/2623995/swings-keylistener-and-multiple-keys-pressed-at-the-same-time
public class SnakePart extends GraphicsItem {
    private Direction direction;
    // double prevX, prevY;
    // or implement a moveTo with Point2D
    // Point2D prevPosition 

    public SnakePart(String partImagePath, int x, int y) {
        super(partImagePath, x, y);
        direction = Direction.UP;
    }

    public SnakePart(String partImagePath) {
        this(partImagePath, 0, 0);
    }

    public SnakePart() {
        this("art/snake_part.png");
    }

    public void setDirection(Direction dir) {
    	direction = dir;
    }

    public Direction getDirection() {
    	return direction;
    }

    public void step() {
        //step(5,5);
        step(getWidth(), getHeight());
    }

    public void step(int horizontalStep, int verticalStep) {
        switch(direction) {
            case UP:
                moveTo(getX(), getY() - verticalStep);
                break;
            case DOWN:
                moveTo(getX(), getY() + verticalStep);
                break;            
            case LEFT:
                moveTo(getX() - horizontalStep, getY());
                break;
            case RIGHT:
                moveTo(getX() + horizontalStep, getY());
                break;
        }
    }

}