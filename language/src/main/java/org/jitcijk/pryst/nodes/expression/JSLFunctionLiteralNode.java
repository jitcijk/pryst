package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.JSLLanguage;
import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLContext;
import com.BScProject.truffle.jsl.runtime.JSLFunction;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "function")
public final class JSLFunctionLiteralNode extends JSLTypedExpressionNode {
	private final String functionName;
	
    /**
     * The resolved function. During parsing (in the constructor of this node), we do not have the
     * {@link SLContext} available yet, so the lookup can only be done at {@link #executeGeneric
     * first execution}. The {@link CompilationFinal} annotation ensures that the function can still
     * be constant folded during compilation.
     */
    @CompilationFinal private JSLFunction cachedFunction;
	
	public JSLFunctionLiteralNode(String functionName) {
		this.functionName = functionName;
	}
	
	
	
	@Override
    public JSLFunction executeGeneric(VirtualFrame frame) {
        if (cachedFunction == null) {
            /* We are about to change a @CompilationFinal field. */
            CompilerDirectives.transferToInterpreterAndInvalidate();
            /* First execution of the node: lookup the function in the function registry. */
            JSLContext context = JSLLanguage.INSTANCE.findContext();
            cachedFunction = context.getFunctionRegistry().lookup(functionName, true);
        }
        return cachedFunction;
    }
	
	@Override
	public JSLType getType() {
		return JSLType.FUNCTION;
	}
	
}