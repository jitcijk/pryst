package com.BScProject.truffle.jsl.nodes.controlflow;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

@NodeInfo(shortName = "if", description = "The node implementing a conditional statement")
public final class JSLIfNode extends JSLStatementNode {
	
	@Child private JSLExpressionNode conditionNode;
	@Child private JSLStatementNode ifPartNode;
	@Child private JSLStatementNode elsePartNode;
	
	private final ConditionProfile condition = ConditionProfile.createCountingProfile();

	public JSLIfNode(JSLExpressionNode conditionNode, JSLStatementNode ifPartNode, JSLStatementNode elsePartNode) {
		this.conditionNode = conditionNode;
		this.ifPartNode = ifPartNode;
		this.elsePartNode = elsePartNode;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		if(condition.profile(evaluateCondition(frame))) {
			ifPartNode.executeVoid(frame);
		} else {
			if (elsePartNode != null) {
				elsePartNode.executeVoid(frame);
			}
		}
	}
	
	private boolean evaluateCondition(VirtualFrame frame) {
		try {
			return conditionNode.executeBoolean(frame);
		} catch (UnexpectedResultException ex) {
			throw new UnsupportedSpecializationException(this, new Node[]{conditionNode}, ex.getResult());
		}
	}
}