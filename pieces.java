public class pieces {
    //these are just attributes
    private int xCor;
    private int yCor;
    private boolean king;

    private String color;

    public pieces() {
        xCor = 0;
        yCor = 0;
        king = false;
    }

    public pieces(int x, int y, boolean b) {
        xCor = x;
        yCor = y;
        king = b;
    }

    public int getX() {
        return xCor;
    }

    public int getY() {
        return yCor;
    }

    public boolean kingStatus() {
        return king;
    }

    public void setX(int x) {
        xCor = x;
    }

    public void setY(int y) {
        yCor = y;
    }

    public void setColor(String a) {
        color = a;
    }

    public String getColor() {
        return color;
    }

    public void makeKing(boolean k) {
        king = k;
    }

    //the previous stuff are just getters and setters
    //this one just prints the piece, it is used when printing the board
    public String printpiece() {
        if (king) {
            return color.toLowerCase();
        }
        return color;
    }
    // this is just for one usecase where i needed to know if it was white or black, i wanted se the iswhiteturn boolean in a method where i dont have access to that so i created this method so i can call it from that method. but if i called it anywhere else it would not work as intended.
    public boolean iswhitemove()
    {
        if(color.equals("W"))
        {
            return true;
        }
        if(color.equals("B"))
        {
            return false;
        }
        return false;
    }
}