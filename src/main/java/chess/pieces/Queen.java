package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        // TOP
        p.setValues(position.getRow()-1, position.getColumn());
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()-1, p.getColumn());
        }

        // TOP LEFT
        p.setValues(position.getRow()-1, position.getColumn()-1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()-1, p.getColumn()-1);
        }

        // TOP RIGHT
        p.setValues(position.getRow()-1, position.getColumn()+1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()-1, p.getColumn()+1);
        }

        // BOTTOM
        p.setValues(position.getRow()+1, position.getColumn());
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()+1, p.getColumn());
        }

        // BOTTOM LEFT
        p.setValues(position.getRow()+1, position.getColumn()-1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()+1, p.getColumn()-1);
        }

        // BOTTOM RIGHT
        p.setValues(position.getRow()+1, position.getColumn()+1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow()+1, p.getColumn()+1);
        }

        // LEFT
        p.setValues(position.getRow(), position.getColumn()-1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow(), p.getColumn()-1);
        }

        // RIGHT
        p.setValues(position.getRow(), position.getColumn()+1);
        while (getBoard().positionExists(p)) {
            if (getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = isThereOpponentPiece(p);
                break;
            }

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow(), p.getColumn()+1);
        }

        return mat;
    }

    @Override
    public String toString() {
        return "Q";
    }
}
