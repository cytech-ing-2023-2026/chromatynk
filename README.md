<div align="center">
  <h1>Chromatynk</h1>
</div>

## Contributors

<div style="width: 75%; margin: 0 auto;" align="center">
  
 | <img src="https://github.com/Iltotore.png" width="100" height="100"> | <img src="https://github.com/JordanViknar.png" width="100" height="100"> | <img src="https://github.com/gorosumo.png" width="100" height="100"> | <img src="https://github.com/LiliRoseGicquel.png" width="100" height="100"> | <img src="https://github.com/Harruki2k.png" width="100" height="100">|
 | --- | --- | --- | --- | --- |
 | [Iltotore](https://github.com/Iltotore) | [JordanViknar](https://github.com/JordanViknar) | [gorosumo](https://github.com/gorosumo) | [LiliRoseGicquel](https://github.com/LiliRoseGicquel) | [Harruki2k](https://github.com/Harruki2k) |
</div>

## Summary

## Features

## How to build

## How to run

## Architecture (dependency tree)
*(Note : This tree does not include external dependencies.)*


```mermaid
graph LR;
	subgraph ast
		Expr.java;
		Program.java;
		Statement.java;
		Type.java;
	end
	Expr.java --> Position.java;
	Expr.java --> Range.java;
	Statement.java --> Position.java;

	subgraph draw
		AxialMirroredCursor.java;
		CentralMirroredCursor.java;
		Color.java;
		Cursor.java;
		CursorId.java;
		DuplicatedCursor.java;
		MimickedCursor.java;
		TangibleCursor.java;
	end
	AxialMirroredCursor.java --> DuplicatedCursor.java;
	CentralMirroredCursor.java --> DuplicatedCursor.java;
	DuplicatedCursor.java --> Cursor.java;
	MimickedCursor.java --> DuplicatedCursor.java;
	TangibleCursor.java --> Cursor.java;
	TangibleCursor.java --> Color.java;

	subgraph editor
		CodeEditor.java;
		CodeEditorController.java;
		FileMenuController.java;
	end
	CodeEditor.java --> CodeEditorController.java;
	CodeEditorController.java --> FileMenuController.java;

	subgraph eval
		CursorAlreadyExistsException.java;
		EvalContext.java;
		EvalException.java;
		MissingCursorException.java;
		MissingVariableException.java;
		Scope.java;
		TypeMismatchException.java;
		Value.java;
		Variable.java;
		VariableAlreadyExistsException.java;
	end
	CursorAlreadyExistsException.java --> CursorId.java;
	CursorAlreadyExistsException.java --> Range.java;
	CursorAlreadyExistsException.java --> EvalException.java;
	EvalContext.java --> Cursor.java;
	EvalContext.java --> CursorId.java;
	EvalException.java --> Range.java;
	MissingCursorException.java --> CursorId.java;
	MissingCursorException.java --> Range.java;
	MissingCursorException.java --> EvalException.java;
	MissingVariableException.java --> Range.java;
	MissingVariableException.java --> EvalException.java;
	Scope.java --> Type.java;
	Scope.java --> Cursor.java;
	Scope.java --> CursorId.java;
	Scope.java --> Range.java;
	TypeMismatchException.java --> Type.java;
	TypeMismatchException.java --> Range.java;
	TypeMismatchException.java --> EvalException.java;
	Value.java --> Type.java;
	Variable.java --> Type.java;
	VariableAlreadyExistsException.java --> Range.java;
	VariableAlreadyExistsException.java --> EvalException.java;

	subgraph parsing
		CommonParser.java;
		ExprParser.java;
		Lexer.java;
		Parser.java;
		ParserBiFunction.java;
		ParsingException.java;
		ParsingFunction.java;
		ParsingIterator.java;
		StatementParser.java;
		Token.java;
		UnexpectedInputException.java;
	end
	ExprParser.java --> CommonParser.java;
	ExprParser.java --> Expr.java;
	ExprParser.java --> Range.java;
	ExprParser.java --> TriFunction.java;
	Lexer.java --> Range.java;
	Lexer.java --> Parser.java;
	Lexer.java --> Token.java;
	Parser.java --> Position.java;
	Parser.java --> Range.java;
	Parser.java --> Tuple2.java;
	ParsingException.java --> Position.java;
	ParsingIterator.java --> Position.java;
	StatementParser.java --> Expr.java;
	StatementParser.java --> Program.java;
	StatementParser.java --> Statement.java;
	StatementParser.java --> Type.java;
	StatementParser.java --> QuadriFunction.java;
	StatementParser.java --> Range.java;
	StatementParser.java --> TriFunction.java;
	StatementParser.java --> Tuple2.java;
	Token.java --> Range.java;
	UnexpectedInputException.java --> Position.java;

	subgraph util
		Position.java;
		QuadriFunction.java;
		Range.java;
		TriFunction.java;
		Tuple2.java;
	end
	Range.java --> Position.java;

	Chromatynk.java --> Program.java;
	Chromatynk.java --> Lexer.java;
	Chromatynk.java --> ParsingException.java;
	Chromatynk.java --> ParsingIterator.java;
	Chromatynk.java --> StatementParser.java;
```