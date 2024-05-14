package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.Map;
import java.util.Optional;

public class Scope {

    private final Scope parent;
    private final Map<String, Variable> directVariables;

    public Scope(Scope parent, Map<String, Variable> variables) {
        this.parent = parent;
        this.directVariables = variables;
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

    public void declareVariable(String name, Variable variable, Range range) throws VariableAlreadyExistsException {
        if (directVariables.containsKey(name)) throw new VariableAlreadyExistsException(range, name);
        else directVariables.put(name, variable);
    }

    private void deleteVariable(String name, Range range) throws MissingVariableException {
        if (directVariables.containsKey(name)) directVariables.remove(name);
        else if (parent == null) throw new MissingVariableException(range,name);
        else parent.deleteVariable(name, range);
    }

    public boolean containsVariable(String name) {
        return getVariable(name).isPresent();
    }
}
