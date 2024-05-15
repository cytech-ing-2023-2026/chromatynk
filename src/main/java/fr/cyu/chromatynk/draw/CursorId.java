package fr.cyu.chromatynk.draw;

public sealed interface CursorId {

    record Str(String id) implements CursorId {}
    record Int(int id) implements CursorId {}

    default String getStringId() {
        return switch (this) {
            case Str(String id) -> id;
            case Int(int id) -> String.valueOf(id);
        };
    }
}
