package behavioral.command.solved;

import java.util.LinkedList;
import java.util.List;

/**
 * INVOKER in the Command Design Pattern.
 *
 * <p>MailTasksRunner receives Command objects, stores them in a queue,
 * and executes them asynchronously using a worker thread.</p>
 *
 * <p>Flow:</p>
 * <pre>
 * Client
 *   |
 *   | addCommand(command)
 *   v
 * MailTasksRunner (Invoker)
 *   |
 *   v
 * pendingCommands
 *   |
 *   v
 * Worker Thread
 *   |
 *   v
 * command.execute()
 * </pre>
 *
 * <p>This class also uses the Singleton Pattern because only one
 * MailTasksRunner instance is created.</p>
 *
 * <p>Note: This is POC code and should not be used directly
 * in production.</p>
 */
public class MailTasksRunner implements Runnable {

	/**
	 * Worker thread responsible for executing pending commands.
	 */
	private Thread runner;

	/**
	 * Queue containing commands waiting for execution.
	 */
	private List<Command> pendingCommands;

	/**
	 * Flag used to stop the worker thread.
	 *
	 * volatile ensures changes made by one thread are
	 * visible to the worker thread.
	 */
	private volatile boolean stop;

	/**
	 * Singleton instance of MailTasksRunner.
	 *
	 * Created once when the class is loaded.
	 */
	private static final MailTasksRunner RUNNER = new MailTasksRunner();

	/**
	 * Returns the single MailTasksRunner instance.
	 *
	 * @return singleton MailTasksRunner instance
	 */
	public static final MailTasksRunner getInstance() {
		return RUNNER;
	}

	/**
	 * Private constructor prevents external object creation.
	 *
	 * <p>When MailTasksRunner is created:</p>
	 * <ol>
	 *   <li>Creates an empty command queue.</li>
	 *   <li>Creates a worker thread.</li>
	 *   <li>Starts the worker thread.</li>
	 *   <li>The thread starts executing {@link #run()}.</li>
	 * </ol>
	 */
	/*
	private static final MailTasksRunner RUNNER = new MailTasksRunner();
		//MailTasksRunner is created when the JVM initializes the MailTasksRunner class.
		//Usually this happens when you first call:
	MailTasksRunner.getInstance();
	 */
	private MailTasksRunner() {
		pendingCommands = new LinkedList<>();

		runner = new Thread(this);

		// Starts worker thread -> run() is called.
		runner.start();
	}

	/**
	 * Worker-thread logic responsible for processing commands.
	 *
	 * <p>Flow:</p>
	 * <pre>
	 * Check Queue
	 *     |
	 *     v
	 * Queue Empty?
	 *   /     \
	 * Yes      No
	 *  |        |
	 * wait()   Remove Command
	 *           |
	 *           v
	 *       execute()
	 * </pre>
	 *
	 * <p>If the queue is empty, the worker thread waits.
	 * When {@link #addCommand(Command)} adds a command,
	 * notifyAll() wakes the worker thread.</p>
	 */
	@Override
	public void run() {

		while (true) {

			Command cmd = null;

			/*
			 * Only one thread should modify/read the queue	inside this critical section at a time.
			 */
			synchronized (pendingCommands) {

				/*
				 * No commands available.
				 * Worker thread waits instead of continuously
				 * checking the queue.
				 */
				if (pendingCommands.isEmpty()) {
					try {
						pendingCommands.wait();

					} catch (InterruptedException e) {

						System.out.println("Runner interrupted");

						/*
						 * If shutdown() caused the interruption,
						 * terminate the worker thread.
						 */
						if (stop) {
							System.out.println("Runner stopping");
							return;
						}
					}
				}

				/*
				 * After waking up, check the queue again.
				 *
				 * If a command exists:
				 * remove the first command for execution.
				 */
				cmd = pendingCommands.isEmpty()
						? null
						: pendingCommands.remove(0);
			}

			/*
			 * No command is available.
			 * Stop the runner.
			 *
			 * return means exit the run() method completely.
			 */
			if (cmd == null)
				return;

			/*
			 * Execute the Command.
			 *
			 * MailTasksRunner does not know the actual
			 * business operation.
			 *
			 * It only knows the Command interface.
			 */
			cmd.execute();
		}
	}

	/**
	 * Adds a command to the pending command queue.
	 *
	 * <p>The command is not executed by the caller.
	 * It is scheduled for execution by the worker thread.</p>
	 *
	 * <p>Flow:</p>
	 * <pre>
	 * Client
	 *   |
	 * addCommand()
	 *   |
	 *   v
	 * Add Command to Queue
	 *   |
	 *   v
	 * notifyAll()
	 *   |
	 *   v
	 * Wake Worker Thread
	 * </pre>
	 *
	 * @param cmd command to execute asynchronously
	 */
	public void addCommand(Command cmd) {

		synchronized (pendingCommands) {

			// Add command to queue.
			pendingCommands.add(cmd);

			/*
			 * Wake the worker thread if it is waiting
			 * because the queue was empty.
			 */
			pendingCommands.notifyAll();
		}
	}

	/**
	 * Stops the command runner.
	 *
	 * <p>First sets the stop flag and then interrupts
	 * the worker thread.</p>
	 *
	 * <p>If the worker is currently waiting, interrupt()
	 * causes InterruptedException and the thread exits.</p>
	 */
	public void shutdown() {

		// Tell worker thread that shutdown is requested.
		this.stop = true;

		// Wake/interrupt worker thread so it can terminate.
		this.runner.interrupt();
	}
}