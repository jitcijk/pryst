package com.BScProject.truffle.jsl.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({@NodeChild("node")})
public abstract class JSLUnaryNode extends JSLExpressionNode {
	
}