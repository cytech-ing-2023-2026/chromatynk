package fr.cyu.chromatynk.ast.statement;

import java.util.List;

public class Block implements Statement {

    private final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
