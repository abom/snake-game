import java.util.Properties;
import java.awt.Dimension;

// config and default config
// http://stackoverflow.com/questions/9630010/storing-configuration-related-information-in-java
public class Config {
	// 480 * 480 = (20*24) * (24*24)
	static Dimension panelSize = new Dimension(504, 432);
	static int panelWidth = 504;
	static int panelHeight = 432;

	static int defaultLength = 5;
	static int snakeX = 240;
	static int snakeY = 240;
	// static int speed = 200;
	// ...
}