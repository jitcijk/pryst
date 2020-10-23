package com.BScProject.truffle.jsl.nodes;

import com.BScProject.truffle.jsl.runtime.JSLUndefinedNameException;
import com.oracle.truffle.api.frame.VirtualFrame;

// The uninitialized rootnodes that are this when they are created. Later they will
// get properties. When a UndefinedFuntionRootNode is executed, exception is thown.
public class JSLUndefinedFunctionRootNode extends JSLRootNode {
	public JSLUndefinedFunctionRootNode(String name) {
		super(null, null, null, name);
	}
	
	@Override
	public Object execute(VirtualFrame frame) {
		throw JSLUndefinedNameException.undefinedFunction(getName());
	}
}