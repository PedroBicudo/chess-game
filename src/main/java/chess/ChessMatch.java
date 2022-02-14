package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public void initialSetup() {
        // Black
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 0));
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 7));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));

        // White
        board.placePiece(new Rook(board, Color.WHITE), new Position(7, 0));
        board.placePiece(new Rook(board, Color.WHITE), new Position(7, 7));
        board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrizChessPieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i < board.getRows(); i++) {
            for (int j=0; j < board.getColumns(); j++) {
                matrizChessPieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return matrizChessPieces;
    }
}