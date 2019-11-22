// Connect4 FXML Controller
// Andrew Gasparovich
// CIS 296
// Prof John Baugh

package Connect4;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class ConnectFXMLController implements Initializable {

    @FXML
    private GridPane gameGrid;
    @FXML
    private Label whoseTurn;

    // For keeping track of whose turn it is
    private int playerTurn = 1;

    // Keeps track of how many pieces have been placed in the game
    // Exists only because I like the pieces to fall down upon reset
    private int numPieces = 0;
    
    // For keeping track of where to display a newly placed piece
    private int column1Pos = 1;
    private int column2Pos = 1;
    private int column3Pos = 1;
    private int column4Pos = 1;
    private int column5Pos = 1;
    private int column6Pos = 1;
    private int column7Pos = 1;
    
    // For keeping track of which player's piece is where
    private int grid[][] = new int[6][7];
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        // Initially the grid has no pieces in it, so I'll use zero
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                grid[i][j] = 0;
            }
        }
    }    
    
    // Animates a piece as dropping off the board
    private void dropPiece(Node piece) {            
        TranslateTransition reset = new TranslateTransition(); 
        reset.setDuration(Duration.millis(3000)); 
        reset.setByY(1000);
        reset.setCycleCount(1); 
        reset.setAutoReverse(false); 
        reset.setNode(piece);
        reset.play();
    }
    
    // Resets the board so another game can be played
    private void resetBoard() {
        // Drop all pieces on the board
        // The objects stay in memory because I don't know how
        // to make the animation work AND remove the circle
        // objects from the GridPane object 
        for (int i = 0; i < numPieces; i++) {
            dropPiece(gameGrid.getChildren().get(i));
        }
        
        // Reset player turn status to player 1 and reset
        // all the column position indices so that new pieces
        // can be played
        whoseTurn.setText("Player 1's turn (black)");
        playerTurn = 0;
        column1Pos = 1;
        column2Pos = 1;
        column3Pos = 1;
        column4Pos = 1;
        column5Pos = 1;
        column6Pos = 1;
        column7Pos = 1;

        // Setting the 2D array to contain all zeros
        // so a new game can be played
        for (int j = 0; j < 6; j++) {
            for (int k = 0; k < 7; k++) {
                grid[j][k] = 0;
            }
        }
    }
    
    // If the grid fills up and there's no winner, the grid
    // will be forcefully reset after a message
    private boolean gridFull() {
        if (column1Pos == 7 && column2Pos == 7 && column3Pos == 7 &&
            column4Pos == 7 && column5Pos == 7 && column6Pos == 7 &&
            column7Pos == 7) {
        
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("The Game Grid is Full!");
            alert.setHeaderText(null);
            alert.setContentText("The board will be reset.");
            alert.showAndWait();
            
            return true;
        }
        return false;
    }
    
    // Displays who the winner is and resets the board
    private void displayWinner(int winnerIndex) {
        String winner = "Player ";
        winner = winner + winnerIndex + " wins!";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("We have a winner!");
        alert.setHeaderText(null);
        alert.setContentText(winner);
        alert.showAndWait();
        resetBoard();
    }
    
    // Checks for a win each time a piece is placed
    // Vertically, horizontally, and diagonally
    private void checkForWinner(int row, int col) {
        
        int playerIndex = grid[row][col];
        int numConnected;
        int tempCol;
        int tempRow;
        
        // This variable will hold the number of pieces connected
        // It will always be at least 1
        numConnected = 1;        
        
        // Vertical downward
        for(int i = row - 1; i > 0 && grid[i][col] == playerIndex; i--) {
            numConnected++;
        }
        
        // Vertical upward
        for(int i = row + 1; i < 6 && grid[i][col] == playerIndex; i++) {
            numConnected++;
        }
        
        // Exits the function, displays the winner, and
        // resets the board if a vertical victory occurs
        if (numConnected >= 4) {
            if (playerTurn % 2 == 0) {
                displayWinner(2);
            }
            else {
                displayWinner(1);
            }
        }
        
        // Start over if this move doesn't result in a vertical win
        numConnected = 1;
        
        // Horizontal left
        for(int j = col - 1; j >= 0 && grid[row][j] == playerIndex; j--) {
                numConnected++;
        }
                
        // Horizontal right
        for(int j = col + 1; j <= 6 && grid[row][j] == playerIndex; j++) {
                numConnected++;
        }
        
        // Exits the function, displays the winner, and
        // resets the board if a horizontal victory occurs
        if (numConnected >= 4) {
            if (playerTurn % 2 == 0) {
                displayWinner(2);
            }
            else {
                displayWinner(1);
            }
            return;
        }
        
        // Start over if this move doesn't result in a horizontal win
        numConnected = 1;
        
        // Diagonal up and right
        tempRow = row - 1;
        tempCol = col + 1; // starts at the piece up and to the right
        
        while (tempRow >= 0 && tempCol <= 6 && grid[tempRow][tempCol] == playerIndex) {
            numConnected++;
            tempRow--; tempCol++;
        }
                
        // Diagonal down and left
        tempRow = row + 1;
        tempCol = col - 1; // starts at the piece down and to the left
        
        while (tempRow <= 5 && tempCol >= 0 && grid[tempRow][tempCol] == playerIndex) {
            numConnected++;
            tempRow++; tempCol--;
        }
        
        // Exits the function, displays the winner, and resets
        // the board if a diagonal victory (positive slope) occurs
        if (numConnected >= 4) {
            if (playerTurn % 2 == 0) {
                displayWinner(2);
            }
            else {
                displayWinner(1);
            }
        }         
                
        // This is needed, otherwise a player could
        // win with a bent diagonal line of pieces
        numConnected = 1;
        
        // Diagonal up and left
        tempRow = row - 1;
        tempCol = col - 1; // starts at the piece up and to the left
        
        while (tempRow >= 0 && tempCol >= 0 && grid[tempRow][tempCol] == playerIndex) {
            numConnected++;
            tempRow--; tempCol--;
        }
                
        // Diagonal down and right
        tempRow = row + 1;
        tempCol = col + 1; // starts at the piece down and to the right
        
        while (tempRow <= 5 && tempCol <= 6 && grid[tempRow][tempCol] == playerIndex) {
            numConnected++;
            tempRow++; tempCol++;
        }
                
        // Exits the function, displays the winner, and resets
        // the board if a diagonal victory (negative slope) occurs
        if (numConnected >= 4) {
            if (playerTurn % 2 == 0) {
                displayWinner(2);
            }
            else {
                displayWinner(1);
            }
        }
                
        // If there's no winner and the grid is full,
        // drop all the pieces and start over
        if (gridFull())
            resetBoard();
    }
    
    // Cool function that makes new pieces drop into the game grid
    private void animatePiece(int col, Circle piece) {
            TranslateTransition translateTransition = new TranslateTransition(); 
            translateTransition.setDuration(Duration.millis(750 - 10 * col)); 
            translateTransition.setNode(piece);
            translateTransition.setByY(gameGrid.getHeight() * 5 - 50 * col);
            translateTransition.setCycleCount(1); 
            translateTransition.setAutoReverse(false); 
            translateTransition.play(); 
    }
    
    // 
    private int playPiece(int columnPos, int column) {
        // Creates game piece out of thin air
        Circle piece = new Circle();
        piece.setRadius(20);
        GridPane.setHalignment(piece, HPos.CENTER);
        numPieces++;
        
        // Paints the game piece according to whose turn it is, the 
        // piece will be black or red. 1 is inserted into the 2D array
        // for black pieces, and -1 for red pieces
        if (playerTurn % 2 == 0) {            
            grid[6 - columnPos][column - 1] = -1;
            piece.setFill(Color.RED);
            whoseTurn.setText("Player 1's turn (black)");
        }
        else {
            grid[6 - columnPos][column - 1] = 1;
            piece.setFill(Color.BLACK);
            whoseTurn.setText("Player 2's turn (red)");
        }
        
        // Animates the piece dropping into the grid and adds the
        // correct value to the 2D array so we know what pieces are where
        animatePiece(columnPos++, piece);
        gameGrid.add(piece, column - 1, 0);
       
        // Return the column position because java doesn't
        // allow passing by reference which makes me sad
        return columnPos;
    }
    
    // The next 7 functions handle putting new pieces into the game grid.
    // These functions correspond to columns. Comments will only be on the first.
    @FXML
    private void playPiece1(ActionEvent event) {
        // Prevents the game grid from overflowing
        if (column1Pos > 6)
            return;
        
        // Plays the piece, updates column position to reflect
        // number of pieces in that column
        column1Pos = playPiece(column1Pos, 1);
        
        // Checks for winner, could be improved to prevent needless checking
        checkForWinner(6 - (column1Pos - 1), 0);
        
        // Your turn!
        playerTurn++;
    }

    @FXML
    private void playPiece2(ActionEvent event) {        
        if (column2Pos > 6)
            return;
        
        column2Pos = playPiece(column2Pos, 2);
        
        checkForWinner(6 - (column2Pos - 1), 1);
        
        playerTurn++;
    }

    @FXML
    private void playPiece3(ActionEvent event) {
        if (column3Pos > 6)
            return;
        
        column3Pos = playPiece(column3Pos, 3);
        
        checkForWinner(6 - (column3Pos - 1), 2);
        
        playerTurn++;
    }

    @FXML
    private void playPiece4(ActionEvent event) {
        if (column4Pos > 6)
            return;
        
        column4Pos = playPiece(column4Pos, 4);
        
        checkForWinner(6 - (column4Pos - 1), 3);
        
        playerTurn++;
    }

    @FXML
    private void playPiece5(ActionEvent event) {
        if (column5Pos > 6)
            return;
        
        column5Pos = playPiece(column5Pos, 5);
        
        checkForWinner(6 - (column5Pos - 1), 4);
        
        playerTurn++;
    }

    @FXML
    private void playPiece6(ActionEvent event) {
        if (column6Pos > 6)
            return;
        
        column6Pos = playPiece(column6Pos, 6);
        
        checkForWinner(6 - (column6Pos - 1), 5);
        
        playerTurn++;
    }

    @FXML
    private void playPiece7(ActionEvent event) {
        if (column7Pos > 6)
            return;
        
        column7Pos = playPiece(column7Pos, 7);
        
        checkForWinner(6 - (column7Pos - 1), 6);
        
        playerTurn++;
    }

    // Allows the user to quit the game and displays
    // a cute little confirmation message
    @FXML
    private void quitPressed(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quit Connect Four");
        alert.setHeaderText("Every time you quit my game, I will club a baby seal.");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
        } else {
            alert.close();
        }
    }

    // Allows the user to reset the board and start a new game
    // out of frustration if they're losing
    @FXML
    private void resetPressed(ActionEvent event) {
        resetBoard();
    }
}