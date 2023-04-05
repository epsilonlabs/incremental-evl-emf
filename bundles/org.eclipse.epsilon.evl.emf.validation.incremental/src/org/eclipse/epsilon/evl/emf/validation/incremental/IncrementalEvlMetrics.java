package org.eclipse.epsilon.evl.emf.validation.incremental;

public class IncrementalEvlMetrics {
	
	private static long initialRunTime = -1;
	private static String initialRunValidationID = "";

	private static long longestRunTime = 0;
	private static String longestRunValidationID = "";
	
	
	public static void reportValidationMetrics(long startTime, long endTime, String validationID) {
		String report = ("\n\n ==VALIDATION METRICS== ");
		long runtime = endTime - startTime;
		
		if (initialRunTime == -1) {
			initialRunTime = runtime;
			initialRunValidationID = validationID;
		}
		
		report = report.concat("\nInitial Validation: " 
				+ initialRunValidationID + " - " + initialRunTime + "ms");
		
		report = report.concat("\nLongest Validation: " 
				+ longestRunValidationID + " - " + longestRunTime + "ms");
		
		report = report.concat("\nLast Validation: " 
				+ validationID + " - " + runtime + "ms");

		report = report.concat("\n");
		
		System.out.println(report);
		
		if (runtime > longestRunTime) {
			longestRunTime = runtime;
			longestRunValidationID = validationID;
		}
		
		
		
	}

	
}
