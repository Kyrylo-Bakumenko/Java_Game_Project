import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CardWindow extends JPanel implements MouseListener, MouseMotionListener {

    // src for "image.jpg" Image by <a href="https://pixabay.com/users/fixipixi_deluxe-2038891/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=1289288">Al Buettner</a> from <a href="https://pixabay.com/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=1289288">Pixabay</a>
    // src for "background2.jpg" https://www.reddit.com/r/tabletopsimulator/comments/35qk30/i_made_a_felt_tabletop_for_your_custom_large/
    private static final Image background = Toolkit.getDefaultToolkit().createImage("images/background2.jpg");
    private static final Image background2 = Toolkit.getDefaultToolkit().createImage("images/image.jpg");
    private static final Image grayBackStack = Toolkit.getDefaultToolkit().createImage("images/gray_back_stack.png");
    private static final Image redBackStack = Toolkit.getDefaultToolkit().createImage("images/red_back_stack.png");
    private Card yourCard = makeNewCard(); // card dim: 172x264, cardS dim: 193x296
    private Card theirCard = makeNewCard();
    private Card yourCardTwo = makeNewCard();
    private Card theirCardTwo = makeNewCard();
    private final Card redCard = redCard();
    private final Card grayCard = grayCard();
    private final Card redCardTwo = redCard();
    private final Card grayCardTwo = grayCard();
    private final Card[] cards = new Card[]{redCard, redCardTwo, grayCard, grayCardTwo};
    private boolean lockedOne = false;
    private boolean lockedTwo = false;
    private boolean lockedThree = false;
    private boolean lockedFour = false;
    private boolean revealed = false;
    private int grayScore = 0;
    private int redScore = 0;
    static boolean resetAble = true;
    private static final int endCutoff = 50;
    private boolean altColors = false;
    private static final int sCardWidth = 193; // selected card width
    private static final int sCardHeight = 296; // selected card height
    private static final int cardWidth = 172; // card width
    private static final int cardHeight = 264; // card height
    private static final int cardStackOffset = 50; // how far left & right decks are offset from left and right of window border
    private static final int boxInitialX = 450; // X location for initial box (grey and red cards), all others are displaced left from this
    private static final int boxInitialY = 500; // X location for initial box (grey and red cards); however, no displacement occurs
    private static final int boxSecondX = 150; // location for extra offset of card boxes (used for secondary boxes with their secondary nature denoted by doubling names: leftleft, rightright)
    private static final int boxPaddingX = 20; // how much padding is added to box width to allow for card visibility when drag-hovering and selected in X direction
    private static final int boxPaddingY = 26; // how much padding is added to box width to allow for card visibility when drag-hovering and selected in Y direction
    private static final int boxArcRoundness = 20; // parameter for rounding boxes
    private static final int buttonWidth = 360; // width of main center button
    private static final int buttonHeight = 100; // height of main center button
    private static final int toggleWidth = 80; // toggle button width
    private static final int toggleHeight = 30; // toggle button height
    private static final int scoreBoxDim = 100; // dimension for square score boxes
    private static final int scoreBoxArcRoundness = 20; // arc rounding parameter for score boxes
    //this breaks everything for some reason //private int 2000 = CardTester.width + 50; // puts the cards offscreen, closest offscreen with 50 pixel pad to prevent accidental selection
    private static final Color defaultBoxColor = new Color(45, 45, 45); // default black button color
    private static final Color toggleColor = new Color(0, 25, 0); // toggle button color9ikjmn
    private static final Color altBoxColor = new Color(32, 16, 36); // alt box color
    private static final Color redWinColor = new Color(180, 0, 0); // text color for when red wins
    private static final Color grayWinColor = new Color(160, 160, 160); // text color for when gray wins
    // scoring is as follows: base points is the difference of card sums, if the suits are the same, this is doubled, if both aces, +11 auto win

    public CardWindow() {
        super();
        placeRedsCard(redCard);
        placeGraysCard(grayCard);
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(defaultBoxColor);
        g.drawImage(background, 0, 0, CardGame.width, CardGame.height, null);
        if (altColors)
            g.drawImage(background2, 0, 0, CardGame.width, CardGame.height, null);
        g.drawImage(grayBackStack, cardStackOffset, cardStackOffset, null);
        g.drawImage(redBackStack, CardGame.width - cardStackOffset - redBackStack.getWidth(null), cardStackOffset, null);
        drawAll(g);
        if (!revealed)
            createButtonMessage(g);
        else if (resetAble)
            createWinMessage(g);
        else
            createEndMessage(g);
        yourCard.paint(g);
        theirCard.paint(g);
        yourCardTwo.paint(g);
        theirCardTwo.paint(g);
        redCard.paint(g);
        grayCard.paint(g);
        grayCardTwo.paint(g);
        redCard.paint(g);
        redCardTwo.paint(g);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Card card : cards) {
            card.setSelect(card.contains(e.getX(), e.getY()) && card.getMoveable());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        for (Card card : cards) {
            if (card.contains(e.getX(), e.getY()) && card.getMoveable()) {
                card.toggleSelect();
                card.setDrag(true);
                card.setDragStart(e.getPoint());
            } else {
                card.setSelect(false);
            }
        }
        if (insideButton(e) && allLocked() && resetAble && !revealed) {
            revealCards();
            addPoints(yourCard, yourCardTwo, theirCard, theirCardTwo);

        }
        if (revealed && !insideButton(e) && !insideToggle(e)) {
            reset();
        }
        toggleBackground(e);
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        for (Card card : cards) {
            if (insideLeftLeftBox(card) && card.equals(grayCard) || insideLeftLeftBox(card) && card.equals(grayCardTwo)) {
                lockLeftLeftBox(card);
            } else if (insideLeftBox(card) && card.equals(grayCard) || insideLeftBox(card) && card.equals(grayCardTwo)) {
                lockLeftBox(card);
            } else if (insideRightBox(card) && card.equals(redCard) || insideRightBox(card) && card.equals(redCardTwo)) {
                lockRightBox(card);
            } else if (insideRightRightBox(card) && card.equals(redCard) || insideRightRightBox(card) && card.equals(redCardTwo)) {
                lockRightRightBox(card);
            }
            card.setDrag(false);
            card.setSelect(false);
        }
        this.repaint();
        //end repaint loop
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("OoB");
        for (Card card : cards) {
            card.setSelect(false);
        }
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println(e.getX() + ", " + e.getY());
        for (Card card : cards) {
            if (card.getSelect() && card.getDrag() && card.contains(e.getX(), e.getY())) {
                card.dragTo(e.getPoint());
            }
        }
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void drawPlaceBoxes(Graphics g, int x, int y) {
        g.fillRoundRect(x, y, sCardWidth + boxPaddingX, sCardHeight + boxPaddingX, 30, 30);
    }

    public Card makeNewCard() {
        String[] suits = new String[]{"C", "D", "H", "S"};
        Card newCard = new Card((int) (Math.random() * 13 + 1), suits[(int) (Math.random() * 4)]);
        newCard.setX(2000);
        newCard.setY(2000);
        return newCard;
    }

    public Card redCard() {
        Card newCard = new Card(-1, "");
        newCard.setX(2000);
        newCard.setY(2000);
        return newCard;
    }

    public Card grayCard() {
        Card newCard = new Card(0, "");
        newCard.setX(2000);
        newCard.setY(2000);
        return newCard;
    }

    public boolean insideRightDeck(MouseEvent e) {
        return e.getX() > CardGame.width - cardStackOffset - redBackStack.getWidth(null) && e.getX() < CardGame.width - cardStackOffset && e.getY() > cardStackOffset && e.getY() < cardStackOffset + redBackStack.getHeight(null);
    }

    public boolean insideLeftDeck(MouseEvent e) {
        return e.getX() > cardStackOffset && e.getX() < cardStackOffset + grayBackStack.getWidth(null) && e.getY() > cardStackOffset && e.getY() < cardStackOffset + grayBackStack.getHeight(null);
    }

    public void revealTheirCard() {
        theirCard.setX(CardGame.width - boxInitialX - cardWidth - boxPaddingX);
        theirCard.setY(boxInitialY + boxPaddingY);
        theirCardTwo.setX(CardGame.width - boxInitialX - cardWidth - boxPaddingX + 300);
        theirCardTwo.setY(boxInitialY + boxPaddingY);
        redCard.setX(2000);
        redCard.setY(2000);
        redCardTwo.setX(2000);
        redCardTwo.setY(2000);
    }

    public void revealYourCard() {
        yourCard.setX(boxInitialX + boxPaddingX);
        yourCard.setY(boxInitialY + boxPaddingY);
        yourCardTwo.setX(boxSecondX + boxPaddingX);
        yourCardTwo.setY(boxInitialY + boxPaddingY);
        grayCard.setX(2000);
        grayCard.setY(2000);
        grayCardTwo.setX(2000);
        grayCardTwo.setY(2000);
    }

    public void revealCards() {
        revealTheirCard();
        revealYourCard();
        revealed = true;
        this.repaint();
    }

    public void lockLeftBox(Card card) {
        card.setMoveable(false);
        card.setX(boxInitialX + boxPaddingX);
        card.setY(boxInitialY + boxPaddingY);
        lockedTwo = true;
        if (!lockedOne)
            placeGraysCard(grayCardTwo);
    }

    public void lockLeftLeftBox(Card card) {
        card.setMoveable(false);
        card.setX(boxSecondX + boxPaddingX);
        card.setY(boxInitialY + boxPaddingY);
        lockedOne = true;
        if (!lockedTwo)
            placeGraysCard(grayCardTwo);
    }

    public void lockRightBox(Card card) {
        card.setMoveable(false);
        card.setX(CardGame.width - boxInitialX - cardWidth - boxPaddingX);
        card.setY(boxInitialY + boxPaddingY);
        lockedThree = true;
        if (!lockedFour)
            placeRedsCard(redCardTwo);
    }

    public void lockRightRightBox(Card card) {
        card.setMoveable(false);
        card.setX(CardGame.width - boxSecondX - cardWidth - boxPaddingX);
        card.setY(boxInitialY + boxPaddingY);
        lockedFour = true;
        if (!lockedThree)
            placeRedsCard(redCardTwo);
    }

    public boolean insideLeftBox(Card card) {
        return card.getX() + sCardWidth / 2 > boxInitialX && card.getX() + sCardWidth / 2 < 663 && card.getY() + sCardHeight / 2 > boxInitialY && card.getY() + sCardHeight / 2 < 816 && card.getMoveable();
    }

    public boolean insideLeftLeftBox(Card card) {
        return card.getX() + sCardWidth / 2 > boxSecondX && card.getX() + sCardWidth / 2 < 363 && card.getY() + sCardHeight / 2 > boxInitialY && card.getY() + sCardHeight / 2 < 816 && card.getMoveable();
    }

    public boolean insideRightBox(Card card) {
        return card.getX() + sCardWidth / 2 > CardGame.width - boxInitialX - (sCardWidth + boxPaddingX) && card.getX() + sCardWidth / 2 < CardGame.width - boxInitialX && card.getY() + sCardHeight / 2 > boxInitialY && card.getY() + sCardHeight / 2 < 816 && card.getMoveable();
    }

    public boolean insideRightRightBox(Card card) {
        return card.getX() + sCardWidth / 2 > CardGame.width - boxSecondX - (sCardWidth + boxPaddingX) && card.getX() + sCardWidth / 2 < CardGame.width - boxSecondX && card.getY() + sCardHeight / 2 > boxInitialY && card.getY() + sCardHeight / 2 < 816 && card.getMoveable();
    }

    public void placeRedsCard(Card redCard) {
        redCard.setX(1378);
        redCard.setY(68);
    }

//    public void placeRedCardTwo() {
//        redCardTwo.setX(1378);
//        redCardTwo.setY(62);
//    }

    public void placeGraysCard(Card grayCard) {
        grayCard.setX(50);
        grayCard.setY(72);
    }

//    public void placeGrayCardTwo() {
//        grayCardTwo.setX(50);
//        grayCardTwo.setY(66);
//    }

    public void drawButton(Graphics g) {
        g.fillRoundRect((CardGame.width / 2) - (buttonWidth / 2), 2 * buttonHeight, buttonWidth, buttonHeight, boxArcRoundness, boxArcRoundness);
    }

    public boolean insideButton(MouseEvent e) {
        return e.getX() > (CardGame.width / 2) - (buttonWidth / 2) && e.getX() < (CardGame.width / 2) + (buttonWidth / 2) && e.getY() > 2 * buttonHeight && e.getY() < 3 * buttonHeight;
    }

    public void createButtonMessage(Graphics g) {
        Font myFont = new Font("Serif", Font.PLAIN, 36);
        g.setFont(myFont);
        g.setColor(Color.ORANGE.brighter());
        g.drawString("Compare!", 730, 260);
    }

    public void createWinMessage(Graphics g) {
        Font myFont = new Font("Serif", Font.BOLD, 36);
        g.setFont(myFont);
        g.setColor(Color.ORANGE.brighter());
        if (yourCard.getValue() + yourCardTwo.getValue() > theirCard.getValue() + theirCardTwo.getValue()) {
            g.drawString("Mine's Bigger!", 690, 260);
        } else if (yourCard.getValue() + yourCardTwo.getValue() < theirCard.getValue() + theirCardTwo.getValue()) {
            g.drawString("You Lose!", 720, 260);
        } else {
            g.drawString("Draw!", 750, 260);
        }
    }

    private void createEndMessage(Graphics g) {
        Font myFont = new Font("Serif", Font.BOLD, 36);
        g.setFont(myFont);
        if (redScore > grayScore) {
            g.setColor(redWinColor);
            g.drawString("Red Player Wins", 670, 260);
        } else {
                g.setColor(grayWinColor);
            g.drawString("Gray Player Wins", 665, 260);
        }
    }

    public void drawScoreBoxes(Graphics g) {
        g.fillRoundRect(boxInitialX, 200, scoreBoxDim, scoreBoxDim, boxArcRoundness, boxArcRoundness);
        g.fillRoundRect(CardGame.width - boxInitialX - scoreBoxDim, 200, scoreBoxDim, scoreBoxDim, boxArcRoundness, boxArcRoundness);
        Font myFont = new Font("Serif", Font.PLAIN, 36);
        g.setFont(myFont);
        Color oldColor = g.getColor();
        g.setColor(Color.GRAY.brighter().brighter());
        g.drawString(String.valueOf(grayScore), boxInitialX + 33, 260);
        g.drawString(String.valueOf(redScore), CardGame.width - boxInitialX - scoreBoxDim + 33, 260);
        g.setColor(oldColor);
    }

    public void drawToggle(Graphics g) {
        g.setColor(toggleColor);
        g.fillRoundRect(760, 0, 80, 30, scoreBoxArcRoundness, scoreBoxArcRoundness);
        Font myFont = new Font("Serif", Font.PLAIN, 18);
        g.setFont(myFont);
        g.setColor(Color.GRAY.brighter().brighter());
        g.drawString("Toggle", 775, 20);
        g.setColor(defaultBoxColor);
    }

    public void toggleBackground(MouseEvent e) {
        if (insideToggle(e))
            altColors = !altColors;
    }

    public void drawAll(Graphics g) {
        if (altColors)
            g.setColor(altBoxColor);
        drawPlaceBoxes(g, boxInitialX, boxInitialY); //width of rect sCardWidth +boxPaddingX, height of sCardHeight +boxPaddingX
        drawPlaceBoxes(g, boxSecondX, boxInitialY);
        drawPlaceBoxes(g, CardGame.width - boxInitialX - (sCardWidth + boxPaddingX), boxInitialY);
        drawPlaceBoxes(g, CardGame.width - boxInitialX - (sCardWidth + boxPaddingX) + 300, boxInitialY);
        drawScoreBoxes(g);
        drawButton(g);
        drawToggle(g);
    }


    public boolean insideToggle(MouseEvent e) {
        return e.getY() < toggleHeight && e.getY() > 0 && e.getX() > (CardGame.width / 2) - (toggleWidth / 2) && e.getX() < (CardGame.width / 2) + (toggleWidth / 2);
    }


    public void addPoints(Card yourCard, Card yourCardTwo, Card theirCard, Card theirCardTwo) {
        if (yourCard.getValue() == 1 && yourCardTwo.getValue() == 1) {
            grayScore += 11;
        } else if (theirCard.getValue() == 1 && theirCardTwo.getValue() == 1) {
            redScore += 11;
        } else {
            int diff = (yourCard.getValue() + yourCardTwo.getValue()) - (theirCard.getValue() + theirCardTwo.getValue());
            if (diff > 0) {
                if (yourCard.getSuit().equals(yourCardTwo.getSuit())) {
                    grayScore += diff * 2;
                } else {
                    grayScore += diff;
                }
            } else if (diff < 0) {
                if (theirCard.getSuit().equals(theirCardTwo.getSuit())) {
                    redScore -= diff * 2;
                } else {
                    redScore -= diff;
                }
            }
        }
        if (redScore > endCutoff || grayScore > endCutoff) {
            resetAble = false;
            for (Card card : cards) {
                card.setMoveable(false);
            }
        }
    }

    public boolean allLocked() {
        return lockedOne && lockedTwo && lockedThree && lockedFour;
    }

    public void reset() {
        if (resetAble) {
            this.yourCard = makeNewCard();
            this.theirCard = makeNewCard();
            this.yourCardTwo = makeNewCard();
            this.theirCardTwo = makeNewCard();
            this.lockedOne = false;
            this.lockedTwo = false;
            this.lockedThree = false;
            this.lockedFour = false;
            this.revealed = false;
            for (Card card : cards) {
                card.setMoveable(true);
            }
            placeGraysCard(grayCard);
            placeRedsCard(redCard);
        }
    }
}
