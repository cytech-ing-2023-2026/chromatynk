package fr.cyu.chromatynk.ast;

import java.util.List;

/**
 * A Chromat'ynk program.
 *
 * @param statements the statements of this program
 */
public record Program(List<Statement> statements) {}
