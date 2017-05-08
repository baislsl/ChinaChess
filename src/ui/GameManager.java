package ui;

import chess.*;
import input.InputManager;
import record.Record;
import record.RecordReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_P;

/**
 * Created by baislsl on 17-2-18.
 */

public class GameManager extends JFrame{



    private InputManager inputManager;
    private GameGUI gameGUI;
    private Chess[][] map = new Chess[9][10];
    private ArrayList<SelectBox> selectBoxes =new ArrayList<>();
    private SelectBox redBox = new SelectBox(4,9,false);

    public GameManager(){

        initializeChess();

        init();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case VK_I:
                        System.out.println("Enter the button");
                        inputManager.retract();
                        gameGUI.repaint();
                        break;
                    case VK_O:
                        System.out.println("Saving the record...");
                        inputManager.save();
                        System.out.println("Save successfully!");
                        break;
                    case VK_P:
                        File file = openFile();
                        initializeChess();
                        loadFile(file);
                        gameGUI.repaint();
                        setFocus(true);
                        break;
                    default:
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    private void init(){

        // main layout of the moveChess
        gameGUI = new GameGUI(map, selectBoxes, redBox);

        final int WIDTH = gameGUI.getBackgroundWidth();
        final int HEIGHT = gameGUI.getBackgroundHeight();
        System.out.println("width = " + WIDTH + " height = " + HEIGHT);

        gameGUI.setBounds(0, 0, WIDTH, HEIGHT);
        gameGUI.setSize(WIDTH, HEIGHT);

        inputManager = new InputManager(map,gameGUI, selectBoxes, redBox);

        // the retract button
        JButton retractBtn = new JButton("悔棋");

        // the load button
        JButton loadBtn = new JButton("载入");

        //the save button
        JButton saveBtn = new JButton("保存");

        add(retractBtn);add(loadBtn);add(saveBtn);
        add(gameGUI);

        setSize(WIDTH + 200, HEIGHT);
        retractBtn.setBounds(WIDTH + 40, 3*HEIGHT/9, 80, HEIGHT/9);
        loadBtn.setBounds(WIDTH + 40, 5*HEIGHT/9, 80, HEIGHT/9);
        saveBtn.setBounds(WIDTH + 40, 7*HEIGHT/9, 80, HEIGHT/9);


        retractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Enter the button");
                inputManager.retract();
                gameGUI.repaint();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saving the record...");
                inputManager.save();
                System.out.println("Save successfully!");
            }
        });

        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = openFile();
                initializeChess();
                loadFile(file);
                gameGUI.repaint();
                setFocus(true);
            }
        });

       setFocus(true);
    }

    private void setFocus(boolean flag){
        addKeyListener(this.inputManager);
        setFocusable(true);
    }

    private void initializeChess(){
        for(int x=0;x<9;x++)
            for (int y=0;y<10;y++)
                map[x][y] = null;

        //将
        map[4][9] = new King(new Point(4,9), Chess.ID.RED, map);
        map[4][0] = new King(new Point(4,0), Chess.ID.BLACK, map);

        //士,仕
        map[3][9] = new Mandarin(new Point(3,9), Chess.ID.RED, map);
        map[5][9] = new Mandarin(new Point(5,9), Chess.ID.RED, map);
        map[3][0] = new Mandarin(new Point(3,0), Chess.ID.BLACK, map);
        map[5][0] = new Mandarin(new Point(5,0), Chess.ID.BLACK, map);

        //相,象
        map[2][0] = new Bishop(new Point(2,0), Chess.ID.BLACK, map);
        map[6][0] = new Bishop(new Point(6,0), Chess.ID.BLACK, map);
        map[2][9] = new Bishop(new Point(2,9), Chess.ID.RED, map);
        map[6][9] = new Bishop(new Point(6,9), Chess.ID.RED, map);

        //马
        map[1][0] = new Knight(new Point(1,0), Chess.ID.BLACK, map);
        map[7][0] = new Knight(new Point(7,0), Chess.ID.BLACK, map);
        map[1][9] = new Knight(new Point(1,9), Chess.ID.RED, map);
        map[7][9] = new Knight(new Point(7,9), Chess.ID.RED, map);

        //车
        map[0][0] = new Rook(new Point(0,0), Chess.ID.BLACK, map);
        map[8][0] = new Rook(new Point(8,0), Chess.ID.BLACK, map);
        map[0][9] = new Rook(new Point(0,9), Chess.ID.RED, map);
        map[8][9] = new Rook(new Point(8,9), Chess.ID.RED, map);

        //炮
        map[1][2] = new Cannon(new Point(1,2), Chess.ID.BLACK, map);
        map[7][2] = new Cannon(new Point(7,2), Chess.ID.BLACK, map);
        map[1][7] = new Cannon(new Point(1,7), Chess.ID.RED, map);
        map[7][7] = new Cannon(new Point(7,7), Chess.ID.RED, map);

        //兵
        for(int i=0;i<10;i+=2){
            map[i][3] = new Pawn(new Point(i,3), Chess.ID.BLACK, map);
            map[i][6] = new Pawn(new Point(i,6), Chess.ID.RED, map);
        }
    }

    private File openFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileNameExtensionFilter("xml格式文件", "xml"));
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return null;
        File f = chooser.getSelectedFile();
        return f;

    }

    private void loadFile(File file){
        RecordReader reader = new RecordReader(file);
        Record record;
        inputManager = new InputManager(map,gameGUI, selectBoxes, redBox);
        while((record = reader.getNextStep())!= null){
            inputManager.conductRecord(record);
        }
        this.addKeyListener(inputManager);
        setFocusable(true);
    }
}
