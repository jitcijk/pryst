package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "return", description = "The node implementing a return statement")
public final class JSLReturnNode extends JSLStatementNode {
	
	@Child private JSLExpressionNode valueNode;

	public JSLReturnNode(JSLExpressionNode valueNode) {
		this.valueNode = valueNode;
	}
	
	
	/* Since we can be deep in interpreter frames we can not just return the value without
	 * having it getting lost. Therefore we throw an exception that is caught in the functionBody.
	 */
	@Override
	public void executeVoid(VirtualFrame frame) {
		Object result;
		if(valueNode != null) {
			result = valueNode.executeGeneric(frame);
		} else {
			result = JSLNull.SINGLETON;
		}
		throw new JSLReturnException(result);
		
	}
	
}