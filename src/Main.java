import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
       
        final int tileSize = 16; 
        int mapWidth = promptMapDimension("mapWidth (60-100):", tileSize);
        int mapHeight = promptMapDimension("mapHeight (60-100):", tileSize);
        int MAP_WIDTH = mapWidth * tileSize;
        int MAP_HEIGHT = mapHeight * tileSize;

        JFrame window = new JFrame("2D Game");

        Game game = new Game(MAP_WIDTH, MAP_HEIGHT);

        JButton newMapButton = new JButton("Create Map");
        newMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.generateNewMap(); 
                newMapButton.setEnabled(false);
            }
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame(); 
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newMapButton);
        buttonPanel.add(startButton);

        JScrollPane scrollPane = new JScrollPane(game);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        window.add(buttonPanel, BorderLayout.NORTH);
        window.add(scrollPane, BorderLayout.CENTER);

        window.pack();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setLocationRelativeTo(null);

        window.setVisible(true);
    }

    private static int promptMapDimension(String message, int tileSize) {
        String input = JOptionPane.showInputDialog(null, message, "Map Size", JOptionPane.QUESTION_MESSAGE);
        int dimension = 0;
        try {
            dimension = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a number.", "Hata", JOptionPane.ERROR_MESSAGE);
            dimension = promptMapDimension(message, tileSize); 
        }
        return dimension;
    }
}
