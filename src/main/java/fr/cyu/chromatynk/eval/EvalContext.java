package fr.cyu.chromatynk.eval;

/*
//Contexte: Map vide

STR x = "test"
STR x = "hello" //Contexte: map.put("x", "hello")
x = 5 //Contexte: vérifier si map.containsKey(x) et ensuite map.put("x", "bonjour")

STR y = "world" //Contexte: [x -> "bonjour", y -> "world"]

x + y //map.get("x") + map.get("y")

FOR i FROM 0 TO 5 Contexte: [i -> 0..5]
  //...
END

Contexte Principal {

  STR x = ...
  y = ...

  x + ...

  Contexte boucle for {
    STR x = ...
    i = ...
    //ICI
    x + ...
    if true {
        x * 5
    }

  }


}

//i

 */

import fr.cyu.chromatynk.ast.Type;

import java.util.Map;
import java.util.Optional;

public class EvalContext {

    private final EvalContext parent;
    private final Map<String, Variable> directVariables;

    public EvalContext(EvalContext parent, Map<String, Variable> variables) {
        this.parent = parent;
        this.directVariables = variables;
    }

    public Optional<EvalContext> getParent() {
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

    public void setValue(String name, Value value) throws MissingVariableException, TypeMismatchException {
        Variable variable = getVariable(name).orElseThrow(() -> new MissingVariableException(this, name));
        if (variable.getType() == value.getType()) variable.setValue(value);
        else throw new TypeMismatchException(this, variable.getType(), value.getType());
    }

    public void declareVariable(String name, Variable variable) throws VariableAlreadyExistsException {
        if (directVariables.containsKey(name)) throw new VariableAlreadyExistsException(this, name);
        else directVariables.put(name, variable);
    }

    private void deleteVariable(String name, EvalContext callingContext) throws MissingVariableException {
        if (directVariables.containsKey(name)) directVariables.remove(name);
        else if (parent == null) throw new MissingVariableException(callingContext,name);
        else parent.deleteVariable(name, callingContext);
    }

    public void deleteVariable(String name) throws MissingVariableException {
        deleteVariable(name, this);
    }

    public boolean containsVariable(String name) {
        return getVariable(name).isPresent();
    }
}
