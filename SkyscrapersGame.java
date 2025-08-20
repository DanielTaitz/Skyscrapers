public class SkyscrapersCore {
    private int size;
    private int[][] grid;          // The player's grid
    private int[] topClues;        // Clues from top
    private int[] bottomClues;     // Clues from bottom
    private int[] leftClues;       // Clues from left
    private int[] rightClues;      // Clues from right

    public SkyscrapersCore(int size, int[] top, int[] bottom, int[] left, int[] right) {
        this.size = size;
        this.grid = new int[size][size];
        this.topClues = top;
        this.bottomClues = bottom;
        this.leftClues = left;
        this.rightClues = right;
    }

    // This pllaces a building (height) at (row, col)
    public void setCell(int row, int col, int value) {
        if (value < 0 || value > size) throw new IllegalArgumentException("Invalid value");
        grid[row][col] = value;
    }

    // this checks if the row has unique values like no duplicates
    private boolean checkRowUnique(int row) {
        boolean[] seen = new boolean[size + 1];
        for (int val : grid[row]) {
            if (val == 0) continue; // empty
            if (seen[val]) return false;
            seen[val] = true;
        }
        return true;
    }

    // Check if column has unique values
    private boolean checkColUnique(int col) {
        boolean[] seen = new boolean[size + 1];
        for (int row = 0; row < size; row++) {
            int val = grid[row][col];
            if (val == 0) continue;
            if (seen[val]) return false;
            seen[val] = true;
        }
        return true;
    }

    // Compute how many buildings are visible from one side
    private int visibleCount(int[] line) {
        int visible = 0;
        int tallest = 0;
        for (int h : line) {
            if (h > tallest) {
                visible++;
                tallest = h;
            }
        }
        return visible;
    }

    // Extract a row
    private int[] getRow(int row) {
        return grid[row].clone();
    }

    // Extract a column
    private int[] getCol(int col) {
        int[] c = new int[size];
        for (int i = 0; i < size; i++) c[i] = grid[i][col];
        return c;
    }

    // Check all clues
    private boolean checkClues() {
        // Left and Right
        for (int r = 0; r < size; r++) {
            int[] row = getRow(r);
            if (leftClues[r] != 0 && visibleCount(row) != leftClues[r]) return false;

            int[] reversedRow = reverse(row);
            if (rightClues[r] != 0 && visibleCount(reversedRow) != rightClues[r]) return false;
        }
        // Top and Bottom
        for (int c = 0; c < size; c++) {
            int[] col = getCol(c);
            if (topClues[c] != 0 && visibleCount(col) != topClues[c]) return false;

            int[] reversedCol = reverse(col);
            if (bottomClues[c] != 0 && visibleCount(reversedCol) != bottomClues[c]) return false;
        }
        return true;
    }

    private int[] reverse(int[] arr) {
        int[] rev = new int[arr.length];
        for (int i = 0; i < arr.length; i++) rev[i] = arr[arr.length - 1 - i];
        return rev;
    }

    // Check if puzzle is solved
    public boolean isSolved() {
        // Check uniqueness
        for (int r = 0; r < size; r++) if (!checkRowUnique(r)) return false;
        for (int c = 0; c < size; c++) if (!checkColUnique(c)) return false;

        // Check clues
        return checkClues();
    }

    // Debug print
    public void printGrid() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
    }

    // Example use case
    public static void main(String[] args) {
        // Example 4x4 puzzle with clues
        int[] top    = {2, 2, 1, 3};
        int[] bottom = {2, 2, 3, 1};
        int[] left   = {3, 2, 2, 1};
        int[] right  = {1, 2, 2, 3};

        SkyscrapersCore game = new SkyscrapersCore(4, top, bottom, left, right);

        // Fill solution (this would normally be solved by player)
        int[][] solution = {
                {2, 1, 4, 3},
                {3, 4, 1, 2},
                {4, 2, 3, 1},
                {1, 3, 2, 4}
        };

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                game.setCell(r, c, solution[r][c]);
            }
        }

        game.printGrid();
        System.out.println("Solved? " + game.isSolved());
    }
}