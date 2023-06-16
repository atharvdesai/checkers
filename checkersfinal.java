import java.util.ArrayList;
import java.util.Scanner;

public class checkersfinal {
    public static void main(String[] args) {
        pieces[][] board = new pieces[8][8];
        setCheckersBoard(board);
        board[0][7].setColor(" ");
        printBoard(board);
        boolean didcapture = false;
        boolean isWhiteTurn = true;
        int count =0;

        hello: while (true) {
            //this is for the multijump function, i have a boolean to check if the previous move was a capture and if it was then they can get a turn again if they can capture


            if (isWhiteTurn)
                System.out.println("Player W:");
            else
                System.out.println("Player B:");

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter fromRow: ");
            int fromRow = scanner.nextInt();

            System.out.print("Enter fromCol: ");
            int fromCol = scanner.nextInt();

            System.out.print("Enter toRow: ");
            int toRow = scanner.nextInt();

            System.out.print("Enter toCol: ");
            int toCol = scanner.nextInt();
            count++;


            if (validMove(board,  fromRow, fromCol, toRow, toCol , isWhiteTurn)) {
                movePiece(board,  fromRow, fromCol , toRow, toCol);
                printBoard(board);
                if(checkWin2(board,isWhiteTurn))
                {
                    break hello;
                }
                if(toCol-fromCol>1||toCol-fromCol<-1)
                {
//                    System.out.println("did capture");
                    if(possiblecaptures(board,isWhiteTurn)==0)
                    {
//                        System.out.println("posmoves ==0");
                        isWhiteTurn = !isWhiteTurn;
                    }
                }
                else
                {
//                    System.out.println("didnt capture");
                    isWhiteTurn = !isWhiteTurn;
                }

            }
        }
    }

    public static void movePiece(pieces[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        //this just basically moves all the attributes from one piece to the other location and puts the piece into that location
        pieces pieceToMove = board[fromRow][fromCol];
        board[toRow][toCol] = pieceToMove;
        board[fromRow][fromCol] = new pieces(fromRow, fromCol, false);
        board[fromRow][fromCol].setColor(" ");
        pieceToMove.setX(toRow);
        pieceToMove.setY(toCol);

        // IF a piece reaches the end of the board then make it into a king
        if ((pieceToMove.getColor().equals("W") && toRow == 0) || (pieceToMove.getColor().equals("B") && toRow == 7)) {
            pieceToMove.makeKing(true);

        }
    }
    //this is just an inbounds function that checks if the coordinate is inbounds, i made a seperate function because i wanted to use it in many places
    public static boolean inbounds(pieces[][] board,int toRow, int toCol)
    {
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }
        return true;
    }
    //this is a validmove function which will just check if the move you made is a valid move

