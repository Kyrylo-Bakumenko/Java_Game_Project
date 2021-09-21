import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Card {
    // src for card images: http://acbl.mybigcommerce.com/52-playing-cards/
    private Image faceUp;
    private Image faceUpSelect;
    private static Image faceDown;
    private static Image faceDownSelect;
    private final String suit; // C, D, H, S
    private final int value; // Ace-Jack,Queen,King --> 1-11,12,13. 0, -1 -> gray, red
    private boolean Select;
    private boolean drag;
    private int x, y;
    private int cursor_offset_x, cursor_offset_y;
    private boolean moveable;
    public Card(int value, String suit){
        this.x = 100;
        this.y = 100;
        this.moveable = true;
        this.suit = suit;
        this.value = value;
        String filePath="images/";
        if(value>=2 && value<=10){
            filePath+=value;
        }else if(value==1){
            filePath+="A";
        }else if(value==11){
            filePath+="J";
        }else if(value==12){
            filePath+="Q";
        }else if(value==13){
            filePath+="K";
        }else if(value==0){
            filePath+="gray_back";
        }else if(value==-1){
            filePath+="red_back";
        }
        filePath += suit;
        File faceUpFile = new File(filePath + ".png");
        File faceUpSelectFile = new File(filePath + "S.png");
//        System.out.println(filePath + ".png" + ", " + filePath + "S.png");
        try{
            faceUp = ImageIO.read(faceUpFile);
            faceUpSelect = ImageIO.read(faceUpSelectFile);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("file error");
        }
    }
    public boolean contains(int x, int y) {
        return this.x <= x && x <= this.x + faceUp.getWidth(null) && this.y <= y && y <= this.y + faceUp.getHeight(null);
    }

    public boolean toggleSelect() {
        this.Select = !this.Select;
        return this.Select;
    }

    public void setDragStart(Point begin) {
        this.cursor_offset_x = begin.x - this.x;
        this.cursor_offset_y = begin.y - this.y;
    }


    public void dragTo(Point target) {
        if (!drag) {
            return;
        }
        this.x = target.x - cursor_offset_x;
        this.y = target.y - cursor_offset_y;
    }
        public void paint(Graphics g){
        Image display;
        if(Select){
            display = faceUpSelect;
        }else{
            display = faceUp;
        }
        g.drawImage(display, x, y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getSelect(){
        return this.Select;
    }

    public void setSelect(boolean a){
        this.Select = a;
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }
    
    public boolean getDrag(){
        return drag;
    }

//    public int getCursor_offset_x() {
//        return cursor_offset_x;
//    }
//
//    public int getCursor_offset_y() {
//        return cursor_offset_y;
//    }
//
//    public void setCursor_offset_x(int cursor_x) {
//        this.cursor_offset_x = cursor_x;
//    }
//
//    public void setCursor_offset_y(int cursor_y) {
//        this.cursor_offset_y = cursor_y;
//    }

    public void setMoveable(boolean b){
        this.moveable = b;
    }

    public boolean getMoveable(){
        return moveable;
    }

    public int getValue(){
        return value;
    }

    public String getSuit(){return suit;}
}
