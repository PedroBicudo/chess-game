package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard;
    private List<Piece> capturedPieces;
    private boolean check;
    private boolean checkMate;

    public ChessMatch() {
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public void initialSetup() {
        placeNewPiece('h', 7, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));

        placeNewPiece('b', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 8, new King(board, Color.BLACK));
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target,capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;

        } else {
            nextTurn();

        }

        return (ChessPiece) capturedPiece;

    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }

    private void validateTargetPosition(Position source, Position target) {
        Piece piece = board.piece(source);
        if (!piece.possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position.");
        }

        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw  new ChessException("The chosen piece is not yours.");
        }

        if (!board.piece(position).isThereAnyPossibleMoves()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private Piece makeMove(Position source, Position target) {
        Piece piece = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(piece);
        }

        board.placePiece(piece, target);

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        Piece piece = board.removePiece(target);
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
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

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer.equals(Color.WHITE)? Color.BLACK: Color.WHITE;
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK: Color.WHITE;
    }

    private ChessPiece king(Color color) {
        for (Piece p: piecesOnTheBoard) {
            if (p instanceof King && ((ChessPiece) p).getColor() == color) {
                return (ChessPiece) p;
            }
        }

        throw new IllegalStateException("There is no "+color+" king on the board");
    }

    private boolean testCheckMate(Color colorKing) {
        if (!testCheck(colorKing)) {
            return false;
        }

        List<Piece> piecesWithTheSameColor = piecesOnTheBoard
                .stream()
                .filter(x -> ((ChessPiece) x).getColor() == colorKing)
                .collect(Collectors.toList());

        for (Piece p: piecesWithTheSameColor) {
            boolean[][] mat = p.possibleMoves();
            for (int i=0; i < mat.length; i++) {
                for (int j=0; j < mat.length; j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(colorKing);
                        undoMove(source, target, capturedPiece);

                        if (!testCheck(colorKing)) {
                            return false;
                        }

                    }
                }
            }
        }

        return true;
    }

    private boolean testCheck(Color colorKing) {
        Position kingPosition = king(colorKing)
                .getChessPosition()
                .toPosition();

        List<Piece> opponentPieces = piecesOnTheBoard
                .stream()
                .filter(x -> ((ChessPiece) x).getColor() == opponent(colorKing))
                .collect(Collectors.toList());

        for (Piece p: opponentPieces) {
            if (p.possibleMove(kingPosition)) {
                return true;
            }
        }

        return false;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

}
