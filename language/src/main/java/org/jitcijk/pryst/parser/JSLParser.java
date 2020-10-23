package com.BScProject.truffle.jsl.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.BScProject.truffle.jsl.nodes.JSLRootNode;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.oracle.truffle.api.source.Source;

public class JSLParser {
	private Source source;
	
	
	public JSLParser(Source source) {
		this.source = source;
	}
	
	//Parse the this.source.
	public Map<String, JSLRootNode> parse() {
		try {
			FileInputStream in = new FileInputStream(source.getPath());
			
			return parseJSL(in, this.source);
			
		} catch (FileNotFoundException ex) {
			System.err.println("Could not find file" + source.getPath());
		}
		return null;
	}

	// Static function for calls from outside.
	public static Map<String, JSLRootNode> parse(Source localSource) {
		try {
			FileInputStream in = new FileInputStream(localSource.getPath());
			
			return parseJSL(in, localSource);
			
		} catch (FileNotFoundException ex) {
			System.err.println("Could not find file" + localSource.getPath());
		}
		return null;
	}
	
	private static Map<String, JSLRootNode> parseJSL(FileInputStream in, Source source) {
		CompilationUnit cu = JavaParser.parse(in);
		TreeTranslatorVisitor visitor = new TreeTranslatorVisitor(source);
		cu.accept(visitor, null);
		
		return visitor.getFunctions();
	}
	
}