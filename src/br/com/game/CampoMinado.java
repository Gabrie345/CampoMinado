package br.com.game;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
public class CampoMinado implements ActionListener {
    JFrame frame;
    JPanel textPanel;
    JPanel buttonPanel;
    JButton[][] buttons;
    JLabel textField;
    JButton resetButton;
    Random random;
    int size;
    int bombs;
    ArrayList<Integer> xPositions;
    ArrayList<Integer> yPositions;
    int[][] solution;

    public CampoMinado() {
        size = 5;
        bombs = 1;
        
        xPositions = new ArrayList<>();
        yPositions = new ArrayList<>();
        random = new Random();

        generateBombPositions();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

        textPanel = new JPanel();
        textPanel.setVisible(true);
        textPanel.setBackground(Color.pink);

        buttonPanel = new JPanel();
        buttonPanel.setVisible(true);
        buttonPanel.setLayout(new GridLayout(size, size));

        textField = new JLabel();
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setFont(new Font("comic sans", Font.BOLD, 20));
        textField.setForeground(Color.BLUE);
        textField.setText(bombs + " Bombs!!");

        resetButton = new JButton();
        resetButton.setForeground(Color.BLUE);
        resetButton.setBackground(Color.pink);
        resetButton.setText("RESET");
        resetButton.setFont(new Font("comic sans", Font.BOLD, 20));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);


        solution = new int[size][size];


        buttons = new JButton[size][size];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFocusable(false);
                buttons[i][j].setFont(new Font("Comic sans", Font.BOLD, 12));
                buttons[i][j].addActionListener(this);
                buttons[i][j].setText("");
                buttonPanel.add(buttons[i][j]);
            }
        }

        textPanel.add(textField);
        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(buttonPanel);

        frame.add(resetButton, BorderLayout.SOUTH);

        frame.setSize(570, 570);
        frame.revalidate();
        frame.setLocationRelativeTo(null);
        generateSolution();
    }

    public void generateBombPositions() {
        Set<Point> bombPositions = new HashSet<>();
        while (bombPositions.size() < bombs) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            bombPositions.add(new Point(x, y));
        }

        for (Point point : bombPositions) {
            xPositions.add(point.x);
            yPositions.add(point.y);
        }
    }

    public void generateSolution() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (xPositions.contains(x) && yPositions.contains(y)) {
                    solution[y][x] = -1; // Representa uma bomba
                } else {
                    int bombsAround = countBombsAround(x, y);
                    solution[y][x] = bombsAround;
                }
            }
        }
    }
    public int countBombsAround(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (xPositions.contains(i) && yPositions.contains(j)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void check(int y, int x) {
        if (solution[y][x] == -1) {
            gameOver();
        } else {
            buttons[y][x].setText(String.valueOf(solution[y][x]));
        }
    }

    public void gameOver() {
        textField.setForeground(ColorUIResource.RED);
        textField.setText("Game Over");
        for (JButton[] button : buttons) {
            for (int j = 0; j < buttons[0].length; j++) {
                button[j].setEnabled(false);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==resetButton){
            frame.dispose();
            new CampoMinado();
        }
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                if (e.getSource() == buttons[i][j]) {
                    check(i, j);
                }
            }
        }
    }

    public void display(){

    }
    public static void main(String[] args) {
        new CampoMinado();
    }
}