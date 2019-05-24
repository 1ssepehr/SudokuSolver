import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * This is a class that imports the puzzle values either via
 * the offline database of from www.websudoku.com.
 */
class PuzzleGrabber {

	/**
	 * Reads the puzzle from www.websudoku.com. Needs Internet connection.
	 *
	 * @param puzzleID    The PuzzleSolver ID of the puzzle on the website
	 * @param puzzleLevel The difficulty of the corresponding puzzle
	 * @return The 9x9 array of {@code Square}s filled with values from the puzzle
	 */
	static Square[][] getFromInternet(String puzzleID, String puzzleLevel) {
		InputStream inputStream = null;
		BufferedReader bufferedReader;
		String line;
		String puzzleAllValues = "";
		String puzzleHideOrShow = "";

		try {
			String address = "https://show.websudoku.com/?level=" + puzzleLevel + "&set_id=" + puzzleID;
			inputStream = (new URL(address)).openStream();  // throws an IOException
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("cheat ID")) {
					int start = line.indexOf("VALUE") + 7;
					puzzleAllValues = line.substring(start, start + 81);
				}
				if (line.contains("editmask")) {
					int start = line.indexOf("VALUE") + 7;
					puzzleHideOrShow = line.substring(start, start + 81);
				}
			}
		} catch (IOException mue) {
			mue.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		Square[][] puzzle = new Square[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int linearLocation = 9 * i + j;
				if ((int) (puzzleHideOrShow.charAt(linearLocation)) - (int) ('0') == 0) {
					puzzle[i][j] = new Square((int) puzzleAllValues.charAt(linearLocation) - (int) ('0'));
				} else {
					puzzle[i][j] = new Square();
				}
			}
		}
		assignSubgrids(puzzle);
		return puzzle;
	}

	/**
	 * Reads the puzzle from the text file {@code sudoku-database.txt}
	 *
	 * @return The 9x9 array of {@code Square}s filled with values from the puzzle
	 */
	static Square[][] getFromFile() {
		Square[][] puzzle = new Square[9][9];
		String filePath = new File("").getAbsolutePath();
		try {
			File database_file = new File(filePath + "/sudoku-database.txt");
			Scanner reader;
			try {
				reader = new Scanner(database_file);
				int rnd = (int) (Math.random() * 1000 * 1000);
				for (int i = 0; i < rnd; i++) {
					reader.nextLine();
				}
				String puzzleString = reader.nextLine();
				for (int i = 0; i < 9; i++)
					for (int j = 0; j < 9; j++)
						puzzle[i][j] = new Square((int) (puzzleString.charAt(9 * i + j)) - (int) ('0'));

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		assignSubgrids(puzzle);
		return puzzle;
	}

	/**
	 * Assigns the subgrid values for the squares of a 9x9 puzzle
	 * @param puzzle The puzzle array to assign the proper subgrids
	 */
	private static void assignSubgrids(Square[][] puzzle) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i < 3 && j < 3) puzzle[i][j].subgrid = Subgrid.A;
				if (i < 3 && j >= 3 && j < 6) puzzle[i][j].subgrid = Subgrid.B;
				if (i < 3 && j >= 6) puzzle[i][j].subgrid = Subgrid.C;
				if (i >= 3 && i < 6 && j < 3) puzzle[i][j].subgrid = Subgrid.D;
				if (i >= 3 && i < 6 && j >= 3 && j < 6) puzzle[i][j].subgrid = Subgrid.E;
				if (i >= 3 && i < 6 && j >= 6) puzzle[i][j].subgrid = Subgrid.F;
				if (i >= 6 && j < 3) puzzle[i][j].subgrid = Subgrid.G;
				if (i >= 6 && j >= 3 && j < 6) puzzle[i][j].subgrid = Subgrid.H;
				if (i >= 6 && j >= 6) puzzle[i][j].subgrid = Subgrid.I;
			}
		}
	}
}