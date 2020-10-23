package com.BScProject.truffle.jsl.parser;

import java.util.Map;

import com.BScProject.truffle.jsl.nodes.JSLRootNode;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.oracle.truffle.api.source.Source;

/**
 * This class visits every Node from the tree.
 * The nodes that are used have their own implementation.
 * The JeSSEL tree is build up in the JSLNodeFactory
 * 
 * TODO Make this also with a proper visitor that returns the nodes. This way we do not have to 
 * have a stateful factory with stacks (which is ugly).
 * 
 * @author bram
 */
public class TreeTranslatorVisitor extends VoidVisitorAdapter<Void> {
	
	private JSLNodeFactory factory;
	
	public TreeTranslatorVisitor(Source source) {
		this.factory = new JSLNodeFactory(source);
	}
	
	@Override
	public void visit(MethodDeclaration methodDecl, Void arg1) {
		factory.startFunction(methodDecl);
		super.visit(methodDecl, arg1);
		factory.finishFunction(methodDecl);
		
	}
	
	@Override
	public void visit(BlockStmt block, Void arg1) {
		// Block 
		factory.startBlock();
		super.visit(block, arg1);
		factory.finishBlock(block);
	}
	
	@Override
	public void visit(Parameter parameter, Void arg1) {
		// Parameter => Add to local variables and move on
		factory.addFormalParameter(parameter);
		super.visit(parameter, arg1);
		
	}
	
	/* ======================= Expressions ========================== */
	@Override
	public void visit(ExpressionStmt expr, Void arg1) {
		// Expression Statement
		super.visit(expr, arg1);
		factory.createExpressionStatement();
	}
	
	
	@Override
	public void visit(EnclosedExpr expr, Void arg1) {
		// Parenthesized expression
		super.visit(expr, arg1);
		factory.createParenExpression(expr);
	}
	
	@Override
	public void visit(BinaryExpr expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createBinary(expr);
	}
	
	@Override
	public void visit(ConditionalExpr expr, Void arg1) {
		super.visit(expr, arg1);
		System.out.println("Conditional Expr (_?_:_) not implemented " + expr.toString()); 
	}
	
	@Override
	public void visit(MethodCallExpr expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createCall(expr);
	}
	
	@Override
	public void visit(UnaryExpr expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createUnary(expr);
	}
	
	@Override
	public void visit(AssignExpr expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createReAssignment(expr);
	}
	
	@Override
	public void visit(NameExpr expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createRead(expr);
	}
	
	@Override
	public void visit(VariableDeclarator expr, Void arg1) {
		super.visit(expr, arg1);
		factory.createDeclAssignment(expr);
	}
	

	/* ==================== Statement ====================== */
	
	@Override
	public void visit(BreakStmt stmt, Void arg1) {
		super.visit(stmt, arg1);
		factory.createBreak(stmt);
	}
	
	@Override
	public void visit(ContinueStmt stmt, Void arg1) {
		super.visit(stmt, arg1);
		factory.createContinue(stmt); 
	}
	
	@Override
	public void visit(IfStmt stmt, Void arg1) {
		super.visit(stmt, arg1);
		factory.createIf(stmt);
	}

	@Override
	public void visit(ReturnStmt stmt, Void arg1) {
		super.visit(stmt, arg1);
		factory.createReturn(stmt);
	}
	
	@Override
	public void visit(WhileStmt stmt, Void arg1) {
		super.visit(stmt, arg1);
		factory.createWhile(stmt);
	}
	
	/* =========== Array Stuff ============== */ 
	public void visit(ArrayCreationExpr expr, Void arg1) {
		// Array creation => Not used, is handled in VariableDeclarator
		super.visit(expr, arg1);
	}
	
	@Override 
	public void visit(ArrayAccessExpr expr, Void arg1) {
		// Array Access => Used for reAssignment and read. Always make read. 
		// Replace when AssignExpr target
		super.visit(expr, arg1);
		factory.createArrayRead(expr);
	}
	
	@Override 
	public void visit(ArrayInitializerExpr expr, Void arg1) {
		super.visit(expr, arg1);
	}
	
	/*============================= Literals ========================*/ 
		@Override
	public void visit(DoubleLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createDoubleLiteral(literal);
	}
	
	@Override
	public void visit(LongLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createLongLiteral(literal);
	}
	
	@Override
	public void visit(BooleanLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createBooleanLiteral(literal);
	}
	
	@Override
	public void visit(IntegerLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createLongLiteral(literal);
		
	}
	
	/* We store Chars just as String, for simplicity. Could be always changed out later */
	@Override
	public void visit(CharLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createStringLiteral(literal);
	}
	
	@Override
	public void visit(StringLiteralExpr literal, Void arg1) {
		super.visit(literal, arg1);
		factory.createStringLiteral(literal);
	}
	
	public Map<String, JSLRootNode> getFunctions() {
		return factory.getAllFunctions();
	}
	
}