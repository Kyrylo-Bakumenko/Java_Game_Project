import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CardGame {
    static int width = 1600;
    static int height = 900;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setVisible(false);
        window.add(new CardWindow());

        JFrame welcome = new JFrame();
        welcome.add(new WelcomeScreen());
        welcome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowAdapter myWelcome = new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                welcome.setSize(width, height);
            }

            public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(null,
                        "('Yes' to go to game)" + '\n' +
                        "('No' to continue reading)", "Continue to game?", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    welcome.setVisible(false);
                    window.setVisible(true);
                } else if (answer == JOptionPane.NO_OPTION) {
                    welcome.setVisible(true);
                }
            }
        };
        welcome.addWindowListener(myWelcome);
        welcome.setVisible(true);

        WindowAdapter myWindow = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(CardWindow.resetAble) {
                    int answer = JOptionPane.showConfirmDialog(null, "'Yes' to return to rule screen \n 'No' to continue playing", "Review rules?", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        welcome.setVisible(true);
                        window.setVisible(false);
                    }
                }else{
                    int answer = JOptionPane.showConfirmDialog(null, "'Yes' to quit \n 'No' to stay", "Exit Game?", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        welcome.setVisible(false);
                        window.setVisible(false);
                        welcome.dispose();
                        window.dispose();
                    }
                }
            }
        };
        window.addWindowListener(myWindow);
    }
}

