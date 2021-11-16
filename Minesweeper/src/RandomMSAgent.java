package Minesweeper.src;
import java.util.Random;

/**
 * This agent uncovers positions randomly. It obviously does not use a SAT
 * solver.
 * You can do whatever you want with this class.
 */
public class RandomMSAgent extends MSAgent {

	private Random rand;
	private boolean displayActivated = false;
	private boolean firstDecision = true;

	public RandomMSAgent(MSField field) {
		super(field);
		this.rand = new Random();
	}

	@Override
	public boolean solve() {

		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x, y, feedback;

		do {
			if (displayActivated) {
				System.out.println(field);
			}
			if (firstDecision) {
				x = 0;
				y = 0;
				firstDecision = false;
			} else {
				x = rand.nextInt(numOfCols);
				y = rand.nextInt(numOfRows);
			}

			if (displayActivated)
				System.out.println("Uncovering (" + x + "," + y + ")");
			feedback = field.uncover(x, y);

		} while (feedback >= 0 && !field.solved());

		if (field.solved()) {
			if (displayActivated) {
				System.out.println("Solved the field");
			}
			return true;
		} else {
			if (displayActivated) {
				System.out.println("BOOM!");
			}
			return false;
		}
	}

	public void activateDisplay() {
		this.displayActivated = true;

	}

	public void deactivateDisplay() {
		this.displayActivated = false;
	}

}
