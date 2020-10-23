package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "break", description = "Node implementing the break statement")
public final class JSLBreakNode extends JSLStatementNode {

	@Override
	public void executeVoid(VirtualFrame frame) {
		throw JSLBreakException.SINGLETON;
	}
}