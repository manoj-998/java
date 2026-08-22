package behavioral.memento.solved;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * STATE object held by the Originator ({@link WorkflowDesigner}).
 *
 * This is the mutable business object that gets modified by commands.
 * It is NOT the Memento - the Memento stores a copy of the values below
 * (name + steps) so this object can be rebuilt later.
 */
public class Workflow {

	private LinkedList<String> steps;
	
	private String name;
	
	public Workflow(String name) {
		this.name = name;
		this.steps = new LinkedList<>();
	}
	
	/** Used during a restore: rebuilds a workflow from the values kept in a Memento. */
	public Workflow(String name, String... steps) {
		this.name = name;
		this.steps = new LinkedList<>();
		if(steps != null && steps.length > 0) {
			Arrays.stream(steps).forEach(s->this.steps.add(s));
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Workflow [name=");
		builder.append(name).append("]\nBEGIN -> ");
		for(String step : steps) {
			builder.append(step).append(" -> ");
		}
		builder.append("END");
		return builder.toString();
	}
	
	public void addStep(String step) {
		steps.addLast(step);
	}
	
	public boolean removeStep(String step) {
		return steps.remove(step);
	}

	/**
	 * Returns a copy of the steps, not the internal list.
	 * The Memento must not share a mutable reference with the live object,
	 * otherwise later edits would silently change the saved snapshot.
	 */
	public String[] getSteps() {
		return steps.toArray(new String[steps.size()]);
	}

	public String getName() {
	    return name;
    }
}
