import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Piece {

    boolean[][] piece;
    int height;
    int width;
    int gen;
    int total;
    JFrame pieceWindow;
    public Piece() {

    }


    public void init(int height, int width, int gen, int total) {
        piece = new boolean[height][width];
        this.height = height;
        this.width = width;
        this.gen = gen;
        this.total = total;

    }

    public void window() {

        pieceWindow = new JFrame("piece " + gen);
        pieceWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton[] label = new JButton[height*width];
        for (int i = 0; i < label.length; i++) {
            label[i] = new JButton("");
            label[i].setName(String.valueOf(i));
            label[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = ((JButton) e.getSource());
//                    System.out.println(button.getName());
                    int element = Integer.parseInt(button.getName());
                    if (!piece[element / width][element % width]) {
                        button.setBackground(Color.MAGENTA);
                        piece[element / width][element % width] = true;

                    } else {
                        button.setBackground(Color.GRAY);
                        piece[element / width][element % width] = false;

                    }
                }
            });
            pieceWindow.getContentPane().add(label[i]);
        }


        pieceWindow.getContentPane().setLayout(new GridLayout(height,width));
        pieceWindow.setSize(1920 / total,500);
        pieceWindow.setLocation((1920 / total) * gen, 0);
        pieceWindow.setVisible(true);
        pieceWindow.toFront();
    }
}
