import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class Window extends JFrame {
    private static Window window;
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private int lastMouseX;
    private int lastMouseY;

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
//                JFileChooser fileChooser = new JFileChooser();
//                int retunrVal = fileChooser.showOpenDialog(window);
//                if (retunrVal == JFileChooser.APPROVE_OPTION) {
//                    File file = fileChooser.getSelectedFile();
//                }
                repaint();
            }
        });
        getContentPane().add(button, BorderLayout.NORTH);

        Panel panel = new Panel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    lastMouseX = mouseEvent.getX();
                    lastMouseY = mouseEvent.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                float dx = (mouseEvent.getX() - lastMouseX);
                float dy = (lastMouseY - mouseEvent.getY());
                for (Entity entity : entities)
                    entity.move(dx, dy);
                lastMouseX = mouseEvent.getX();
                lastMouseY = mouseEvent.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

        setVisible(true);

        entities.add(new Line(0,0,100,100));
    }

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {
            for (Entity entity : entities) {
                entity.draw(g);
            }
        }

    }
}
