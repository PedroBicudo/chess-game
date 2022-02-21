package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner in = new Scanner(System.in);

        while (true) {
            UI.printBoard(chessMatch.getPieces());

            System.out.println();
            System.out.print("Source: ");
            ChessPosition source = UI.readChessPosition(in);

            System.out.println();
            System.out.print("Target: ");
            ChessPosition target = UI.readChessPosition(in);

            ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
        }

    }
}
