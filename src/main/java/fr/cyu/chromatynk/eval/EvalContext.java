package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.draw.TangibleCursor;
import fr.cyu.chromatynk.util.Range;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.function.Consumer;

/**
 * An evaluation context.
 */
public class EvalContext {

    private final List<Bytecode> instructions;
    private int nextAddress;
    private final Stack<Value> values;
    private final Deque<Scope> scopes;
    private final Deque<CursorId> selectionHistory;
    private final GraphicsContext graphics;

    /**
     * Create a new evaluation context.
     *
     * @param instructions the instruction set to execute
     * @param nextAddress the address of the next instruction to execute
     * @param values the stack of the manipulated values
     * @param scopes the execution/scopes stack
     * @param selectionHistory the cursor selection history
     * @param graphics the graphics context to draw on
     */
    public EvalContext(List<Bytecode> instructions, int nextAddress, Stack<Value> values, Deque<Scope> scopes, Deque<CursorId> selectionHistory, GraphicsContext graphics) {
        this.instructions = instructions;
        this.nextAddress = nextAddress;
        this.values = values;
        this.scopes = scopes;
        this.selectionHistory = selectionHistory;
        this.graphics = graphics;
    }

    /**
     * Get the address of the next instruction.
     */
    public int getNextAddress() {
        return nextAddress;
    }

    /**
     * Set the address of the next instruction.
     *
     * @param nextAddress the address of the next instruction to read
     */
    public void setNextAddress(int nextAddress) throws EvalException {
        if(nextAddress < 0 || nextAddress >= instructions.size()) throw new InvalidAddressException(getCurrentRange(), nextAddress);
        this.nextAddress = nextAddress;
    }

    /**
     * Get the next instruction.
     */
    public Bytecode peek() {
        return instructions.get(nextAddress);
    }

    public boolean hasNext() {
        return nextAddress < instructions.size();
    }

    public Bytecode next() {
        Bytecode bytecode = peek();
        nextAddress++;
        return bytecode;
    }

    public Range getCurrentRange() {
        return nextAddress == 0 ? Range.sameLine(0, 0) : instructions.get(nextAddress-1).range();
    }

    /**
     * Get the current cursor's id.
     *
     * @return the ID of the cursor currently selected
     */
    public CursorId getCurrentCursorId() {
        return selectionHistory.peekFirst();
    }

    /**
     * Set the current cursor's id.
     *
     * @param currentCursorId the ID of the cursor to use
     */
    public void selectCursorId(CursorId currentCursorId) {
        this.selectionHistory.removeFirstOccurrence(currentCursorId);
        this.selectionHistory.push(currentCursorId);
    }

    /**
     * Get the current cursor.
     *
     * @return the cursor in scope associated with the currently selected ID.
     */
    public Cursor getCurrentCursor() {
        return getCursor(getCurrentCursorId()).get();
    }

    private void removeDeletedCursorsFromHistory() throws EvalException {
        this.selectionHistory.removeIf(c -> !containsCursor(c));
        if(selectionHistory.isEmpty()) throw new EvalException(getCurrentRange(), "No selected cursor left");
    }

    /**
     * Get the graphics context to draw on.
     */
    public GraphicsContext getGraphics() {
        return graphics;
    }

    /**
     * Get canvas' width.
     */
    public double getWidth() {
        return graphics.getCanvas().getWidth();
    }

    /**
     * Get canvas' height.
     */
    public double getHeight() {
        return graphics.getCanvas().getHeight();
    }

    /**
     * Get canvas' largest dimension between width and height.
     */
    public double getLargestDimension() {
        return Math.max(getWidth(), getHeight());
    }

    /**
     * Push a new value to the stack.
     *
     * @param value the value to put on the top of the stack
     */
    public void pushValue(Value value) {
        values.push(value);
    }

