package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "continue", description = "The node implementing the continue statement")
public final class JSLContinueNode extends JSLStatementNode {

	@Override
	public void executeVoid(VirtualFrame frame) {
		throw JSLContinueException.SINGLETON;
	}
	
}