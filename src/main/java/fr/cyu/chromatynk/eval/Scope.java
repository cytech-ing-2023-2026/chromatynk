package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;

import java.util.Map;
import java.util.Optional;
import java.util.Stack;

/**
 * The scope of an evaluated block.
 */
public class Scope {

    private final Map<String, Variable> variables;
    private final Map<CursorId, Cursor> cursors;

    /**
     * Create a new scope.
     *
     * @param variables the variables declared in this scope
     * @param cursors the cursors declared in this scope
     */
    public Scope(Map<String, Variable> variables, Map<CursorId, Cursor> cursors) {
        this.variables = variables;
        this.cursors = cursors;
    }

    /**
     * Get a variable declared in this scope.
     *
     * @param name the name of the variable
     * @return the local variable corresponding to the given name if present
     */
    public Optional<Variable> getVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    /**
     * Get a cursor declared in this scope.
     *
     * @param id the id of the cursor
     * @return the local cursor corresponding to the given name if present
     */
    public Optional<Cursor> getCursor(CursorId id) {
        return Optional.ofNullable(cursors.get(id));
    }

    /**
     * Locally declare a variable.
     *
     * @param name the name of the variable
     * @param variable the declared variable
     */
    public void declareVariable(String name, Variable variable) throws TypeMismatchException, VariableAlreadyExistsException {
        variables.put(name, variable);
    }

    /**
     * Locally declare a cursor.
     *
     * @param id the id of the cursor
     * @param cursor the cursor variable
     */
    public void declareCursor(CursorId id, Cursor cursor) {
        cursors.put(id, cursor);
    }

    /**
     * Delete a variable.
     *
     * @param name the name of the variable
     */
    public void deleteVariable(String name) {
        variables.remove(name);
    }

    /**
     * Delete a cursor.
     *
     * @param id the id of the cursor
     */
    public void deleteCursor(CursorId id) {
        cursors.remove(id);
    }

    /**
     * Check if a variable exists.
     *
     * @param name the name to check for
     * @return `true` if a variable with the given name is found
     */
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    /**
     * Check if a cursor exists.
     *
     * @param id the id to check for
     * @return `true` if a cursor with the given id is found
     */
    public boolean containsCursor(CursorId id) {
        return cursors.containsKey(id);
    }

    @Override
    public String toString() {
        return "Scope{" +
                "directVariables=" + variables +
                ", directCursors=" + cursors +
                '}';
    }
}