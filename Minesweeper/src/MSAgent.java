package Minesweeper.src;
/**
 * This is an abstract representation of a Minesweeper agent. Your Agent has to
 * be a subclass of this one. If you want, you can delete the methods
 * activateDisplay and deactivateDisplay. Apart from that, you are not allowed
 * to change anything in this class.
 */
public abstract class MSAgent {

	/**
	 * The secret Minesweeper field you need to explore.
	 */
	protected MSField field;

	public MSAgent(MSField field) {
		this.field = field;
	}

	/**
	 * Solves the game (or not).
	 * 
	 * @return True, if the game was solved successfully.
	 */
	public abstract boolean solve();

	/**
	 * From now on, you see how the game is being solved in the console.
	 */
	public abstract void activateDisplay();

	/**
	 * From now on, you don't see any progress in the console.
	 */
	public abstract void deactivateDisplay();
}
