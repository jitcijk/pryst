package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "debugger", description = "The node implementing a debugger statement")
public class JSLDebuggerNode extends JSLStatementNode {
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		// Nothing
	}
	
	@Override
	protected boolean isTaggedWith(Class<?> tag) {
		if (tag == DebuggerTags.AlwaysHalt.class) {
			return true;
		} else {
			return super.isTaggedWith(tag);
		}
	}
}