package behavioral.memento.solved.command;


/**
 * COMMAND
 * One user action on the workflow designer, packaged as an object.
 *
 * Memento is combined with Command here:
 * - Command remembers WHAT was done, so it can be replayed/queued.
 * - Memento remembers the STATE before it was done, so it can be undone.
 */
public interface WorkflowCommand {

    /** Takes a snapshot first, then performs the change. */
    void execute();

    /** Puts back the snapshot taken during execute(). */
    void undo();
}
