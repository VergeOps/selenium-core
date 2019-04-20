package com.automation.selenium.core.assertions;

/**
 * 
 * Assertion tool class. A class to handle soft assertions
 * @author Amanda Adams
 *
 */
public class SoftAssert {
	
	
	private org.testng.asserts.SoftAssert softAssert;
	
	/**
	 * Constructor with a SoftAssert instance passed through parameter
	 * @param softAssert Instance of SoftAssert
	 */
	public SoftAssert(org.testng.asserts.SoftAssert softAssert) {
	    this.softAssert = softAssert;
	}
	
	/**
	 * Asserts that two strings are equal, it can be case sensitive or not. If they are not, an assertion is thrown
	 * 
	 * @param actual the actual value to validate
	 * @param expected the expected value to do the validation
	 * @param message the message for the assertion 
	 * @param ignoreCase the flag for the case sensitive
	 */
	public void assertEquals(String actual, String expected, String message, Boolean ignoreCase) {

		if (expected == null) {
			expected = "";
		} else if (ignoreCase) {
			expected = expected.toLowerCase();
		}

		if (actual == null) {
			actual = "";
		} else if (ignoreCase) {
			actual = actual.toLowerCase();
		}
		softAssert.assertEquals(actual.trim(), expected.trim(), message);
	}

	/**
	 * Asserts that two strings are equal, case sensitive. If they are not, an assertion is thrown
	 * 
	 * @param actual the actual value to validate
	 * @param expected the expected value to do the validation
	 * @param message the message for the assertion
	 */
	public void assertEquals(String actual, String expected, String message) {
		assertEquals(actual, expected, message, false);

	}

	/**
	 * Asserts that two strings are not equal, case sensitive. If they are, an assertion is thrown
	 * 
	 * @param one a string to compare
	 * @param two a string to compare
	 * @param message the message for the assertion
	 */
	public void assertNotEquals(String one, String two, String message) {

		if (one == null) {
			one = "";
		}

		if (two == null) {
			two = "";
		}

		softAssert.assertNotEquals(one.trim(), two.trim(), message);

	}

	/**
	 * Assert that an object is not null, if it is, an assertion is thrown
	 * 
	 * @param value the object to do the validation
	 * @param message the message for the assertion 
	 */
	public void assertNotNull(Object value, String message) {

		softAssert.assertNotNull(value, message);

	}

	/**
	 * Assert that an object is null, if it is not, an assertion is thrown
	 * 
	 * @param value the object to do the validation
	 * @param message the message for the assertion
	 */
	public void assertNull(Object value, String message) {

		softAssert.assertNull(value, message);

	}

	/**
	 * Assert that an object is true, if it is not, an assertion is thrown
	 * 
	 * @param value the boolean object to do the validation
	 * @param expmessage the message for the assertion
	 */
	public void assertTrue(Boolean value, String expmessage) {

		softAssert.assertTrue(value, expmessage);

	}

	/**
	 * Assert that an object is false, if it is not, an assertion is thrown
	 * 
	 * @param value the boolean object to do the validation
	 * @param message the message for the assertion
	 */
	public void assertFalse(Boolean value, String message) {

		softAssert.assertFalse(value, message);

	}

	/**
	 * Assert that a String contains another String, if the string is not found, an assertion is thrown
	 * 
	 * @param container the container string used for search
	 * @param textToMatch the string to search for
	 * @param message the message for the assertion
	 */
	public void assertContains(String container, String textToMatch, String message) {

		assertContains(container, textToMatch, message, false);

	}

	/**
	 * Assert that a String contains another String, it can be case sensitive, if the string is not found, an assertion is thrown
	 * 
	 * @param container the container string used for the search
	 * @param textToMatch the string to search for
	 * @param message the message for the assertion
	 * @param ignoreCase the flag to make case sensitive the search
	 */
	public void assertContains(String container, String textToMatch, String message, Boolean ignoreCase) {

		message = message + " Actual Text: " + container + " Text to match: " + textToMatch;

		if (ignoreCase) {
			container = container.toLowerCase();
			textToMatch = textToMatch.toLowerCase();
		}

		softAssert.assertTrue(container.contains(textToMatch), message);

	}
	
	/**
	 * Method to call the assertAll
	 *  @see org.testng.asserts.SoftAssert#assertAll()
	 */
	public void assertAll() {
		softAssert.assertAll();
	}

}