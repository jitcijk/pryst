package com.BScProject.truffle.jsl.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import com.BScProject.truffle.jsl.nodes.JSLRootNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLType.BasicType;
import com.BScProject.truffle.jsl.nodes.call.JSLInvokeNode;

import com.BScProject.truffle.jsl.nodes.controlflow.JSLBlockNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLBreakNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLContinueNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLDebuggerNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLFunctionBodyNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLIfNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLReturnNode;
import com.BScProject.truffle.jsl.nodes.controlflow.JSLWhileNode;

import com.BScProject.truffle.jsl.nodes.expression.JSLBooleanLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLDoubleLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLFunctionLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLLogicalNotNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLongLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLMulLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLNopExpressionNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLAddDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLAddLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryNotNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLParenExpressionNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLStringLiteralNode;
import com.BScProject.truffle.jsl.nodes.local.JSLCreateLocalBooleanArrayNode;
import com.BScProject.truffle.jsl.nodes.local.JSLCreateLocalDoubleArrayNode;
import com.BScProject.truffle.jsl.nodes.local.JSLCreateLocalLongArrayNode;
import com.BScProject.truffle.jsl.nodes.local.JSLCreateLocalStringArrayNode;
import com.BScProject.truffle.jsl.nodes.local.JSLReadArrayVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadBooleanArgumentNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadBooleanArrayValueNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadDoubleArgumentNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadDoubleArrayValueNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadLongArgumentNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadLongArrayValueNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadStringArgumentNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadStringArrayValueNodeGen;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
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
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

/**
 * Helper class used by the SL {@link RecafParser} to create nodes. The code is factored out of the
 * automatically generated parser to keep the attributed grammar of SL small.
 */
