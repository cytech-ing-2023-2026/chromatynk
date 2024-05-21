package fr.cyu.chromatynk;

import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.bytecode.Compiler;
import fr.cyu.chromatynk.eval.Clock;
import fr.cyu.chromatynk.eval.EvalContext;
import fr.cyu.chromatynk.eval.EvalException;
import fr.cyu.chromatynk.eval.Interpreter;
import fr.cyu.chromatynk.parsing.Lexer;
import fr.cyu.chromatynk.parsing.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class Chromatynk {

    /**
     * Parse a sequence of token from the given source.
     *
     * @param source the source code to parse
     * @return the parsed token sequence
     * @throws ParsingException
     */
    public static List<Token> lexProgram(String source) throws ParsingException {
        return Lexer.TOKENS_PARSER.parse(ParsingIterator.fromString(source)).value();
    }

    /**
     * Parse a program from the given source.
     *
     * @param source the source code to parse
     * @return the parsed program
     * @throws ParsingException
     */
    public static Program parseProgram(String source) throws ParsingException {
        return StatementParser
                .program()
                .parse(new ParsingIterator<>(Lexer.TOKENS_PARSER.parse(ParsingIterator.fromString(source)).value()))
                .value();
    }

    public static EvalContext executeProgram(Program program, GraphicsContext graphics, Clock clock) throws EvalException {
        List<Bytecode> instructions = Compiler.compileProgram(program);
        return Interpreter.evaluateAll(EvalContext.create(instructions, graphics), clock);
    }
}
