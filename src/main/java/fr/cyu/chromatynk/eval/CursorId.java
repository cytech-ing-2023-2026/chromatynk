package fr.cyu.chromatynk.eval;

public sealed interface CursorId {

    record Str(String id) implements CursorId {}
    record Int(int id) implements CursorId {}
}
