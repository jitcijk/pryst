package com.BScProject.truffle.jsl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.BScProject.truffle.jsl.runtime.JSLUndefinedNameException;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.source.MissingNameException;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotEngine.Value;

/**
 * The main class of the programming language JeSSEL, which is a subset of Java, implemented in Truffle.
 * Static Strongly typed.
 * 
 * Types:
 * int (represented by a long internally 
 * long
 * float (represented by a double internally
 * double
 * boolean
 * Function
 * Object
 * Null => As an Singleton object
 * 
 * 
 * 
 * 
 * @author Bram at 25/04/2017
 */
public final class JSLMain {
	
	public static void main(String[] args) throws IOException, RuntimeException, MissingNameException {
		Source source;
		if (args.length == 0) {
			source = null;
			System.out.println("No file specified. Aborted");
			System.exit(0);
		} else {
			source = Source.newBuilder(new File(args[0])).mimeType(JSLLanguage.MIME_TYPE).build();
		}
		executeSource(source, System.in, System.out);
	}
	
	
	public static void executeSource(Source source, InputStream in, PrintStream out) {
		 out.println("=== Running on "+ Truffle.getRuntime().getName() +  " ===");
		 PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
		 assert engine.getLanguages().containsKey(JSLLanguage.MIME_TYPE);
		 
		 try {
			 Value result = engine.eval(source);			 
			 if (result == null) {
				 out.println("No main() function defined in the source file");
			 }
		 } catch (Throwable ex) {
			 Throwable cause = ex.getCause();
			 if (cause instanceof UnsupportedSpecializationException) {
				 //TODO Implement a nice formatting of the Exception here
				 out.println(cause.getMessage());
				 out.println("UnsupportedSpecializationException - Implement a nice formatting of the Exception here");
			 } else if ( cause instanceof JSLUndefinedNameException) {
				 out.println(cause.getMessage());
			 } else {
				 ex.printStackTrace();
			 }
		 }
		 engine.dispose();
	}
	
	
	
	
}