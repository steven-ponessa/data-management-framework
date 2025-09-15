package com.ibm.wfm.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagramGenerator {

	public ClassDiagramGenerator() {

	}

	public static void main(String[] args) {

		String packageName = null; // Replace with your package name
		String classListString = null;
		String outputFilePath = null;
		boolean includePre = false;
		boolean validParams = true;

		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-p") || args[optind].equalsIgnoreCase("--package-name")) {
					packageName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-c") || args[optind].equalsIgnoreCase("--class-list")) {
					classListString = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-o") || args[optind].equalsIgnoreCase("--output-path")) {
					outputFilePath = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-pre") || args[optind].equalsIgnoreCase("--pre")) {
					includePre = true;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("--help")) {
					validParams = false;
				} else {
					System.out.println(String.format("Unknown parameter (%s) specfified.", args[optind]));
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			validParams = false;
		}
		if (packageName==null && classListString==null) {
			System.out.println("Either a valid package name or class list must specified.");
			validParams = false;
		}

		if (!validParams) {
			System.out.println("-- TO-DO : Add usage string.");
			System.exit(-99);
			return;
		}
		

		if (classListString!=null) {
			System.out.println(generateClassDiagram(classListString));
		}
		else {
			ClassDiagramGenerator cdg = new ClassDiagramGenerator();
	
			try {
				if (!cdg.generate(packageName, outputFilePath, includePre)) {
					System.out.println("Process failed at: " + new java.util.Date());
				}
				else
					System.out.println("Process completed at: " + new java.util.Date());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Process failed at: " + new java.util.Date());
				e.printStackTrace();
			}
		}
	}
	
    /**
     * Generates a Mermaid.js class diagram markdown for a list of classes.
     *
     * @param classes List of Java classes to include in the class diagram.
     * @return Mermaid.js class diagram in markdown format.
     */
    public static String generateClassDiagram(String classListString) {
		List<String> classNames = List.of(classListString.split(","));
        try {
            List<Class<?>> classes = getClassesFromNames(classNames);
            return ClassDiagramGenerator.generateClassDiagram(classes);

        } catch (ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
		return null;
    	
    }
	
    /**
     * Generates a Mermaid.js class diagram markdown for a list of classes.
     *
     * @param classes List of Java classes to include in the class diagram.
     * @return Mermaid.js class diagram in markdown format.
     */
    public static String generateClassDiagram(List<Class<?>> classes) {
        StringBuilder diagram = new StringBuilder("```mermaid\nclassDiagram\n");
        Map<String, List<Class<?>>> namespaceMap = new HashMap<>();
        List<String> relationships = new ArrayList<>();

        // Group classes by package into namespaces
        for (Class<?> clazz : classes) {
            String namespace = clazz.getPackageName();
            namespaceMap.computeIfAbsent(namespace, k -> new ArrayList<>()).add(clazz);
        }

        // Generate class definitions within namespaces
        for (Map.Entry<String, List<Class<?>>> entry : namespaceMap.entrySet()) {
            String namespace = entry.getKey(); //.replace(".", "::");
            diagram.append("namespace ").append(namespace).append(" {\n");

            for (Class<?> clazz : entry.getValue()) {
                diagram.append("  class ").append(clazz.getSimpleName()).append(" {\n");

                // Add fields
                for (Field field : clazz.getDeclaredFields()) {
                    String visibility = getVisibility(field.getModifiers());
                    diagram.append("    ").append(visibility)
                            .append(" ").append(field.getType().getSimpleName())
                            .append(" ").append(field.getName()).append("\n");
                }

                // Add methods
                for (Method method : clazz.getDeclaredMethods()) {
                    String visibility = getVisibility(method.getModifiers());
                    String returnType = method.getReturnType().getSimpleName();
                    String parameters = getMethodParameters(method);
                    diagram.append("    ").append(visibility)
                            .append(" ").append(returnType)
                            .append(" ").append(method.getName())
                            .append("(").append(parameters).append(")").append("\n");
                }

                diagram.append("  }\n");

                // Add parent class relationship
                Class<?> parent = clazz.getSuperclass();
                if (parent != null && !parent.equals(Object.class)) {
                    relationships.add(parent.getSimpleName() + " <|-- " + clazz.getSimpleName());
                }

                // Add implemented interfaces
                for (Class<?> iface : clazz.getInterfaces()) {
                    relationships.add(iface.getSimpleName() + " <|.. " + clazz.getSimpleName());
                }
            }

            diagram.append("}\n");
        }

        // Add relationships
        for (String relationship : relationships) {
            diagram.append(relationship).append("\n");
        }

        diagram.append("```\n");
        return diagram.toString();
    }

    /**
     * Maps Java modifiers to visibility symbols used in Mermaid.js.
     *
     * @param modifiers Modifier bitmask.
     * @return Visibility symbol as a string.
     */
    private static String getVisibility(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return "+";
        } else if (Modifier.isProtected(modifiers)) {
            return "#";
        } else if (Modifier.isPrivate(modifiers)) {
            return "-";
        } else {
            return "~";
        }
    }

    /**
     * Formats method parameters for inclusion in the Mermaid.js diagram.
     *
     * @param method Method to process.
     * @return A comma-separated string of parameter types.
     */
    private static String getMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters.append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                parameters.append(", ");
            }
        }
        return parameters.toString();
    }

    /**
     * Converts a list of fully qualified class names into a list of Class objects.
     *
     * @param classNames List of fully qualified class names.
     * @return List of Class objects corresponding to the provided class names.
     * @throws ClassNotFoundException if any class name is not found.
     */
    public static List<Class<?>> getClassesFromNames(List<String> classNames) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (String className : classNames) {
            classes.add(Class.forName(className));
        }
        return classes;
    }

	public boolean generate(String packageName) {
		try {
			return generate(packageName, null, false);
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public boolean generate(String packageName, String outputFilePath, boolean includePre) throws FileNotFoundException {
		String o = "&lt;"; //<
		String c = "&gt;"; //>
		if (outputFilePath != null) {
			// Create a FileOutputStream to write to the file
			FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

			// Create a PrintStream that writes to the file
			PrintStream printStream = new PrintStream(fileOutputStream);

			// Redirect System.out to the PrintStream
			System.setOut(printStream);
		}
		List<Class<?>> classes = ClassDiagramGenerator.getClassesInPackage(packageName, null);

		if (includePre) System.out.println("<pre class=\"mermaid\">");
		System.out.println("classDiagram");

		for (Class<?> clazz : classes) {
			
			if (clazz.getSimpleName().trim().length()==0) {
				continue;
			}
			System.out.println("class " + clazz.getSimpleName() + " {");

			if (clazz.isInterface())
				System.out.println(o+o+"interface"+c+c);
			if (Modifier.isAbstract(clazz.getModifiers()))
				System.out.println(o+o+"abstract"+c+c);

			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				int modifiers = field.getModifiers();
				String mod = "-"; // private
				if (java.lang.reflect.Modifier.isPublic(modifiers))
					mod = "+";
				else if (java.lang.reflect.Modifier.isProtected(modifiers))
					mod = "#";
				else if (java.lang.reflect.Modifier.isNative(modifiers))
					mod = "~";
				String mod2 = "";
				if (java.lang.reflect.Modifier.isStatic(modifiers))
					mod2 += "$";

				System.out.println("  " + mod + field.getType().getSimpleName() + " " + field.getName() + mod2);
			}

			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				int modifiers = method.getModifiers();
				String mod = "-"; // private
				if (java.lang.reflect.Modifier.isPublic(modifiers))
					mod = "+";
				else if (java.lang.reflect.Modifier.isProtected(modifiers))
					mod = "#";
				else if (java.lang.reflect.Modifier.isNative(modifiers))
					mod = "~";
				String mod2 = "";
				if (java.lang.reflect.Modifier.isStatic(modifiers))
					mod2 += "$";

				Class<?>[] parameterTypes = method.getParameterTypes();

				String parms = "";
				String del = "";

				for (Class<?> parameterType : parameterTypes) {
					parms += (del + parameterType.getName());
					del = ", ";
				}

				System.out.println("  " + mod + method.getReturnType().getSimpleName() + " " + method.getName() + "("
						+ parms + ")" + mod2);
			}
			
			if ((fields==null || fields.length==0) && (methods==null || methods.length==0)) {
				System.out.println("  -String placeHolder");
			}

			System.out.println("}");

			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !superClass.getSimpleName().equals("Object")) {
				if (clazz.getSimpleName() != null && clazz.getSimpleName().trim().length() > 0)
					System.out.println(clazz.getSimpleName() + " --|> "
							+ (superClass.getName().startsWith(packageName) ? superClass.getSimpleName()
									: superClass.getName().replace('.', '_'))
							+ ": inherits");
			}

			Class<?>[] interfaces = clazz.getInterfaces();

			if (interfaces.length > 0) {
				for (Class<?> interfaceClass : interfaces) {
					if (clazz.getSimpleName() != null && clazz.getSimpleName().trim().length() > 0)
						System.out.println(
								clazz.getSimpleName() + " --|> " + interfaceClass.getSimpleName() + ": implements");
				}
			}
		}

		if (includePre) System.out.println("</pre>");
		return true;
	}

	/**
	 * Recursively fetches a list of all the classes in a given directory (and
	 * sub-directories) that have the @UnitTestable annotation.
	 * 
	 * @param packageName The top level package to search.
	 * @param loader      The class loader to use. May be null; we'll just grab the
	 *                    current threads.
	 * @return The list of all @UnitTestable classes.
	 */
	public static List<Class<?>> getClassesInPackage(String packageName, ClassLoader loader) {
		// State what package we are exploring

		// Create the list that will hold the testable classes
		List<Class<?>> ret = new ArrayList<Class<?>>();
		// Create the list of immediately accessible directories
		List<File> directories = new ArrayList<File>();
		// If we don't have a class loader, get one.
		if (loader == null)
			loader = Thread.currentThread().getContextClassLoader();
		// Convert the package path to file path
		String path = packageName.replace('.', '/');
		// Try to get all of nested directories.
		try {
			// Get all of the resources for the given path
			Enumeration<URL> res = loader.getResources(path);
			// While we have directories to look at, recursively
			// get all their classes.
			while (res.hasMoreElements()) {
				// Get the file path the the directory
				String dirPath = URLDecoder.decode(res.nextElement().getPath(), "UTF-8");

				// Make a file handler for easy managing
				File dir = new File(dirPath);
				// Check every file in the directory, if it's a
				// directory, recursively add its viable files
				for (File file : dir.listFiles()) {
					if (file.isDirectory())
						ret.addAll(getClassesInPackage(packageName + '.' + file.getName(), loader));
				}
			}
		} catch (IOException e) {
			// We failed to get any nested directories. State
			// so and continue; this directory may still have
			// some UnitTestable classes.
			System.out.println("Failed to load resources for [" + packageName + ']');
		}
		// We need access to our directory, so we can pull
		// all the classes.
		URL tmp = loader.getResource(path);

		if (tmp == null)
			return ret;
		File currDir = new File(tmp.getPath());
		// Now we iterate through all of the classes we find
		for (String classFile : currDir.list()) {
			// Ensure that we only find class files; can't load gif's!
			if (classFile.endsWith(".class")) {
				// Attempt to load the class or state the issue
				try {
					// Try loading the class
					Class<?> add = Class.forName(packageName + '.' + classFile.substring(0, classFile.length() - 6));
					// If the class has the correct annotation, add it
					ret.add(add);
				} catch (NoClassDefFoundError e) {
					// The class loader could not load the class
					System.out.println("We have found class [" + classFile + "], and couldn't load it.");
				} catch (ClassNotFoundException e) {
					// We couldn't even find the damn class
					System.out.println("We could not find class [" + classFile + ']');
				}
			}
		}
		return ret;
	}

}