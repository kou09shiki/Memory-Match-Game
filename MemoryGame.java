import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame {
    private int gridSize = 4; // 4x4 grid, 8 pairs
    private CardButton firstSelected = null, secondSelected = null;
    private int matchedPairs = 0;
    private JLabel statusLabel = new JLabel("Find all pairs!", JLabel.CENTER);

    public MemoryGame() {
        setTitle("Colorful Memory Matching Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 580);
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(gridSize, gridSize, 10, 10));
        boardPanel.setBackground(new Color(230,240,255));
        ArrayList<Color> colors = new ArrayList<>();
        // 8 Pairs of distinct colors
        Color[] colorList = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.PINK
        };
        for (int i = 0; i < gridSize * gridSize / 2; i++) {
            colors.add(colorList[i]);
            colors.add(colorList[i]);
        }
        Collections.shuffle(colors);

        for (int i = 0; i < gridSize * gridSize; i++) {
            CardButton card = new CardButton(colors.get(i));
            card.addActionListener(e -> handleCardClick(card));
            boardPanel.add(card);
        }
        statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        statusLabel.setForeground(new Color(88,0,146));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void handleCardClick(CardButton card) {
        if (card.isMatched() || card.isFaceUp()) return;
        if (firstSelected != null && secondSelected != null) return;
        card.showColor();
        if (firstSelected == null) {
            firstSelected = card;
        } else if (secondSelected == null && card != firstSelected) {
            secondSelected = card;
            checkForMatch();
        }
    }

    private void checkForMatch() {
        if (firstSelected.getColor().equals(secondSelected.getColor())) {
            firstSelected.setMatched(true);
            secondSelected.setMatched(true);
            matchedPairs++;
            statusLabel.setText("Matched! " + matchedPairs + " of 8 pairs");
            resetSelectionSoon(true);
            if (matchedPairs == 8) {
                statusLabel.setText("Congratulations! 🎉 You matched all pairs!");
            }
        } else {
            statusLabel.setText("No Match! Try again.");
            resetSelectionSoon(false);
        }
    }

    private void resetSelectionSoon(boolean isMatch) {
        Timer t = new Timer(isMatch ? 600 : 1200, e -> {
            if (!isMatch) {
                firstSelected.hideColor();
                secondSelected.hideColor();
            }
            firstSelected = null;
            secondSelected = null;
            ((Timer)e.getSource()).stop();
        });
        t.start();
    }

    class CardButton extends JButton {
        private Color color; // this card's color
        private boolean faceUp = false;
        private boolean matched = false;

        public CardButton(Color color) {
            this.color = color;
            hideColor();
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }

        public Color getColor() { return color; }

        public boolean isFaceUp() { return faceUp; }
        public boolean isMatched() { return matched; }
        public void setMatched(boolean val) { matched = val; setEnabled(!val); }

        public void showColor() {
            setBackground(color);
            setText("");
            faceUp = true;
        }

        public void hideColor() {
            setBackground(new Color(100, 60, 180));
            setText("?");
            setFont(new Font("Dialog", Font.BOLD, 30));
            setForeground(Color.WHITE);
            faceUp = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}

