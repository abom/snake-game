import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageHelper {
    // adopted from oracle java examples
    static BufferedImage loadImage(String imagePath) {
        BufferedImage img = null;
       	try {
          img = ImageIO.read(new File(imagePath)); //"../art/" +  imageName
        }
        catch(IOException e) {
          // throw e;
        }
        return img;
    }
}