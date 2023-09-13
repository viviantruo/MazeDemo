// Name: Vivian Truong

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
            }
        }
        this.startRow = startRow;
        this.endRow = endRow;
    } // end constructor

    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {
        MyMaze myMaze = new MyMaze(rows, cols, startRow, endRow);
        int cRow;
        int cCol;
        int nRow = -1; //neighbor row
        int nCol = -1;
        int random = -1;
        boolean invalidNeighbor = true;
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        myMaze.maze[endRow][cols-1].setRight(false);
        myMaze.maze[startRow][0].setVisited(true);
        stack.push(new int[]{startRow, 0});
        while (!stack.isEmpty()) {
            cRow = ((int[])stack.top())[0];
            cCol = ((int[])stack.top())[1];
            if (!myMaze.UnvisitedNeighbors(cRow, cCol)) {
                stack.pop();
            } else {
                invalidNeighbor = true;
                while (invalidNeighbor) { // try to find valid neighbor
                    random = (int) Math.floor(Math.random() * 4);
                    switch (random) {
                        case 0: // up neighbor
                            nRow = cRow - 1;
                            nCol = cCol;
                            break;
                        case 1: // down neighbor
                            nRow = cRow + 1;
                            nCol = cCol;
                            break;
                        case 2: // left neighbor
                            nRow = cRow;
                            nCol = cCol - 1;
                            break;
                        case 3: // right neighbor
                            nRow = cRow;
                            nCol = cCol + 1;
                            break;
                    }
                    if (myMaze.inBounds(nRow, nCol) && !myMaze.maze[nRow][nCol].getVisited()) {
                        invalidNeighbor = false; // found unvisited neighbor, stop looping
                    }
                }
                myMaze.maze[nRow][nCol].setVisited(true);
                stack.push(new int[]{nRow, nCol});
                switch (random) { // remove walls
                    case 0: // up neighbor
                        myMaze.maze[nRow][nCol].setBottom(false);
                        break;
                    case 1: // down neighbor
                        myMaze.maze[cRow][cCol].setBottom(false);
                        break;
                    case 2: // left neighbor
                        myMaze.maze[nRow][nCol].setRight(false);
                        break;
                    case 3: // right neighbor
                        myMaze.maze[cRow][cCol].setRight(false);
                        break;
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                myMaze.maze[i][j].setVisited(false);
            }
        }
        return myMaze;
    } // end makeMaze

    public boolean inBounds(int row, int col) { // checks if a given coordinate is in bounds
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
            return true;
    } // end validRowCol

    public boolean UnvisitedNeighbors(int row, int col) { // returns true if there are any unvisited cells next to the given coordinate
        if (this.inBounds(row - 1, col) &&
                !this.maze[row - 1][col].getVisited()) { // if neighbor above is unvisited
            return true;
        }
        if (this.inBounds(row + 1, col) &&
                !this.maze[row + 1][col].getVisited()) { // down neighbor
            return true;
        }
        if (this.inBounds(row, col - 1) &&
                !this.maze[row][col - 1].getVisited()) { // left neighbor
            return true;
        }
        if (this.inBounds(row, col + 1) &&
                !this.maze[row][col + 1].getVisited()) { // right neighbor
            return true;
        }
        return false;
        } // end UnvisitedNeighbors

    public void printMaze() {
        for (int j = 0; j < maze[0].length; j++) { //print top boundary
            System.out.print("|---");
        }
        System.out.println("|");
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (j == 0) { // left boundary
                    if (i == startRow) {
                        System.out.print(" ");
                    } else {
                        System.out.print("|");
                    }
                }
                if (maze[i][j].getVisited()) {
                    System.out.print(" * ");
                } else {
                    System.out.print("   ");
                }
                if (maze[i][j].getRight()) { // vertical walls
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
                if (j == maze[0].length - 1) {
                    System.out.println(); // next row
                }
            }
            for (int j = 0; j < maze[0].length; j++) { // horizontal walls
                if (maze[i][j].getBottom()) {
                    System.out.print("|---");
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.println("|");
        }
    }

    public void solveMaze() {
        Q2Gen<int[]> queue = new Q2Gen<int[]>();
        queue.add(new int[]{startRow, 0});
        int[] rowCol; // maze coordinates
        while (queue.length() != 0) {
            rowCol = (int[]) queue.remove();
            maze[rowCol[0]][rowCol[1]].setVisited(true);
            if (rowCol[0] == endRow && rowCol[1] == maze[0].length-1) { // reached the exit
                break;
            } // add neighbors to queue
            if (this.inBounds(rowCol[0] - 1, rowCol[1]) &&
                    !maze[rowCol[0] - 1][rowCol[1]].getVisited() && !maze[rowCol[0] - 1][rowCol[1]].getBottom()) { // above neighbor
                queue.add(new int[] {rowCol[0] - 1, rowCol[1]});
            }
            if (this.inBounds(rowCol[0] + 1, rowCol[1]) &&
                    !maze[rowCol[0] + 1][rowCol[1]].getVisited() && !maze[rowCol[0]][rowCol[1]].getBottom()) { // below neighbor
                queue.add(new int[] {rowCol[0] + 1, rowCol[1]});
            }
            if (this.inBounds(rowCol[0], rowCol[1] - 1) &&
                    !maze[rowCol[0]][rowCol[1] - 1].getVisited() && !maze[rowCol[0]][rowCol[1] - 1].getRight()) { // left
               queue.add(new int[] {rowCol[0], rowCol[1] - 1});
            }
            if (this.inBounds(rowCol[0], rowCol[1] + 1) &&
                    !maze[rowCol[0]][rowCol[1] + 1].getVisited() && !maze[rowCol[0]][rowCol[1]].getRight()) { // right
                queue.add(new int[] {rowCol[0], rowCol[1] + 1});
            }
        }
        this.printMaze();
    } // end solveMaze

    public static void main(String[] args){
        MyMaze maze = makeMaze(4, 5, 0, 2);
        System.out.println("Random Maze");
        maze.printMaze();
        System.out.println("\nMaze Solution");
        maze.solveMaze();
    }
}
