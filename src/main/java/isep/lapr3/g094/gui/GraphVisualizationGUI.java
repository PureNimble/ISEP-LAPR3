package isep.lapr3.g094.gui;

import isep.lapr3.g094.struct.graph.Edge;
import isep.lapr3.g094.struct.graph.map.MapGraph;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import isep.lapr3.g094.domain.type.Location;

class TransformableJLabel extends JLabel {
    AffineTransform at = new AffineTransform();

    public TransformableJLabel(ImageIcon icon) {
        super(icon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(((ImageIcon) getIcon()).getImage(), at, null);
    }
}

public class GraphVisualizationGUI extends JFrame {

    private JSVGCanvas svgCanvas;

    @SuppressWarnings("unchecked")
    public GraphVisualizationGUI(MapGraph mapGraph) throws FileNotFoundException {
        super("Graphviz Example");

        StringBuilder dotBuilder = new StringBuilder("digraph G {\n");

        // Add nodes
        mapGraph.vertices().forEach(vertex -> {
            Location location = (Location) vertex;
            dotBuilder.append(String.format("    %s;\n", location.getId()));
        });

        // Add edges
        mapGraph.edges().forEach(edge -> {
            Edge edg = (Edge) edge;
            String originId = ((Location) edg.getVOrig()).getId();
            String destId = ((Location) edg.getVDest()).getId();
            dotBuilder.append(
                    String.format("    %s -> %s [label=\"%s\"];\n", originId, destId, edg.getWeight().toString()));
        });
        dotBuilder.append("}\n");

        String dotString = dotBuilder.toString();

        String pngFile1 = "src/main/resources/esinf/graph.png";
        try (PrintWriter out = new PrintWriter("graph.dot")) {
            out.println(dotString);
        }

        try {
            Process process = new ProcessBuilder("dot", "-Tpng", "graph.dot", "-o", pngFile1).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Load the PNG image
            File pngFile = new File("src/main/resources/esinf/graph.png");
            BufferedImage img = ImageIO.read(pngFile);

            // Create a JLabel to display the image
            JLabel label = new JLabel(new ImageIcon(img)) {
                AffineTransform at = new AffineTransform();

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(img, at, null);
                }
            };

            Point dragStartPoint = new Point();
            label.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    dragStartPoint.setLocation(e.getPoint());
                }
            });
            TransformableJLabel transformableLabel = new TransformableJLabel(new ImageIcon(img));
            label.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    Point dragEndPoint = e.getPoint();
                    double dx = dragEndPoint.getX() - dragStartPoint.getX();
                    double dy = dragEndPoint.getY() - dragStartPoint.getY();
                    ((TransformableJLabel) e.getSource()).at.translate(dx, dy);
                    dragStartPoint.setLocation(dragEndPoint);
                    label.repaint();
                }
            });

            add(label, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 1000);
        setVisible(true);
    }

    @Override
    public void dispose() {
        if (svgCanvas != null) {
            if (svgCanvas.getUpdateManager() != null) {
                svgCanvas.getUpdateManager().getUpdateRunnableQueue().getThread().interrupt();
            }
            svgCanvas.dispose();
        }
        super.dispose();
    }
}