public class JSLNodeFactory {
    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;
        protected final Map<String, JSLType> types;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            this.types = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
                types.putAll(outer.types);
            }   
        }
    }
    private LexicalScope lexicalScope;
    private final Source source;
    private final Map<String, JSLRootNode> allFunctions;
    private HashMap<String, JSLType> functionTypeMap;
    private Stack<ArrayList<JSLStatementNode>> bodyStatementListStack;
    
	/* This NodeFactory is loosely based on the NodeFactory that is used in the SimpleLanguage implementation
	 * However, they use a parser that an return Expressions, since JavaParser does not have a nice way of
	 * doing so we use two stacks to simulate this behaviour.
	 * Stack works very well with traversing a tree:
	 * 	 +	
	 *  / \
	 * 1   3
	 * We traverse the tree in post-order, therefore when we encounter the '+' node, we know the 1 and 3 are
	 * on the stack and ready to be popped. When the expression is made we push this new node to the stack.
	 */
	private Stack<JSLStatementNode> statementStack = new Stack<>();
	private Stack<JSLTypedExpressionNode> expressionStack = new Stack<>();


    /* State while parsing a function. */
    private int functionStartPos;
    private int functionBodyStartPos; // does not include parameter list
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    private JSLTypedExpressionNode indexArrayNode;
    private JSLType currentFunctionType;
    private ArrayList<JSLType> functionArgumentTypes;

    /* Need this to set the SourceSections properly. In the JavaParser, Ranges are used, which are
     * stored as the line and column. But Truffle wants Sourcesections to be in 'characters since
     * begin of file' so we need this to convert the ranges properly */
    private ArrayList<Integer> charsUpUntilLine = new ArrayList<>();

    public JSLNodeFactory(Source source) {
        this.source = source;
        this.allFunctions = new HashMap<>();
        this.bodyStatementListStack = new Stack<>();
        this.functionTypeMap = new HashMap<>();
        try {
        	Scanner inScan = new Scanner(new File(source.getPath()));
    		getCharsPerLine(inScan);
        } catch (FileNotFoundException ex) {
        	System.err.println("Could not find file" + source.getPath());
        }
    }

    public Map<String, JSLRootNode> getAllFunctions() {
        return allFunctions;
    }

    public void startFunction(MethodDeclaration node) {
        assert functionStartPos == 0;
        assert parameterCount == 0;
        assert frameDescriptor == null;
        assert lexicalScope == null;

        functionStartPos = getBegin(node);
        
        BlockStmt body = node.getBody().orElse(null);
        if(body != null) {
        	functionBodyStartPos = getBegin(body);
        }
        
        String functionName = node.getNameAsString();
        
        JSLType type = new JSLType(BasicType.FUNCTION);
        type.setFunctionReturn(JSLFactoryUtility.typeToJSL(node.getType()));
        functionTypeMap.put(functionName, type);
        
        frameDescriptor = new FrameDescriptor();
        functionArgumentTypes = new ArrayList<>();
        currentFunctionType = JSLFactoryUtility.typeToJSL(node.getType());
        startBlock();
    }
    /*
     * Method parameters are assigned to local variables at the beginning of the method. This
     * ensures that accesses to parameters are specialized the same way as local variables are
     * specialized.
     */
    public void addFormalParameter(Parameter parameter) {
        createStringLiteral(parameter.getNameAsString(), getBegin(parameter), getLength(parameter));
        JSLType jslType = JSLFactoryUtility.typeToJSL(parameter.getType());
        
        functionArgumentTypes.add(jslType);
        JSLTypedExpressionNode assignment = createParameterAssignment(handleParameter(parameterCount, parameter), jslType);
        bodyStatementListStack.peek().add(assignment);
        parameterCount++;
    }
    
    /* 
     * The body of the method sits on the stack. This is used to create a JeSSEL rootNode, which is stored in the functionMap
     */
    public void finishFunction(MethodDeclaration methodDecl) {
    	JSLStatementNode bodyNode = statementStack.pop();
        final int bodyEndPos = bodyNode.getSourceSection().getCharEndIndex();
        final SourceSection functionSrc = source.createSection(functionStartPos, bodyEndPos - functionStartPos);
        
        // Finish block pushes the block to the statementStack, so pop it directly to get it "returned"
        finishBlock(functionBodyStartPos, getLength(methodDecl));
        final JSLStatementNode methodBlock = statementStack.pop();
        assert lexicalScope == null : "Wrong scoping of blocks in parser";
        final JSLFunctionBodyNode functionBodyNode = new JSLFunctionBodyNode(methodBlock);
        functionBodyNode.setSourceSection(functionSrc);
        
        String functionName = methodDecl.getNameAsString();
        
        // Store type of function arguments
        functionTypeMap.get(functionName).setFunctionArguments(functionArgumentTypes.toArray(new JSLType[functionArgumentTypes.size()]));
        
        // Make new rootNode from the function and put it in the Map
        final JSLRootNode rootNode = new JSLRootNode(frameDescriptor, functionBodyNode, functionSrc, functionName);
        allFunctions.put(functionName, rootNode);

        functionStartPos = 0;
        functionBodyStartPos = 0;
        parameterCount = 0;
        frameDescriptor = null;
        lexicalScope = null;
        currentFunctionType = JSLType.VOID;
    }

    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
        bodyStatementListStack.push(new ArrayList<>());
    }
    public void finishBlock(BlockStmt block) {
    	finishBlock(getBegin(block), getLength(block));    	
    }
    
    /*
     * Restores lexicalScope, flattens the blocks uses this to create a new JSLBlockNode
     */
    public void finishBlock(int begin, int length) {
        lexicalScope = lexicalScope.outer;
        ArrayList<JSLStatementNode> bodyStatementList = bodyStatementListStack.pop();
        
        List<JSLStatementNode> flattenedNodes = new ArrayList<>(bodyStatementList.size());
        flattenBlocks(bodyStatementList, flattenedNodes);
        for (JSLStatementNode statement : flattenedNodes) {
            SourceSection sourceSection = statement.getSourceSection();
            if (sourceSection != null && !isHaltInCondition(statement)) {
                statement.addStatementTag();
            }
        }
        JSLBlockNode blockNode = new JSLBlockNode(flattenedNodes.toArray(new JSLStatementNode[flattenedNodes.size()]));
        blockNode.setSourceSection(source.createSection(begin, length));
        statementStack.push(blockNode);
        if(!bodyStatementListStack.isEmpty()){
        	bodyStatementListStack.peek().add(blockNode);
        }
    }

    private static boolean isHaltInCondition(JSLStatementNode statement) {
        return (statement instanceof JSLIfNode) || (statement instanceof JSLWhileNode);
    }

    private void flattenBlocks(Iterable<? extends JSLStatementNode> bodyNodes, List<JSLStatementNode> flattenedNodes) {
        for (JSLStatementNode n : bodyNodes) {
            if (n instanceof JSLBlockNode) {
                flattenBlocks(((JSLBlockNode) n).getStatements(), flattenedNodes);
            } else {
                flattenedNodes.add(n);
            }
        }
    }
    
    /**
     * Transforms expression into statement.
     */
    public void createExpressionStatement() {
    	JSLTypedExpressionNode expr = expressionStack.pop();
    	bodyStatementListStack.peek().add(expr);    	
    }

    /**
     * Returns an {@link SLDebuggerNode} for the given token.
     *
     * @param debuggerToken The token containing the debugger node's info.
     * @return A SLDebuggerNode for the given token.
     */
    public void createDebugger(Node node) {
        final JSLDebuggerNode debuggerNode = new JSLDebuggerNode();
        debuggerNode.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        bodyStatementListStack.peek().add(debuggerNode);
    }

    /**
     * Returns an {@link SLBreakNode} for the given token.
     *
     * @param breakToken The token containing the break node's info.
     * @return A SLBreakNode for the given token.
     */
    public void createBreak(BreakStmt stmt) {
        final JSLBreakNode breakNode = new JSLBreakNode();
        breakNode.setSourceSection(source.createSection(getBegin(stmt), getLength(stmt)));
        bodyStatementListStack.peek().add(breakNode);
    }

    /**
     * Returns an {@link SLContinueNode} for the given token.
     *
     * @param continueToken The token containing the continue node's info.
     */
    public void createContinue(ContinueStmt node) {
        final JSLContinueNode continueNode = new JSLContinueNode();
        continueNode.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        bodyStatementListStack.peek().add(continueNode);
    }

    /**
     * Pushes a {@link SLWhileNode} for the given parameters.
     *
     * @param whileToken The token containing the while node's info
     * @param conditionNode The conditional node for this while loop
     * @param bodyNode The body of the while loop
     */
    public void createWhile(WhileStmt stmt) {
        JSLTypedExpressionNode conditionNode = expressionStack.pop();
        
        JSLStatementNode bodyNode = statementStack.pop();
        bodyStatementListStack.peek().remove(bodyStatementListStack.peek().size()-1);
    	
    	conditionNode.addStatementTag();
        final JSLWhileNode whileNode = new JSLWhileNode(conditionNode, bodyNode);
        whileNode.setSourceSection(source.createSection(getBegin(stmt), getLength(stmt)));
        bodyStatementListStack.peek().add(whileNode);
    }

    /**
     * Pushes an {@link SLIfNode} for the given parameters.
     *
     * @param ifToken The token containing the if node's info
     * @param conditionNode The condition node of this if statement
     * @param thenPartNode The then part of the if
     * @param elsePartNode The else part of the if
     */
    public void createIf(IfStmt stmt) {
    	JSLTypedExpressionNode conditionNode = expressionStack.pop();
    	// We first handle if=>push to stack, then handle else which is also pushed if present. 
    	// Therefore if else is present, it is on top of stack and needs to be popped first.
    	JSLStatementNode elsePart = null;
    	if (stmt.getElseStmt().isPresent()) {
    		elsePart = statementStack.pop();
    		bodyStatementListStack.peek().remove(bodyStatementListStack.peek().size()-1);
    	}
    	// pop from stack and remove last element of bodyList
    	JSLStatementNode thenPart = statementStack.pop();
    	bodyStatementListStack.peek().remove(bodyStatementListStack.peek().size()-1);
    	
    
    	conditionNode.addStatementTag();        
        final JSLIfNode ifNode = new JSLIfNode(conditionNode, thenPart, elsePart);
        ifNode.setSourceSection(source.createSection(getBegin(stmt), getLength(stmt)));
        bodyStatementListStack.peek().add(ifNode);
    }

    /**
     * Pushes an {@link SLReturnNode} to the statement stack for the given parameters.
     *
     * @param t The token containing the return node's info
     * @param valueNode The value of the return
     * 
     */
    public void createReturn(ReturnStmt stmt) {
    	final JSLTypedExpressionNode valueNode;
    	// Check if return type corresponds with function type (if void, always return NopExpression
    	if (currentFunctionType == JSLType.VOID) {
    		valueNode = new JSLNopExpressionNode();
    	} else {
    		valueNode = expressionStack.pop();
    		checkReturnType(currentFunctionType, JSLFactoryUtility.getType(valueNode));
    	}
        
        final JSLReturnNode returnNode = new JSLReturnNode(valueNode);
        returnNode.setSourceSection(source.createSection(getBegin(stmt), getLength(stmt)));
        bodyStatementListStack.peek().add(returnNode);
    }
    
    private static void checkReturnType(JSLType functionType, JSLType valueType) {
    	if(!functionType.canAccept(valueType)){
    		throw new JSLParseException("Return type of function(" +valueType.getBasicType()+ ") does not correspond to the signature of the function ("+ functionType.getBasicType()+")");
    	}
    }

    public void createBinary(BinaryExpr expr) {
    	// Pop in reversed order
    	final JSLTypedExpressionNode rightNode = expressionStack.pop();
    	final JSLTypedExpressionNode leftNode = expressionStack.pop();
    	final String operator = expr.getOperator().asString();
    	
    	final JSLTypedExpressionNode result = JSLFactoryUtility.makeTypedBinary(operator, leftNode, rightNode);
        result.setSourceSection(source.createSection(getBegin(expr), getLength(expr)));
        expressionStack.push(result);
    }
    
    /* 
     * Makes expressionNode for the Unary operators ++ -- + - and !
     */
    public void createUnary(UnaryExpr expr) {
    	final String operator = expr.getOperator().asString();
    	final JSLTypedExpressionNode valueNode = expressionStack.pop();
    	
    	makeTypedUnary(valueNode, operator, expr);    	
    }
    
    /**
     * Pushes an {@link SLInvokeNode} to the stack.
     */
    public void createCall(MethodCallExpr expr) {
    	String funcName = expr.getNameAsString();
    	int nrParameters = expr.getArguments().size();
    	JSLTypedExpressionNode arr[] = new JSLTypedExpressionNode[nrParameters];
    	
    	// We pushed 1,2,3,4,5 so then the stack is 5,4,3,2,1 but we want it 1,2,3,4,5 like inputted by user
    	for (int i = nrParameters-1; i >= 0; i--) {
    		arr[i] = expressionStack.pop();
    	}
    	JSLTypedExpressionNode functionNode = new JSLFunctionLiteralNode(funcName);
    	
    	JSLType type = functionTypeMap.get(funcName);
    	JSLTypedExpressionNode invoke = new JSLInvokeNode(functionNode, arr, type);
    	expressionStack.push(invoke);
    }

    /**
     * Returns an {@link SLWriteLocalVariableNode} for the given parameters.
     * @param valueNode 
     *
     * @param nameNode The name of the variable being assigned
     * @param valueNode The value to be assigned
     * @return An SLExpressionNode for the given parameters.
     * 
     * ==========================================================================================
     */
    // For in parameter
    public JSLTypedExpressionNode createParameterAssignment(JSLTypedExpressionNode valueNode, JSLType type) {
    	JSLTypedExpressionNode nameNode = expressionStack.pop();
        
    	
    	String name = ((JSLStringLiteralNode) nameNode).executeGeneric(null);
        FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
        lexicalScope.locals.put(name, frameSlot);
        lexicalScope.types.put(name, type);
        
        // Check type you want to write
        final JSLTypedExpressionNode result = JSLFactoryUtility.makeWriteLocalVariableNode(frameSlot, valueNode, type);

        if (valueNode.getSourceSection() != null) {
            final int start = nameNode.getSourceSection().getCharIndex();
            final int length = valueNode.getSourceSection().getCharEndIndex() - start;
            result.setSourceSection(source.createSection(start, length));
        }
        return result;
    }
    
    // int a = 4;
    public void createDeclAssignment(VariableDeclarator expr) {
    	JSLType type = JSLFactoryUtility.typeToJSL(expr.getType());
    	if (type == JSLType.ARRAY){
    		createArrayDecl(expr);
    	} else {
    		String name = expr.getNameAsString();
    	   	JSLTypedExpressionNode valueNode;
	    	
	    	if(expr.getInitializer().isPresent()) {
	    		valueNode = expressionStack.pop();
	    		JSLFactoryUtility.doTypeCheck(type, JSLFactoryUtility.getType(valueNode), false);
	    	} else {
	    		valueNode = JSLFactoryUtility.makeDefaultAssignment(expr);
	    	}
	    	
	    	 FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
	         lexicalScope.locals.put(name, frameSlot);
	         lexicalScope.types.put(name, type);
	         
	         final JSLTypedExpressionNode result = JSLFactoryUtility.makeWriteLocalVariableNode(frameSlot, valueNode, type);
	         
	         if (valueNode.getSourceSection() != null) {
	             result.setSourceSection(source.createSection(getBegin(expr), getLength(expr)));
	         }
	         expressionStack.push(result);
    	}
    }
    
    // a = 4
    public void createReAssignment(AssignExpr expr) {
    	// valueNode is evaluated last => first on stack
    	JSLTypedExpressionNode valueNode = expressionStack.pop();    	
    	
    	final JSLTypedExpressionNode result;
    	String name = expr.getTarget().toString(); 
    	
    	if(name.contains("[")){
    		/* 	We made the createArrayRead that popped the index from the expressionStack
    		 	So we stored it in a variable so we can use it here. 
    			We know we can do this safely because we always first encounter a ArrayAccessExpr */
    		
    		JSLTypedExpressionNode indexNode = indexArrayNode;
    		
    		//We pop the readArrayNode, and not use it, we replace it with a writeArrayNode here
    		expressionStack.pop();
    		
    		//This is a assignment to an array
    		name = name.replaceAll("\\[.+\\]", "");
    		FrameSlot frameSlot = lexicalScope.locals.get(name);
    		JSLType type = lexicalScope.types.get(name);
    		JSLFactoryUtility.doTypeCheck(type.getTypeOfArrayContents(), JSLFactoryUtility.getType(valueNode), true);
    		result = JSLFactoryUtility.makeWriteLocalArrayNode(frameSlot, indexNode, valueNode, type);   		
    	} else {
        	// unused, but otherwise it stays on stack.
        	JSLTypedExpressionNode nameNode = expressionStack.pop();
    		
            FrameSlot frameSlot = lexicalScope.locals.get(name);
            JSLType type = lexicalScope.types.get(name);
            
            JSLFactoryUtility.doTypeCheck(type, JSLFactoryUtility.getType(valueNode), false);
//            valueNode = JSLFactoryUtility.applyAssignOperator(expr.getOperator(), frameSlot, valueNode, type);
            
            // Check type you want to write
            result = JSLFactoryUtility.makeWriteLocalVariableNode(frameSlot, valueNode, type);

            if (valueNode.getSourceSection() != null) {
                final int start = nameNode.getSourceSection().getCharIndex();
                final int length = valueNode.getSourceSection().getCharEndIndex() - start;
                result.setSourceSection(source.createSection(start, length));
            }
    	}
    	expressionStack.push(result);
    }


    /**
     * Returns a {@link SLReadLocalVariableNode} if this read is a local variable or a
     * {@link SLFunctionLiteralNode} if this read is global. In SL, the only global names are
     * functions.
     *
     * @param nameNode The name of the variable/function being read
     * @return either:
     *         <ul>
     *         <li>A SLReadLocalVariableNode representing the local variable being read.</li>
     *         <li>A SLFunctionLiteralNode representing the function definition</li>
     *         </ul>
     */
    public void createRead(NameExpr expr) {
        String name = expr.getNameAsString();
        
        final JSLTypedExpressionNode result;
        final FrameSlot frameSlot = lexicalScope.locals.get(name);
        final JSLType type = lexicalScope.types.get(name);
        // Get the type of the name and ReadLocalDoubleVariable or ReadLongVariable
        if (frameSlot != null) {
            /* Reading an Array index is handled by the ArrayAccessExpr, not here */
        	if(type == JSLType.ARRAY) {
        		return;
        	}
        	// If it is an array but no index [, we assume it is a functionCall and the array is a parameter
            result = JSLFactoryUtility.createTypedRead(frameSlot, type);
        } else {
        	// This probably never happens since in a function call we do not find the NameExpr but
        	// MethodCallDecl
        	System.err.println("Should not come here " + name);
        	result = new JSLFunctionLiteralNode(name);
        }
        result.setSourceSection(source.createSection(getBegin(expr), getLength(expr)));
        expressionStack.push(result);
    }
    
    
    public void createArrayRead(ArrayAccessExpr expr) {
    	String name = expr.getName().toString();
    	
    	FrameSlot frameSlot = lexicalScope.locals.get(name);
    	JSLType type = lexicalScope.types.get(name);
    	
    	JSLTypedExpressionNode indexNode = expressionStack.pop();
    	
    	indexArrayNode = indexNode;
    	JSLTypedExpressionNode result;
    	if (type.getTypeOfArrayContents() == JSLType.DOUBLE) {
    		result = JSLReadDoubleArrayValueNodeGen.create(JSLReadArrayVariableNodeGen.create(frameSlot), indexNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.LONG) {
    		result = JSLReadLongArrayValueNodeGen.create(JSLReadArrayVariableNodeGen.create(frameSlot), indexNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.BOOLEAN) {
    		result = JSLReadBooleanArrayValueNodeGen.create(JSLReadArrayVariableNodeGen.create(frameSlot), indexNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.STRING) {
    		result = JSLReadStringArrayValueNodeGen.create(JSLReadArrayVariableNodeGen.create(frameSlot), indexNode);
    	} else {
    		throw new JSLParseException("Array is not a primitive Type");
    	}
    	expressionStack.push(result);
    }

    
    /**
     * Creates String literal from StringLiteralExpr
     */
    public void createStringLiteral(StringLiteralExpr expr) {
    	createStringLiteral(expr.getValue(), getBegin(expr), getLength(expr));
    }
    
    
    /**
     * Creates String literal from CharLiteralExpr
     */
    public void createStringLiteral(CharLiteralExpr expr) {
        createStringLiteral(expr.getValue(), getBegin(expr), getLength(expr));
    }
    
    /**
     * Create Long literal from Integer 
     * @param node The javaparser IntegerLiteralExpr node
     */
    public void createLongLiteral(IntegerLiteralExpr node) {
        JSLTypedExpressionNode result = new JSLLongLiteralNode(Long.parseLong(node.getValue()));
        result.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        expressionStack.push(result);
    }
    
    /**
     *  Create Long literal from long
     *  @param node The javaparser LongLiteralExpr node
     */
    public void createLongLiteral(LongLiteralExpr node) {
        JSLTypedExpressionNode result = new JSLLongLiteralNode(Long.parseLong(node.getValue()));
        result.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        expressionStack.push(result);
    }
    
    /** 
     * Create Double from double (Float is stored in this as well) 
     * @param node The javaparser DoubleLiteralExpr node
     */
    public void createDoubleLiteral(DoubleLiteralExpr node) {
    	JSLTypedExpressionNode result = new JSLDoubleLiteralNode(Double.parseDouble(node.getValue()));
        result.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        expressionStack.push(result);
    }
   
    /**
     * Creates expression node for boolean and pushes it to the stack.
     * @param node The javaparser BooleanLiteralExpr node
     */
    public void createBooleanLiteral(BooleanLiteralExpr node) {
        JSLTypedExpressionNode result = new JSLBooleanLiteralNode(node.getValue());
        result.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        expressionStack.push(result);
    }

    /**
     * Creates expression node for parenthesized expression, pushes it to the expression stack.
     * @param node The javaparser EnclosedExpr node
     */
    public void createParenExpression(EnclosedExpr node) {
        final JSLParenExpressionNode result = new JSLParenExpressionNode(expressionStack.pop());
        result.setSourceSection(source.createSection(getBegin(node), getLength(node)));
        expressionStack.push(result);
    }
    
    
    /* Extracts the Read___ArgumentNode for the proper type based on the ParameterType */
    private JSLTypedExpressionNode handleParameter(int parameterCount, Parameter parameter) {
    	JSLType type = JSLFactoryUtility.typeToJSL(parameter.getType());
		if (type == JSLType.DOUBLE) {
			return JSLReadDoubleArgumentNodeGen.create(parameterCount);
		} else if (type == JSLType.LONG) {
			return JSLReadLongArgumentNodeGen.create(parameterCount);
		} else if (type == JSLType.STRING) {
			return JSLReadStringArgumentNodeGen.create(parameterCount);
		} else if (type == JSLType.BOOLEAN) {
			return JSLReadBooleanArgumentNodeGen.create(parameterCount);
		}  else {
			throw new JSLParseException("Primitive parameter type not (yet) supported: " + type.getBasicType());
		}
    }
    

     
    /*  Makes a new Array:
    	implements the statement: int[] a = new int[10] */
    private void createArrayDecl(VariableDeclarator expr) {
    	String name = expr.getNameAsString();
    	JSLType type = JSLFactoryUtility.typeToJSL(expr.getType());
    	
        FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
        lexicalScope.locals.put(name, frameSlot);
    	
    	if(expr.getInitializer().isPresent()) {
    		if (expr.getInitializer().orElse(null) instanceof ArrayCreationExpr){
    			JSLType elementType = JSLFactoryUtility.typeToJSL(((ArrayCreationExpr)expr.getInitializer().orElse(null)).getElementType());
    			type.setArrayType(elementType);
    			makeArrayDecl(type, frameSlot);
    		
    		} else if (expr.getInitializer().orElse(null) instanceof ArrayInitializerExpr) {
    			// int[] arr = {1,2,3,4,5}
    			ArrayInitializerExpr initExpr = (ArrayInitializerExpr)expr.getInitializer().orElse(null);
    			int size = initExpr.getValues().size();
    			makeArrayInit(size, frameSlot, type);
    		
    		} else {
    			throw new JSLParseException("This construct is not supported: " + expr.getInitializer().orElse(null).toString());
    		}
    	}
    	lexicalScope.types.put(name, type);
    	// Push NopExpression, because the VariableDeclarator is an Expression
    	// So will be inside a ExpressionStatement which needs something to pop from expressionStack
    	expressionStack.push(new JSLNopExpressionNode());
    }
    
    /* Makes Array Declaration */
    private void makeArrayDecl(JSLType type, FrameSlot frameSlot) {
    	JSLType elementType = type.getTypeOfArrayContents();
		JSLTypedExpressionNode arrSizeNode = expressionStack.pop();
		
		JSLStatementNode array;
		if(elementType == JSLType.DOUBLE){
			array = new JSLCreateLocalDoubleArrayNode(frameSlot, arrSizeNode);
		} else if (elementType == JSLType.LONG) {
			array = new JSLCreateLocalLongArrayNode(frameSlot, arrSizeNode);
		} else if (elementType == JSLType.BOOLEAN) {
			array = new JSLCreateLocalBooleanArrayNode(frameSlot, arrSizeNode);
		} else if (elementType == JSLType.STRING){
			array = new JSLCreateLocalStringArrayNode(frameSlot, arrSizeNode);
		} else {
			throw new JSLParseException("Do not support this array type " + elementType);
		}
		bodyStatementListStack.peek().add(array);
    }
    
    private void makeArrayInit(int size, FrameSlot frameSlot, JSLType type) {
    	if(size == 0) { 
    		return;
    	}
		JSLTypedExpressionNode[] nodeArr = new JSLTypedExpressionNode[size];
		
		// Stack is filled with values, since size is not 0;
		JSLType elementType = expressionStack.peek().getType();
		type.setArrayType(elementType);
		
		// Push length of array to be created since it is used by makeArrayDecl
		expressionStack.push(new JSLLongLiteralNode(size));
		makeArrayDecl(type, frameSlot);
		
		for(int i = size-1; i >= 0; i--) {
			JSLTypedExpressionNode node = expressionStack.pop();
			nodeArr[i] = node;
		}
		
		// Write the values
		for(int i = 0; i < size; i++){
			JSLTypedExpressionNode indexNode = new JSLLongLiteralNode(i);
			bodyStatementListStack.peek().add(JSLFactoryUtility.makeWriteLocalArrayNode(frameSlot, indexNode, nodeArr[i], type));
		}
    }
    
    
	/* Expression for ++ -- (The precedence is not taken into account (No difference between ++i and i++) */
    private void makeTypedUnary(JSLTypedExpressionNode valueNode, String operator, UnaryExpr expr) {
    	JSLTypedExpressionNode result;
    	int change;
    	if (operator == "++" || operator == "--") {
    		change = (operator == "++" ? 1 : -1);
    		if(valueNode.getType() == JSLType.LONG){
    			 result = JSLAddLongNodeGen.create(valueNode, new JSLLongLiteralNode(change));
    		} else {
    			result =  JSLAddDoubleNodeGen.create(valueNode, new JSLDoubleLiteralNode(change));
    		}
    		writeVariable(JSLFactoryUtility.getNameUnaryExpression(expr.getExpression().toString()), result, expr);  
    		return;
    	} else if (operator == "+" || operator == "-") {
    		change = (operator == "+" ? 1 : -1);
    		if(valueNode.getType() == JSLType.LONG){
    			result =  JSLMulLongNodeGen.create(valueNode, new JSLLongLiteralNode(change));
    		} else {
    			result = JSLAddDoubleNodeGen.create(valueNode, new JSLDoubleLiteralNode(change));
    		}
    	} else if (operator == "!") { // is ! 
    		result = JSLLogicalNotNodeGen.create(valueNode);
    	} else if (operator == "~") {
    		result = JSLBinaryNotNodeGen.create(valueNode);
    	} else {
    		throw new JSLParseException("Do not know this unary operator: " + operator);
    	}
    	expressionStack.push(result);
    }
    
    /* Writes the valueNode to the variable with variableName */
    private void writeVariable(String variableName, JSLTypedExpressionNode valueNode, Node expr){
        FrameSlot frameSlot = lexicalScope.locals.get(variableName);
        JSLType type = lexicalScope.types.get(variableName);
        
        // Check type you want to write.
        JSLFactoryUtility.doTypeCheck(type, JSLFactoryUtility.getType(valueNode), false);
        final JSLTypedExpressionNode result = JSLFactoryUtility.makeWriteLocalVariableNode(frameSlot, valueNode, type);

        if (valueNode.getSourceSection() != null) {
            result.setSourceSection(source.createSection(getBegin(expr), getLength(expr)));
        }
        expressionStack.push(result);
    }
    
    
    /**
     * Creates the string variable, this is never called from the JavaParserVisitor.
     * @param value Value of the to be created String
     * @param begin Start of the String Node
     * @param length Length of the String Node
     */
    private void createStringLiteral(String value, int begin, int length) {
    	final JSLStringLiteralNode result = new JSLStringLiteralNode(value.intern());
        result.setSourceSection(source.createSection(begin, length));
        expressionStack.push(result);
    }
    

    /** 
     * For every line store how much characters have come before it
     * @param scanner To get the lines of the source
     */
    private void getCharsPerLine(Scanner scanner) {
    	int charCounter = 0;
    	
    	while (scanner.hasNext()) {
    		charsUpUntilLine.add(charCounter);
    		String line = scanner.nextLine();
    		charCounter += line.length();
    	}
    }
    
    /**
     * Gets the number of characters to the start of this Node. (Used to set SourceSection)
     * @param node Node to find start of
     * @return The number of chars to the start of the node from the start of the source.
     */
    private int getBegin(Node node) {
    	Range range = getRange(node);
    	Position begin = range.begin;
    	int charToBegin = begin.column + charsUpUntilLine.get(begin.line);
    	return charToBegin;
    }
    
    /**
     *  Gets the length of a Node in characters (Used to set SourceSection)
     * 	@param node Node to find the length of
     * 	@return The number of characters between the start of the node and the end of it.
     */
    private int getLength(Node node) {
    	Range range = getRange(node);
    	Position begin = range.begin;
    	Position end = range.end; 
    	
    	int charToBegin = begin.column + charsUpUntilLine.get(begin.line-1);
    	int charToEnd = end.column + charsUpUntilLine.get(end.line-1);
    	
    	return charToEnd - charToBegin;
    }
    
    // Gets range of a Node. Defaults to a range from 0,0 to 0,0 if node has no range
    private Range getRange(Node node) {
    	return node.getRange().orElse(new Range(new Position(0,0), new Position(0,0)));
    }
}
