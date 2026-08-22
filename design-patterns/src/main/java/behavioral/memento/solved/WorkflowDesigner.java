package behavioral.memento.solved;


/**
 * ORIGINATOR
 * The object whose state we want to save and restore.
 *
 * Its state is the {@link Workflow} it is currently designing.
 * It knows how to take a snapshot of that state ({@link #getMemento()})
 * and how to roll back to a snapshot ({@link #setMemento(Memento)}).
 *
 * KEY TERMS
 * - Originator : owns the state, creates and consumes the Memento.
 * - State      : the current workflow (name + ordered steps).
 * - Snapshot   : a copy of the state taken before a change.
 *
 * The Originator never keeps the history itself. Whoever asks for a
 * Memento is responsible for holding on to it (here, the commands).
 */
public class WorkflowDesigner {

    private Workflow workflow;

    public void createWorkflow(String name) {
        workflow = new Workflow(name);
    }

    public Workflow getWorkflow() {
        return this.workflow;
    }

    /**
     * SAVE - creates a snapshot of the current state.
     *
     * When no workflow exists yet, an "empty" Memento is returned so that
     * undoing the very first command can bring the designer back to the
     * "nothing created yet" state instead of failing.
     */
    public Memento getMemento() {
       if(workflow == null) {
    	   return new Memento();
       }
       return new Memento(workflow.getSteps(), workflow.getName());
    }

    /**
     * RESTORE - rebuilds the state from a snapshot.
     *
     * A brand new Workflow is built from the saved values instead of reusing
     * the old object, so later changes cannot leak back into the snapshot.
     */
    public void setMemento(Memento memento) {
    	if(memento.isEmpty()) {
    		this.workflow = null;
    	} else {
    		this.workflow = new Workflow(memento.getName(), memento.getSteps());
    	}
    }

    public void addStep(String step) {
        workflow.addStep(step);
    }

    public void removeStep(String step) {
        workflow.removeStep(step);
    }

    public void print() {
        System.out.println(workflow);
    }

    /**
     * MEMENTO
     * Immutable-by-convention snapshot of the Originator's state.
     *
     * KEY TERM - Narrow / wide interface:
     * this is an inner class with private constructors and private getters.
     * Java lets the enclosing class (WorkflowDesigner) read those private
     * members, but nobody outside can. So the outside world sees only the
     * type `WorkflowDesigner.Memento` (wide interface = "an opaque token"),
     * while the Originator sees the actual data (narrow interface).
     *
     * That is how Memento preserves ENCAPSULATION: the commands can hold and
     * pass around a snapshot, but they cannot read or tamper with the steps.
     */
    public class Memento {
    	
    	private String[] steps;
    	
    	private String name;
    	
    	/** Snapshot of the "no workflow created yet" state. */
    	private Memento() {
    		
    	}
    	
    	private Memento(String[] steps, String name) {
    		this.steps = steps;
    		this.name = name;
    	}
    	
    	private String[] getSteps() {
    		return steps;
    	}
    	
    	private String getName() {
    		return name;
    	}
    	
    	/** True when this snapshot represents the state before any workflow existed. */
    	private boolean isEmpty() {
    		return this.getSteps() == null && this.getName() == null;
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
