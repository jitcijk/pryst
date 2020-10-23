package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.BranchProfile;

@NodeInfo(shortName = "body")
public final class JSLFunctionBodyNode extends JSLExpressionNode {

	@Child private JSLStatementNode bodyNode;

	private final BranchProfile exceptionTaken = BranchProfile.create();
	private final BranchProfile nullTaken = BranchProfile.create();
	
	public JSLFunctionBodyNode(JSLStatementNode bodyNode) {
		this.bodyNode = bodyNode;
		addRootTag();
	}
	
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		try {
			bodyNode.executeVoid(frame);
		} catch (JSLReturnException ex) {
			exceptionTaken.enter();
			return ex.getResult();
		}
		
		nullTaken.enter();
		return JSLNull.SINGLETON;
	}
	
}