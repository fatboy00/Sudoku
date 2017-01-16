public class SudokuQ {
  /**
  * Print the specified Sudoku problem and its solution.  The
  * problem is encoded as specified in the class documentation
  * above.
  *
  * @param args The command-line arguments encoding the problem.
  */


  public static void main(String[] args) {
    // normal sudoku
    String game1="4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
    // puzzle with dissallowed variables - this seems to work.
    String game2="4.....8.5.3......xxx....7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
		// puzzle with dissallowed variables - and immediately inconsistent value.
    String game3="44....8.5.3......xxx....7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
		// inconsistent puzzle
    String game4="41736.825.3.1........7......2.43..6.....8.4...4..1....2896435715632719..1748...3.";
    // worlds hardest puzzle is game 2
    String game5="8..........36......7..9.2...5...7.......457.....1...3...1....68..85...1..9....4..";

	  int[][] matrix = parseProblem(game5);
    writeMatrix(matrix);
    long startTime = System.nanoTime();

    if (solve(0,0,matrix)) {    // solves in place
      long endTime = System.nanoTime();
      System.out.print("Time taken (s) = ");
      System.out.println((endTime - startTime)/1000000000.0);
      writeMatrix(matrix);
    } else {
      System.out.println("NONE");
      long endTime = System.nanoTime();
      System.out.print("Time taken (s) = ");
      System.out.println((endTime - startTime)/1000000000.0);
    }
  }

  static boolean solve(int i, int j, int[][] cells) {
    if (i == 9) {
      i = 0;
      if (++j == 9)
      return true;
    }
    if (cells[i][j] != 0)  // skip filled cells
      return solve(i+1,j,cells);

    for (int val = 1; val <= 9; ++val) {
      if (legal(i,j,val,cells)) {
        cells[i][j] = val;
        if (solve(i+1,j,cells))
          return true;
      }
    }
    cells[i][j] = 0; // reset on backtrack
    return false;
  }

  static boolean legal(int i, int j, int val, int[][] cells) {
    for (int k = 0; k < 9; ++k)  // row
      if (val == cells[k][j])
        return false;

    for (int k = 0; k < 9; ++k) // col
      if (val == cells[i][k])
        return false;

    int boxRowOffset = (i / 3)*3;
    int boxColOffset = (j / 3)*3;
    for (int k = 0; k < 3; ++k) // box
      for (int m = 0; m < 3; ++m)
        if (val == cells[boxRowOffset+k][boxColOffset+m])
          return false;

    return true; // no violations, so it's legal
  }

  static int[][] parseProblem(String GameInput) {
    int[][] problem = new int[9][9]; // default 0 vals
//
    int i = 0, j = 0, k = 0;
    String StringMatch =  "123456789.0";
    char[] Game = GameInput.toCharArray();
    while (k < GameInput.length()) {
      if (StringMatch.indexOf(Game[k])>=0) {
        if (Game[k] == '.'){
          Game[k] = '0';
        }
        problem[i][j] = String.valueOf(Game[k]).charAt(0) - '0';
        if (j < 8) {
          j++;
        } else if (j == 8) {
          j = 0;
          i++;
        } else {
          break;
        }
      }
      k++;
    }
    return problem;
  }

  static void writeMatrix(int[][] solution) {
	   for (int i = 0; i < 9; ++i) {
      if (i % 3 == 0)
        System.out.println(" -----------------------");
      for (int j = 0; j < 9; ++j) {
        if (j % 3 == 0) System.out.print("| ");
          System.out.print(solution[i][j] == 0
            ? " "
            : Integer.toString(solution[i][j]));

          System.out.print(' ');
        }
        System.out.println("|");
      }
      System.out.println(" -----------------------");
    }

} // end class
