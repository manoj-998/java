package behavioral.command;

import java.util.Stack;

/**
 * Command Pattern with Undo and Redo.
 *
 * Components:
 * Command          -> Command
 * Concrete Command -> WriteCommand
 * Receiver         -> TextEditor
 * Invoker          -> CommandManager
 * Client           -> main()
 */
public class UndoRedoExample {

    /**
     * RECEIVER
     * Performs the actual text operations.
     */
    static class TextEditor {
        private String text = "";

        void write(String value) {
            text += value;
        }

        void delete(int length) {
            text = text.substring(0, text.length() - length);
        }

        void show() {
            System.out.println("Text: " + text);
        }
    }

    /**
     * COMMAND
     * Every command supports execute and undo.
     */
    interface Command {
        void execute();
        void undo();
    }

    /**
     * CONCRETE COMMAND
     * Represents a write operation.
     */
    static class WriteCommand implements Command {
        private final TextEditor editor;
        private final String text;

        WriteCommand(TextEditor editor, String text) {
            this.editor = editor;
            this.text = text;
        }

        /**
         * Execute / Redo:
         * Adds text to the editor.
         */
        @Override
        public void execute() {
            editor.write(text);
        }

        /**
         * Undo:
         * Removes the text added by this command.
         */
        @Override
        public void undo() {
            editor.delete(text.length());
        }
    }

    /**
     * INVOKER
     *
     * Maintains:
     * undoStack -> commands that can be undone
     * redoStack -> commands that can be redone
     */
    static class CommandManager {
        private final Stack<Command> undoStack = new Stack<>();
        private final Stack<Command> redoStack = new Stack<>();

        /**
         * Executes a new command.
         *
         * After execution:
         * 1. Add command to undo stack.
         * 2. Clear redo stack because a new action occurred.
         */
        void execute(Command command) {
            command.execute();
            undoStack.push(command);
            redoStack.clear();
        }

        /**
         * Undo the last command.
         *
         * 1. Remove command from undo stack.
         * 2. Call undo().
         * 3. Add command to redo stack.
         */
        void undo() {
            if (undoStack.isEmpty()) {
                System.out.println("Nothing to undo");
                return;
            }

            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }

        /**
         * Redo the last undone command.
         *
         * 1. Remove command from redo stack.
         * 2. Execute it again.
         * 3. Add it back to undo stack.
         */
        void redo() {
            if (redoStack.isEmpty()) {
                System.out.println("Nothing to redo");
                return;
            }

            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    /**
     * CLIENT
     * Demonstrates execute, undo and redo.
     */
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        CommandManager manager = new CommandManager();

        // Execute "Hello"
        manager.execute(new WriteCommand(editor, "Hello"));
        editor.show();

        // Execute " World"
        manager.execute(new WriteCommand(editor, " World"));
        editor.show();

        // Undo " World"
        manager.undo();
        editor.show();

        // Undo "Hello"
        manager.undo();
        editor.show();

        // Redo "Hello"
        manager.redo();
        editor.show();

        // Redo " World"
        manager.redo();
        editor.show();
    }
}