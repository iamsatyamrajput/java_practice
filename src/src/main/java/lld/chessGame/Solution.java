package lld.chessGame;

import java.util.List;
import java.util.ArrayList;

public class Solution implements Q08ChessBoardInterface {
    
    Game game;
    @Override
    public void init(Helper08 helper, String[][] chessboard) {
        game = new Game();
        game.initializeBoard(chessboard);
    }

    @Override
    public String move(int startRow, int startCol, int endRow, int endCol) {
        return game.move(startRow, startCol, endRow, endCol);
    }

    @Override
    public int getGameStatus() {
        return game.getGameStatus();
    }

    @Override
    public int getNextTurn() {
        return game.getNextTurn();
    }
}

class Position {
    int row;
    int col;
}

class Player {
    String name;
    String color;
    List<Piece> wonPieces;
    
    public Player(String color) {
        this.color = color;
        this.wonPieces = new ArrayList<>();
    }
}

class Game {
    Board board;
    Player player1;
    Player player2;
    Boolean firstPlayerMove;
    
    public Game() {
        board = new Board();
        player1 = new Player("white");
        player2 = new Player("black");
        firstPlayerMove = true; // White goes first
    }

    public void initializeBoard(String[][] chessboard) {
        // Initialize the board with pieces based on the input chessboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String pieceCode = chessboard[row][col];
                if (pieceCode != null && !pieceCode.isEmpty()) {
                    Piece piece = createPiece(pieceCode, row, col);
                    board.placePiece(row, col, piece);
                }
            }
        }
    }
    
    private Piece createPiece(String pieceCode, int row, int col) {
        String color = Character.isUpperCase(pieceCode.charAt(0)) ? "white" : "black";
        char pieceType = Character.toLowerCase(pieceCode.charAt(0));
        
        MoveStrategy strategy = getMoveStrategy(pieceType);
        return new Piece(color, strategy, pieceType);
    }
    
    private MoveStrategy getMoveStrategy(char pieceType) {
        switch (pieceType) {
            case 'p': return new PawnMoveStrategy();
            case 'r': return new RookMoveStrategy();
            case 'n': return new KnightMoveStrategy();
            case 'b': return new BishopMoveStrategy();
            case 'q': return new QueenMoveStrategy();
            case 'k': return new KingMoveStrategy();
            default: return new PawnMoveStrategy(); // fallback
        }
    }

    public String move(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = board.get(startRow, startCol);
        if (piece == null) {
            return "Invalid move: No piece at starting position";
        }
        
        // Check if it's the correct player's turn
        String currentPlayerColor = firstPlayerMove ? "white" : "black";
        if (!piece.color.equals(currentPlayerColor)) {
            return "Invalid move: Not your turn";
        }
        
        String result = piece.move(board, startRow, startCol, endRow, endCol);
        if (result.equals("Valid move")) {
            firstPlayerMove = !firstPlayerMove; // Switch turns only on valid moves
        }
        return result;
    }
    
    public int getGameStatus() {
        return 0; // 0 = ongoing, 1 = check, 2 = checkmate, 3 = stalemate
    }
    
    public int getNextTurn() {
        return firstPlayerMove ? 1 : 0; // 1 = white, 0 = black
    }
}

class Board {
    Piece[][] board;
    
    public Board() {
        board = new Piece[8][8];
    }
    
    public Piece get(int x, int y) {
        if (isValidPosition(x, y)) {
            return board[x][y];
        }
        return null;
    }
    
    public void placePiece(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    public boolean isEmpty(int row, int col) {
        return get(row, col) == null;
    }
    
    public boolean isOpponentPiece(int row, int col, String playerColor) {
        Piece piece = get(row, col);
        return piece != null && !piece.color.equals(playerColor);
    }
}

class Piece {
    String color;
    MoveStrategy moveStrategy;
    char pieceType;
    
    public Piece(String color, MoveStrategy moveStrategy, char pieceType) {
        this.color = color;
        this.moveStrategy = moveStrategy;
        this.pieceType = pieceType;
    }
    
    public String move(Board board, int startRow, int startCol, int row, int col) {
        return moveStrategy.move(board, startRow, startCol, row, col);
    }
    
    public String getColor() {
        return color;
    }
    
    public char getPieceType() {
        return pieceType;
    }
}

interface MoveStrategy {
    String move(Board board, int startRow, int startCol, int row, int col);
}

class RookMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        // Rook moves horizontally or vertically
        if (startRow != row && startCol != col) {
            return "Invalid move: Rook can only move horizontally or vertically";
        }
        
        // Check if path is clear
        if (!isPathClear(board, startRow, startCol, row, col)) {
            return "Invalid move: Path is blocked";
        }
        
        // Check if destination is valid
        if (!isValidDestination(board, row, col, board.get(startRow, startCol).color)) {
            return "Invalid move: Cannot capture own piece";
        }
        
        return "Valid move";
    }
    
    private boolean isPathClear(Board board, int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow) {
            // Horizontal movement
            int minCol = Math.min(startCol, endCol);
            int maxCol = Math.max(startCol, endCol);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (!board.isEmpty(startRow, col)) {
                    return false;
                }
            }
        } else {
            // Vertical movement
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (!board.isEmpty(row, startCol)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isValidDestination(Board board, int row, int col, String pieceColor) {
        return board.isEmpty(row, col) || board.isOpponentPiece(row, col, pieceColor);
    }
}

class KnightMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        // Knight moves in L-shape: 2 squares in one direction, 1 square perpendicular
        int rowDiff = Math.abs(row - startRow);
        int colDiff = Math.abs(col - startCol);
        
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return "Invalid move: Knight must move in L-shape";
        }
        
        // Check if destination is valid
        if (!isValidDestination(board, row, col, board.get(startRow, startCol).color)) {
            return "Invalid move: Cannot capture own piece";
        }
        
        return "Valid move";
    }
    
    private boolean isValidDestination(Board board, int row, int col, String pieceColor) {
        return board.isEmpty(row, col) || board.isOpponentPiece(row, col, pieceColor);
    }
}

class BishopMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        // Bishop moves diagonally
        if (Math.abs(row - startRow) != Math.abs(col - startCol)) {
            return "Invalid move: Bishop must move diagonally";
        }
        
        // Check if path is clear
        if (!isPathClear(board, startRow, startCol, row, col)) {
            return "Invalid move: Path is blocked";
        }
        
        // Check if destination is valid
        if (!isValidDestination(board, row, col, board.get(startRow, startCol).color)) {
            return "Invalid move: Cannot capture own piece";
        }
        
        return "Valid move";
    }
    
    private boolean isPathClear(Board board, int startRow, int startCol, int endRow, int endCol) {
        int rowStep = (endRow > startRow) ? 1 : -1;
        int colStep = (endCol > startCol) ? 1 : -1;
        
        int currentRow = startRow + rowStep;
        int currentCol = startCol + colStep;
        
        while (currentRow != endRow && currentCol != endCol) {
            if (!board.isEmpty(currentRow, currentCol)) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }
    
    private boolean isValidDestination(Board board, int row, int col, String pieceColor) {
        return board.isEmpty(row, col) || board.isOpponentPiece(row, col, pieceColor);
    }
}

class QueenMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        // Queen moves like rook OR bishop
        boolean isHorizontalOrVertical = (startRow == row || startCol == col);
        boolean isDiagonal = (Math.abs(row - startRow) == Math.abs(col - startCol));
        
        if (!isHorizontalOrVertical && !isDiagonal) {
            return "Invalid move: Queen must move horizontally, vertically, or diagonally";
        }
        
        // Check if path is clear
        if (!isPathClear(board, startRow, startCol, row, col)) {
            return "Invalid move: Path is blocked";
        }
        
        // Check if destination is valid
        if (!isValidDestination(board, row, col, board.get(startRow, startCol).color)) {
            return "Invalid move: Cannot capture own piece";
        }
        
        return "Valid move";
    }
    
    private boolean isPathClear(Board board, int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow || startCol == endCol) {
            // Horizontal or vertical movement (like rook)
            if (startRow == endRow) {
                int minCol = Math.min(startCol, endCol);
                int maxCol = Math.max(startCol, endCol);
                for (int col = minCol + 1; col < maxCol; col++) {
                    if (!board.isEmpty(startRow, col)) {
                        return false;
                    }
                }
            } else {
                int minRow = Math.min(startRow, endRow);
                int maxRow = Math.max(startRow, endRow);
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (!board.isEmpty(row, startCol)) {
                        return false;
                    }
                }
            }
        } else {
            // Diagonal movement (like bishop)
            int rowStep = (endRow > startRow) ? 1 : -1;
            int colStep = (endCol > startCol) ? 1 : -1;
            
            int currentRow = startRow + rowStep;
            int currentCol = startCol + colStep;
            
            while (currentRow != endRow && currentCol != endCol) {
                if (!board.isEmpty(currentRow, currentCol)) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
        }
        return true;
    }
    
    private boolean isValidDestination(Board board, int row, int col, String pieceColor) {
        return board.isEmpty(row, col) || board.isOpponentPiece(row, col, pieceColor);
    }
}

class KingMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        // King moves one square in any direction
        int rowDiff = Math.abs(row - startRow);
        int colDiff = Math.abs(col - startCol);
        
        if (rowDiff > 1 || colDiff > 1) {
            return "Invalid move: King can only move one square";
        }
        
        // Check if destination is valid
        if (!isValidDestination(board, row, col, board.get(startRow, startCol).color)) {
            return "Invalid move: Cannot capture own piece";
        }
        
        return "Valid move";
    }
    
    private boolean isValidDestination(Board board, int row, int col, String pieceColor) {
        return board.isEmpty(row, col) || board.isOpponentPiece(row, col, pieceColor);
    }
}

class PawnMoveStrategy implements MoveStrategy {
    @Override
    public String move(Board board, int startRow, int startCol, int row, int col) {
        Piece piece = board.get(startRow, startCol);
        String color = piece.color;
        
        // Determine direction based on color
        int direction = color.equals("white") ? -1 : 1; // White moves up, Black moves down
        
        // Check if it's a forward move
        if (startCol == col) {
            int rowDiff = row - startRow;
            
            // Single square move
            if (rowDiff == direction) {
                if (board.isEmpty(row, col)) {
                    return "Valid move";
                } else {
                    return "Invalid move: Square is occupied";
                }
            }
            
            // Double square move from starting position
            if ((color.equals("white") && startRow == 6 && rowDiff == -2) ||
                (color.equals("black") && startRow == 1 && rowDiff == 2)) {
                if (board.isEmpty(startRow + direction, col) && board.isEmpty(row, col)) {
                    return "Valid move";
                } else {
                    return "Invalid move: Path is blocked";
                }
            }
            
            return "Invalid move: Pawn can only move forward";
        }
        
        // Diagonal capture
        if (Math.abs(startCol - col) == 1 && (row - startRow) == direction) {
            if (board.isOpponentPiece(row, col, color)) {
                return "Valid move";
            } else {
                return "Invalid move: No opponent piece to capture";
            }
        }
        
        return "Invalid move: Invalid pawn movement";
    }
}