    public static boolean validMove(pieces[][] board, int fromRow, int fromCol, int toRow, int toCol, boolean isWhiteTurn) {
        int capture =0;
        // Checks if move is in bounds, im just calling the inbounds method
        if(!inbounds(board,toRow,toCol))
        {
            System.out.println("Move out of bounds");
            return false;
        }
        else{
            // Checks if move is diagonal
            int rowDiff = toRow - fromRow;
            int colDiff = toCol - fromCol;
            if (Math.abs(rowDiff) != Math.abs(colDiff)) {
                System.out.println("Invalid move, not diagonal");
                return false;
            }

            // Check if piece is moving in the right direction
            pieces movingPiece = board[fromRow][fromCol];
            if (movingPiece.kingStatus() == false) {
                if (isWhiteTurn && rowDiff >= 0) {
                    System.out.println("Invalid move, white pieces can only move up diagonally");
                    return false;
                } else if (!isWhiteTurn && rowDiff <= 0) {
                    System.out.println("Invalid move, black pieces can only move down diagonally");
                    return false;
                }
            }
            // Checks if move is more than 1 diagonal long, this means that you captured a piece or you entered wrong coordinates
            if (rowDiff > 1||rowDiff<-1) {
                // Checks if there is an opposite color piece to capture
                int captureRow = (fromRow + toRow) / 2;
                int captureCol = (fromCol + toCol) / 2;
                pieces capturePiece = board[captureRow][captureCol];
                if (capturePiece == null || capturePiece.printpiece().equals(" ")) {
                    System.out.println("Invalid move, must capture an opponent's piece");
                    return false;
                } else if (capturePiece.printpiece().equals("W") && isWhiteTurn) {
                    System.out.println("Invalid move, cannot capture own piece");
                    return false;
                } else if (capturePiece.printpiece().equals("B") && !isWhiteTurn) {
                    System.out.println("Invalid move, cannot capture own piece");
                    return false;
                }
                else
                    capture++;
                board[captureRow][captureCol].setColor(" "); // Capture the piece
            }
            //this just checks if you can capture a piece and you didnt capture a piece becasue when you can you have to capture a piece
            if(possiblecaptures(board,isWhiteTurn)>0&&capture==0)
            {
                System.out.println("You can capture a piece so you must capture it");
                return false;
            }
        }
        return true;

    }
    //this is just a method that checks all the 4 sides you could capture and then adds the possible captures to an arraylist and
    // checks if your move is contained within it, if it is not then it is an invalid move, if there are any possible captures
    public static boolean canCapture(pieces[][] board, int fromRow, int fromCol, int toRow, int toCol)
    {
        ArrayList<pieces> a = new ArrayList<>();
        if(inbounds(board,fromRow+2,fromCol+2))
        {
            if(!board[fromRow+1][fromCol+1].printpiece().equals(" ")&&board[fromRow+2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol+2]);
        }
        if(inbounds(board,fromRow-2,fromCol+2))
        {
            if(!board[fromRow-1][fromCol+1].printpiece().equals(" ")&&board[fromRow-2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol+2]);
        }
        if(inbounds(board,fromRow+2,fromCol-2))
        {
            if(!board[fromRow+1][fromCol-1].printpiece().equals(" ")&&board[fromRow+2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol-2]);
        }
        if(inbounds(board,fromRow-2,fromCol-2))
        {
            if(!board[fromRow-1][fromCol-1].printpiece().equals(" ")&&board[fromRow-2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol-2]);
        }

        for(int i =0; i<a.size();i++)
        {
            if(a.get(i).getX()==board[toRow][toCol].getX()&&a.get(i).getY()==board[toRow][toCol].getY())
            {
                return false;
            }
        }
        if(a.size()==0)
        {
            return false;
        }
        return true;
    }
    //the other can capture is made for when i just want to check if there is something that can be captured,
    // i do this when checking for double jumps, after someone has made a capture already then i put it into this
    // check function to see if they can capture again, if they can then i tell them to go again, in this case i
    // cannot use the other cancapture method becasue that one also takes in the place they want to go to but in
    // this case i don't have that information
    public static boolean canCapture1(pieces[][] board, int fromRow, int fromCol)
    {
        ArrayList<pieces> a = new ArrayList<>();
        if(inbounds(board,fromRow+2,fromCol+2))
        {
            if(!board[fromRow+1][fromCol+1].printpiece().equals(" ")&&board[fromRow+2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol+2]);
        }
        if(inbounds(board,fromRow-2,fromCol+2))
        {
            if(!board[fromRow-1][fromCol+1].printpiece().equals(" ")&&board[fromRow-2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol+2]);
        }
        if(inbounds(board,fromRow+2,fromCol-2))
        {
            if(!board[fromRow+1][fromCol-1].printpiece().equals(" ")&&board[fromRow+2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol-2]);
        }
        if(inbounds(board,fromRow-2,fromCol-2))
        {
            if(!board[fromRow-1][fromCol-1].printpiece().equals(" ")&&board[fromRow-2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol-2]);
        }
        if(a.size()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //this cancapture function is for the winchecking method, one of the possible ways to lose in checkers
    // is to have no possible moves left, so then i would have to check all the possible captures,
    // here i just count all the possible captures and then add it to the possible moves, when it reaches
    // zero( which means there are no more moves) then the person loses
    public static int canCapture2(pieces[][] board, int fromRow, int fromCol, boolean isWhiteMove)
    {
        String capturePiece = "";
        if(isWhiteMove)
        {
            capturePiece = "B";
        }
        else
            capturePiece = "W";
        ArrayList<pieces> a = new ArrayList<>();
        if(inbounds(board,fromRow+2,fromCol+2))
        {
            if(board[fromRow+1][fromCol+1].printpiece().equals(capturePiece)&&board[fromRow+2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol+2]);
        }
        if(inbounds(board,fromRow-2,fromCol+2))
        {
            if(board[fromRow-1][fromCol+1].printpiece().equals(capturePiece)&&board[fromRow-2][fromCol+2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol+2]);
        }
        if(inbounds(board,fromRow+2,fromCol-2))
        {
            if(board[fromRow+1][fromCol-1].printpiece().equals(capturePiece)&&board[fromRow+2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow+2][fromCol-2]);
        }
        if(inbounds(board,fromRow-2,fromCol-2))
        {
            if(board[fromRow-1][fromCol-1].printpiece().equals(capturePiece)&&board[fromRow-2][fromCol-2].printpiece().equals(" "))
                a.add(board[fromRow-2][fromCol-2]);
        }
        return a.size();
    }


    //this just goes through the loop and prints the board, it also adds the numbers on the side so the person knows what piece to choose easily
    public static void printBoard(pieces[][] board) {
        int numRows = board.length;
        int numCols = board[0].length;

        System.out.println("   0 1 2 3 4 5 6 7");

        // Print row labels and board contents
        for (int row = 0; row < numRows; row++) {
            // Print row label (number)
            System.out.print((row) + " ");

            // Print row contents
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + board[row][col].printpiece());
            }
            System.out.println("|");
        }
    }
    //this setCheckers board function just sets the pieces which was kinda annoying to do, i first
    // manually set it up then for everything that is not occupied by a piece I just set it to a space
    public static void setCheckersBoard(pieces[][] pieces) {
        pieces[0][1] = new pieces(0, 1, false);
        pieces[0][1].setColor("B");
        pieces[0][3] = new pieces(0, 3, false);
        pieces[0][3].setColor("B");
        pieces[0][5] = new pieces(0, 5, false);
        pieces[0][5].setColor("B");
        pieces[0][7] = new pieces(0, 7, false);
        pieces[0][7].setColor("B");
        pieces[1][0] = new pieces(1, 0, false);
        pieces[1][0].setColor("B");
        pieces[1][2] = new pieces(1, 2, false);
        pieces[1][2].setColor("B");
        pieces[1][4] = new pieces(1, 4, false);
        pieces[1][4].setColor("B");
        pieces[1][6] = new pieces(1, 6, false);
        pieces[1][6].setColor("B");
        pieces[2][1] = new pieces(2, 1, false);
        pieces[2][1].setColor("B");
        pieces[2][3] = new pieces(2, 3, false);
        pieces[2][3].setColor("B");
        pieces[2][5] = new pieces(2, 5, false);
        pieces[2][5].setColor("B");
        pieces[2][7] = new pieces(2, 7, false);
        pieces[2][7].setColor("B");

        // Set white pieces
        pieces[7][0] = new pieces(7, 0, false);
        pieces[7][0].setColor("W");
        pieces[7][2] = new pieces(7, 2, false);
        pieces[7][2].setColor("W");
        pieces[7][4] = new pieces(7, 4, false);
        pieces[7][4].setColor("W");
        pieces[7][6] = new pieces(7, 6, false);
        pieces[7][6].setColor("W");
        pieces[6][1] = new pieces(6, 1, false);
        pieces[6][1].setColor("W");
        pieces[6][3] = new pieces(6, 3, false);
        pieces[6][3].setColor("W");
        pieces[6][5] = new pieces(6, 5, false);
        pieces[6][5].setColor("W");
        pieces[5][0] = new pieces(5, 0, false);
        pieces[5][0].setColor("W");
        pieces[5][2] = new pieces(5, 2, false);
        pieces[5][2].setColor("W");
        pieces[5][4] = new pieces(5, 4, false);
        pieces[5][4].setColor("W");
        pieces[5][6] = new pieces(5, 6, false);
        pieces[5][6].setColor("W");
        pieces[6][7] = new pieces(6, 7, false);
        pieces[6][7].setColor("W");

        // set empty spaces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] == null) {
                    pieces[i][j] = new pieces(i, j, false);
                    pieces[i][j].setColor(" ");
                }
            }
        }
    }
    //I check for all the possible non capture moves for the win condiition, i explained why i do this in the cancapture2
    public static int possibleNonCaptureMoves(pieces[][] board,int row, int column,boolean isWhiteMove)
    {
        int moves = 0;
        pieces current = board[row][column];
        if(validMove1(board,row,column,row+1,column+1,isWhiteMove))
        {
            moves++;
        }
        if(validMove1(board,row,column,row-1,column+1,isWhiteMove))
        {
            moves++;
        }
        if(validMove1(board,row,column,row-1,column-1,isWhiteMove))
        {
            moves++;
        }
        if(validMove1(board,row,column,row+1,column-1,isWhiteMove))
        {
            moves++;
        }
        return moves;
    }
    //this function just goes through all the pieces and then checks all the possible moves that they can make, then it addds a total of black moves and white moves,
    // when there are 0 of one color then it declares that person as the loser
    public static boolean checkWin2( pieces[][] board,boolean isWhiteMove)
    {
        int white =0;
        int black = 0;
        int captures =0;

        for(int i =0; i<board.length;i++)
        {
            for(int j =0; j<board[i].length;j++)
            {
                pieces current = board[i][j];
                if(current.getColor().equals("B"))
                {

                    black+=canCapture2(board,i,j,isWhiteMove)+ possibleNonCaptureMoves(board,i,j,current.iswhitemove());
                }
                if(current.getColor().equals("W"))
                {
                    white+=canCapture2(board,i,j,isWhiteMove)+ possibleNonCaptureMoves(board,i,j,current.iswhitemove());
                }
            }
        }
        if(white==0)
        {
            System.out.println("Black wins because there are no more moves for white");
            return true;
        }
        if(black ==0)
        {
            System.out.println("White wins because there are no more moves for white");
            return true;
        }
        return false;
    }
    public static boolean validMove1(pieces[][] board, int fromRow, int fromCol, int toRow, int toCol, boolean isWhiteTurn) {
        int capture =0;
        // Checks if move is in bounds, im just calling the inbounds method
        if(!inbounds(board,toRow,toCol))
        {
            return false;
        }
        else{
            // Checks if move is diagonal
            int rowDiff = toRow - fromRow;
            int colDiff = toCol - fromCol;
            if (Math.abs(rowDiff) != Math.abs(colDiff)) {
                return false;
            }

            // Check if piece is moving in the right direction
            pieces movingPiece = board[fromRow][fromCol];
            if (movingPiece.kingStatus() == false) {
                if (isWhiteTurn && rowDiff >= 0) {
                    return false;
                } else if (!isWhiteTurn && rowDiff <= 0) {
                    return false;
                }
            }
            // Checks if move is more than 1 diagonal long, this means that you captured a piece or you entered wrong coordinates
            if (rowDiff > 1||rowDiff<-1) {
                // Checks if there is an opposite color piece to capture
                int captureRow = (fromRow + toRow) / 2;
                int captureCol = (fromCol + toCol) / 2;
                pieces capturePiece = board[captureRow][captureCol];
                if (capturePiece == null || capturePiece.printpiece().equals(" ")) {

                    return false;
                } else if (capturePiece.printpiece().equals("W") && isWhiteTurn) {

                    return false;
                } else if (capturePiece.printpiece().equals("B") && !isWhiteTurn) {

                    return false;
                }
                else
                    capture++;
                board[captureRow][captureCol].setColor(" "); // Capture the piece
            }
            //this just checks if you can capture a piece and you didnt capture a piece becasue when you can you have to capture a piece
            if(canCapture(board,fromRow,fromCol,toRow,toCol)&&capture==0)
            {

                return false;
            }
        }
        return true;

    }
    public static int possiblecaptures( pieces[][] board,boolean isWhiteMove)
    {
        int white =0;
        int black = 0;
        int captures =0;

        for(int i =0; i<board.length;i++)
        {
            for(int j =0; j<board[i].length;j++)
            {
                pieces current = board[i][j];
                if(current.getColor().equals("B")||current.getColor().equals("b"))
                {

                    black+=canCapture2(board,i,j,false);
                }
                if(current.getColor().equals("W")||current.getColor().equals("w"))
                {
                    white+=canCapture2(board,i,j,true);
                }
            }
        }
        if(isWhiteMove)
        {
            captures+= white;
        }
        if(!isWhiteMove)
        {
            captures+=black;
        }
        return captures;
    }
}
