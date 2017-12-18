import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

public class Window extends JFrame {

    public static void main(String[] args) {
        Window window = new Window();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(100,200);
        window.setSize(300,400);

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
        window.getContentPane().add(BorderLayout.NORTH, button);

        window.setVisible(true);
        window.repaint();
    }
}
