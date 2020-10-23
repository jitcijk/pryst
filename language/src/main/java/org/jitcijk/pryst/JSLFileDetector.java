package com.BScProject.truffle.jsl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

public final class JSLFileDetector extends FileTypeDetector {
	@Override
	public String probeContentType(Path path) throws IOException {
		if (path.getFileName().toString().endsWith(".jsl")) {
			return JSLLanguage.MIME_TYPE;
		}
		return null;
	}
}