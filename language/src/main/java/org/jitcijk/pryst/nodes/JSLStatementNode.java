package com.BScProject.truffle.jsl.nodes;

import java.io.File;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.Instrumentable;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(language = "JSL", description = "The abstract base node for all JSL statements")
@Instrumentable(factory = JSLStatementNodeWrapper.class)
public abstract class JSLStatementNode extends Node {
	
	private SourceSection sourceSection;
	
	private boolean hasStatementTag;
	private boolean hasRootTag;
	
	@Override
	public final SourceSection getSourceSection() {
		return sourceSection;
	}
	
	public void setSourceSection(SourceSection sourceSection) {
		assert this.sourceSection != null : "overwriting exisiting SourceSection";
		this.sourceSection = sourceSection;
	}
	
	// Every StatementNode should have a no-return execute function
	public abstract void executeVoid(VirtualFrame frame);
	
	
	//Marks node as Statement for instrumention purposes
	public final void addStatementTag() {
		hasStatementTag = true;
	}
	
	public final void addRootTag() {
		hasRootTag = true;
	}
	
	@Override
	protected boolean isTaggedWith(Class<?> tag) {
		if (tag == StandardTags.StatementTag.class) {
			return hasStatementTag;
		} else if (tag == StandardTags.RootTag.class) {
			return hasRootTag;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return formatSourceSection(this);
	}
	
	public static String formatSourceSection(Node node) {
		if (node == null) {
			return "<unknown>";
		}
		SourceSection section = node.getSourceSection();
		boolean estimated = false;
		if (section == null) {
			section = node.getEncapsulatingSourceSection();
			estimated = true;
		} 
		
		if (section == null || section.getSource() == null) {
			return "<unknown source>";
		} else {
			String sourceName = new File(section.getSource().getName()).getName();
			int startLine = section.getStartLine();
			return String.format("%s:%d%s", sourceName, startLine, estimated ? "~" : "");
		}
	}
}