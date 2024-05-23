package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.Set;
import java.util.stream.Collectors;

public class TypeMismatchException extends TypingException{


    public TypeMismatchException(Range range, Set<Type> expected, Type actual) {
        super(range, "Type mismatch\nExpected: " + expected.stream().map(Type::getName).collect(Collectors.joining(", ")) + "\nGot: " + actual.getName());
    }
}
