import java.util.*;
public class SudokuSolverObjError
{

	static int N = 9;
	static int Nroot = 3;
	static String Alpha = "123456789";
	static int[][] BoxRowIndex = new int[][] {
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{3,3,3,3,3,3,3,3,3},
		{3,3,3,3,3,3,3,3,3},
		{3,3,3,3,3,3,3,3,3},
		{6,6,6,6,6,6,6,6,6},
		{6,6,6,6,6,6,6,6,6},
		{6,6,6,6,6,6,6,6,6},
	};static int[][] BoxColIndex = new int[][] {
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6},
		{0,0,0,3,3,3,6,6,6}
	};
	public static void main(String [] args)
	{
		long startTime = System.nanoTime();
		String Game1="4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
		String Game3="41736.825.3.1........7......2.43..6.....8.4...4..1....2896435715632719..1748...3.";
		// worlds hardest puzzle is game 2
		String Game2="8..........36......7..9.2...5...7.......457.....1...3...1....68..85...1..9....4..";
		// create a new object "Sudoku" of class SudokuSolver
		SudokuSolver Sudoku = new SudokuSolver();
		// create a new object of class "SolutionGrid".  This is a nested Class.
		SudokuSolver.SolutionGrid Values = Sudoku.new SolutionGrid();
		// create a new object for the starting PuzzleGrid
		SudokuSolver.PuzzleGrid Puzzle = Sudoku.new PuzzleGrid();
		// create a new object for the starting PuzzleGrid
		Puzzle.InitPuzzle(Game1);
		Values = Sudoku.Initialize(Values, Puzzle);
		Puzzle.PrintPuzzle();
		Values.PrintPuzzle();
		System.out.println(Values.IsSolvable);
/** **/
		if (!Values.testIsSolved()){
			System.out.println(" inside if statement Grid is : " + Values.testIsSolved());
			System.out.println(" minLength check = " + Values.minLength);

			Values = Sudoku.Search(Values);
		}
/** **/
		long endTime = System.nanoTime();
		System.out.print("Time taken (s) = ");
		System.out.println((endTime - startTime)/1000000000.0);
		Values.PrintPuzzle();
		System.out.println(Values.testIsSolved());
		System.out.println(" is solved var = " + Values.IsSolved);
		System.out.println(" is solvable = : " + Values.IsSolvable);

	}
	// -----------------------------------
	/**
	 * Initialize
	 * board squares.
	**/
	SolutionGrid Initialize(SolutionGrid Values, PuzzleGrid Puzzle) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Values = Assign(Values, i, j, Puzzle.Grid[i][j]);
				if (Values.IsSolvable == false) {
					break;
				}
			}
		}
		return Values;
	}
	// -----------------------------------
	/**
	 * Initialize
	 * board squares.
	**/
	SolutionGrid Search(SolutionGrid Values) {
		int Row = -1, Col = -1;
		int minLength = 9;
//		while (!Values.testIsSolved() && Values.IsSolvable) { // do we need this loop?  Is Solvable creates a problem
			// now cycle through each cell to see which has shortest length
			RowLoop: // Label for loop break statements
			for (int i = 0; i < N; i++) {
				ColLoop: // Label for loop break statements
				for (int j = 0; j < N; j++) {
					// Do something here
					if (Values.Grid[i][j].length() < minLength && Values.Grid[i][j].length()>1 ){
						minLength = Values.Grid[i][j].length();
						// Record Row and Column
						Row = i;
						Col = j;
					}
				} // ColLoop
			} // RowLoop
// maybe this needs to be split into a couple of functions to work?
// also do i need to set a new minimum cell?  maybe this is already set?
			Values = Iterate(Values, Row, Col);
			System.out.println("After Iterate: Row = " + Row + " Col = " + Col + " IsSolvable = " + Values.IsSolvable);

//		} // while loop
		return Values;
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	SolutionGrid Iterate(SolutionGrid Values, int Row, int Col) {
		// now loop throughthe chars
		char [] chars = Values.Grid[Row][Col].toCharArray();
		CharLoop: // label for break statement
		for (char c : chars) {
			System.out.println(" Row = " + Row + " Col = " + Col + " Char = " + c + " IsSolvable = " + Values.IsSolvable);
			Values.IsSolvable = true; //  trying to solve the problem of IsSolvable being set to false on new Char
			Values = Assign(Values, Row, Col, String.valueOf(c));
			System.out.println("Before Break: Row = " + Row + " Col = " + Col + " Char = " + c + " IsSolvable = " + Values.IsSolvable);
			if (Values.IsSolvable) Search(Values);  //  what happens to IsSolvable if false and want to loop through next char?
			System.out.println("After Break: Row = " + Row + " Col = " + Col + " Char = " + c + " IsSolvable = " + Values.IsSolvable);
		}
		System.out.println("After Char Loop: Row = " + Row + " Col = " + Col + " IsSolvable = " + Values.IsSolvable);

		return Values;
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	SolutionGrid Assign(SolutionGrid Values, int Row, int Col, String Value) {
		// first replace Soultion Grid Value if this is one of the 'Alpha Strings'
		if (Alpha.indexOf(Value)>=0 && Values.IsSolvable) {
			Values.Grid[Row][Col] = Value;
			// Row elimination
			for (int Index = 0; Index < N; Index++) {
				// Do Column
				if (Index != Row) {
					Values = Eliminate(Values, Index, Col, Value);
					if (!Values.IsSolvable) return Values;
				}
				// Do Row
				if (Index != Col) {
					Values = Eliminate(Values, Row, Index, Value);
					if (!Values.IsSolvable) return Values;
				}
				// Box elimination
				if (Index >= BoxRowIndex[Row][Col] && Index < BoxRowIndex[Row][Col] + Nroot) {
					for (int j = BoxColIndex[Row][Col]; j < BoxColIndex[Row][Col] + Nroot; j++) {
						if (Index != Row && j!= Col) {
							Values = Eliminate(Values, Index, j, Value);
							if (!Values.IsSolvable) return Values;
						}
					}
				}
			}
		}
		return Values;
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	SolutionGrid Eliminate(SolutionGrid Values, int Row, int Col, String Value) {
		// first replace Solution Grid Value
		if (!Values.IsSolvable) {
			return Values;
		}
//		System.out.println(" Row = " + Row + " Col = " + Col + " Value = " + Value);
//		Values.PrintPuzzle();

		char [] chars = Alpha.toCharArray();
		int PreviousLength = Values.Grid[Row][Col].length(); // check length of String before elimination
		Values.Grid[Row][Col] = Values.Grid[Row][Col].replace(Value, ""); // eliminate the value from this cell
		if (Values.Grid[Row][Col].length() < Values.minLength && Values.Grid[Row][Col].length() > 1) Values.minLength = Values.Grid[Row][Col].length();
		// method 1.  PENCIL IN: Check if a square is reduced to only one possible value (but had more than one value before)
		if (Values.Grid[Row][Col].length() == 0) {
			Values.IsSolvable = false; // This is in case there is a contradiction
			System.out.println("contradiction found in method 1");
			Values.PrintPuzzle();
			return Values;
		}  else if(Values.Grid[Row][Col].length() == 1 && PreviousLength > 1) {
			Values = Assign(Values, Row, Col, Values.Grid[Row][Col]);
	 	}
		// method 2.  CROSS HATCH: now cycle through each region and see where a number can go
		// can we be smart and only cycle through only regions that would change on this paa of eliminate?
		// first cycle through each row:
/**

		int[] Count = new int[Alpha.length()];
		Arrays.fill(Count,0);
		String RowString = "";
		String ColString = "";
		String BoxString = "";
		//
		// Step 1.  Create a long string of all the Strings in each region put together
		for (int Index = 0; Index < N; Index++) {
			// Row first:
			RowString = RowString + Values.Grid[Row][Index];
			// Col Next:
			ColString = ColString + Values.Grid[Index][Col];
			// Box Last:
			if (Index >= BoxRowIndex[Row][Col] && Index < BoxRowIndex[Row][Col] + Nroot) {
				for (int j = BoxColIndex[Row][Col]; j < BoxColIndex[Row][Col] + Nroot; j++) {
					BoxString = BoxString + Values.Grid[Index][j];
				}
			}
		}
		//
		// Step 2.  Cycle through characters to see if more ONLY one of a particular char, then check
		// if the cell length was greater than 1
		for (char c : chars) {
			// check rows first.  Is there only one of the chars there?
			if (RowString.indexOf(c) == RowString.lastIndexOf(c)) {
				// check consistency (does char exist in box?)
				if (RowString.indexOf(c) >=0) {
					// System.out.println(" c = " + c + " RowString = " + RowString + " First Index = " + RowString.indexOf(c) + " Last Index = " + RowString.lastIndexOf(c));
					// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
					for (int Index = 0; Index < N; Index++) {
						if (Values.Grid[Row][Index].length() > 1 && Values.Grid[Row][Index].indexOf(c) >=0) {
							Values = Assign(Values, Row, Index, String.valueOf(c));
							if (!Values.IsSolvable) return Values;
						}
					}
				} else {
					Values.IsSolvable = false; // This is in case there is a contradiction
					System.out.println("contradiction found in method 2 rows");
					return Values;
				}
			}
			// check cols second.  Is there only one of the chars there?
			if (ColString.indexOf(c) == ColString.lastIndexOf(c)) {
				// check consistency (does char exist in box?)
				if (ColString.indexOf(c) >=0) {
					// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
					for (int Index = 0; Index < N; Index++) {
						if (Values.Grid[Index][Col].length() > 1 && Values.Grid[Index][Col].indexOf(c) >=0) {
							// System.out.println(" c = " + c + " ColString = " + ColString + " First Index = " + ColString.indexOf(c) + " Last Index = " + ColString.lastIndexOf(c));
							Values = Assign(Values, Index, Col, String.valueOf(c));
							if (!Values.IsSolvable) return Values;
						}
					}
				} else {
					Values.IsSolvable = false; // This is in case there is a contradiction
					System.out.println("contradiction found in method 2 cols");
					return Values;
				}
				// check box last.  Is there only one of the chars there?
				if (BoxString.indexOf(c) == BoxString.lastIndexOf(c)) {
					// check consistency (does char exist in box?)
					if (BoxString.indexOf(c) >=0) {
						// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
						for (int i = BoxRowIndex[Row][Col]; i < BoxRowIndex[Row][Col] + Nroot; i++) {
							for (int j = BoxColIndex[Row][Col]; j < BoxColIndex[Row][Col] + Nroot; j++) {
								if (Values.Grid[i][j].length() > 1 && Values.Grid[i][j].indexOf(c) >=0) {
									// System.out.println(" c = " + c + " ColString = " + ColString + " First Index = " + ColString.indexOf(c) + " Last Index = " + ColString.lastIndexOf(c));
									Values = Assign(Values, i, j, String.valueOf(c));
									if (!Values.IsSolvable) return Values;
								}
							}
						}
					} else {
						Values.IsSolvable = false; // This is in case there is a contradiction
						System.out.println("contradiction found in method 2 box");
						return Values;
					}
				}
			}
		}

/** **/

		return Values;
	}



// -----------------------------------
	/**
	 * Create Object for Board
	**/
	class PuzzleGrid {
		String [][] Grid = new String [N][N];

		// create constructor for PuzzleGrid
		PuzzleGrid() {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					Grid[i][j]= Alpha;
				}
			}
		}
		// ---
		void InitPuzzle(String GameInput) {
			int i = 0, j = 0, k = 0;
			String StringMatch = Alpha + ".0";
			char[] Game = GameInput.toCharArray();
			while (k < GameInput.length()) {
				if (StringMatch.indexOf(Game[k])>=0) {
					if (Game[k] == '0'){
						Game[k] = '.';
					}
					Grid[i][j] = String.valueOf(Game[k]);
					if (j < N - 1) {
						j++;
					} else if(i < N - 1 ){
						j = 0;
						i++;
					} else {
						break;
					}
				}
				k++;
			}
		}
		// ---
		void PrintPuzzle() {
			// change horizontal lines below  (probable by adding print string into one string and measuring length)
			System.out.println("| --------- --------- --------- | --------- --------- --------- | --------- --------- --------- |");
			// add blanks as way of achieving above;
			String Blanks = "         ";
			for (int i = 0; i < N; i++) {
				System.out.print("| ");
				for (int j = 0; j < N; j++) {
					System.out.print(Grid[i][j] + Blanks.substring((Grid[i][j].length())));
					System.out.print(" ");
					if (j % Nroot == Nroot - 1) {
						System.out.print("| ");
					}
				}
				System.out.println();
				if (i % Nroot == Nroot - 1) {
					System.out.println("| --------- --------- --------- | --------- --------- --------- | --------- --------- --------- |");
				}
			}
		}
		// ---
		void PuzzGridTest() {
			System.out.println(" method PuzzGridTest");
		}
	}
// -----------------------------------
	/**
	 * Create Solution Object
	**/
	class SolutionGrid extends PuzzleGrid {
		boolean IsSolvable = true;
		boolean IsSolved = false;
		int minLength = 9;


		// create constructor for SolutionGrid
		void SolGridTest() {
			System.out.println(" method SolGridTest");
		}
		// create constructor for PuzzleGrid
		boolean testIsSolved() {
			int len = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					len = len + Grid[i][j].length();
				}
			}
			IsSolved = (len == N * N);
			return IsSolved;
		}

	}
}
