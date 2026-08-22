package behavioral.memento.solved.command;


import behavioral.memento.solved.WorkflowDesigner;

/**
 * CONCRETE COMMAND - appends a step to the workflow.
 *
 * Undo does not "remove the step"; it replaces the whole workflow with the
 * snapshot taken before the step was added. That is the difference between
 * a reverse-operation undo and a state-restore (Memento) undo.
 */
public class AddStepCommand extends AbstractWorkflowCommand {

    private String step;

    public AddStepCommand(WorkflowDesigner designer, String step) {
        super(designer);
        this.step = step;
    }

    /** Snapshot BEFORE the change, then change. */
    @Override
    public void execute() {
        this.memento = receiver.getMemento();

        receiver.addStep(step);
    }
}
