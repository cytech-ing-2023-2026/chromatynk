package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypingContext {

    private final TypingContext parent;
    private final Map<String, Type> directVariables;

    public TypingContext(TypingContext parent, Map<String, Type> variables) {
        this.parent = parent;
        this.directVariables = variables;
    }

    public TypingContext() {
        this(null, new HashMap<>());
    }

    public Optional<TypingContext> getParent() {
        return Optional.ofNullable(parent);
    }

    public Optional<Type> getDirectType(String name) {
        return Optional.ofNullable(directVariables.get(name));
    }

    public Optional<Type> getType(String name) {
        return getDirectType(name).or(() -> getParent().flatMap(p -> p.getType(name)));
    }

    public void declareVariable(String name, Type type, Range declarationRange) throws VariableAlreadyDeclaredException {
        if (directVariables.containsKey(name)) throw new VariableAlreadyDeclaredException(declarationRange, name);
        else directVariables.put(name, type);
    }

    private void deleteVariable(String name, Range range) throws MissingVariableException {
        if (directVariables.containsKey(name)) directVariables.remove(name);
        else if (parent == null) throw new MissingVariableException(range, name);
        else parent.deleteVariable(name, range);
    }

    public boolean containsVariable(String name) {
        return getType(name).isPresent();
    }
}
