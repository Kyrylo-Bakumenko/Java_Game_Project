import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel{

    private final Color almostBlack = new Color(30, 30, 30); // welcome screen background color
    private final Color textColor = new Color(180, 180, 180); // welcome screen text color
    private final Color altTextColor = Color.ORANGE.brighter(); // welcome screen alternative text color
    private final Image cardsHeart = Toolkit.getDefaultToolkit().createImage("images/honor_heart-14.png"); //dim 607x359
    private final Image cardsSpade = Toolkit.getDefaultToolkit().createImage("images/honors_spade-14.png");
    private final double imgScale = (double)2/3;
    private final int offsetti = 50; //random offset amount for visual benefit of text and images

    public WelcomeScreen() {
        super();
        setBackground(almostBlack);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(almostBlack);
        g.drawImage(cardsHeart, 0, offsetti, (int)(cardsHeart.getWidth(null)*imgScale), (int)(cardsHeart.getHeight(null)*imgScale), null);
        g.drawImage(cardsSpade, 1580-(int)(cardsSpade.getWidth(null)*imgScale), offsetti,(int)(cardsSpade.getWidth(null)*imgScale), (int)(cardsSpade.getHeight(null)*imgScale), null);
        addWelcomeText(g);
        repaint();
    }

    public void addWelcomeText(Graphics g){
        int bigGap = 80;
        int smallGap = 50;
        Font myFont = new Font("TNR", Font.PLAIN, 54);
        g.setFont(myFont);
        g.setColor(textColor);
        String text = "Welcome to my Card Game!";
        g.drawString(text, 450, 150+offsetti);
        myFont = new Font("TNR", Font.PLAIN, 24);
        g.setFont(myFont);
        text="\n The goal of the game is to draw cards from Gray and Red decks and then reveal which card is Bigger!";
        g.drawString(text, 150, 270 + bigGap);
        text="\n Then points are accumulated by the following rules: ";
        g.drawString(text, 150, 270 + 2*bigGap);
        g.setColor(altTextColor);
        text="\n\t -Base points are the difference of card sums, card values follow: (1-14, ace-king)";
        g.drawString(text, 150 + smallGap, 270 + 3*bigGap);
        text="\n\t -If the winner's suits are the same, their points are doubled";
        g.drawString(text, 150 + smallGap, 270 + 3*bigGap + 1*smallGap);
        text="\n\t -If both are aces, +11 points regardless of the opponents cards (auto win for the round)";
        g.drawString(text, 150 + smallGap, 270 + 3*bigGap + 2*smallGap);
        text="\n\t -Click 'Compare' to compare the cards and anywhere else to move to the next round";
        g.drawString(text, 150 + smallGap, 270 + 3*bigGap + 3*smallGap);
        text="\n\t -When a player reaches 50 points -- They Win!";
        g.drawString(text, 150 + smallGap, 270 + 3*bigGap + 4*smallGap);
        g.setColor(textColor);
        text="\n Fun! Exit this window to begin!";
        myFont = new Font("TNR", Font.ITALIC, 24);
        g.setFont(myFont);
        g.drawString(text, 150, 270 + 4*bigGap + 4*smallGap);

    }
}
