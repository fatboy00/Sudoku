import java.util.*;
public class SudokuSolver
{
	static int N = 9;
	static int NROOT = 3;
	static String ALPHA = "123456789";
	static String [] ALPHA_GRID = {
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
		"123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789","123456789",
	};

	static int[] regionStart = new int[] {
		0,0,0,3,3,3,6,6,6,0,0,0,3,3,3,6,6,6,0,0,0,3,3,3,6,6,6,
		27,27,27,30,30,30,33,33,33,27,27,27,30,30,30,33,33,33,27,27,27,30,30,30,33,33,33,
		54,54,54,57,57,57,60,60,60,54,54,54,57,57,57,60,60,60,54,54,54,57,57,57,60,60,60,
	};

	static Map<Integer, Integer[]> peers = new HashMap<Integer, Integer[]>();

	public static void main(String [] args)
	{
		long startTime = System.nanoTime();
		// create a new object "Sudoku" of class SudokuSolver
		SudokuSolver sudoku = new SudokuSolver();
		// create a new object of class "PuzzleGrid".  This is a nested Class.
		// this new object for the starting PuzzleGrid
		SudokuSolver.PuzzleGrid puzzleStart = sudoku.new PuzzleGrid(true, false, ALPHA_GRID);
		// copy the bloody thing at this point so we have the ALPHA initial Values in as well.
		SudokuSolver.PuzzleGrid puzzle = sudoku.new PuzzleGrid(puzzleStart);

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
		// initialize the board
		puzzleStart.initPuzzle(game5);
		long initTime = System.nanoTime();
		sudoku.createPeers();
		long peersTime = System.nanoTime();
		// initialize the puzzle solution
		puzzle = sudoku.initialize(puzzle, puzzleStart);
		long iTime = System.nanoTime();		
		puzzle = sudoku.search(puzzle);

		//  Time Taken
		long endTime = System.nanoTime();
		System.out.print("Time taken (s) = ");
		System.out.println((endTime - startTime)/1000000000.0);
		System.out.print("Load =  " + (initTime - startTime)/1000000000.0 + " peers = " + (peersTime - initTime)/1000000000.0 + " initialize = " + (iTime - peersTime)/1000000000.0);


		puzzleStart.printPuzzle();
		puzzle.printPuzzle();
		if (!puzzle.getIsSolvable()) {
			System.out.println(" NOT  SOLVABLE");
		}
		if (puzzle.getSolved()) {
			System.out.println(" SOLVED !");
		}



	}// end of main

