import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class PicturePuzzleGame extends JFrame implements ActionListener {

    private JPanel panel;
    private JButton[][] buttons;
    private BufferedImage originalImage;
    private BufferedImage[][] subImages;
    private int rows = 4;
    private int cols = 4;
    private int buttonWidth = 200;
    private int buttonHeight = 200;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public PicturePuzzleGame() {
        setTitle("Picture Puzzle Game");
        setSize(cols * buttonWidth, rows * buttonHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        panel = new JPanel(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];

        try {
            originalImage = ImageIO.read(new File("scenery_.jpg"));
            subImages = splitImage(originalImage, rows, cols);
            shuffleImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(this);
                buttons[i][j].setIcon(new ImageIcon(subImages[i][j].getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                panel.add(buttons[i][j]);
            }
        }

        add(panel);
        setVisible(true);
    }

    private BufferedImage[][] splitImage(BufferedImage image, int rows, int cols) {
        int pieceWidth = image.getWidth() / cols;
        int pieceHeight = image.getHeight() / rows;
        BufferedImage[][] pieces = new BufferedImage[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                pieces[y][x] = image.getSubimage(x * pieceWidth, y * pieceHeight, pieceWidth, pieceHeight);
            }
        }
        return pieces;
    }

    private void shuffleImages() {
        Random random = new Random();
        for (int i = rows - 1; i > 0; i--) {
            for (int j = cols - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                BufferedImage temp = subImages[i][j];
                subImages[i][j] = subImages[m][n];
                subImages[m][n] = temp;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (e.getSource() == buttons[i][j]) {
                    if (selectedRow == -1 && selectedCol == -1) {
                        // First button clicked
                        selectedRow = i;
                        selectedCol = j;
                    } else {
                        // Second button clicked, swap images
                        Icon tempIcon = buttons[i][j].getIcon();
                        buttons[i][j].setIcon(buttons[selectedRow][selectedCol].getIcon());
                        buttons[selectedRow][selectedCol].setIcon(tempIcon);
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PicturePuzzleGame());
    }
}
