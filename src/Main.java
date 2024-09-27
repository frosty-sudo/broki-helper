import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.util.Scanner;  // Import the Scanner class

import static java.lang.System.console;
import static java.lang.System.exit;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static int boardHeight = 7;
    public static int boardWidth = 5;
    public static Piece[] numberSegments;
    public static Scanner scanner;
    public static int[][] demons;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final boolean gluttony = true;

    public static void main(String[] args) {



        scanner = new Scanner(System.in);

        int[][] board = variableSetter();



        for (Piece segment : numberSegments) {
            segment.window();
        }



        JFrame acceptWindow = new JFrame("HelloWorldSwing");
        acceptWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton ok = new JButton("OK");
        acceptWindow.add(ok);
        acceptWindow.setSize(200,200);
        acceptWindow.setLocation(0,500);

        acceptWindow.setVisible(true);
        acceptWindow.toFront();

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateSpaces(numberSegments, 0, board);
                acceptWindow.setVisible(false);
                for (Piece segment : numberSegments) {
                    segment.pieceWindow.setVisible(false);
                }
                exit(0);
            }
        });


    }




    static void calculateSpaces(Piece[] segments, int segment, int[][] previewBoard) {

        boolean[][] piece = segments[segment].piece;

        outer:
        for (int i = 0; i < (boardHeight-piece.length+1)*(boardWidth-piece[0].length+1); i++)
        {

            int [][] board = new int[previewBoard.length][];
            for(int element = 0; element < previewBoard.length; element++)
                board[element] = previewBoard[element].clone();

            int pieceLen = (boardWidth - (piece[0].length-1));

            int yOffset = i / pieceLen;
            int xOffset = i % pieceLen;


            int demonCounter = 0;
            for (int y = 0; y < piece.length; y++) {
                for (int x = 0; x < piece[0].length; x++) {
                    if (piece[y][x]) {
                        for (int demon = 0; demon < demons.length; demon++) {
                            if (demons[demon][1]-1 == y + yOffset && demons[demon][0]-1 == x + xOffset) {
                                demonCounter++;
                            }
                        }
                        if (board[y + yOffset][x + xOffset] >= 0 || demonCounter > 1 || (gluttony && board[y + yOffset][x + xOffset] == -0x11111111)) {
                            continue outer;
                        }
                        board[y + yOffset][x + xOffset] = segment;
                    }
                }
            }
            if (segment+1 < segments.length) {

                calculateSpaces(segments, segment+1, board);
            } else {
                showBoard(board);
            }
        }
    }



    static void showBoard(int[][] board) {
        for (int y = 0; y < board.length; y++) {
            System.out.println();
            for (int x = 0; x < board[0].length; x++) {
                String character = "-";
                switch (board[y][x]){
                    case 0:
                        character = "\u001B[31m@";
                        break;
                    case 1:
                        character = "\u001B[32m*";
                        break;
                    case 2:
                        character = "\u001B[33m+";
                        break;
                    case 3:
                        character = "\u001B[34mO";
                        break;
                    case 4:
                        character = "\u001B[35m%";
                        break;
                    case 5:
                        character = "\u001B[36m#";
                        break;
                    case 6:
                        character = "\u001B[37m§";
                        break;
                    case 7:
                        character = "\u001B[93m£";
                        break;
                    case -0x11111111:
                        character = " ";
                        break;
                }
//                System.out.print("|" + (board[y][x]? "True" : "False")  + "|");
//                System.out.print("| " + (board[y][x] >= 0 || board[y][x] == -0x11111111 ? character + ANSI_RESET : "-")  + " |");
                System.out.print("| " + (character + ANSI_RESET)  + " |");
//                System.out.print("|#|");
            }

        }
        System.out.println();
        System.out.println();
    }




    static int[][] variableSetter() {






        System.out.println("Height, width and amount of blocks");
        boardHeight = scanner.nextInt();
        boardWidth = scanner.nextInt();
        numberSegments = new Piece[scanner.nextInt()];

        int[][] board = new int[boardHeight][boardWidth];
        for(int[] arr1 : board) {
            Arrays.fill(arr1, -1);
        }

        switch (2) {
            case 1:
                System.out.println("How many demons?");
                demons = new int[scanner.nextInt()][2];

                for (int i = 0; i < demons.length; i++) {
                    demons[i][0] = scanner.nextInt();
                    demons[i][1] = scanner.nextInt();
                }
                break;
            case 2:
                ArrayList<int[]> demonsArray = new ArrayList<int[]>();


                JFrame demonPlacer = new JFrame("Click on the demons");
                JPanel p = new JPanel(new GridLayout(boardHeight, boardWidth)); //PREFERRED!

                demonPlacer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton[] buttons = new JButton[boardWidth*boardHeight];

                for(int i = 0; i < buttons.length; i++) {

                    buttons[i] = new JButton("");
                    buttons[i].setName(String.valueOf(i));
                    p.add(buttons[i]);
                    buttons[i].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JButton button = (JButton) e.getSource();
                            int element = Integer.parseInt(button.getName());


                            if (e.getButton() == 1) {
                                boolean tapped = false;
                                for (int x = 0; x < demonsArray.size(); x++) {
                                    if (demonsArray.get(x)[0] == 1+(element % boardWidth) && demonsArray.get(x)[1] == 1+(element / boardWidth)) {
                                        demonsArray.remove(x);
                                        button.setBackground(Color.GRAY);
                                        tapped = true;
                                        break;
                                    }
                                }

                                if (!tapped) {
                                    demonsArray.add(new int[]{
                                            1+(element % boardWidth),
                                            1+(element / boardWidth)
                                    });

                                    demons = new int[demonsArray.size()][2];

                                    for (int i = 0; i < demons.length; i++) {
                                        demons[i] = demonsArray.get(i);
                                    }
                                    button.setBackground(Color.MAGENTA);
                                }

                            } else if (e.getButton() == 3 && gluttony) {
                                button.setBackground(Color.GREEN);
                                for (int x = 0; x < demonsArray.size(); x++) {
                                    if (demonsArray.get(x)[0] == 1+(element % boardWidth) && demonsArray.get(x)[1] == 1+(element / boardWidth)) {
                                        demonsArray.remove(x);
                                        break;
                                    }
                                }
                                board[(element / boardWidth)][(element % boardWidth)] = -0x11111111;
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }


                    });
                }

                JButton windowCloser = new JButton("OK");
                windowCloser.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        demonPlacer.setVisible(false);
                    }
                });

                demonPlacer.add(p);

                demonPlacer.add(windowCloser);
//                demonPlacer.setLayout(new GridLayout(boardHeight, boardWidth));
                demonPlacer.setLayout(new GridLayout(2,1));
                demonPlacer.setSize(500,500);
                demonPlacer.setVisible(true);

        }





        for (int i = 0; i < numberSegments.length; i++) {
            numberSegments[i] = new Piece();
            System.out.println("Enter " + i + " height and width");
            numberSegments[i].init(scanner.nextInt(), scanner.nextInt(), i, numberSegments.length);
        }


        return board;
    }
}