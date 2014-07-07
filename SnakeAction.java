public interface SnakeAction {
	void snakeHitsRat();
	void snakeHitsItself();	
	void snakeHitsBorders();
	// void partOutOfBorders(SnakePart part, Direction border);
	void partOutOfBorders(SnakePart part);
}