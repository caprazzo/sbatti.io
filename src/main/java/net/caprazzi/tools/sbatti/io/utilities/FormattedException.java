package net.caprazzi.tools.sbatti.io.utilities;

import org.slf4j.helpers.MessageFormatter;

@SuppressWarnings("serial")
public class FormattedException extends Exception {
	
	public FormattedException(String format, Object... args) {
		super(MessageFormatter.format(format, args).getMessage());
	}

	public FormattedException(Exception e, String format, Object... args) {
		super(MessageFormatter.format(format, args).getMessage(), e);
	}
	
}
