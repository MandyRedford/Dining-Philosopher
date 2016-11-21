
/**
* The Main Class, runs the program
* 
* 
 */

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopherProblem {

	private static final int NO_OF_PHILOSOPHER = 5;
	private static final int SIMULATION_MILLIS = 1000 * 60 * 1;

	/**
	 * is the main function that creates the philiophers threads and compiles the data
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String args[]) throws InterruptedException {
		ExecutorService executorService = null; 
		Philosopher[] philosophers = null;//creates an empty array 
		try {

			philosophers = new Philosopher[NO_OF_PHILOSOPHER];//Initializes the array of philosophers

			// As many forks as Philosophers
			ReentrantLock[] forks = new ReentrantLock[NO_OF_PHILOSOPHER]; // creates as many forks as there are philosophers
			Arrays.fill(forks, new ReentrantLock());

			executorService = Executors.newFixedThreadPool(NO_OF_PHILOSOPHER);

			for (int i = 0; i < NO_OF_PHILOSOPHER; i++) {
				philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % NO_OF_PHILOSOPHER]);
				executorService.execute(philosophers[i]);
			}
			// Main thread sleeps until time of simulation
			// puts down forks at the end of simulation.
			Thread.sleep(SIMULATION_MILLIS);
			for (Philosopher philosopher : philosophers) {
				philosopher.isTummyFull.set(true);
			}
			// all philosophers are done eating...

		} finally {
			executorService.shutdown();

			// Wait period for all thread to finish
			Thread.sleep(1000);

			// Time for check
			for (Philosopher philosopher : philosophers) {
				System.out.println("Philosopher (" + philosopher.getId() + ") turns eating = "
						+ philosopher.getNoOfTurnsToEat());
				System.out.flush();
			}
		}
	}
}