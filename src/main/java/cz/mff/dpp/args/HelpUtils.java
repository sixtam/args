package cz.mff.dpp.args;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * Collection of utility functions to help with usage of the library.
 * 
 * 
 * @author Martin Sixta
 * @author Stepan Bokoc
 * 
 */
class HelpUtils {

	/**
	 * 
	 * Prints help to the standard output for annotated objects.
	 * 
	 * 
	 * @param introspector
	 *            annotated objects
	 */
	public static void printHelp(final Introspector introspector) {

		System.out.println("Usage: OPTIONS");
		for (Option option : introspector.getDeclaredOptions()) {
			printHelpForOption(option, introspector.optionToAccesible(option));
		}

		System.out.println("Usage: ARGUMENTS");
		for (Entry<AccessibleObject, Argument> entry : introspector
				.getArguments()) {
			printHelpForArgument(entry.getValue(), entry.getKey());
		}

	}

	/**
	 * 
	 * Prints help for an annotated object.
	 * 
	 * @param option
	 *            annotation for the accessible object
	 * @param accessible
	 *            annotated object to print help for
	 */
	private static void printHelpForOption(Option option,
			AccessibleObject accessible) {

		System.out.printf("\t%s ", option.name());
		for (String alias : option.aliases()) {
			System.out.printf(", %s ", alias);
		}

		if (ReflectUtils.isFlagType(accessible)) {
			System.out.printf("[flag]");
		} else if (ReflectUtils.isSimpleType(accessible)) {
			System.out
					.printf("[%s]", ReflectUtils.getValueTypeName(accessible));
		} else if (ReflectUtils.isArrayType(accessible)) {
			System.out.printf("[array of %s]",
					ReflectUtils.getValueTypeName(accessible));
		}

		if (option.required()) {
			System.out.printf(" REQUIRED ");
		}

		if (ReflectUtils.isEnumType(accessible)) {
			System.out.printf("\n\t\tAllowed values: %s",
					ReflectUtils.getEnumConstants(accessible));
		} 

		System.out.printf("\n\t\t %s \n", option.description());

		if (option.incompatible().length > 0) {
			System.out.printf("\n\t\t NOTE: Cannot be used together with: ");
			for (String incompatilbe : option.incompatible()) {
				System.out.printf("%s ", incompatilbe);

			}
		}

		if (option.mustUseWith().length > 0) {
			System.out.printf("\n\t\t NOTE: Used together with: ");
			for (String with : option.mustUseWith()) {
				System.out.printf("%s ", with);

			}
		}

		printConstraint(accessible);
		
		System.out.println();

	}

	private static void printHelpForArgument(Argument argument,
			AccessibleObject accessible) {

		if (!argument.name().isEmpty()) {
			System.out.printf("\t%s ", argument.name());
		} else {
			System.out.printf("\t%s ", "ARGUMENT");
		}

		if (ReflectUtils.isArrayType(accessible)) {
			System.out.printf("[array of %s] ",
					ReflectUtils.getValueTypeName(accessible));
			
			if (argument.size() > 0 ) {
				System.out.printf("(index: %d, size: %d)", argument.index(), argument.size() );
			} else {
				System.out.printf("(all from index %d)", argument.index());
			}
		} else {
			System.out
					.printf("[%s] ", ReflectUtils.getValueTypeName(accessible));
			System.out.printf("(index: %d)", argument.index() );
		}

		if (argument.required()) {
			System.out.printf(" REQUIRED ");
		}

		System.out.printf("\n\t\t %s \n", argument.description());
		
		printConstraint(accessible);
		System.out.println();

	}
	
	private static void printConstraint(AccessibleObject accessible) {
		Constraint constraint = ReflectUtils.getConstraint(accessible);
		
		if (constraint == null) {
			return;
		}
		boolean some = false;
		
		System.out.printf("\t\tconstraints: ");
		
		if (!constraint.min().isEmpty()) {
			System.out.printf("min=%s ",constraint.min()); 
			some = true;
		}
		
		if (!constraint.max().isEmpty()) {
			System.out.printf("max=%s ",constraint.max()); 
			some = true;
		}
		
		if (constraint.allowedValues().length > 0) {
			System.out.printf("allowed values=%s ",Arrays.toString(constraint.allowedValues())); 
			System.out.printf("(ignore case=%s) ",constraint.ignoreCase());
			some = true;
		}
		
		if (!constraint.regexp().isEmpty()) {
			System.out.printf("regexp=%s ",constraint.regexp());
			some = true;
		}
		
		if (!some) {
			System.out.println("none");
		} else {
			System.out.println("");
		}
		
	}

}
