// Generated from language/src/main/java/org/jitcijk/pryst/parser/Pryst.g4 by ANTLR 4.8
package org.jitcijk.pryst.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PrystParser}.
 */
public interface PrystListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PrystParser#compileUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompileUnit(PrystParser.CompileUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#compileUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompileUnit(PrystParser.CompileUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(PrystParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(PrystParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(PrystParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(PrystParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(PrystParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(PrystParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeTypeOrVoid(PrystParser.TypeTypeOrVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeTypeOrVoid(PrystParser.TypeTypeOrVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(PrystParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(PrystParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(PrystParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(PrystParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(PrystParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(PrystParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(PrystParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(PrystParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(PrystParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(PrystParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(PrystParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(PrystParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(PrystParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(PrystParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(PrystParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(PrystParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(PrystParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(PrystParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(PrystParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(PrystParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(PrystParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(PrystParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(PrystParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(PrystParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(PrystParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(PrystParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(PrystParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(PrystParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(PrystParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(PrystParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PrystParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PrystParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(PrystParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(PrystParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(PrystParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(PrystParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PrystParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PrystParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PrystParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PrystParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(PrystParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(PrystParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(PrystParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(PrystParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(PrystParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(PrystParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchStatement(PrystParser.TryCatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchStatement(PrystParser.TryCatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(PrystParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(PrystParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PrystParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PrystParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStatement(PrystParser.ThrowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStatement(PrystParser.ThrowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(PrystParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(PrystParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(PrystParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(PrystParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(PrystParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(PrystParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(PrystParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(PrystParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(PrystParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(PrystParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(PrystParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(PrystParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(PrystParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(PrystParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(PrystParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(PrystParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(PrystParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(PrystParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(PrystParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(PrystParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(PrystParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(PrystParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(PrystParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(PrystParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(PrystParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(PrystParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PrystParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PrystParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(PrystParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(PrystParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameters(PrystParser.LambdaParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameters(PrystParser.LambdaParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void enterLambdaBody(PrystParser.LambdaBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void exitLambdaBody(PrystParser.LambdaBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(PrystParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(PrystParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(PrystParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(PrystParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(PrystParser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(PrystParser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(PrystParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(PrystParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#typeType}.
	 * @param ctx the parse tree
	 */
	void enterTypeType(PrystParser.TypeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#typeType}.
	 * @param ctx the parse tree
	 */
	void exitTypeType(PrystParser.TypeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrystParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(PrystParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrystParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(PrystParser.PrimitiveTypeContext ctx);
}