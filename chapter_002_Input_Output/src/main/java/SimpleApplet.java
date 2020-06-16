import java.applet.Applet;
import java.awt.*;

/*
<applet code = "SimpleApplet" width=200 height=60>
</applet>
 */
public class SimpleApplet extends Applet {
    @Override
    public void paint(Graphics g) {
        g.drawString("A little black magic", 50, 50);
    }
}
