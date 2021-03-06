import java.util.Scanner;
import java.lang.*;
public class MineSweeper{
  private Board board; //the board (this is not a direct reference to the Tile[][] array)
  private String difficulty; //difficulty level if applicable
  private boolean gameOver; //is the game over?
  private double startTime; //time started
  private String prevMove; //user's previous move
  private int movesDone; //number of moves the user made so far
  private int numFlags;
  // constructor for string difficulty parameter.
  public MineSweeper(String diff){
    difficulty = diff;
    board = new Board(difficulty);
    numFlags = board.numMines();
	  gameOver = false;
	  startTime = System.currentTimeMillis();
    movesDone = 0;
  }
  public int getmovesDone(){
    return movesDone;
  }

  public int numFlags() {
	return numFlags;
  }
  public double startTime() {
    return startTime;
  }
  //constructor for actual lengths
  public MineSweeper(String hl, String vl) {
	 int horiz = Integer.parseInt(hl);
	 int vert = Integer.parseInt(vl);
	 board = new Board(horiz, vert);
   numFlags = board.numMines();
	 gameOver = false;
	 startTime = System.currentTimeMillis();
   movesDone = 0;
  }
  public boolean makeMove(String inp) {
	  if(inp.length() != 5) {System.out.println("Follow the correct move format! It's on the README"); return false;} //length of input correct?
    int rowSel = 0; //row selected
    rowSel += (inp.charAt(0) - 65) * 26; //first letter digit
    rowSel += inp.charAt(1) - 65; //second letter digit
    int colSel = 0; //col selected
    colSel += (Character.getNumericValue(inp.charAt(2))) * 10; //first num digit
    colSel += Character.getNumericValue(inp.charAt(3)); //second num digit
    if(inp.charAt(4) == 'f') { //if user chose to flag
      if(board.board[rowSel][colSel].isCleared()) {System.out.println("Tile already cleared"); return false;} //can't flag cleared tile
      if (movesDone != 0){numFlags--;}
      board.board[rowSel][colSel].flag();
    }
    if(inp.charAt(4) == 'u') { //if user chose to unflag
      if(board.board[rowSel][colSel].isCleared()) {System.out.println("Can't unflag this tile"); return false;} //can't unflag clear tile
      if (movesDone != 0){numFlags++;}
      board.board[rowSel][colSel].unflag();
    }
    if(inp.charAt(4) == 'c') { //if user chose to clear
      if (board.board[rowSel][colSel].isFlagged()) {System.out.println("Unflag this tile to clear"); return false;} //can't clear flagged tile
      if (movesDone == 0){
        int x = board.getHsize();
        int y = board.getVsize();
        while (board.board[rowSel][colSel].isMine() || board.board[rowSel][colSel].getTileNum() != 0){
          board = new Board(x, y);
        }
        numFlags = board.numMines();

      }else{
        if (board.board[rowSel][colSel].isMine()){
          return true; //if mine hit, gameOver becomes true
        }
      }


      board.clearSpread(colSel, rowSel);
      movesDone++;
	    return false;
    }

    return false;
  }

  public String toString() {
   String s = "Time elapsed: " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds" + "\n";
   s += "Number of flags left: " + numFlags + "\n";
	 s += board.toString();
	 s += "\n";
	 s += "Previous move: " + prevMove + "\n";
	 return s;
  }
//makes all mines open when they lose.
  public void allOpen(){
    for (int x = 0; x < board.getHsize(); x++){
      for (int y = 0; y < board.getVsize();y++){
        board.getTile(x, y).setOpen();
      }
    }
  }
  public String prevMove() {
	 return prevMove;
  }

  public static void main(String[] args) {
    String directions = ""; //prints if user does something wrong
    directions += "Requirements for starting the game:" + "\n";
    directions += "1. If your input length is 1, make sure it states either hard, easy or medium. (capitals don't matter).\n";
    directions += "2. If your input length is 2, make sure it the sizes don't exceed 27 or are letters.\n\n";
    directions += "Requirements during the game:\n";
    directions += "1. String input needs to be 5 characters long.\n";
    directions += "2. First 2 inputs are capital letters and next 2 are numbers, and last is either f, u, or c.\n";

	  MineSweeper game = null;
    try {
      if (args.length == 1){
		      game = new MineSweeper(args[0]);
      }
      else if (args.length == 2){
		      game = new MineSweeper(args[0], args[1]);
      }
      boolean moveVar = false; //turns false if player hits mine
      boolean gameWon = false;
	    System.out.println(game.toString());
      while(!moveVar) { //while the player hasn't hit a mine or hasn't opened all tiles
      if(game.board.allFlaggedOrCleared() && game.board.allMinesFlagged()) {
        System.out.println("You win!");
        System.out.println("Time completed: " + ((System.currentTimeMillis() - game.startTime()) / 1000) + " seconds");
        gameWon = true;
        break;
      }
      //allows players to make moves.
        Scanner sc = new Scanner(System.in);
		    String nl = sc.nextLine();
        moveVar = game.makeMove(nl);

		    game.prevMove = nl;
		    System.out.println(game.toString());
      }
      if(!gameWon) {
      game.allOpen();
      System.out.println(game.toString());
		  System.out.println("Game over! You hit a mine!");
      }
    }
    catch (Exception e){
      System.out.println(directions);
    }
  }

}
