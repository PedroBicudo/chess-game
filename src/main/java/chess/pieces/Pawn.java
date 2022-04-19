package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private boolean isFirstMove;

    public Pawn(Board board, Color color) {
        super(board, color);
        isFirstMove = true;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        if (getColor() == Color.WHITE) {

            // TOP
            p.setValues(position.getRow()-1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // TOP + 1
            p.setValues(position.getRow()-2, position.getColumn());
            if (getMoveCount() == 0 && getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                Position p2 = new Position(position.getRow()-1, position.getColumn());
                if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
            }

            // TOP LEFT
            p.setValues(position.getRow()-1, position.getColumn()-1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // TOP RIGHT
            p.setValues(position.getRow()-1, position.getColumn()+1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

        }

        if (getColor() == Color.BLACK) {

            // BOTTOM
            p.setValues(position.getRow()+1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // BOTTOM + 1
            p.setValues(position.getRow()+2, position.getColumn());
            if (getMoveCount() == 0 && getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                Position p2 = new Position(position.getRow()+1, position.getColumn());
                if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }

            }

            // BOTTOM LEFT
            p.setValues(position.getRow()+1, position.getColumn()-1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // BOTTOM RIGHT
            p.setValues(position.getRow()+1, position.getColumn()+1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

        }


        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }

}
