package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeChild("valueNode")
@NodeInfo(shortName = "!")
public abstract class JSLLogicalNotNode extends JSLTypedExpressionNode {

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }
    
    public JSLType getType() {
    	return JSLType.BOOLEAN;
    }
}