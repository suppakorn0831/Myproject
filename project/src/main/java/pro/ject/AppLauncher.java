package pro.ject;

import javax.swing.*;


import javax.swing.UIManager;

public class AppLauncher {
    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PageStart().setVisible(true);

            }
        });
    }
}