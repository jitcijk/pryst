package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.source.SourceSection;

public final class JSLWhileRepeatingNode extends Node implements RepeatingNode {
	
	@Child private JSLExpressionNode condition;
	
	@Child private JSLStatementNode bodyNode;
	
	private final BranchProfile continueTaken = BranchProfile.create();
	private final BranchProfile breakTaken = BranchProfile.create();
	
	private SourceSection sourceSection;
	
	public JSLWhileRepeatingNode(JSLExpressionNode condition, JSLStatementNode bodyNode) {
		this.condition = condition;
		this.bodyNode = bodyNode;
	}
	
	public SourceSection getSourceSection() {
		return sourceSection;
	}
	
	public void setSourceSection(SourceSection section) {
		assert this.sourceSection == null : "overwriting existing SourceSection!";
		this.sourceSection = section;
	}
	
	@Override
	public boolean executeRepeating(VirtualFrame frame) {
		if(!evaluateCondition(frame)){
			return false;
		}
		
		try {
			bodyNode.executeVoid(frame);
			return true;
		} catch (JSLContinueException ex) {
			continueTaken.enter();
			return true;
		} catch (JSLBreakException ex) {
			breakTaken.enter();
			return false;
		}
	}
	
	
	private boolean evaluateCondition(VirtualFrame frame) {
		try {
			return condition.executeBoolean(frame);
		} catch (UnexpectedResultException ex) {
			throw new UnsupportedSpecializationException(this, new Node[]{condition}, ex.getResult());
		}
	}
	
	@Override
	public String toString() {
		return JSLStatementNode.formatSourceSection(this);
	}
	
	
	
	
}