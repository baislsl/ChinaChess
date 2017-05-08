import ui.GameManager;

import javax.swing.*;

/**
 * Created by baislsl on 17-2-18.
 */
public class Main {
    public static void main(String[] args){
        GameManager gameManager = new GameManager();
        gameManager.setLocationRelativeTo(null);
        gameManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameManager.setVisible(true);
    }
}
