package com.BScProject.truffle.jsl.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
public abstract class JSLBinaryNode extends JSLExpressionNode {
}