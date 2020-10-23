package com.BScProject.truffle.jsl.nodes.controlflow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "block", description = "A statementblock that executes a list of other statements")
public final class JSLBlockNode extends JSLStatementNode {
	@Children private final JSLStatementNode[] bodyNodes;
	
	public JSLBlockNode(JSLStatementNode[] bodyNodes) {
		this.bodyNodes = bodyNodes;
	}

	// Since we know the number of bodyNodes at compilation, we can explode this loop
	@ExplodeLoop
	@Override
	public void executeVoid(VirtualFrame frame) {
		for(JSLStatementNode statement : bodyNodes) {
			statement.executeVoid(frame);
		}
	}
	
	public List<JSLStatementNode> getStatements() {
		return Collections.unmodifiableList(Arrays.asList(bodyNodes));
	}
	
}