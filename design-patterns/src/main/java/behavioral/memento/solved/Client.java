package behavioral.memento.solved;

import behavioral.memento.solved.command.AddStepCommand;
import behavioral.memento.solved.command.CreateCommand;
import behavioral.memento.solved.command.WorkflowCommand;


import java.util.LinkedList;

/**
 * CLIENT
 * Runs a few commands against the designer and then undoes them.
 *
 * HOW THE PIECES FIT
 * - Originator : WorkflowDesigner  (owns the state, makes/applies snapshots)
 * - Memento    : WorkflowDesigner.Memento  (the snapshot, opaque to everyone else)
 * - Caretaker  : each command holds one Memento, and this LinkedList holds the
 *                commands - so the list is the undo history.
 *
 * KEY TERM - LIFO history:
 * commands are added with addLast() and undone with removeLast(), so the list
 * is used as a stack. Undo must go newest-first, otherwise a snapshot would be
 * restored on top of a state it was never taken from.
 *
 * Expected output: full workflow, then without "Application Approved",
 * then without "Submit Application" as well.
 */
public class Client {

    public static void main(String[] args) {
        WorkflowDesigner designer = new WorkflowDesigner();
        LinkedList<WorkflowCommand> commands = runCommands(designer);
        designer.print();
        commands.removeLast().undo();
        designer.print();
        commands.removeLast().undo();
        designer.print();

    }

    /** Safe version of the undo used above: pops the newest command and rolls it back. */
    private static void undoLastCommand(LinkedList<WorkflowCommand> commands) {
        if(!commands.isEmpty())
            commands.removeLast().undo();
    }

    /**
     * Every command follows the same two lines: record it in the history,
     * then execute it (execute() is what captures the snapshot).
     */
    private static LinkedList<WorkflowCommand> runCommands(WorkflowDesigner designer) {
        LinkedList<WorkflowCommand> commands = new LinkedList<>();

        WorkflowCommand cmd = new CreateCommand(designer,"Leave Workflow");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer,"Create Leave Application");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer,"Submit Application");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer,"Application Approved");
        commands.addLast(cmd);
        cmd.execute();

        return commands;
    }
}
