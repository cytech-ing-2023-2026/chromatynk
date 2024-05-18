package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

import java.util.*;

/**
 * An evaluation context.
 */
public class EvalContext {

    private final List<Bytecode> instructions;
    private int currentInstruction;
    private final Stack<Value> values;
    private final Deque<Scope> scopes;
    private CursorId currentCursorId;

    /**
     * Create a new evaluation context.
     *
     * @param instructions the instruction set to execute
     * @param currentInstruction the address of the next instruction to execute
     * @param values the stack of the manipulated values
     * @param scopes the execution/scopes stack
     * @param currentCursorId the id of the current cursor
     */
    public EvalContext(List<Bytecode> instructions, int currentInstruction, Stack<Value> values, Deque<Scope> scopes, CursorId currentCursorId) {
        this.instructions = instructions;
        this.currentInstruction = currentInstruction;
        this.values = values;
        this.scopes = scopes;
        this.currentCursorId = currentCursorId;
    }

    /**
     * Get the current cursor's id.
     *
     * @return the ID of the cursor currently selected
     */
    public CursorId getCurrentCursorId() {
        return currentCursorId;
    }

    public void setCurrentCursorId(CursorId currentCursorId) {
        this.currentCursorId = currentCursorId;
    }

    /**
     * Get the current cursor.
     *
     * @return the cursor in scope associated with the currently selected ID.
     */
    public Cursor getCurrentCursor() {
        return getCursor(currentCursorId).get();
    }

    /**
     * Get the current scope.
     *
     * @return the first scope of the stack
     */
    public Scope getCurrentScope() {
        return scopes.peekFirst();
    }

    /**
     * Get a declared variable.
     *
     * @param name the name of the variable
     * @return the variable corresponding to the given name if present
     */
    public Optional<Variable> getVariable(String name) {
        for(Scope scope : scopes) {
            if(scope.containsVariable(name)) return scope.getVariable(name);
        }

        return Optional.empty();
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
        if (getCurrentScope().containsVariable(name)) throw new VariableAlreadyExistsException(range, name);
        else if(variable.getValue().map(v -> v.getType() == variable.getType()).orElse(true))
            getCurrentScope().declareVariable(name, variable);
        else throw new TypeMismatchException(range, Set.of(variable.getType()), variable.getValue().get().getType());
    }

    /**
     * Delete a variable.
     *
     * @param name the name of the variable
     * @param range the range where the deletion occurs
     * @throws MissingVariableException if no variable with the given name exist
     */
    public void deleteVariable(String name, Range range) throws MissingVariableException {
        for (Scope scope : scopes) {
            if(scope.containsVariable(name)) scope.deleteVariable(name);
        }
    }

    /**
     * Get a declared cursor.
     *
     * @param id the id of the cursor
     * @return the cursor corresponding to the given name if present
     */
    public Optional<Cursor> getCursor(CursorId id) {
        for(Scope scope : scopes) {
            if(scope.containsCursor(id)) return scope.getCursor(id);
        }

        return Optional.empty();
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

    /**
     * Locally declare a cursor.
     *
     * @param id the id of the cursor
     * @param cursor the cursor variable
     * @param range the range where the declaration occurs
     * @throws CursorAlreadyExistsException if a cursor with the given id already exists
     */
    public void declareCursor(CursorId id, Cursor cursor, Range range) throws CursorAlreadyExistsException {
        if(getCurrentScope().containsCursor(id)) throw new CursorAlreadyExistsException(range, id);
        else getCurrentScope().declareCursor(id, cursor);
    }
}
