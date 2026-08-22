package behavioral.memento.solved.command;


import behavioral.memento.solved.WorkflowDesigner;

/**
 * CONCRETE COMMAND - removes a step from the workflow.
 *
 * Restoring the snapshot also restores the step's original POSITION,
 * which a simple "add it back" undo would lose.
 */
public class RemoveStepCommand extends AbstractWorkflowCommand {

    private String step;

    public RemoveStepCommand(WorkflowDesigner designer, String step) {
        super(designer);
        this.step = step;
    }

    /** Snapshot BEFORE the change, then change. */
    @Override
    public void execute() {
        memento = receiver.getMemento();
        receiver.removeStep(step);
    }
}
