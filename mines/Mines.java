package mines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Mines {

    private final Place[][] board;
    @SuppressWarnings("unused")
    private final int numMines;
    private final int width;
    private final int height;
    private boolean showAll = false;

    // Enum to define cell states
    private enum States { FREE, MINE }

    // Inner class representing a single cell on the board
    private class Place {
        private final int row;
        private final int col;
        private boolean isFlagged;
        private boolean isOpen = false;
        private States state;

        // Constructor for Place
        Place(int i, int j, States s) {
            row = i;
            col = j;
            state = s;
        }

        // Convert cell to display string
        @Override
        public String toString() {
            if (!isOpen && !showAll) {
                return isFlagged ? "F" : ".";
            }
            if (state == States.MINE) return "X";
            int count = minesNearMe();
            return count == 0 ? " " : String.valueOf(count);
        }

        // Get all valid neighboring cells
        public ArrayList<Place> neighbours() {
            ArrayList<Place> res = new ArrayList<>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((i != 0 || j != 0) && isValid(row + i, col + j)) {
                        res.add(board[row + i][col + j]);
                    }
                }
            }
            return res;
        }

        // Count mines in neighboring cells
        public int minesNearMe() {
            int count = 0;
            for (Place p : neighbours()) {
                if (p.state == States.MINE) count++;
            }
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Place)) return false;
            Place other = (Place) o;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    // Creates board and places mines randomly
    public Mines(int height, int width, int numMines) {
        this.height = height;
        this.width = width;
        this.numMines = numMines;

        // Initialize board with free cells
        board = new Place[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Place(i, j, States.FREE);
            }
        }

        // Place mines randomly
        Random rnd = new Random();
        for (int k = 0; k < numMines; k++) {
            addMine(rnd.nextInt(height), rnd.nextInt(width));
        }
    }

    // Add mine to specific position
    public boolean addMine(int row, int col) {
        board[row][col].state = States.MINE;
        return true;
    }

    // Open a cell - returns false if mine hit
    public boolean open(int row, int col) {
        if (board[row][col].state == States.MINE) return false;

        HashSet<Place> checked = new HashSet<>();
        openNeighbours(board[row][col], checked);
        return true;
    }

    // Recursive method to open neighboring cells with no mines nearby
    private void openNeighbours(Place p, HashSet<Place> checked) {
        if (checked.contains(p)) return;

        checked.add(p);
        if (p.state != States.MINE) p.isOpen = true;

        if (p.minesNearMe() == 0) {
            for (Place n : p.neighbours()) {
                openNeighbours(n, checked);
            }
        }
    }

    // Check if coordinates are within board bounds
    private boolean isValid(int i, int j) {
        return i >= 0 && i < height && j >= 0 && j < width;
    }

    // Toggle flag on a cell
    public void toggleFlag(int row, int col) {
        board[row][col].isFlagged = !board[row][col].isFlagged;
    }

    // Check if game is won (all non-mine cells opened)
    public boolean isDone() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!board[i][j].isOpen && board[i][j].state != States.MINE) return false;
            }
        }
        return true;
    }

    public String get(int row, int col) {
        return board[row][col].toString();
    }

    // Set whether to show all cells (for game over)
    public void setShowAll(boolean show) {
        showAll = show;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) b.append(get(i, j));
            b.append('\n');
        }
        return b.toString();
    }
}