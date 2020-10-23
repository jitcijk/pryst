package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(shortName = "while", description = "The node implementing a while loop")
public final class JSLWhileNode extends JSLStatementNode {
	
	@Child private LoopNode loopNode;
	
	public JSLWhileNode(JSLExpressionNode condition, JSLStatementNode loopBody) {
		this.loopNode = Truffle.getRuntime().createLoopNode(new JSLWhileRepeatingNode(condition, loopBody));
	}
	
	@Override
	public void setSourceSection(SourceSection sourceSection) {
		super.setSourceSection(sourceSection);
		((JSLWhileRepeatingNode) loopNode.getRepeatingNode()).setSourceSection(sourceSection);
	}

	@Override
	public void executeVoid(VirtualFrame frame) {
		loopNode.executeLoop(frame);
		
	}
	
	
	
}