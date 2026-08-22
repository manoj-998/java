package behavioral.memento.solved.command;


import behavioral.memento.solved.WorkflowDesigner;

/**
 * CONCRETE COMMAND - creates a new workflow.
 *
 * Executed first, so the snapshot it saves is the empty Memento
 * (no workflow yet). Undoing it therefore sets the workflow back to null.
 */
public class CreateCommand extends AbstractWorkflowCommand {

    private String name;

    public CreateCommand(WorkflowDesigner designer, String name) {
        super(designer);
        this.name = name;
    }

    /** Snapshot BEFORE the change, then change. This order is the whole trick. */
    @Override
    public void execute() {
        this.memento = receiver.getMemento();
        receiver.createWorkflow(name);
    }

}
