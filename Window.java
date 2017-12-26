/**
 * Java 1. HomeWork8. Dxf viewer.
 * @author Yury Mitroshin
 * @version dated Dec 20, 2017
 * @link https://github.com/yurchess/gb_homework8
 */

import GraphicEntity.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Window extends JFrame {
    private static final String WINDOW_TITLE = "DXF Viewer";

    private static Window window;
    private Entities entities = new Entities();
    private int lastMouseX;
    private int lastMouseY;
    private Panel panel;

    public static void main(String[] args) {
        window = new Window();
    }

    private Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(WINDOW_TITLE);
        setWindowInitialSizeAndLoc();

        JPanel btnPanel = new JPanel();
        getContentPane().add(btnPanel, BorderLayout.NORTH);

        JButton button = new JButton("Open .dxf");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().contains(".dxf") || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "DXF file (*.dxf)";
                    }
                });
                int returnVal = fileChooser.showOpenDialog(window);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    setTitle(WINDOW_TITLE + " '" + file.getName() + "'");
                    DXFReader dxfReader = new DXFReader(file.getAbsolutePath());
                    entities = dxfReader.getEntities();
                    if (entities.getSize() > 0) {
                        setEntityToFullPanel();
                    }
                    repaint();
                }
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
                double dx = (mouseEvent.getX() - lastMouseX) / entities.getScale();
                double dy = (lastMouseY - mouseEvent.getY()) / entities.getScale();
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
                double newScale = 1;
                if (mouseWheelEvent.getUnitsToScroll() < 0) { // Up
                    newScale = entities.getScale() * 1.1f;
                    if (newScale > 300) newScale = 300;
                } else if (mouseWheelEvent.getUnitsToScroll() > 0) // Down
                    newScale = entities.getScale() * 0.9f;

                double dx = mouseWheelEvent.getX() * (1 / newScale - 1 / entities.getScale());
                double dy = (mouseWheelEvent.getY() - panel.getHeight()) * (1 / entities.getScale() - 1 / newScale);
                entities.move(dx, dy);
                entities.setScale(newScale);
                panel.repaint();
            }
        });

        setVisible(true);
    }

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.clearRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
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
        double dx = -entities.getBoundsRect().getLeft();
        double dy = -entities.getBoundsRect().getBottom();
        entities.move(dx, dy);
        entities.setScale((float) newScale);
    }

    private void setWindowInitialSizeAndLoc() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.75),(int) (screenSize.height * 0.75));
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
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
