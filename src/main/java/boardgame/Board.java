package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error Creating Board: there must be at least 1 row and 1 column");
        }

        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("There is already on position "+position);
        }

        if (piece == null) {
            throw new BoardException("Piece must be not null");
        }

        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return piece(position.getRow(), position.getColumn());
    }

    public Piece piece(int row, int column) {
        return pieces[row][column];
    }

    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }

        return piece(position) != null;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    private boolean positionExists(int row, int column) {
        return (row >= 0 && column >= 0) && (row < this.rows && column < this.columns);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] pieces) {
        this.pieces = pieces;
    }
}
