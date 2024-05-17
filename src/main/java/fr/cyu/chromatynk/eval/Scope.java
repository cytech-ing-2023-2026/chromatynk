package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The scope of an evaluated block.
 */
public class Scope {

    private final Scope parent;
    private final Map<String, Variable> directVariables;
    private final Map<CursorId, Cursor> directCursors;

    /**
     * Create a new scope.
     *
     * @param parent the parent of this scope
     * @param variables the variables declared in this scope
     * @param cursors the cursors declared in this scope
     */
    public Scope(Scope parent, Map<String, Variable> variables, Map<CursorId, Cursor> cursors) {
        this.parent = parent;
        this.directVariables = variables;
        this.directCursors = cursors;
    }

    /**
     * Get this scope's parent
     */
    public Optional<Scope> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Get a variable declared in this scope.
     *
     * @param name the name of the variable
     * @return the local variable corresponding to the given name if present
     */
    public Optional<Variable> getDirectVariable(String name) {
        return Optional.ofNullable(directVariables.get(name));
    }

    /**
     * Get a variable accessible from this scope.
     *
     * @param name the name of the variable
     * @return the variable corresponding to the given name if present
     */
    public Optional<Variable> getVariable(String name) {
        return getDirectVariable(name).or(() -> getParent().flatMap(p -> p.getVariable(name)));
    }

    /**
     * Get a cursor declared in this scope.
     *
     * @param id the id of the cursor
     * @return the local cursor corresponding to the given name if present
     */
    public Optional<Cursor> getDirectCursor(CursorId id) {
        return Optional.ofNullable(directCursors.get(id));
    }

    /**
     * Get a cursor accessible from this scope.
     *
     * @param id the id of the cursor
     * @return the cursor corresponding to the given name if present
     */
    public Optional<Cursor> getCursor(CursorId id) {
        return getDirectCursor(id).or(() -> getParent().flatMap(p -> p.getCursor(id)));
    }

    /**
     * Get the value of an accessible variable.
     *
     * @param name the name of the variable
     * @return the value of the variable if present
     */
    public Optional<Value> getValue(String name) {
        return getVariable(name).flatMap(Variable::getValue);
    }

    /**
     * Get the type of an accessible variable.
     *
     * @param name the name of the variable
     * @return the type of the variable if present
     */
    public Optional<Type> getType(String name) {
        return getVariable(name).map(Variable::getType);
    }

    /**
     * Set the value of an accessible variable.
     *
     * @param name the name of the variable
     * @param value the value to assign to the variable
     * @param range the range where the assignation occurs
     * @throws MissingVariableException if no variable with the given name exists
     * @throws TypeMismatchException if the type of the value and the type of the variable don't match
     */
    public void setValue(String name, Value value, Range range) throws MissingVariableException, TypeMismatchException {
        Variable variable = getVariable(name).orElseThrow(() -> new MissingVariableException(range, name));
        if (variable.getType() == value.getType()) variable.setValue(value);
        else throw new TypeMismatchException(range, Set.of(variable.getType()), value.getType());
    }

    /**
     * Locally declare a variable.
     *
     * @param name the name of the variable
     * @param variable the declared variable
     * @param range the range where the declaration occurs
     * @throws TypeMismatchException if the type of the value and the type of the variable don't match
     * @throws VariableAlreadyExistsException if a variable with the given name already exists
     */
    public void declareVariable(String name, Variable variable, Range range) throws TypeMismatchException, VariableAlreadyExistsException {
        if (directVariables.containsKey(name)) throw new VariableAlreadyExistsException(range, name);
        else if(variable.getValue().map(v -> v.getType() == variable.getType()).orElse(true)) directVariables.put(name, variable);
        else throw new TypeMismatchException(range, Set.of(variable.getType()), variable.getValue().get().getType());
    }

    /**
     * Locally declare a cursor.
     *
     * @param id the id of the cursor
     * @param cursor the cursor variable
     * @param range the range where the declaration occurs
     * @throws CursorAlreadyExistsException if a cursor with the given id already exists
     */
    public void declareCursor(CursorId id, Cursor cursor, Range range) throws CursorAlreadyExistsException {
        if(directCursors.containsKey(id)) throw new CursorAlreadyExistsException(range, id);
        else directCursors.put(id, cursor);
    }

    /**
     * Delete a variable.
     *
     * @param name the name of the variable
     * @param range the range where the deletion occurs
     * @throws MissingVariableException if no variable with the given name exist
     */
    public void deleteVariable(String name, Range range) throws MissingVariableException {
        if (directVariables.containsKey(name)) directVariables.remove(name);
        else if (parent == null) throw new MissingVariableException(range,name);
        else parent.deleteVariable(name, range);
    }

    /**
     * Delete a cursor.
     *
     * @param id the id of the cursor
     * @param range the range where the deletion occurs
     * @throws MissingCursorException if no cursor with the given id exist
     */
    public void deleteCursor(CursorId id, Range range) throws MissingCursorException {
        if(directCursors.containsKey(id)) directCursors.remove(id);
        else if(parent == null) throw new MissingCursorException(range, id);
        else parent.deleteCursor(id, range);
    }

    /**
     * Check if a variable exists.
     *
     * @param name the name to check for
     * @return `true` if a variable with the given name is found
     */
    public boolean containsVariable(String name) {
        return getVariable(name).isPresent();
    }

    /**
     * Check if a cursor exists.
     *
     * @param id the id to check for
     * @return `true` if a cursor with the given id is found
     */
    public boolean containsCursor(CursorId id) {
        return getCursor(id).isPresent();
    }

    @Override
    public String toString() {
        return "Scope{" +
                "parent=" + parent +
                ", directVariables=" + directVariables +
                ", directCursors=" + directCursors +
                '}';
    }
}