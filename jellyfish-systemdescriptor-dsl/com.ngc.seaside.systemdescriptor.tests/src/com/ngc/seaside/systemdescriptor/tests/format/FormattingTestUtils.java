package com.ngc.seaside.systemdescriptor.tests.format;

import java.util.Random;
import java.util.function.Function;

import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTestRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTester;

import com.google.inject.Inject;

public class FormattingTestUtils {

	/**
	 * Adds a single space character between tokens.
	 */
	public static final Function<String, String> ONE_LINE_DEFORMATTER = code -> code.trim().replaceAll("\\s+", " ");

	/**
	 * Adds arbitrary whitespace between tokens.
	 */
	public static final Function<String, String> RANDOM_SPACING_DEFORMATTER = code -> {
		Random random = new Random(code.length());
		StringBuilder builder = new StringBuilder(" \t\n\r \t\n");
		for(String token : code.split("\\s+")) {
			builder.append(token);
			for (int i = 0; i < 3; i++) {
				if (random.nextBoolean()) {
					builder.append(' ');
				}
				if (random.nextBoolean()) {
					builder.append('\t');
				}
				if (random.nextBoolean()) {
					builder.append('\n');
				}
				if (random.nextBoolean()) {
					builder.append('\r');
				}
			}
		};
		return builder.toString();
	};
	
	/**
	 * Does not change the format of the tokens.
	 */
	public static final Function<String, String> PASSTHROUGH_DEFORMATTER = Function.identity();
	
	@Inject
	private FormatterTester formatterTester;

	public void testFormatter(String correctFormat) {
		testFormat(correctFormat, ONE_LINE_DEFORMATTER, RANDOM_SPACING_DEFORMATTER, PASSTHROUGH_DEFORMATTER);
	}

	@SafeVarargs
	public final void testFormat(String correctFormat, Function<String, String>... deformatters) {
		for (Function<String, String> deformatter : deformatters) {
			String badFormat = deformatter.apply(correctFormat);
			FormatterTestRequest testRequest = new FormatterTestRequest();
			testRequest.setExpectation(correctFormat);
			testRequest.setToBeFormatted(badFormat);
			FormatterRequest request = new FormatterRequest();
			testRequest.setRequest(request);
			formatterTester.assertFormatted(testRequest);
		}
	}
	
}
