package fr.cyu.chromatynk.draw;

/**
 * A cursor (textual or numeric) ID.
 */
public sealed interface CursorId {

    /**
     * A textual cursor id.
     *
     * @param id the id of a cursor as {@link String}
     */
    record Str(String id) implements CursorId {}

    /**
     * An integer cursor id.
     *
     * @param id the id of a cursor as {@link Integer}
     */
    record Int(int id) implements CursorId {}

    /**
     * Get a user-friendly textual representation of this id.
     *
     * @return this id's value formatted in a user-friendly way
     */
    default String getStringId() {
        return switch (this) {
            case Str(String id) -> '"' + id + '"';
            case Int(int id) -> String.valueOf(id);
        };
    }
}
