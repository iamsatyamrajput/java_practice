package lld.chessGame;

public interface Q08ChessBoardInterface {
 void init(Helper08 helper, String[][] chessboard);
 String move(int startRow, int startCol, int endRow, int endCol);
 int getGameStatus();
 int getNextTurn();
 }

 class Helper08{
 void print(String s){System.out.print(s);} void println(String s){print(s+"\n");}
 }
