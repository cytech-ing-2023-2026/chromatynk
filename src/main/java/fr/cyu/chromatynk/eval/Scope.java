package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

import java.util.Map;
import java.util.Optional;

public class Scope {

    private final Scope parent;
    private final Map<String, Variable> directVariables;
    private final Map<CursorId, Cursor> directCursors;

    public Scope(Scope parent, Map<String, Variable> variables, Map<CursorId, Cursor> cursors) {
        this.parent = parent;
        this.directVariables = variables;
        this.directCursors = cursors;
    }

    public Optional<Scope> getParent() {
        return Optional.ofNullable(parent);
    }

    public Optional<Variable> getDirectVariable(String name) {
        return Optional.ofNullable(directVariables.get(name));
    }

    public Optional<Variable> getVariable(String name) {
        return getDirectVariable(name).or(() -> getParent().flatMap(p -> p.getVariable(name)));
    }

    public Optional<Cursor> getDirectCursor(CursorId id) {
        return Optional.ofNullable(directCursors.get(id));
    }

    public Optional<Cursor> getCursor(CursorId id) {
        return getDirectCursor(id).or(() -> getParent().flatMap(p -> p.getCursor(id)));
    }

    public Optional<Value> getValue(String name) {
        return getVariable(name).flatMap(Variable::getValue);
    }

    public Optional<Type> getType(String name) {
        return getVariable(name).map(Variable::getType);
    }

    public void setValue(String name, Value value, Range range) throws MissingVariableException, TypeMismatchException {
        Variable variable = getVariable(name).orElseThrow(() -> new MissingVariableException(range, name));
        if (variable.getType() == value.getType()) variable.setValue(value);
        else throw new TypeMismatchException(range, variable.getType(), value.getType());
    }

    public void declareVariable(String name, Variable variable, Range range) throws TypeMismatchException, VariableAlreadyExistsException {
        if (directVariables.containsKey(name)) throw new VariableAlreadyExistsException(range, name);
        else if(variable.getValue().map(v -> v.getType() == variable.getType()).orElse(true)) directVariables.put(name, variable);
        else throw new TypeMismatchException(range, variable.getType(), variable.getValue().get().getType());
    }

    public void declareCursor(CursorId id, Cursor cursor, Range range) throws CursorAlreadyExistsException {
        if(directCursors.containsKey(id)) throw new CursorAlreadyExistsException(range, id);
        else directCursors.put(id, cursor);
    }

    public void deleteVariable(String name, Range range) throws MissingVariableException {
        if (directVariables.containsKey(name)) directVariables.remove(name);
        else if (parent == null) throw new MissingVariableException(range,name);
        else parent.deleteVariable(name, range);
    }

    public void deleteCursor(CursorId id, Range range) throws MissingCursorException {
        if(directCursors.containsKey(id)) directCursors.remove(id);
        else if(parent == null) throw new MissingCursorException(range, id);
        else parent.deleteCursor(id, range);
    }

    public boolean containsVariable(String name) {
        return getVariable(name).isPresent();
    }

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
