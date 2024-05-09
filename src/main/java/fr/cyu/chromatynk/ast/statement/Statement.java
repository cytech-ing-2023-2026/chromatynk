package fr.cyu.chromatynk.ast.statement;
import fr.cyu.chromatynk.ast.expr.Expr;

import java.util.List;

/** A Sealed interface Statement {}*
 *An interface named Statement with classes inside in order to use it
 */
public sealed interface Statement {
    /** A forward {@code distance}*
     * @param distance the value wanted in order to move forwards
     */
    record Forward(Expr distance) implements Statement{}
    /** A backward {@code distance}*
     * @param distance the value wanted in order to move backwards
     */
    record Backward(Expr distance) implements Statement{}
    /** A For loop {@code initialisation, condition, step, statement}*
     * @param initialisation the initialisation expression for the loop
     * @param condition the condition expression for the loop
     * @param step the step expression for the loop
     * @param statement the statements contained within the loop, the block being executed for each iteration
     */
    record For(Expr initialisation, Expr condition, Expr step, Statement statement) implements Statement{}
    /** A Turn {@code angle}*
     * @param angle the value for the rotation of the turn
     */
    record Turn(Expr angle) implements Statement{}
    /** A While loop {@code condition, body}*
     * @param condition the condition during which the while continues to loop
     * @param body the body of the loop executed as long as the condition is true
     */
    record While(Expr condition, Statement body) implements Statement{}
    /** A position {@code x, y}*
     * @param x the pixel or the percent value for the x
     * @param y the pixel or the percent value for the y
     */
    record Pos(Expr x, Expr y) implements Statement{}
    /** A Block {@code list<Statement> statements}*
     * @param statements the statements
     */
    record Block(List<Statement> statements) implements Statement{}
    /** A Move {@code distance_x, distance_y}*
     * @param distance_x the position wanted for the cursor
     * @param distance_y the position wanted for the cursor
     */
    record Move(Expr distance_x, Expr distance_y) implements Statement{}
    /** A Hide {@code isVisible, hideObject}*
     * @param isVisible the cursor visible or not
     * @param hideObject the cursor we want to hide
     */
    record Hide(boolean isVisible, Expr hideObject) implements Statement{}
    /** A Show {@code isVisible, showObject}*
     * @param isVisible the cursor visible or not
     * @param showObject the cursor we want to the screen
     */
    record Show(boolean isVisible, Expr showObject) implements Statement{}
    /** A Press {@code percentValue, intValue}*
     * @param percentValue the percent value wanted for the pression of the cursor
     */
    record Press(Expr percentValue, int intValue) implements Statement{}
    /** A Thick {@code value}*
     * @param value the int value wanted for the thickness of the line
     */
   record Thick(int value){}
    /** A LookAtCursor {@code idCursor, idNewCursor}*
     * @param idCursor the string value of the cursor
     * @param idNewCursor the string value (not existing yet) of the new cursor
     */
    record LookAtCursor(String idCursor, String idNewCursor){}
    /** A LookAt {@code idCursor, position_x, position_y}*
     * @param idCursor the string value of the cursor
     * @param positionX the position of the cursor
     * @param positionY the position of the cursor
     */
    record LookAt(String idCursor, Expr positionX, Expr positionY){}
    /** A CursorId {@code id}*
     * @param id the string value of the cursor
     */
    record CursorId(String id) implements Statement{}
    /** A SelectCursorId {@code id}*
     * @param id the string id of the cursor we want to select
     */
    record SelectCursorId(String id) implements Statement{}
    /** A RemoveCursorId {@code id}*
     * @param id the string value of the cursor we want to remove
     */
    record RemoveCursorId(String id) implements Statement{}
    /** A If {@code initialisation, condition, step}*
     * @param initialisation the beginning value
     * @param condition the condition(s) of the if
     * @param step the step of the condition(s)
     * */
    record If(Expr initialisation, Expr condition, Expr step) implements Statement{}
    /** A Mimic {@code temporaryCursor, CursorId }*
     * @param temporaryCursor the temporary cursor
     * @param CursorId the id of the new cursor
     */
    record Mimic(String temporaryCursor, String CursorId) implements Statement{}
    /** A MirrorDeuxPoints {@code value_x1, value_x2, value_y1, value_y2}*
     * @param value_x1, value_x2, value_y1, value_y2 the position of the cursor
     */
    record MirrorDeuxPoints(Expr value_x1, Expr value_x2, Expr value_y1, Expr value_y2) implements Statement{}
    /** A MirrorUnPoint {@code value_x, value_y}*
     * @param value_x, value_y the position of the cursor
     */
    record MirrorUnPoint(Expr value_x, Expr value_y) implements Statement{}




}
