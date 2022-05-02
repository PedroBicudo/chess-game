package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.security.InvalidParameterException;
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
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public ChessMatch() {
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public void initialSetup() {
        // WHITE
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        for (char col='a'; col <= 'h'; col++) {
            placeNewPiece(col, 2, new Pawn(board, Color.WHITE, this));
        }

        // BLACK
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));

        for (char col='a'; col <= 'h'; col++) {
            placeNewPiece(col, 7, new Pawn(board, Color.BLACK, this));
        }

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

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // #special-move: promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            Position pos = movedPiece.getChessPosition().toPosition();
            if (
                (movedPiece.getColor() == Color.WHITE && pos.getRow() == 0) ||
                (movedPiece.getColor() == Color.BLACK && pos.getRow() == 7)
            ) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");

            } else {
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;

        } else {
            nextTurn();

        }

        // #special-move: en passant
        if (
                movedPiece instanceof Pawn &&
                ((target.getRow() == source.getRow() - 2) ||
                (target.getRow() == source.getRow() + 2))
        ) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;

    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            throw new InvalidParameterException("Invalid type for promotion");
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        switch (type) {
            case "B": return new Bishop(board, color);
            case "N": return new Knight(board, color);
            case "Q": return new Queen(board, color);
            default: return new Rook(board, color);
        }
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

        ((ChessPiece) piece).increaseMoveCount();
        board.placePiece(piece, target);

        // #special-move: castling (right)
        if (piece instanceof King && (target.getColumn() == source.getColumn()+2)) {
            Position sourceRook = new Position(source.getRow(), source.getColumn()+3);
            Position targetRook = new Position(source.getRow(), source.getColumn()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
            board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // #special-move: castling (right)
        if (piece instanceof King && (target.getColumn() == source.getColumn()-2)) {
            Position sourceRook = new Position(source.getRow(), source.getColumn()-4);
            Position targetRook = new Position(source.getRow(), source.getColumn()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
            board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // #special-move: en passant
        if (piece instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPos;
                if (((Pawn) piece).getColor() == Color.WHITE) {
                    pawnPos = new Position(target.getRow()+1, target.getColumn());
                } else {
                    pawnPos = new Position(target.getRow()-1, target.getColumn());

                }
                capturedPiece = board.removePiece(pawnPos);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

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

        // #special-move: castling (right)
        if (piece instanceof King && (target.getColumn() == source.getColumn()+2)) {
            Position sourceRook = new Position(source.getRow(), source.getColumn()+3);
            Position targetRook = new Position(source.getRow(), source.getColumn()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
            board.placePiece(rook, sourceRook);
        }

        // #special-move: castling (right)
        if (piece instanceof King && (target.getColumn() == source.getColumn()-2)) {
            Position sourceRook = new Position(source.getRow(), source.getColumn()-4);
            Position targetRook = new Position(source.getRow(), source.getColumn()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
            board.placePiece(rook, sourceRook);
        }

        // #special-move: en passant
        if (piece instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPos;

                if (((Pawn) piece).getColor() == Color.WHITE) {
                    pawnPos = new Position(3, target.getColumn()+1);
                } else {
                    pawnPos = new Position(4, target.getColumn()-1);

                }
                board.placePiece(pawn, pawnPos);
            }
        }

        ((ChessPiece) piece).decreaseMoveCount();

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

                        if (!testCheck) {
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

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }
}