	//  **********************************************************
	//  *                                                        *
	//  *********************** Methods **************************
	//  *                                                        *
	//  **********************************************************
	// -----------------------------------
	/**
	 * Initialize
	 * board squares.
	**/
	PuzzleGrid initialize(PuzzleGrid puzzle, PuzzleGrid puzzleStart) {
		for (int cell = 0; cell < N * N; cell++) {
			if (ALPHA.indexOf(puzzleStart.getGridCell(cell))>=0 ) {
				puzzle = assign(puzzle, cell , puzzleStart.getGridCell(cell));
			}
		}
		return puzzle;
	}
	// -----------------------------------
	/**
	 * create peers list
	 * No Peers = 3 * N - 2* NROOT -1
	**/
	void createPeers() {
		for (int cell = 0; cell < N * N; cell++) {
			// Initialize variables
			int col = cell % N;
			int row = (cell - col) / N;
			int i = 0;
			peers.put(cell, new Integer[(3 * N) - (2 * NROOT) - 1]);
			// System.out.println(" cell = " + cell + " row = " + row + " col " + col);
			for (int index = 0; index < N; index++) {
				// Do Column
				if (index != row) {
					peers.get(cell)[i] = ((index * N) + col);
					i++;
					}
				// Do row
				if (index != col) {
					peers.get(cell)[i] = ((row * N) + index);
					i++;
					}
				// Box elimination
				int boxCell = 0;
				if(index < NROOT) {
					boxCell = (index + regionStart[cell]);
				} else if ( index >= NROOT && index < 2 * NROOT) {
					boxCell = (index + regionStart[cell] + (N - NROOT));
				} else {
					boxCell = (index + regionStart[cell] + (2 * (N - NROOT)));
				}
				int boxCol = boxCell % N;
				int boxRow = (boxCell - boxCol) / N;
				if (boxRow != row && boxCol != col) {
					peers.get(cell)[i] = (boxCell);
					i++;
				}
			} // Index Iteration
		}
		return;
	}
	// -----------------------------------
	/**
	 * Search the board
	 * board squares.
	**/
	PuzzleGrid search(PuzzleGrid puzzle) {
		int cell = -1;
		int minLength = 9;
		for (int cellCount = 0; cellCount < N * N; cellCount++) {
			if (puzzle.getGridCellLength(cellCount) < minLength && puzzle.getGridCellLength(cellCount)>1 ) {
				minLength = puzzle.getGridCellLength(cellCount);
				// Record cell
				cell = cellCount;
			}
		}
		if (cell < 0) {
			puzzle.setSolved(true);
		} else {
			puzzle = iterate(puzzle, cell);
		}
		return puzzle;
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	PuzzleGrid iterate(PuzzleGrid puzzle, int cell) {
		// now loop throughthe chars
		char [] chars = puzzle.setGridCellToCharArray(cell);
		CharLoop: // label for break statement
		for (char c : chars) {
			// create a clone of puzzle to see if it is legitimate
			PuzzleGrid clonePuzzle = new PuzzleGrid(puzzle);
			clonePuzzle = assign(clonePuzzle, cell, String.valueOf(c)); // assign the char as a guess.
			if (clonePuzzle.getIsSolvable()) clonePuzzle= search(clonePuzzle);  //  what happens to IsSolvable if false and want to loop through next char?
			if (clonePuzzle.getSolved()) {
				return clonePuzzle;
			}
		}
		return puzzle;
	}

	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	PuzzleGrid assign(PuzzleGrid puzzle, int cell, String value) {
		// create a clone of puzzle to see if it is legitimate
		PuzzleGrid clonePuzzle = new PuzzleGrid(puzzle);
		int peerNumber = (3 * N) - (2 * NROOT) - 1;
		if (clonePuzzle.getIsSolvable()) {
			// first replace Soultion Grid Value if this is one of the 'Alpha Strings'
			clonePuzzle.setGridCell(value,cell);
			for (int index = 0; index < peerNumber; index++) {
				clonePuzzle = eliminate(clonePuzzle, peers.get(cell)[index], value);
				if (!clonePuzzle.getIsSolvable()) {
					puzzle.setIsSolvable(false);
					return puzzle;
				}
			}
		}
		return clonePuzzle;
	}
	// -----------------------------------
	/**
	 * Assign Value to Values and call a function that eliminates from Peer Cells
	 * board squares.
	**/
	PuzzleGrid eliminate(PuzzleGrid puzzle, int cell, String value) {
		// Initialize variables
		// int cell = (row * N) + col;
		int col = cell % N;
		int row = (cell - col) / N;
		// create a clone of puzzle to see if it is legitimate
		PuzzleGrid clonePuzzle = new PuzzleGrid(puzzle);

		// Place ALPHA into char array
		char [] chars = ALPHA.toCharArray();
		int PreviousLength = clonePuzzle.getGridCellLength(cell); // check length of String before elimination
		clonePuzzle.replaceGridCell(value, "", cell);

		// method 1.  PENCIL IN: Check if a square is reduced to only one possible value (but had more than one value before)
		if (clonePuzzle.getGridCellLength(cell) == 0) {
			puzzle.setIsSolvable(false); // This is in case there is a contradiction
			return puzzle;
		}  else if(clonePuzzle.getGridCellLength(cell) == 1 && PreviousLength > 1) {
			clonePuzzle = assign(puzzle, cell, clonePuzzle.getGridCell(cell));
		}
/** **/
		// method 2.  CROSS HATCH: now cycle through each region and see where a number can go
		// can we be smart and only cycle through only regions that would change on this paa of eliminate?
		// first cycle through each row:
		String rowString = "";
		String colString = "";
		String boxString = "";

		//
		// Step 1.  Create a long string of all the Strings in each region put together
		for (int index = 0; index < N; index++) {
			// Row first:
			rowString = rowString + clonePuzzle.getGridCell((row * N) + index);
			// Col Next:
			colString = colString + clonePuzzle.getGridCell((index * N) + col);
			// Box Last:
			if(index < NROOT) {
				boxString = boxString + clonePuzzle.getGridCell(index + regionStart[cell]);
			} else if ( index >= NROOT && index < 2 * NROOT) {
				boxString = boxString + clonePuzzle.getGridCell(index + regionStart[cell] + (N - NROOT));
			} else {
				boxString = boxString + clonePuzzle.getGridCell(index + regionStart[cell] + (2 * (N - NROOT)));
			}
		}
		//
		// Step 2.  Cycle through characters to see if more ONLY one of a particular char, then check
		// if the cell length was greater than 1
		for (char c : chars) {
			// check rows first.  Is there only one of the chars there?
			if (rowString.indexOf(c) == rowString.lastIndexOf(c)  && rowString.indexOf(c) >=0) {
				// check consistency (does char exist in box?)
				if (rowString.indexOf(c) >=0) {
					// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
					for (int index = 0; index < N; index++) {
						if (clonePuzzle.getGridCellLength((row * N) + index) > 1 && clonePuzzle.getGridCellContains(((row * N) + index), c) >=0) {
							clonePuzzle = assign(clonePuzzle, (row * N) + index, String.valueOf(c));
							if (!clonePuzzle.getIsSolvable()) {
								puzzle.setIsSolvable(false); // This is in case there is a contradiction
								return puzzle;  // inconsistency found later
							}
						}
					}
				} else {
					puzzle.setIsSolvable(false); // This is in case there is a contradiction
					return puzzle;
				}
			}
			// check cols second.  Is there only one of the chars there?
			if (colString.indexOf(c) == colString.lastIndexOf(c)) {
				// check consistency (does char exist in box?)
				if (colString.indexOf(c) >=0) {
					// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
					for (int index = 0; index < N; index++) {
						if (clonePuzzle.getGridCellLength((index * N) + col) > 1 && clonePuzzle.getGridCellContains(((index * N) + col), c) >=0) {
							clonePuzzle = assign(clonePuzzle, (index * N) + col, String.valueOf(c));
							if (!clonePuzzle.getIsSolvable()) {
								puzzle.setIsSolvable(false); // This is in case there is a contradiction
								return puzzle;  // inconsistency found later
							}
						}
					}
				} else {
					puzzle.setIsSolvable(false); // This is in case there is a contradiction
					return puzzle;
				}
				// check box last.  Is there only one of the chars there?
				if (boxString.indexOf(c) == boxString.lastIndexOf(c)) {
					// check consistency (does char exist in box?)
					if (boxString.indexOf(c) >=0) {
						// now got to check if the cell it is in has a length greater than 1 (else already been assigned)
						for (int index = 0; index < N; index++) {
							int boxCell = 0;
							if(index < NROOT) {
								boxCell = (index + regionStart[cell]);
							} else if ( index >= NROOT && index < 2 * NROOT) {
								boxCell = (index + regionStart[cell] + (N - NROOT));
							} else {
								boxCell = (index + regionStart[cell] + (2 * (N - NROOT)));
							}
							if (clonePuzzle.getGridCellLength(boxCell) > 1 && clonePuzzle.getGridCellContains(boxCell, c) >=0) {
								clonePuzzle = assign(clonePuzzle, boxCell, String.valueOf(c));
								if (!clonePuzzle.getIsSolvable()) {
									puzzle.setIsSolvable(false); // This is in case there is a contradiction
									return puzzle;  // inconsistency found later
								}
							}
						}
					} else {
						puzzle.setIsSolvable(false); // This is in case there is a contradiction
						return puzzle;
					}
				}
			}
		}

		return clonePuzzle;
	}


	//  *********************** Nested Classes **************************
	// -----------------------------------
	/**
	 * Create Object for Board
	**/
	class PuzzleGrid {
		boolean isSolvable;
		boolean solved;
		String [] grid = new String [N * N]; // YES - do I need to define dimensions here?
		// -----------------------------------------
		// create constructor for PuzzleGrid
		PuzzleGrid(boolean aIsSolvable, boolean aSolved, String [] aGrid) {
			isSolvable = aIsSolvable;
			solved = aSolved;
			grid = aGrid;
		}
		// -----------------------------------------
		// copy constructor for PuzzleGrid
		PuzzleGrid(PuzzleGrid puzzleGrid) {
			this(puzzleGrid.getIsSolvable(), puzzleGrid.getSolved(), puzzleGrid.getGrid());
		}
		// -----------------------------------------
		// getter for grid
		String[] getGrid() {
			return Arrays.copyOf(grid, grid.length); // an array of Strings does not have .length(), its just .length
		}
		// -----------------------------------------
		// getter for gridcell
		String getGridCell(int index) {
			return grid[index];
		}
		// setter for gridcell
		void setGridCell(String cell, int index) {
			this.grid[index] = cell;
		}
		// replace for gridcell
		void replaceGridCell(String cell, String replacement, int index) {
			this.grid[index] = this.grid[index].replace(cell, replacement);
		}
		// replace for gridcell
		char[] setGridCellToCharArray(int index) {
			return this.grid[index].toCharArray();
		}
		// -----------------------------------------
		// getter for gridcell length
		int getGridCellLength(int index) {
			return grid[index].length();
		}
		// -----------------------------------------
		// getter for gridcell contains a char
		int getGridCellContains(int index, char c) {
			return grid[index].indexOf(c);
		}

		// -----------------------------------------
		// getter & setter for isSolvable
		boolean getIsSolvable() {
			return isSolvable;
		}
		void setIsSolvable(boolean isSolvable) {
			this.isSolvable = isSolvable;
		}
		// -----------------------------------------
		// getter for solved
		boolean getSolved() {
			return solved;
		}
		// setter for solved
		void setSolved(boolean solved) {
			this.solved = solved;
		}
		// -----------------------------------------
		void initPuzzle(String GameInput) {
			int i = 0, j = 0, k = 0;
			String StringMatch = ALPHA + ".0";
			char[] Game = GameInput.toCharArray();
			while (k < GameInput.length()) {
				if (StringMatch.indexOf(Game[k])>=0) {
					if (Game[k] == '0'){
						Game[k] = '.';
					}
					grid[i] = String.valueOf(Game[k]);
					if (i < N * N) {
						i++;
					} else {
						break;
					}
				}
				k++;
			}
		}
		// -----------------------------------------
		void printPuzzle() {
			// change horizontal lines below  (probable by adding print string into one string and measuring length)
			System.out.println(" Is this Solvable ? = " + isSolvable);
			System.out.println(" Is this Solved ? = " + solved);
			System.out.println("| --------- --------- --------- | --------- --------- --------- | --------- --------- --------- |");
			// add blanks as way of achieving above;
			String blanks = "         ";
			for (int i = 0; i < N; i++) {
				System.out.print("| ");
				for (int j = 0; j < N; j++) {
					System.out.print(grid[(i * N) + j] + blanks.substring((grid[(i * N) + j].length())));
					System.out.print(" ");
					if (j % NROOT == NROOT - 1) {
						System.out.print("| ");
					}
				}
				System.out.println();
				if (i % NROOT == NROOT - 1) {
					System.out.println("| --------- --------- --------- | --------- --------- --------- | --------- --------- --------- |");
				}
			}
		} // end of printPuzzle method
	} // end of PuzzleGrid Sub Class
} // end of SudokuSolver Class
