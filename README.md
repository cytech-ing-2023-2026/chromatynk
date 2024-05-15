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

## Architecture

```mermaid
graph TD;
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

	subgraph util
		Position.java;
		QuadriFunction.java;
		Range.java;
		TriFunction.java;
		Tuple2.java;
	end

	App.java;
	Chromatynk.java;
```