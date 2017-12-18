import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

public class Window extends JFrame {
    private static Window window;

    public static void main(String[] args) {
        window = new Window();
    }

    Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(100,200);
        setSize(300,400);

        JButton button = new JButton("Open image");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int retunrVal = fileChooser.showOpenDialog(window);
                if (retunrVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                }
            }
        });
        getContentPane().add(button, BorderLayout.NORTH);

        Panel panel = new Panel();
        getContentPane().add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.drawLine(-10,-300,150,150);
        }
    }
}
