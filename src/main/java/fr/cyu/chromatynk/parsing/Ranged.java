package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Range;

/**
 * A parsing element carrying its {@link Range}.
 */
public interface Ranged {

    /**
     * The starting and ending position of this parsing element.
     */
    Range range();
}
