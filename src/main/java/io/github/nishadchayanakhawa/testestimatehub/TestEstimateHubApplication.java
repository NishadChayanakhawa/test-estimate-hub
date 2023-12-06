package io.github.nishadchayanakhawa.testestimatehub;

// Import section
// spring libraries
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <b>Class Name</b>: TestEstimateHubApplication<br>
 * <b>Description</b>: Entry point for the Test Estimate Hub application.<br>
 * This class is annotated with {@code @SpringBootApplication}, indicating that
 * it is the main configuration class and enabling Spring Boot
 * auto-configuration.<br>
 * It contains the main method to start the application.
 * <p>
 * <b>Author:</b> nishad.chayanakhawa
 * </p>
 */
@SpringBootApplication
public class TestEstimateHubApplication {

	/**
	 * <b>Method Name</b>: main<br>
	 * <b>Description</b>: Main method to start the Test Estimate Hub
	 * application.<br>
	 * It launches the Spring Boot application by calling
	 * {@link SpringApplication#run(Class, String...)}.
	 * 
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Run the Spring Boot application
		SpringApplication.run(TestEstimateHubApplication.class, args);
	}
}
