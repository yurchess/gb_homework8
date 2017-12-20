import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Window extends JFrame {
    private static Window window;
    private Entities entities = new Entities();
    private int lastMouseX;
    private int lastMouseY;
    private Panel panel;

    public static void main(String[] args) {
        window = new Window();
    }

    Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(100,200);
        setSize(300,400);

        JPanel btnPanel = new JPanel();
        getContentPane().add(btnPanel, BorderLayout.NORTH);

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
        btnPanel.add(button);
        addAdjustButton(btnPanel);

        panel = new Panel();
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
                float dx = (mouseEvent.getX() - lastMouseX) / entities.getScale();
                float dy = (lastMouseY - mouseEvent.getY()) / entities.getScale();
                entities.move(dx, dy);
                lastMouseX = mouseEvent.getX();
                lastMouseY = mouseEvent.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                float newScale = 1;
                if (mouseWheelEvent.getUnitsToScroll() < 0) { // Up
                    newScale = entities.getScale() * 1.1f;
                    if (newScale > 300) newScale = 300;
                }
                else if (mouseWheelEvent.getUnitsToScroll() > 0) // Down
                    newScale = entities.getScale() * 0.9f;

                float dx = mouseWheelEvent.getX() * (1/newScale - 1/entities.getScale());
                float dy = (mouseWheelEvent.getY() - panel.getHeight()) * (1/entities.getScale() - 1/newScale);
                entities.move(dx, dy);
                entities.setScale(newScale);
                panel.repaint();
            }
        });

        setVisible(true);

        entities.add(new Line(0,0,100,100));
        entities.add(new Circle(100,100,48));
        entities.add(new Arc(0,0,100, 0,180));
        entities.add(new Arc(100,100,50, 0,180));
        setEntityToFullPanel();
    }

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.clearRect(0,0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
            entities.draw(g);
        }
    }

    private void setEntityToFullPanel() {
        double newScale = 1;
        if (entities.getBoundsRect().getWidth() == 0)
            newScale = panel.getHeight() / entities.getBoundsRect().getHeight();
        else if (entities.getBoundsRect().getHeight() == 0)
            newScale = panel.getWidth() / entities.getBoundsRect().getWidth();
        else {
            double xScale = panel.getWidth() / entities.getBoundsRect().getWidth();
            double yScale = panel.getHeight() / entities.getBoundsRect().getHeight();
            newScale = 0.99f * Math.min(xScale, yScale);
        }
        float dx = - entities.getBoundsRect().getLeft();
        float dy = - entities.getBoundsRect().getBottom();
        entities.move(dx, dy);
        entities.setScale((float) newScale);
    }

    private void addAdjustButton(JComponent component) {
        JButton button = new JButton("Adjust");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setEntityToFullPanel();
                panel.repaint();
            }
        });
        component.add(button);
    }
}
