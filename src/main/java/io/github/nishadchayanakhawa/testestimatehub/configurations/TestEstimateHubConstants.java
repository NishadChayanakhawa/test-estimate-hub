package io.github.nishadchayanakhawa.testestimatehub.configurations;

public class TestEstimateHubConstants {
	private TestEstimateHubConstants() {
		
	}
	private static final String API="/api";
	public static final String CONFIGURATION_API=API + "/configuration";
	
	public static final String USER_MANAGEMENT_API=CONFIGURATION_API + "/user";
	public static final String TEST_TYPE_CONFIGURATION_API=CONFIGURATION_API + "/testType";
	public static final String CHANGE_TYPE_CONFIGURATION_API=CONFIGURATION_API + "/changeType";
	public static final String GENERAL_CONFIGURATION_API=CONFIGURATION_API + "/general";
	public static final String APPLICATION_CONFIGURATION_API= CONFIGURATION_API + "/application";
	
	public static final String RELEASE_MANAGEMENT_API= API + "/release";
	public static final String CHANGE_MANAGEMENT_API= API + "/change";
	
	public static final String SERVING_REQUEST_DEBUG_MESSAGE="Serving {} request for {}";
	public static final String SERVING_GET_REQUEST_DEBUG_MESSAGE="Serving {} request for {}/{}";
	
	public static final String NON_BLANK_MESSAGE="cannot be blank";
	public static final String NON_EMPTY_MESSAGE="should have at least one value";
	public static final String MINIMUM_VALUE_MESSAGE="cannot be lower than";
	public static final String MAXIMUM_VALUE_MESSAGE="cannot be more than";
	
	public static String getDuplicateEntityExceptionErrorMessage(String entity, String field, String value) {
		return String.format("%s '%s' already exists for another %s", field, value, entity);
	}
}