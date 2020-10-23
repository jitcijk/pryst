package com.BScProject.truffle.jsl.nodes.call;

import com.BScProject.truffle.jsl.nodes.interop.JSLForeignToJSLTypeNode;
import com.BScProject.truffle.jsl.nodes.interop.JSLForeignToJSLTypeNodeGen;
import com.BScProject.truffle.jsl.runtime.JSLFunction;
import com.BScProject.truffle.jsl.runtime.JSLUndefinedNameException;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

public abstract class JSLDispatchNode extends Node {
	protected static final int INLINE_CACHE_SIZE = 2;
	
	public abstract Object executeDispatch(VirtualFrame frame, Object function, Object[] arguments);
	
	// Calling function multiple times can be benifit from functions being cached.
	@Specialization(limit = "INLINE_CACHE_SIZE",
					guards = "function == cachedFunction",
					assumptions = "cachedFunction.getCallTargetStable()")
	protected static Object doDirect(VirtualFrame frame, JSLFunction function, Object[] arguments,
							@Cached("function") JSLFunction cachedFunction,
							@Cached("create(cachedFunction.getCallTarget())") DirectCallNode callNode) {
		return callNode.call(frame, arguments);
	}
	
	
	@Specialization(contains = "doDirect")
	protected static Object doIndirect(VirtualFrame frame, JSLFunction function, Object[] arguments,
			@Cached("create()") IndirectCallNode callNode){
		return callNode.call(frame, function.getCallTarget(), arguments);
	}
	
	@Fallback
	protected static Object unknownFunction(Object function, Object[] arguments) {
		throw JSLUndefinedNameException.undefinedFunction(function);
	}
	
	/* Language interoperability. Copied from SL */
	
    @Specialization(guards = "isForeignFunction(function)")
    protected static Object doForeign(VirtualFrame frame, TruffleObject function, Object[] arguments,
                    // The child node to call the foreign function
                    @Cached("createCrossLanguageCallNode(arguments)") Node crossLanguageCallNode,
                    // The child node to convert the result of the foreign call to a SL value
                    @Cached("createToJSLTypeNode()") JSLForeignToJSLTypeNode toSLTypeNode) {

        try {
            /* Perform the foreign function call. */
            Object res = ForeignAccess.sendExecute(crossLanguageCallNode, frame, function, arguments);
            /* Convert the result to a SL value. */
            return toSLTypeNode.executeConvert(frame, res);

        } catch (ArityException | UnsupportedTypeException | UnsupportedMessageException e) {
            /* Foreign access was not successful. */
            throw JSLUndefinedNameException.undefinedFunction(function);
        }
    }

    protected static boolean isForeignFunction(TruffleObject function) {
        return !(function instanceof JSLFunction);
    }

    protected static Node createCrossLanguageCallNode(Object[] arguments) {
        return Message.createExecute(arguments.length).createNode();
    }

    protected static JSLForeignToJSLTypeNode createToJSLTypeNode() {
        return JSLForeignToJSLTypeNodeGen.create();
    }
}