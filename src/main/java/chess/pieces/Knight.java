package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        // possible moves:
        //    1 x 2
        //  3   x   4
        //  x x N x x
        //  5   x   6
        //    7 x 8

        // 1
        p.setValues(position.getRow()-2, position.getColumn()-1);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 2
        p.setValues(position.getRow()-2, position.getColumn()+1);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 3
        p.setValues(position.getRow()-1, position.getColumn()-2);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 4
        p.setValues(position.getRow()-1, position.getColumn()+2);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 5
        p.setValues(position.getRow()+1, position.getColumn()-2);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 6
        p.setValues(position.getRow()+1, position.getColumn()+2);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 7
        p.setValues(position.getRow()+2, position.getColumn()-1);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // 8
        p.setValues(position.getRow()+2, position.getColumn()+1);
        if (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
            } else {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }


        return mat;
    }

    @Override
    public String toString() {
        return "N";
    }
}
