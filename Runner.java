import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Runner {

	public static void main(String[] args) {
		Puzzle puzzle = new Puzzle();
		String puzzlePath = new File("").getAbsolutePath();
		puzzlePath = puzzlePath.concat("\\src\\Easy.txt");
		File PuzzleFile = new File(puzzlePath);
		try {
			Scanner nextIteration = new Scanner(System.in);
			Scanner PuzzleReader = new Scanner(PuzzleFile);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					puzzle.board[i][j].value = PuzzleReader.nextInt();
				}
			}

			System.out.println("INITIAL PUZZLE");
			int attempt = 0;
			puzzle.display();
			while(!puzzle.isComplete()) {
				puzzle.updateBlacklist();
				puzzle.refillWithBlacklist();

				if (puzzle.hasTheBoardChanged) {
					attempt++;
					System.out.println("ATTEMPT " + attempt);
					puzzle.display();
				} else {
					System.out.println("STUCK");
					System.exit(0);
				}
			}
			System.out.println("PUZZLE SOLVED.");
			PuzzleReader.close();
			nextIteration.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
