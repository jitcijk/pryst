package com.BScProject.truffle.jsl.nodes;

import java.util.Map;

import com.BScProject.truffle.jsl.JSLLanguage;
import com.BScProject.truffle.jsl.runtime.JSLContext;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

/**
 * 2 Extra tasks that JSLRootNode doesnt do:
 * - Lazily registering functions on first execution
 * - Conversion of arguments to types understood by JSL (So callers from
 * 		other languages can also use it.
 *
 */

public final class JSLEvalRootNode extends JSLRootNode {
	
	private final Map<String, JSLRootNode> functions;
	@CompilationFinal private JSLContext context;
	
	public JSLEvalRootNode(FrameDescriptor frameDescriptor, JSLExpressionNode bodyNode, SourceSection sourceSection, String name, Map<String, JSLRootNode> functions) {
		super(frameDescriptor, bodyNode, sourceSection, name);
		this.functions = functions;
	}
	
	@Override
	public Object execute(VirtualFrame frame) {
		if (context == null) {
			CompilerDirectives.transferToInterpreterAndInvalidate();
			
			context = JSLLanguage.INSTANCE.findContext();
			context.getFunctionRegistry().register(functions);
		}
		
		// If no main function:
		if (getBodyNode() == null) {
			return null;
		}
		
		// Conversion of arguments 
		Object[] arguments = frame.getArguments();
		for(int i = 0; i < arguments.length; i++) {
			arguments[i] = JSLContext.fromForeignValue(arguments[i]);
		}
		
		// Now we can execute the main 
		return super.execute(frame);
	}
	
}