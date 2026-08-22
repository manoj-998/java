package behavioral.memento.solved.command;


import behavioral.memento.solved.WorkflowDesigner;

/**
 * CARETAKER (per command) + base class for all concrete commands.
 *
 * KEY TERMS
 * - Caretaker : holds the Memento but never looks inside it. Notice that this
 *               class can only declare the type WorkflowDesigner.Memento - every
 *               member of that class is private, so the data stays hidden.
 * - Receiver  : the object the command acts on, i.e. the Originator.
 *
 * Undo is identical for every command (restore the snapshot), so it lives here
 * once instead of being repeated in each subclass. Only execute() differs.
 */
public abstract class AbstractWorkflowCommand implements WorkflowCommand {

    /** Snapshot of the designer taken just before this command changed anything. */
    protected WorkflowDesigner.Memento memento;

    protected WorkflowDesigner receiver;

    public AbstractWorkflowCommand(WorkflowDesigner designer) {
        this.receiver = designer;
    }

    /**
     * Generic undo: hand the saved snapshot back to the Originator and let it
     * rebuild itself. The command does not know what is inside the snapshot.
     */
    @Override
    public void undo() {
        receiver.setMemento(memento);
    }
}
