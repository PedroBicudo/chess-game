package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class King extends ChessPiece {

    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // top
        p.setValues(position.getRow()-1, position.getColumn());
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // top left
        p.setValues(position.getRow()-1, position.getColumn()-1);
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // top right
        p.setValues(position.getRow()-1, position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // bottom
        p.setValues(position.getRow()+1, position.getColumn());
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // bottom left
        p.setValues(position.getRow()+1, position.getColumn()-1);
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // bottom right
        p.setValues(position.getRow()+1, position.getColumn()+1);
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // left
        p.setValues(position.getRow(), position.getColumn()-1);
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // right
        p.setValues(position.getRow(), position.getColumn()+1);
        if (canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // #special-move: Castling
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {

            // castling (right)
            p.setValues(position.getRow(), position.getColumn()+3);
            if (testRookCastling(p)) {
                Position p1 = new Position(position.getRow(), position.getColumn()+1);
                Position p2 = new Position(position.getRow(), position.getColumn()+2);
                mat[position.getRow()][position.getColumn()+2] = canMove(p1, p2);
            }

            // castling (left)
            p.setValues(position.getRow(), position.getColumn()-4);
            if (testRookCastling(p)) {
                Position p1 = new Position(position.getRow(), position.getColumn()-1);
                Position p2 = new Position(position.getRow(), position.getColumn()-2);
                Position p3 = new Position(position.getRow(), position.getColumn()-3);
                mat[position.getRow()][position.getColumn()-2] = canMove(p1, p2, p3);

            }

        }

        return mat;
    }

    private boolean canMove(Position target) {
        if (!getBoard().positionExists(target)) {
            return false;
        }

        ChessPiece chessPiece = (ChessPiece) getBoard().piece(target);
        return chessPiece == null ||
                chessPiece.getColor() != getColor();
    }

    private boolean canMove(Position ...targets) {
        for (Position p: targets) {
            if (getBoard().piece(p) != null) {
                return false;
            }
        }

        return true;
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

}
