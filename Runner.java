import java.util.Scanner;
public class Runner {

	/**
	 * The main function that runs the Sudoku Solver.
	 */
	public static void main(String[] args) {
		Scanner Console = new Scanner(System.in);
		PuzzleSolver puzzle = null;

		System.out.println("How would you want the puzzle? \n" +
							"1. Get from the Internet (www.websudoku.com) \n" +
							"2. Read from the offline file");
		int decision = (Console.nextLine().charAt(0) - '0');
		switch (decision) {
			case 1:
				System.out.print("Give the WebSudoku's PuzzleSolver ID: ");
				String puzzleID = Console.nextLine();
				System.out.print("Give the difficulty of that puzzle (easy = 1, medium = 2, hard = 3, evil = 4): ");
				String puzzleLevel = Console.nextLine();
				System.out.println("\nAccessing the puzzle...\n");
				puzzle = new PuzzleSolver(PuzzleGrabber.getFromInternet(puzzleID, puzzleLevel));
				break;
			case 2:
				System.out.println("\nAccessing a random puzzle...\n");
				puzzle = new PuzzleSolver(PuzzleGrabber.getFromFile());
				break;
			default:
				System.out.println("Not a valid answer. The program will close now.");
				System.exit(0);
				break;
		}

		System.out.println("Initial PuzzleSolver");
		puzzle.display();
		System.out.println("Processing...");

		long startTime = System.nanoTime();

		while (!puzzle.isComplete()) {
			puzzle.iterate();
			if (!puzzle.hasTheBoardChanged) {
				puzzle.display();
				System.out.println("Stuck :(");
				System.exit(0);
			}
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		int ms = (int) (duration / 1000000);

		puzzle.display();
		System.out.println("PuzzleSolver solved in " + ms + "ms.");
	}
}