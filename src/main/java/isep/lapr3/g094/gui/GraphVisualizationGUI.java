package isep.lapr3.g094.gui;
import javax.swing.*;


public class GraphVisualizationGUI extends JFrame {
    
    public void showGraph() {
        JFrame frame = new JFrame("Instruções");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocation(1430, 0);

        frame.setAlwaysOnTop(true);

        frame.getContentPane().setBackground(java.awt.Color.LIGHT_GRAY);

        java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 14);

        JLabel label = new JLabel(
                "<html><body style='width: 100%; padding: 10px;'><h2>Bem vindo ao tutorial de visualização do grafo!</h2>"
                        + "<p>Neste tutorial iremos demonstrar passo a passo como visualizar o grafo gerado no site Cosmograph. Sendo assim, vamos lá começar:</p>"
                        + "<ol>"
                        + "<li>Clique no botão 'Load Graph'</li>"
                        + "<li>Clique no botão 'Select Data File'</li>"
                        + "<li>Selecione o ficheiro .csv gerado pelo programa</li>"
                        + "<li>Clique no botão 'Launch'</li>"
                        + "</ol>"
                        + "<p>E já está! O grafo foi gerado com sucesso!</p></body></html>");

        label.setFont(font);

        frame.getContentPane().add(label);
        frame.setVisible(true);
    }
}