    /**
     * Pop a value from the stack.
     *
     * @return the popped value
     */
    public Value popValue() {
        return values.pop();
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
     * Get program's global scope.
     *
     * @return the last scope of the stack
     */
    public Scope getGlobalScope() {
        return scopes.peekLast();
    }

    /**
     * Create a new scope and enter it.
     */
    public void createScope() {
        scopes.push(new Scope(new HashMap<>(), new HashMap<>()));
    }

    /**
     * Exit the current scope.
     */
    public void exitScope() throws EvalException {
        scopes.pop();
        removeDeletedCursorsFromHistory();
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
        return getVariable(name).map(Variable::getValue);
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
     * @throws MissingVariableException if no variable with the given name exists
     * @throws TypeMismatchException if the type of the value and the type of the variable don't match
     */
    public void setValue(String name, Value value) throws MissingVariableException, TypeMismatchException {
        Variable variable = getVariable(name).orElseThrow(() -> new MissingVariableException(getCurrentRange(), name));
        if (value instanceof Value.Int intValue && variable.getType() == Type.FLOAT) {
            value = new Value.Float(intValue.value());
        }
        if (variable.getType() == value.getType()) variable.setValue(value);
        else throw new TypeMismatchException(getCurrentRange(), Set.of(variable.getType()), value.getType());
    }

    /**
     * Locally declare a variable.
     *
     * @param name the name of the variable
     * @param variable the declared variable
     * @throws TypeMismatchException if the type of the value and the type of the variable don't match
     * @throws VariableAlreadyExistsException if a variable with the given name already exists
     */
    public void declareVariable(String name, Variable variable) throws TypeMismatchException, VariableAlreadyExistsException {
        if (getCurrentScope().containsVariable(name)) throw new VariableAlreadyExistsException(getCurrentRange(), name);
        else {
            if(variable.getValue() instanceof Value.Int intValue && variable.getType()==Type.FLOAT) {
                variable.setValue(new Value.Float(intValue.value()));
            }

            if(variable.getValue().getType() == variable.getType()) getCurrentScope().declareVariable(name, variable);
            else throw new TypeMismatchException(getCurrentRange(), Set.of(variable.getType()), variable.getValue().getType());
        }
    }

    /**
     * Delete a variable.
     *
     * @param name the name of the variable
     * @throws MissingVariableException if no variable with the given name exist
     */
    public void deleteVariable(String name) throws MissingVariableException {
        for (Scope scope : scopes) {
            if(scope.containsVariable(name)) {
                scope.deleteVariable(name);
                return;
            }
        }

        throw new MissingVariableException(getCurrentRange(), name);
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
     * @throws CursorAlreadyExistsException if a cursor with the given id already exists
     */
    public void declareCursor(CursorId id, Cursor cursor) throws CursorAlreadyExistsException {
        if(getCurrentScope().containsCursor(id)) throw new CursorAlreadyExistsException(getCurrentRange(), id);
        else getCurrentScope().declareCursor(id, cursor);
    }

    /**
     * Delete a cursor.
     *
     * @param id the id of the cursor
     */
    public void deleteCursor(CursorId id) throws EvalException {
        for (Scope scope : scopes) {
            if(scope.containsCursor(id)) {
                scope.deleteCursor(id);
                selectionHistory.removeFirstOccurrence(id);
                if(selectionHistory.isEmpty()) throw new EvalException(getCurrentRange(), "No selected cursor left");
                return;
            }
        }

        throw new MissingCursorException(getCurrentRange(), id);
    }

    public void forEachCursor(Consumer<Cursor> consumer) {
        for(Scope scope : scopes) {
            for(Cursor cursor : scope.getCursors()) consumer.accept(cursor);
        }
    }

    @Override
    public String toString() {
        return "EvalContext{" +
                "\ninstructions=" + instructions +
                ",\nnextAddress=" + nextAddress +
                ",\nvalues=" + values +
                ",\nscopes=" + scopes +
                ",\nselectionHistory=" + selectionHistory +
                "\n}";
    }

    public static EvalContext create(List<Bytecode> instructions, GraphicsContext graphics) {
        CursorId id = new CursorId.Int(0);
        Cursor cursor = new TangibleCursor(0, 0);
        Scope scope = new Scope(new HashMap<>(), new HashMap<>());
        scope.declareCursor(id, cursor);

        Deque<Scope> scopes = new ArrayDeque<>();
        Deque<CursorId> selectionHistory = new ArrayDeque<>();
        scopes.push(scope);
        selectionHistory.push(id);

        return new EvalContext(instructions, 0, new Stack<>(), scopes, selectionHistory, graphics);
    }
}
