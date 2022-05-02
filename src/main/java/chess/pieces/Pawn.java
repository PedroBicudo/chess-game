package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private boolean isFirstMove;
    private ChessMatch chessMatch;
    private static final int BLACK_EN_PASSANT_ROW = 3;
    private static final int WHITE_EN_PASSANT_ROW = 4;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        isFirstMove = true;
        this.chessMatch = chessMatch;
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

            // #special-move: en passant
            if (position.getRow() == BLACK_EN_PASSANT_ROW) {
                Position left = new Position(position.getRow(),position.getColumn()-1);
                if (
                        getBoard().positionExists(left) &&
                        isThereOpponentPiece(left) &&
                        getBoard().piece(left) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[left.getRow()-1][left.getColumn()] = true;

                }

                Position right = new Position(position.getRow(),position.getColumn()+1);
                if (
                        getBoard().positionExists(right) &&
                        isThereOpponentPiece(right) &&
                        getBoard().piece(right) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[right.getRow()-1][right.getColumn()] = true;

                }
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

            // #special-move: en passant
            if (position.getRow() == WHITE_EN_PASSANT_ROW) {
                Position left = new Position(position.getRow(),position.getColumn()-1);
                if (
                        getBoard().positionExists(left) &&
                                isThereOpponentPiece(left) &&
                                getBoard().piece(left) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[left.getRow()+1][left.getColumn()] = true;

                }

                Position right = new Position(position.getRow(),position.getColumn()+1);
                if (
                        getBoard().positionExists(right) &&
                                isThereOpponentPiece(right) &&
                                getBoard().piece(right) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[right.getRow()+1][right.getColumn()] = true;

                }
            }

        }


        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }

}
