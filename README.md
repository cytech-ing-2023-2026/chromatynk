<div align="center">
  <h1><img src="src/main/resources/icon.png" height=50/> Chromat'ynk</h1>
</div>

*Chromat'ynk* allows users to draw arbitrary shapes on a canvas using simple instructions, such as moving forward by a certain number of pixels or turning by a specific angle.

Users can customize the thickness and color of the lines drawn. The program includes a custom instruction language and an interpreter to process these instructions. Additionally, it features the ability to save and load creations, and renders the drawings in **real-time**.

### Made by

<div style="width: 75%; margin: 0 auto;" align="center">
  
 | <img src="https://github.com/Iltotore.png" width="100" height="100"> | <img src="https://github.com/JordanViknar.png" width="100" height="100"> | <img src="https://github.com/gorosumo.png" width="100" height="100"> | <img src="https://github.com/LiliRoseGicquel.png" width="100" height="100"> | <img src="https://github.com/Harruki2k.png" width="100" height="100">|
 | --- | --- | --- | --- | --- |
 | [Iltotore](https://github.com/Iltotore) | [JordanViknar](https://github.com/JordanViknar) | [gorosumo](https://github.com/gorosumo) | [LiliRoseGicquel](https://github.com/LiliRoseGicquel) | [Harruki2k](https://github.com/Harruki2k) |
</div>

## Features

<div style="width: 75%; margin: 0 auto;" align="center">

| | Support |
| --- | --- |
| Performs unit drawing instructions | ✅ |
| Shows results | ✅ |
| Open & save files | ✅ |
| Save canvasses | ✅ |
| Choose draw speed | ✅ |
| Step-by-step mode | ✅ |
| Proper error management | ✅ |
| Parallel cursors | ✅ |
| Tab system | ✅ |
</div>

## How to build

### Instructions

Chromat'ynk requires Java 21 or later, due to the usage of pattern matching and `record`.

First, clone the project with `git` :

```bash
git clone https://github.com/cytech-ing1-gi22/chromatynk
```

Then simply run this command, which uses the bundled *maven* wrapper (or you can use your existing `mvn` installation instead) :

| Linux | Windows |
| --- | --- | 
| ```./mvnw package``` | ```mvnw.cmd package``` |

This command will package the application into a `.jar` file, which you can easily run at any time.

## How to run

*(Note : running the application also requires Java 21 or later. The application has been successfully tested on GraalVM and OpenJDK.)*

### Instructions

Assuming you cloned the repo already, you can :
- Follow the build instructions and then run the packaged `.jar` file with your favorite Java 21-compatible JVM.
- Use this command to run directly from the root folder :

| Linux | Windows |
| --- | --- |
| ```./mvnw javafx:run``` | ```mvnw.cmd javafx:run``` |

*(You can replace `./mvwn` and `mvnw.cmd` with an existing *maven* installation.)*

## Architecture
*(Note : This tree does not include external libraries.)*

```mermaid
classDiagram
direction BT
class AndClock {
  + AndClock(Clock, Clock) 
  + tick(boolean) boolean
}
class AxialMirroredCursor {
  + AxialMirroredCursor(Cursor, double, double, double, double) 
  + drawLineAt(GraphicsContext, double, double, double, double) void
  - getSymmetric(double, double) Tuple2~Double, Double~
  + drawAt(GraphicsContext, double, double, double, double) void
}
class Bytecode {
<<Interface>>
  + range() Range
   boolean effectful
}
class CentralMirroredCursor {
  + CentralMirroredCursor(Cursor, double, double) 
  - getSymmetricY(double) double
  - getSymmetricX(double) double
  + drawAt(GraphicsContext, double, double, double, double) void
  + drawLineAt(GraphicsContext, double, double, double, double) void
}
class ChangeCanvasSizeController {
  + ChangeCanvasSizeController(CodeEditorController, Stage) 
  - applyChanges() void
}
class Chromatynk {
  + Chromatynk() 
  + execute(EvalContext, Clock) EvalContext
  + typecheckProgram(Program) void
  + lexSource(String) List~Token~
  + compileSource(String, GraphicsContext) EvalContext
  + parseSource(String) Program
}
class ChromatynkException {
  + ChromatynkException(Range, String, String) 
  + getFullMessage(String) String
   Range range
   String header
}
class Clock {
<<Interface>>
  + tick(boolean) boolean
}
class CodeEditor {
  + CodeEditor() 
  + start(Stage) void
  + main(String[]) void
}
class CodeEditorController {
  + CodeEditorController(Stage) 
  + nextInstruction() void
  + computeHighlighting(Executor) Task~StyleSpans~Collection~String~~~
  + saveImage() void
  - modifyCanvas(ActionEvent) void
  - onSuccess() void
  - clearCursorCanvas() void
  + stopScript() void
  - onProgress(EvalContext) void
  + refreshSecondaryClock() void
  + openFile() void
  + saveFile() void
  + runScript() void
  + initialize(URL, ResourceBundle) void
  - onError(String, Throwable) void
  + clearTextArea() void
  + quit() void
  - postExecution() void
  + clearCanvas() void
   Clock clock
   Canvas canvas
   Canvas cursorCanvas
   Clock periodClock
}
class Color {
  + Color(double, double, double) 
  + toANSI() String
  + green() double
  + blue() double
  - distanceSquaredTo(Color) double
  + red() double
   double blueProjection
   double brightness
   double redProjection
}
class CommonParser {
  + CommonParser() 
  + tokenOf(Class~T~) Parser~Token, T~
  + tokenOf(Class~T~, String) Parser~Token, T~
  + anyToken() Parser~Token, Token~
}
class Compiler {
  + Compiler() 
  + compileExpression(Expr, List~Bytecode~) void
  + compileProgram(Program) List~Bytecode~
  + compileStatement(Statement, List~Bytecode~, int) void
}
class Cursor {
<<Interface>>
  + turn(double) void
  + drawLineAt(GraphicsContext, double, double, double, double) void
  + move(GraphicsContext, double, double) void
  + drawAt(GraphicsContext, double, double, double, double) void
  + copyTangible() Cursor
  + move(GraphicsContext, double) void
   double dirY
   Color color
   double opacity
   boolean visible
   double thickness
   double y
   double dirX
   double x
}
class CursorAlreadyExistsException {
  + CursorAlreadyExistsException(Range, CursorId) 
}
class CursorId {
<<Interface>>
   String stringId
}
class DuplicatedCursor {
  + DuplicatedCursor(Cursor) 
   double dirY
   Cursor duplicated
   Color color
   double opacity
   boolean visible
   double thickness
   double y
   double dirX
   double x
}
class EvalContext {
  + EvalContext(List~Bytecode~, int, Stack~Value~, Deque~Scope~, Deque~CursorId~, GraphicsContext) 
  + containsCursor(CursorId) boolean
  + deleteCursor(CursorId) void
  + peek() Bytecode
  + deleteVariable(String) void
  + hasNext() boolean
  + getCursor(CursorId) Optional~Cursor~
  + exitScope() void
  - removeDeletedCursorsFromHistory() void
  + create(List~Bytecode~, GraphicsContext) EvalContext
  + forEachCursor(Consumer~Cursor~) void
  + pushValue(Value) void
  + toString() String
  + createScope() void
  + declareCursor(CursorId, Cursor) void
  + next() Bytecode
  + declareVariable(String, Variable) void
  + selectCursorId(CursorId) void
  + containsVariable(String) boolean
  + getType(String) Optional~Type~
  + setValue(String, Value) void
  + getVariable(String) Optional~Variable~
  + getValue(String) Optional~Value~
  + popValue() Value
   int nextAddress
   double largestDimension
   double width
   double height
   Cursor currentCursor
   CursorId currentCursorId
   Scope currentScope
   Range currentRange
   Scope globalScope
   GraphicsContext graphics
}
class EvalException {
  + EvalException(Range, String) 
}
class ExecutionTimer {
  + ExecutionTimer(EvalContext, Clock, Runnable, Consumer~Throwable~, Consumer~EvalContext~) 
  + handle(long) void
   Clock clock
}
class Expr {
<<Interface>>
  + range() Range
}
class ExprParser {
  + ExprParser() 
  + invokable() Parser~Token, Expr~
  - parseUnaryOperator(Operator, Expr, String, Map~Class~Operator~, BiFunction~Range, Expr, Expr~~) Expr
  + unaryOperator() Parser~Token, Expr~
  + parenthesized() Parser~Token, Expr~
  + multiplicationOperator() Parser~Token, Expr~
  + suffixOperator() Parser~Token, Expr~
  + anyExpr() Parser~Token, Expr~
  + varCall() Parser~Token, Expr~
  + literal() Parser~Token, Expr~
  - binaryOperatorParser(Parser~Token, Expr~, String, Map~Class~Operator~, TriFunction~Range, Expr, Expr, Expr~~) Parser~Token, Expr~
  - parseColorComponent(String, double) double
  + arithmeticOperator() Parser~Token, Expr~
  - parseHexColor(Range, String) LiteralColor
  + comparisonOperator() Parser~Token, Expr~
  + prefixOperator() Parser~Token, Expr~
  + booleanOperator() Parser~Token, Expr~
}
class FileMenuController {
  + FileMenuController(Stage, CodeArea) 
  + saveFile() void
  + openFile() void
}
class ForeverClock {
  + ForeverClock() 
  + tick(boolean) boolean
}
class ImageMenuController {
  + ImageMenuController(Stage, Canvas) 
  - isImageBlank(WritableImage) boolean
  + saveImage() void
}
class Interpreter {
  + Interpreter() 
  - asBoolean(Range, Value) boolean
  - asPercentage(Range, Value, double) double
  + evaluate(EvalContext, Bytecode) void
  - asColorAndAlpha(Range, Value) Tuple2~Color, Double~
  - asCursorId(Range, Value) CursorId
  + evaluateAll(EvalContext, Clock) EvalContext
  - isNumeric(Value) boolean
  - asNumericOrPercentage(Range, Value, double) double
  - asNumeric(Range, Value) double
}
class InvalidAddressException {
  + InvalidAddressException(Range, int) 
}
class InvalidCanvasSizeException {
  + InvalidCanvasSizeException(String, Double, Double) 
}
class InvalidExpressionException {
  + InvalidExpressionException(Range, String) 
}
class Lexer {
  + Lexer() 
}
class Main {
  + Main() 
  + main(String[]) void
}
class MimickedCursor {
  + MimickedCursor(Cursor, double, double) 
  + at(Cursor, double, double) MimickedCursor
  + drawLineAt(GraphicsContext, double, double, double, double) void
  + drawAt(GraphicsContext, double, double, double, double) void
}
class MissingCursorException {
  + MissingCursorException(Range, CursorId) 
}
class MissingVariableException {
  + MissingVariableException(Range, String) 
}
class MissingVariableException {
  + MissingVariableException(Range, String) 
}
class Parser~I, O~ {
<<Interface>>
  + pure(O) Parser~I, O~
  + zip(Parser~I, O2~) Parser~I, Tuple2~O, O2~~
  + keyword(String) Parser~Character, String~
  + repeat() Parser~I, List~O~~
  + repeatUntil(Parser~I, ?~) Parser~I, List~O~~
  + fatal() Parser~I, O~
  + optional() Parser~I, Optional~O~~
  + map(ParsingFunction~O, T~) Parser~I, T~
  + firstSucceeding(Parser~I, O~[]) Parser~I, O~
  + matching(String) Parser~Character, String~
  + mapWithRange(ParsingBiFunction~Range, O, T~) Parser~I, T~
  + mapError(Function~ParsingException, ParsingException~) Parser~I, O~
  + debug(String) Parser~I, O~
  + anyOf(T[]) Parser~T, T~
  + anyOfSet(Set~T~) Parser~T, T~
  + prefixed(Parser~I, ?~) Parser~I, O~
  + parse(ParsingIterator~I~) Result~O~
  + symbol(String) Parser~Character, String~
  + suffixed(Parser~I, ?~) Parser~I, O~
  + lazy(Supplier~Parser~I, O~~) Parser~I, O~
  + repeatReduce(Parser~I, BiFunction~O, O, O~~) Parser~I, O~
  + firstSucceeding(Iterable~Parser~I, O~~) Parser~I, O~
  + valueWithRange(ParsingFunction~Range, T~) Parser~I, T~
  + any() Parser~T, T~
}
class ParsingBiFunction~I1, I2, O~ {
<<Interface>>
  + apply(I1, I2) O
}
class ParsingException {
  - ParsingException(Range, String) 
}
class ParsingFunction~I, O~ {
<<Interface>>
  + apply(I) O
}
class ParsingIterator~T~ {
  + ParsingIterator(List~T~, int, Position, Predicate~T~, Predicate~T~) 
  + ParsingIterator(List~T~) 
  + nextKeepWhitespaces() T
  + handleWhitespaces() void
  + next() T
  + copy() ParsingIterator~T~
  + of(T[]) ParsingIterator~T~
  + fromString(String) ParsingIterator~Character~
  + peek() T
  + hasNext() boolean
  + forEachRemainingKeepWhitespaces(Consumer~T~) void
   Predicate~T~ lineSeparatorPredicate
   int cursor
   Position position
   List~T~ input
   Predicate~T~ whitespacePredicate
}
class PeriodClock {
  + PeriodClock(long) 
  + tick(boolean) boolean
}
class Position {
  + Position(int, int) 
  + row() int
  + isBefore(Position) boolean
  + nextRow() Position
  + min(Position, Position) Position
  + max(Position, Position) Position
  + nextColumn() Position
  + column() int
}
class PrettyPrintable {
<<Interface>>
  + toPrettyString() String
}
class Program {
  + Program(List~Statement~) 
  + statements() List~Statement~
}
class QuadriFunction~I1, I2, I3, I4, R~ {
<<Interface>>
  + apply(I1, I2, I3, I4) R
}
class Range {
  + Range(Position, Position) 
  + to() Position
  + merge(Range) Range
  + from() Position
  + subLines(String) String
  + sameLine(int, int) Range
  + toCursorRange(String) Tuple2~Integer, Integer~
  + sameLine(int, int, int) Range
   boolean sameLine
}
class Ranged {
<<Interface>>
  + range() Range
}
class RangedParsingIterator~T~ {
  + RangedParsingIterator(List~T~) 
  + RangedParsingIterator(List~T~, int, Position, Predicate~T~, Predicate~T~) 
  + ofRanged(T[]) ParsingIterator~T~
  + copy() ParsingIterator~T~
   Position position
}
class Scope {
  + Scope(Map~String, Variable~, Map~CursorId, Cursor~) 
  + containsCursor(CursorId) boolean
  + getVariable(String) Optional~Variable~
  + declareVariable(String, Variable) void
  + deleteVariable(String) void
  + containsVariable(String) boolean
  + deleteCursor(CursorId) void
  + declareCursor(CursorId, Cursor) void
  + getCursor(CursorId) Optional~Cursor~
  + toString() String
   Collection~Cursor~ cursors
}
class Statement {
<<Interface>>
  + range() Range
}
class StatementParser {
  + StatementParser() 
  + ifCondition() Parser~Token, Statement~
  + variableAssignment() Parser~Token, Statement~
  + oneLineBody() Parser~Token, Body~
  + program() Parser~Token, Program~
  + mirrorAxial() Parser~Token, Statement~
  + twoArgs() Parser~Token, Statement~
  + deleteVariable() Parser~Token, Statement~
  + forLoop() Parser~Token, Statement~
  + anyStatement() Parser~Token, Statement~
  + mirrorCentral() Parser~Token, Statement~
  + zeroArg() Parser~Token, Statement~
  + multiLineBody() Parser~Token, Body~
  + mimic() Parser~Token, Statement~
  + oneArg() Parser~Token, Statement~
  + variableDeclaration() Parser~Token, Statement~
  + whileLoop() Parser~Token, Statement~
  + instruction() Parser~Token, Statement~
  + threeArgs() Parser~Token, Statement~
  + body() Parser~Token, Body~
  + type() Parser~Token, Tuple2~Range, Type~~
}
class StepByStepClock {
  + StepByStepClock(boolean) 
  + resume() void
  + tick(boolean) boolean
  + pause() void
}
class TangibleCursor {
  + TangibleCursor(double, double) 
  + TangibleCursor(double, double, double, double, boolean, Color, double, double) 
  + drawLineAt(GraphicsContext, double, double, double, double) void
  + drawAt(GraphicsContext, double, double, double, double) void
   double dirY
   Color color
   double opacity
   boolean visible
   double thickness
   double y
   double dirX
   double x
}
class TimeoutClock {
  + TimeoutClock(long) 
  + tick(boolean) boolean
  + fps(int) TimeoutClock
}
class Token {
<<Interface>>
  + toPrettyString() String
}
class TriFunction~I1, I2, I3, R~ {
<<Interface>>
  + apply(I1, I2, I3) R
}
class Tuple2~A, B~ {
  + Tuple2(A, B) 
  + a() A
  + b() B
}
class Tuple3~A, B, C~ {
  + Tuple3(A, B, C) 
  + a() A
  + b() B
  + c() C
}
class Type {
<<enumeration>>
  - Type(String, Value) 
  + fromName(String) Optional~Type~
  + valueOf(String) Type
  + values() Type[]
   String name
   Value defaultValue
   boolean numeric
}
class TypeMismatchException {
  + TypeMismatchException(Range, Set~Type~, Type) 
}
class TypeMismatchException {
  + TypeMismatchException(Range, Set~Type~, Type) 
}
class Typer {
  + Typer() 
  + assertTypeMatch(Range, Set~Type~, Type) void
  + getType(Expr, TypingContext) Type
  + checkTypes(Statement, TypingContext) void
}
class TypingContext {
  + TypingContext() 
  + TypingContext(TypingContext, Map~String, Type~) 
  + getDirectType(String) Optional~Type~
  + deleteVariable(String, Range) void
  + getType(String) Optional~Type~
  + declareVariable(String, Type, Range) void
  + containsVariable(String) boolean
   Optional~TypingContext~ parent
}
class TypingException {
  + TypingException(Range, String) 
}
class UnexpectedInputException {
  + UnexpectedInputException(Range, String, String) 
  + anyOf(Range, Set~T~, String) UnexpectedInputException
  + anyOfValue(Range, Set~T~, T) UnexpectedInputException
   String actual
   String expected
}
class Value {
<<Interface>>
   Type type
}
class Variable {
  + Variable(Type) 
  + Variable(Type, Value) 
  + toString() String
   Type type
   Value value
}
class VariableAlreadyDeclaredException {
  + VariableAlreadyDeclaredException(Range, String) 
}
class VariableAlreadyExistsException {
  + VariableAlreadyExistsException(Range, String) 
}

AndClock  ..>  Clock 
AndClock "1" *--> "clockA 1" Clock 
AxialMirroredCursor  -->  DuplicatedCursor 
AxialMirroredCursor  ..>  Tuple2~A, B~ : «create»
CentralMirroredCursor  -->  DuplicatedCursor 
ChangeCanvasSizeController "1" *--> "codeEditorController 1" CodeEditorController 
ChangeCanvasSizeController  ..>  InvalidCanvasSizeException : «create»
Chromatynk  ..>  RangedParsingIterator~T~ : «create»
Chromatynk  ..>  TypingContext : «create»
ChromatynkException "1" *--> "range 1" Range 
CodeEditor  ..>  CodeEditorController : «create»
CodeEditorController  ..>  AndClock : «create»
CodeEditorController  ..>  ChangeCanvasSizeController : «create»
CodeEditorController "1" *--> "timeoutClock 1" Clock 
CodeEditorController  ..>  ExecutionTimer : «create»
CodeEditorController "1" *--> "currentExecution 1" ExecutionTimer 
CodeEditorController  ..>  FileMenuController : «create»
CodeEditorController "1" *--> "fileMenuController 1" FileMenuController 
CodeEditorController  ..>  ImageMenuController : «create»
CodeEditorController "1" *--> "imageMenuController 1" ImageMenuController 
CodeEditorController  ..>  ParsingIterator~T~ : «create»
CodeEditorController  ..>  PeriodClock : «create»
CodeEditorController  ..>  StepByStepClock : «create»
CodeEditorController "1" *--> "stepByStepClock 1" StepByStepClock 
CommonParser  ..>  UnexpectedInputException : «create»
Compiler  ..>  Position : «create»
Compiler  ..>  Range : «create»
Cursor  ..>  TangibleCursor : «create»
CursorAlreadyExistsException  -->  EvalException 
DuplicatedCursor  ..>  Cursor 
DuplicatedCursor "1" *--> "duplicated 1" Cursor 
EvalContext "1" *--> "instructions *" Bytecode 
EvalContext  ..>  CursorAlreadyExistsException : «create»
EvalContext "1" *--> "selectionHistory *" CursorId 
EvalContext  ..>  EvalException : «create»
EvalContext  ..>  InvalidAddressException : «create»
EvalContext  ..>  MissingCursorException : «create»
EvalContext  ..>  MissingVariableException : «create»
EvalContext "1" *--> "scopes *" Scope 
EvalContext  ..>  Scope : «create»
EvalContext  ..>  TangibleCursor : «create»
EvalContext  ..>  TypeMismatchException : «create»
EvalContext  ..>  VariableAlreadyExistsException : «create»
EvalException  -->  ChromatynkException 
ExecutionTimer "1" *--> "clock 1" Clock 
ExecutionTimer "1" *--> "context 1" EvalContext 
ExprParser "1" *--> "BOOLEAN_OPS *" TriFunction~I1, I2, I3, R~ 
ExprParser  ..>  UnexpectedInputException : «create»
ForeverClock  ..>  Clock 
Interpreter  ..>  AxialMirroredCursor : «create»
Interpreter  ..>  CentralMirroredCursor : «create»
Interpreter  ..>  Color : «create»
Interpreter  ..>  EvalException : «create»
Interpreter  ..>  InvalidExpressionException : «create»
Interpreter  ..>  MissingCursorException : «create»
Interpreter  ..>  MissingVariableException : «create»
Interpreter  ..>  TangibleCursor : «create»
Interpreter  ..>  Tuple2~A, B~ : «create»
Interpreter  ..>  Tuple3~A, B, C~ : «create»
Interpreter  ..>  TypeMismatchException : «create»
Interpreter  ..>  Variable : «create»
InvalidAddressException  -->  EvalException 
InvalidExpressionException  -->  EvalException 
Lexer "1" *--> "LITERAL_BOOL_PARSER 1" Parser~I, O~ 
Lexer  ..>  UnexpectedInputException : «create»
MimickedCursor  -->  DuplicatedCursor 
MissingCursorException  -->  EvalException 
MissingVariableException  -->  EvalException 
MissingVariableException  -->  TypingException 
Parser~I, O~  ..>  Range : «create»
Parser~I, O~  ..>  Tuple2~A, B~ : «create»
Parser~I, O~  ..>  UnexpectedInputException : «create»
ParsingException  -->  ChromatynkException 
ParsingIterator~T~  ..>  Position : «create»
ParsingIterator~T~ "1" *--> "position 1" Position 
PeriodClock  ..>  Clock 
Program "1" *--> "statements *" Statement 
Range  ..>  Position : «create»
Range "1" *--> "from 1" Position 
Range  ..>  Tuple2~A, B~ : «create»
RangedParsingIterator~T~  -->  ParsingIterator~T~ 
RangedParsingIterator~T~  ..>  Position : «create»
RangedParsingIterator~T~  ..>  Ranged 
Scope "1" *--> "cursors *" Cursor 
Scope "1" *--> "cursors *" CursorId 
Scope "1" *--> "variables *" Variable 
StatementParser "1" *--> "THREE_ARG_STATEMENTS *" QuadriFunction~I1, I2, I3, I4, R~ 
StatementParser "1" *--> "TWO_ARG_STATEMENTS *" TriFunction~I1, I2, I3, R~ 
StatementParser  ..>  Tuple2~A, B~ : «create»
StatementParser  ..>  UnexpectedInputException : «create»
StepByStepClock  ..>  Clock 
TangibleCursor  ..>  Color : «create»
TangibleCursor "1" *--> "color 1" Color 
TangibleCursor  ..>  Cursor 
TimeoutClock  ..>  Clock 
Token  -->  PrettyPrintable 
Token  -->  Ranged 
Type "1" *--> "defaultValue 1" Value 
TypeMismatchException  -->  EvalException 
TypeMismatchException  -->  TypingException 
Typer  ..>  MissingVariableException : «create»
Typer  ..>  TypeMismatchException : «create»
Typer  ..>  TypingContext : «create»
Typer  ..>  TypingException : «create»
TypingContext  ..>  MissingVariableException : «create»
TypingContext "1" *--> "directVariables *" Type 
TypingContext  ..>  VariableAlreadyDeclaredException : «create»
TypingException  -->  ChromatynkException 
UnexpectedInputException  -->  ParsingException 
Variable "1" *--> "type 1" Type 
Variable "1" *--> "value 1" Value 
VariableAlreadyDeclaredException  -->  TypingException 
VariableAlreadyExistsException  -->  EvalException 
```
