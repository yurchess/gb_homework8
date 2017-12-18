import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public static void main(String[] args) {
        Window window = new Window();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(100,200);
        window.setSize(300,400);
        window.setVisible(true);

        JButton button = new JButton("MyBtn");
        window.getContentPane().add(BorderLayout.NORTH, button);
    }
}
