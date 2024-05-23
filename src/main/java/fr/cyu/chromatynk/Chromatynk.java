package fr.cyu.chromatynk;

import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.bytecode.Compiler;
import fr.cyu.chromatynk.eval.Clock;
import fr.cyu.chromatynk.eval.EvalContext;
import fr.cyu.chromatynk.eval.EvalException;
import fr.cyu.chromatynk.eval.Interpreter;
import fr.cyu.chromatynk.parsing.Lexer;
import fr.cyu.chromatynk.parsing.*;
import fr.cyu.chromatynk.typing.Typer;
import fr.cyu.chromatynk.typing.TypingContext;
import fr.cyu.chromatynk.typing.TypingException;
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
    public static List<Token> lexSource(String source) throws ParsingException {
        return Lexer.TOKENS_PARSER.parse(ParsingIterator.fromString(source)).value();
    }

    /**
     * Parse a program from the given source.
     *
     * @param source the source code to parse
     * @return the parsed program
     * @throws ParsingException
     */
    public static Program parseSource(String source) throws ParsingException {
        return StatementParser
                .program()
                .parse(new ParsingIterator<>(lexSource(source)))
                .value();
    }

    public static void typecheckProgram(Program program) throws TypingException {
        TypingContext typingContext = new TypingContext();
        for(Statement statement : program.statements()) Typer.checkTypes(statement, typingContext);
    }

    public static EvalContext compileSource(String source, GraphicsContext graphics) throws ParsingException, TypingException {
        Program program = parseSource(source);
        typecheckProgram(program);

        List<Bytecode> instructions = Compiler.compileProgram(program);
        return EvalContext.create(instructions, graphics);
    }

    public static EvalContext execute(EvalContext context, Clock clock) throws EvalException {
        return Interpreter.evaluateAll(context, clock);
    }
}
