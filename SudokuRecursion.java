
public class SudokuRecursion
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
		String Game1="4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
		// create a new object "Sudoku" of class SudokuRecursion
		SudokuRecursion Sudoku = new SudokuRecursion();
		// create a new object of class "SolutionGrid".  This is a nested Class.
		SudokuRecursion.SolutionGrid Values = Sudoku.new SolutionGrid();
		// create a new object for the starting PuzzleGrid
		SudokuRecursion.PuzzleGrid Puzzle = Sudoku.new PuzzleGrid();
		// create a new object for the starting PuzzleGrid
		//Puzzle.InitPuzzle(Game1);
		//Puzzle.PrintPuzzle();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Sudoku.Assign(Values, i, j, Puzzle.Grid[i][j]);
				if (Values.IsSolvable == false) {
					break;
				}
			}
		}

		//Values.PrintPuzzle();
		int num = 5;
		System.out.printf("%d! = %d\n", num, factorial(num));
		SudokuRecursion.Fact Factor = Sudoku.new Fact();
		System.out.println("Factor = " + Factor.n);
		Factor = factorialObj(Factor);
		System.out.printf("Object Factorial = %d\n", Factor.n);

	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	void Assign(SolutionGrid Values, int Row, int Col, String Value) {
		// first replace Soultion Grid Value if this is one of the 'Alpha Strings'
		if (Alpha.indexOf(Value)>=0) {
			Values.Grid[Row][Col] = Value;
			// Row elimination
			for (int i = 0; i < N; i++) {
				if (i != Row) {
					Eliminate(Values, i, Col, Value);
				}
			}
			// Col elimination
			for (int j = 0; j < N; j++) {
				if (j != Col) {
					Eliminate(Values, Row, j, Value);
				}
			}
			// Box elimination
			for (int i = BoxRowIndex[Row][Col]; i < BoxRowIndex[Row][Col] + Nroot; i++) {
				for (int j = BoxColIndex[Row][Col]; j < BoxColIndex[Row][Col] + Nroot; j++) {
					if (i != Row && j!= Col) {
						Eliminate(Values, i, j, Value);
					}
				}
			}
		}
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	void Eliminate(SolutionGrid Values, int Row, int Col, String Value) {
		// first replace Solution Grid Value
		if (!Values.IsSolvable) {
			return;
		}
		Values.Grid[Row][Col] = Values.Grid[Row][Col].replace(Value, "");
		if (Values.Grid[Row][Col].length() == 0) {
			Values.IsSolvable = false;
			return;
		}  else if(Values.Grid[Row][Col].length() == 1) {
			// Assign(Values, Row, Col, Values.Grid[Row][Col]);
	 	}
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
			String[] Game = GameInput.split(""); // check out toCharArray for this to speed up.
			while (k < GameInput.length()) {
				if (StringMatch.indexOf(Game[k])>=0) {
					// change '0' to '.' later...doesnt work right now - string matching issue
					if (Game[k] == "."){
						Game[k] = "0";
					}
					Grid[i][j] = Game[k];
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


		// create constructor for SolutionGrid
		void SolGridTest() {
			System.out.println(" method SolGridTest");
		}
	}
	// -----------------------------------
		/**
		 * Trial Factorial recursion method/function
		**/
		public static long factorial(int n) {
			if (n<0) throw new IllegalArgumentException("Can't calculate factorial of negative");
//			return (n<2) ? 1 : n*factorial(n-1);
			if (n<2) {
				return 1;
			} else {
				return n*factorial(n-1);
			}
		}

	// -----------------------------------
		/**
		 * Trial Factorial reursion method/function using object
		**/
		public static Fact factorialObj(Fact Fact) {
			if (Fact.n<0) throw new IllegalArgumentException("Can't calculate factorial of negative");
//			return (Fact.n<2) ? 1 : Fact.n*factorialObj(Fact.n - 1);
				if (Fact.n<2) {
					Fact.n = 1;
				} else {
					Fact.n = Fact.n - 1;
					Fact.n = (Fact.n + 1) * factorialObj(Fact).n;
			}
			return Fact;
		}


	// -----------------------------------
		/**
		 * Trial Factorial reursion object
		**/

		class Fact {
			long n = 5;
			// create constructor for SolutionGrid
			void Fact() {
				System.out.println("Fact = " + n);
			}
		}

}

/**
// this next bit is not required as method for pulling in puzzle from String (or some entry format)
// is inside class "PuzzleGrid".  Keep these notes as they will remind about class/static inconsistency
// call the method initializeBoard.  This is part of class SudokuRecursion and so must be called as follows
//Sudoku.initializeBoard(Values, Game1);
// or could be called if the method is made static? i.e public static void initializeBoard()
// initializeBoard();


// Print out peer finder grid
for (int i = 0; i < N; i++) {
	System.out.print("| ");
	for (int j = 0; j < N; j++) {
		System.out.print("(" + BoxRowIndex[i][j] + "," + BoxColIndex[i][j] + "), ");
		System.out.print(" ");
		if (j % Nroot == Nroot - 1) {
			System.out.print("| ");
		}
	}
	System.out.println();
	if (i % Nroot == Nroot - 1) {
		System.out.println("--------- --------- --------- ");
	}
}
// -----------------------------------
 * Create board array and create and initialize sets for the
 * board squares.
 * NOT USED AT MOMENT
void initializeBoard(SolutionGrid Values, String Game)
{
	Values.PrintPuzzle();
	Values.InitPuzzle(Game);
	System.out.println("Initializing Board");
	Values.PrintPuzzle();
	Values.SolGridTest();
	Values.PuzzGridTest();
	System.out.println("End Initializing Board");

}

while (!Values.testIsSolved()) {
	// now cycle through each cell to see which has shortest length
	RowLoop: // Label for loop break statements
	for (int i = 0; i < N; i++) {
		ColLoop: // Label for loop break statements
		for (int j = 0; j < N; j++) {
			// Do something here
			if (Values.Grid[i][j].length() == Values.minLength){
				// now cycle through each character as a guess
				char [] chars = Values.Grid[i][j].toCharArray();
				CharLoop: // label for break statement
				for (char c : chars) {
					// System.out.println(" Row = " + i + " Col = " + j + " Char = " + c);
					Values = Assign(Values, i, j, String.valueOf(c));
					// Values.PrintPuzzle();
					// exit loop if solvable on this branch
					if (Values.IsSolvable) {
						break RowLoop;
					}
					System.out.println("not solvable in search.  char was " + c + " Row was " + i + " Col was " + j);
				}
			}
		} // ColLoop
	} // RowLoop
} // while loop
**/
