package com.ibm.wfm.utils;



import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ibm.wfm.beans.AttributeStatistic;
import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.ServiceLineDim;

/**
 * @author ponessa
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Helpers {
	private static final String COMMA = ",";
	private static final String DEFAULT_SEPARATOR = COMMA;
	private static final String DOUBLE_QUOTES = "\"";
	private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
	private static final String NEW_LINE_UNIX = "\n";
	private static final String NEW_LINE_WINDOWS = "\r\n";

	public static void main(String[] args) {
		/*
		int[] values = null;
		String[] input = {"all","none",null,"0,2,4,6,8", "0:8","0:8,10,12,14:20"}; //,"1,2,4,12:8"};
		for (int i=0; i<input.length; i++) {
			values = Helpers.parseIntegerList(input[i]);
			System.out.println(i+". "+input[i]+" : "+(values==null?values:Arrays.toString(values)));
		}

		System.out.println("Process started at: " + new java.util.Date());
		String json = "[\n"
				+ "  {\n"
				+ "    \"code\": \"10J00   \",\n"
				+ "    \"fullKey\": \"10J00\",\n"
				+ "    \"description\": \"IBM Consulting\",\n"
				+ "    \"level\": 0,\n"
				+ "    \"children\": [\n"
				+ "      {\n"
				+ "        \"code\": \"15BS    \",\n"
				+ "        \"fullKey\": \"10J00:15BS\",\n"
				+ "        \"description\": \"Business Support\",\n"
				+ "        \"level\": 1,\n"
				+ "        \"children\": null,\n"
				+ "        \"growthPlatformId\": 10000,\n"
				+ "        \"growthPlatformCd\": \"15BS    \",\n"
				+ "        \"growthPlatformNm\": \"Business Support\",\n"
				+ "        \"growthPlatformDesc\": \"Business Support\",\n"
				+ "        \"brandCd\": \"10J00   \",\n"
				+ "        \"effTms\": null,\n"
				+ "        \"expirTms\": null,\n"
				+ "        \"rowStatusCd\": null\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"code\": \"15CAI   \",\n"
				+ "        \"fullKey\": \"10J00:15CAI\",\n"
				+ "        \"description\": \"Hybrid Cloud Services\",\n"
				+ "        \"level\": 1,\n"
				+ "        \"children\": null,\n"
				+ "        \"growthPlatformId\": 1,\n"
				+ "        \"growthPlatformCd\": \"15CAI   \",\n"
				+ "        \"growthPlatformNm\": \"Hybrid Cloud Services\",\n"
				+ "        \"growthPlatformDesc\": \"Hybrid Cloud Services\",\n"
				+ "        \"brandCd\": \"10J00   \",\n"
				+ "        \"effTms\": null,\n"
				+ "        \"expirTms\": null,\n"
				+ "        \"rowStatusCd\": null\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"code\": \"15CPT   \",\n"
				+ "        \"fullKey\": \"10J00:15CPT\",\n"
				+ "        \"description\": \"Business Transformation Services\",\n"
				+ "        \"level\": 1,\n"
				+ "        \"children\": null,\n"
				+ "        \"growthPlatformId\": 3,\n"
				+ "        \"growthPlatformCd\": \"15CPT   \",\n"
				+ "        \"growthPlatformNm\": \"Business Transformation Services\",\n"
				+ "        \"growthPlatformDesc\": \"Business Transformation Services\",\n"
				+ "        \"brandCd\": \"10J00   \",\n"
				+ "        \"effTms\": null,\n"
				+ "        \"expirTms\": null,\n"
				+ "        \"rowStatusCd\": null\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"code\": \"15JIA   \",\n"
				+ "        \"fullKey\": \"10J00:15JIA\",\n"
				+ "        \"description\": \"Promontory\",\n"
				+ "        \"level\": 1,\n"
				+ "        \"children\": null,\n"
				+ "        \"growthPlatformId\": 2,\n"
				+ "        \"growthPlatformCd\": \"15JIA   \",\n"
				+ "        \"growthPlatformNm\": \"Promontory\",\n"
				+ "        \"growthPlatformDesc\": \"Promontory\",\n"
				+ "        \"brandCd\": \"10J00   \",\n"
				+ "        \"effTms\": null,\n"
				+ "        \"expirTms\": null,\n"
				+ "        \"rowStatusCd\": null\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"code\": \"15TSL   \",\n"
				+ "        \"fullKey\": \"10J00:15TSL\",\n"
				+ "        \"description\": \"IBM Consulting Top Service Line (AUO)\",\n"
				+ "        \"level\": 1,\n"
				+ "        \"children\": null,\n"
				+ "        \"growthPlatformId\": 21,\n"
				+ "        \"growthPlatformCd\": \"15TSL   \",\n"
				+ "        \"growthPlatformNm\": \"IBM Consulting Top Service Line (AUO)\",\n"
				+ "        \"growthPlatformDesc\": \"IBM Consulting Top Service Line (AUO)\",\n"
				+ "        \"brandCd\": \"10J00   \",\n"
				+ "        \"effTms\": null,\n"
				+ "        \"expirTms\": null,\n"
				+ "        \"rowStatusCd\": null\n"
				+ "      }\n"
				+ "    ],\n"
				+ "    \"brandId\": 21,\n"
				+ "    \"brandCd\": \"10J00   \",\n"
				+ "    \"brandNm\": \"IBM Consulting\",\n"
				+ "    \"brandDesc\": \"IBM Consulting\",\n"
				+ "    \"effTms\": null,\n"
				+ "    \"expirTms\": null,\n"
				+ "    \"rowStatusCd\": null\n"
				+ "  }\n"
				+ "]";
		
		try {
			JsonNode jsonTree = new ObjectMapper().readTree(json);
			
			com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
			JsonNode firstObject = jsonTree.elements().next();
			firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
			CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
			
			String returnFileName = "test.csv";
			CsvMapper csvMapper = new CsvMapper();

			csvMapper.writerFor(JsonNode.class)
			  .with(csvSchema)
			  .writeValue(new File("/Users/steve/temp/" + returnFileName), jsonTree);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<BrandDim> brands = new ArrayList<>();
		brands.add(new BrandDim("10J00", "GBS", "GBGBBGDFDFDfdfdfdfdFDFDFD"));
		brands.add(new BrandDim("10J", "GBS", "GBGBBGDFDFDFD"));
		brands.add(new BrandDim(null, "G", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("10J", "GBS", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("10J", "GBS", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("GBGBBGDFDFDFDFDFD", "GBS", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("....*....1....*....2....*", "GBS", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("10J", "GBSdddddd dfdfdfdfdfd", "GBGBBGDFDFDFDFDFD"));
		brands.add(new BrandDim("10J", "GBS", "GBGBBGDFDFDFDFDFD"));
		
		try {
			List<AttributeStatistic> attributeStatistics =  getStatsForList(brands, BrandDim.class);
			for (AttributeStatistic as: attributeStatistics) {
				System.out.println(as.toString());
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Process completed at: " + new java.util.Date());

		String[] dimensions = {"brand","growthPlatform","serviceLine","offeringPortfolio","offering","offeringComponent"};
		for (String dimension: dimensions) {
			String dataFileName = dimension+"Dim-new.csv";
			String urlName = fromCamelCase(dimension, "-")+"s";
			System.out.println("dataFileName="+dataFileName+", urlName="+urlName);
		}
		
		for (int i=0; i<100; i++) {
			System.out.println(i+"="+Helpers.getAlphabeticPosition(i));
		}
		*/
		
		String jdbcUrl = "jdbc:db2://{host}:{port}/{database}:user={userid};password={password};sslConnection=true;sslTrustStoreLocation={sslTrustStoreLocationIHub};port-again={port}";
		
		System.out.println(jdbcUrl);
		jdbcUrl = Helpers.replaceVariables(jdbcUrl);
		System.out.println(jdbcUrl);

		
	}
	
	public static String sayHello(String text) {
		return "Hello "+text;
	}
	
    @SuppressWarnings("unchecked")
    public static <T> List<T> castList(List<?> originalList, Class<T> targetType) {
        List<T> castedList = new ArrayList<>();
        for (Object obj : originalList) {
            if (targetType.isInstance(obj)) {
                castedList.add((T) obj);
            } else {
                // Handle or ignore elements that cannot be cast
            }
        }
        return castedList;
    }

    public static String getAlphabeticPosition(int num) {
        StringBuilder result = new StringBuilder();

        while (num >= 0) {
            int remainder = num % 26;
            char ch = (char) ('A' + remainder);
            result.insert(0, ch);
            num = (num - remainder) / 26 - 1;
        }

        return result.toString();
    }
	
    public static String getRandomNonce(int length) {
        StringBuffer buffer = new StringBuffer(
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    
	public static String readWfmDmfUserCookie(
			@CookieValue(name = "wfm-dmf-user", defaultValue = "none") String wfmDmfUserCookie) {
		return wfmDmfUserCookie;
	}

	// public Optional<String> readServletCookie(HttpServletRequest request, String
	// name){
	public static String readServletCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (name.equalsIgnoreCase(cookie.getName()))
				return cookie.getValue();
		}
		return null;
		/*
		 * return Arrays.stream(request.getCookies())
		 * .filter(cookie->name.equals(cookie.getName())) .map(Cookie::getValue)
		 * .findAny();
		 */
	}

	public static String getBasePath(HttpServletRequest request) {
		return ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
	}
	
	public static <T> List<AttributeStatistic> getStatsForList(List<T> objectList, Class zClass) throws IntrospectionException {
		List<AttributeStatistic> attributeStatistics = null; //new ArrayList<>();
		for (Field field : zClass.getDeclaredFields()) {
			if (attributeStatistics==null) attributeStatistics = new ArrayList<>();
			AttributeStatistic attributeStatistic = new AttributeStatistic(field.getName());
			attributeStatistic.setMaxLength(getMaxAttributeLength(objectList, zClass, field.getName()));
			attributeStatistic.setMinLength(getMinAttributeLength(objectList, zClass, field.getName()));
			attributeStatistics.add(attributeStatistic);
		}
		return attributeStatistics;
	}
	
	public static <T> int getMaxAttributeLength(List<T> objectList, Class zClass, String attributeName) throws IntrospectionException {
		Optional<Method> readMethod = getMethodForField(zClass, attributeName);
	
		int maxAttributeLength=-1;
		if (readMethod.isPresent()) {
			OptionalInt i = objectList.stream()
		    .mapToInt(data -> getFieldLength(data, readMethod.get()))
		    .max();
			
			if (i.isPresent()) maxAttributeLength = i.getAsInt();
		}
		return maxAttributeLength;
	}
	
	public static <T> int getMinAttributeLength(List<T> objectList, Class zClass, String attributeName) throws IntrospectionException {
		Optional<Method> readMethod = getMethodForField(zClass, attributeName);
	
		int minAttributeLength=-1;
		if (readMethod.isPresent()) {
			OptionalInt i = objectList.stream()
		    .mapToInt(data -> getFieldLength(data, readMethod.get()))
		    .min();
			
			if (i.isPresent()) minAttributeLength = i.getAsInt();
		}
		return minAttributeLength;
	}
	
	public static Optional<Method> getMethodForField(Class clazz, String fieldName) throws IntrospectionException {
		return Arrays.stream(Introspector.getBeanInfo(clazz).getPropertyDescriptors())
		  .filter(propertyDescriptor -> propertyDescriptor.getName().equalsIgnoreCase(fieldName))
		  .findFirst()
		  .map(PropertyDescriptor::getReadMethod);
	}
	
	public static int getFieldLength(Object staff, Method readMethod)  {
		try {
		  return ((String) readMethod.invoke(staff)).length();
		} catch(Exception e){ 
			return -1;
		}
	}
	
	
	public static String matchColum(List<String> selectColumnList, String parameter) {
		for (String col: selectColumnList) {
			if (parameter.contains(".")) {
				if (col.equals(parameter.toUpperCase().replace("-", "_"))) return col;
			}
			else if (col.endsWith(parameter.toUpperCase().replace("-", "_"))) return col;
		}
		return null;
	}
	
	public static String replaceContentInBrackets(String source, String replaceTarget, String replaceContent) {
		//return source.replaceAll("\\{.*?\\}", replaceContent);
		//return source.replaceAll("\\{.^"+replaceTarget+"$?\\}", replaceContent);
		//System.out.println(source.replaceAll("\\{^growth-platform-cd$\\}", replaceContent));
		System.out.println(source.replaceAll("\\{"+replaceTarget+"\\}", replaceContent));
		System.out.println(source);
		String sql = source;
		sql = sql.replaceAll("\\{"+replaceTarget+"\\}", replaceContent);
		System.out.println(sql);
		return source.replaceAll("\\{"+replaceTarget+"\\}", replaceContent);
	}
	
    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }
	
	public static String getWhereCondition(String clause, String columnName) {
		return getWhereCondition(clause, columnName, false);
	}
	
	public static String getWhereCondition(String clause, String columnName, boolean isNumeric) {
		String encloseStr = isNumeric?"":"'";
		String predicate = columnName;
		if (clause.contains(",")) {
			predicate+=" IN (";
			String[] conditions = clause.split(",");
			for (int i=0; i<conditions.length; i++) {
				String condition = conditions[i];
				predicate+=(i>0?",":"")+encloseStr+condition.trim()+encloseStr;
			}
			predicate+=")";
			return predicate;
		}
		if (clause.contains("*") || clause.contains("%")) {
			predicate+=" LIKE "+encloseStr+clause.replace('*', '%')+encloseStr;
			return predicate;
		}
		return predicate+"="+encloseStr+clause.trim()+encloseStr;
	}
	public static String getLastChararacters(String s, int numChars) {
		return s.substring(Math.max(s.length() - numChars, 0));
	}
	
	public static String buildStringFromOffsets(String[] columns, int[] offsets) {
		StringBuffer output = new StringBuffer();
		for (int offset: offsets) {
			if (offset<columns.length)
				output.append(",").append(Helpers.formatCsvField(columns[offset]));
		}
		return output.toString();		
	}
	
	public static int[] parseIntegerList(String keyStr) {
		
		if (keyStr==null || keyStr.equalsIgnoreCase("none")) return null;
		if (keyStr.equalsIgnoreCase("all")) {
			int[] keyOffsets = new int[1];
			keyOffsets[0]=-1;
			return keyOffsets;
		}

		String[] keysString = keyStr.split(",");
		int keysStringLength = keysString.length;
		for (int i = 0; i < keysString.length; i++) {
			String minMax[] = keysString[i].split(":");
			if (minMax.length == 2) {
				keysStringLength = keysStringLength + Integer.parseInt(minMax[1]) - Integer.parseInt(minMax[0]);
			}
		}

		int[] keyOffsets = new int[keysStringLength];
		int offset = 0;
		for (int i = 0; i < keysString.length; i++) {
			String minMax[] = keysString[i].split(":");
			if (minMax.length == 2) {
				int min = Integer.parseInt(minMax[0]);
				int max = Integer.parseInt(minMax[1]);
				for (int o = min; o <= max; o++) {
					keyOffsets[offset++] = o;
				}
			} else {
				keyOffsets[offset++] = Integer.parseInt(keysString[i]);
			}
		}
		return keyOffsets;
	}
	
	public static boolean isSwitchOn(int switchValue, int value) {
		return ((value & switchValue) == switchValue) ;
	}

	public static void zipFile(String sourceFileName, String compressedFileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(compressedFileName);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		File fileToZip = new File(sourceFileName);
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		zipOut.close();
		fis.close();
		fos.close();
	}
	
    public static void zipDirecory(String sourceFileName, String compressedFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(compressedFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFileName);

        zipFileRecurssive(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }	
    
    private static void zipFileRecurssive(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
            	zipFileRecurssive(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void zipMultipleFiles(List<String> srcFiles, String compressedFileName) throws IOException {
    	zipMultipleFiles(null, srcFiles, compressedFileName);
    	return;
    }
    
	public static void zipMultipleFiles(String sourceDir, List<String> srcFiles, String compressedFileName) throws IOException {
		FileOutputStream fos = new FileOutputStream((sourceDir==null?"":sourceDir+"/")+compressedFileName);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (String srcFile : srcFiles) {
			File fileToZip = new File((sourceDir==null?"":sourceDir+"/")+srcFile);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();
	}

	public static boolean isPrimitive(Class<?> type) {
		return (type == int.class || type == long.class || type == double.class || type == float.class
				|| type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}

	public static Class<?> boxPrimitiveClass(Class<?> type) {
		if (type == int.class) {
			return Integer.class;
		} else if (type == long.class) {
			return Long.class;
		} else if (type == double.class) {
			return Double.class;
		} else if (type == float.class) {
			return Float.class;
		} else if (type == boolean.class) {
			return Boolean.class;
		} else if (type == byte.class) {
			return Byte.class;
		} else if (type == char.class) {
			return Character.class;
		} else if (type == short.class) {
			return Short.class;
		} else {
			String string = "class '" + type.getName() + "' is not a primitive";
			throw new IllegalArgumentException(string);
		}
	}

	public static String toCsvLine(String[] fields) {
		StringBuffer outSb = new StringBuffer();
		int i = 0;
		for (String field : fields) {
			outSb.append(i++ > 0 ? "," : "").append(formatCsvField(field));
		}
		return outSb.toString();
	}

	public static String formatCsvField(String field) {

		if (field == null)
			return field;
		
		if (field.equalsIgnoreCase("null"))
			return "";

		String result = field;

		if (result.contains(DEFAULT_SEPARATOR) || result.contains(DOUBLE_QUOTES) || result.contains(NEW_LINE_UNIX)
				|| result.contains(NEW_LINE_WINDOWS) || result.startsWith("0")) {

			// if field contains double quotes, replace it with two double quotes \"\"
			result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

			// must wrap by or enclosed with double quotes
			result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

		}
		return result;

	}

	public static String convertObjectAttributeToColumnName(String attributeName) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < attributeName.length(); i++) {
			Character ch = attributeName.charAt(i);

			if (Character.isLowerCase(ch))
				out.append(ch);
			else
				out.append("_").append(ch);

		} // end for
		return out.toString().toUpperCase();
	}

	public static ArrayList<String> patternMatcher(String s, String pattern) {
		ArrayList<String> matchList = null;
		Pattern p = Pattern.compile(pattern);
		Matcher m1 = p.matcher(s);

		while (m1.find()) {
			String match = m1.group();
			if (matchList == null)
				matchList = new ArrayList<String>();
			if (!matchList.contains(match))
				matchList.add(match);
		}

		/*
		 * Collections.sort method is sorting the elements of ArrayList in ascending
		 * order.
		 */
		if (matchList != null)
			Collections.sort(matchList);

		return matchList;
	}

	public static boolean isInteger(String strNum) {
		try {
			int i = Integer.parseInt(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	public static String getMethod(Object o, String attributeNm) {
		try {
			Method method = o.getClass().getMethod("get" + attributeNm, null);
			Object value = method.invoke(o, null);
			return String.valueOf(
					method.getReturnType().equals(double.class) ? Helpers.formatDouble((Double) value) : value);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String formatHtml(String htmlString) {
		/*
		 * Tidy tidy = new Tidy(); tidy.setXHTML(true); tidy.setIndentContent(true);
		 * tidy.setPrintBodyOnly(false); tidy.setTidyMark(false);
		 * 
		 * // HTML to DOM Document htmlDOM = tidy.parseDOM(new
		 * ByteArrayInputStream(htmlString.getBytes()), null);
		 * 
		 * // Pretty Print OutputStream out = new ByteArrayOutputStream();
		 * tidy.pprint(htmlDOM, out);
		 * 
		 * return out.toString();
		 */
		return htmlString;
	}

	public static void showObject(Object o) {
		System.out.println(o.getClass().getCanonicalName() + "\t    \t    ");
		for (Method method : o.getClass().getDeclaredMethods()) {
			if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
					&& method.getReturnType() != void.class
					&& (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				Object value = null;
				try {
					value = method.invoke(o);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					return;
				}
				if (value != null) {
					System.out.println(method.getName() + "\t "
							+ (method.getReturnType().equals(double.class) ? Helpers.formatDouble((Double) value)
									: value)
							+ "\t  " + method.getReturnType());
				}
			}
		}
	}

	public static String toCamelCase(String text) {
		if (text.matches("([a-z]+[a-zA-Z0-9]+)+"))
			return text;

		String bactrianCamel = Stream.of(text.split("[^a-zA-Z0-9]"))
				.map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase()).collect(Collectors.joining());
		return bactrianCamel.toLowerCase().substring(0, 1) + bactrianCamel.substring(1);
	}
	
	public static String fromCamelCase(String text) {
		return fromCamelCase(text,"-");
	}
	
	public static String fromCamelCase(String text, String delimiter) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";
 
        // Replacement string
        String replacement = "$1"+delimiter+"$2";
 
        // Replace the given regex with replacement string and convert it to lower case.
        text = text.replaceAll(regex, replacement).toLowerCase();
 
        return text;
    }

	public static String toProperCase(String text) {
		if (text.matches("([a-z]+[a-zA-Z0-9]+)+"))
			return text;

		return Stream.of(text.split("[^a-zA-Z0-9]"))
				.map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase()).collect(Collectors.joining());
	}
	
	public static String toVariableCase(String text) {
		text = toProperCase(text);
		return text.substring(0,1).toLowerCase()+text.substring(1);
	}
	
	public static String relational2JavaDataType(String relationalDataType) {
		if (relationalDataType.trim().equalsIgnoreCase("CHAR") || 
			relationalDataType.trim().equalsIgnoreCase("CHARACTER") ||
		    relationalDataType.trim().equalsIgnoreCase("VARCHAR") ||
		    relationalDataType.trim().equalsIgnoreCase("CLOB")) return "String";
		else if (relationalDataType.trim().equalsIgnoreCase("INT") || relationalDataType.trim().equalsIgnoreCase("INTEGER") || relationalDataType.trim().equalsIgnoreCase("SHORT")) return "int";
		else if (relationalDataType.trim().equalsIgnoreCase("DOUBLE") || relationalDataType.trim().equalsIgnoreCase("DEC") || relationalDataType.trim().equalsIgnoreCase("DECIMAL")) return "double";
		else if (relationalDataType.trim().equalsIgnoreCase("bigint")) return "Long";
		else if (relationalDataType.trim().equalsIgnoreCase("long")) return "long";
		else if (relationalDataType.trim().equalsIgnoreCase("smallint")) return "Short";
		else return toProperCase(relationalDataType);
	}

	public static String toHex(String arg) {
		return String.format("%x", new BigInteger(arg.getBytes(/* YOUR_CHARSET? */)));
	}

	public static String toHexString(byte[] ba) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < ba.length; i++)
			str.append(String.format("%x", ba[i]));
		return str.toString();
	}

	private static String months[] = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private static String days_of_week[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday",
			"Saturday" };
	private int counter = 0;

	private int[] counters;

	private static DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	private static DecimalFormat idf = new DecimalFormat("###,###,###,##0");

	public static String getMonthYear() {
		return "XXX 2006";
	}

	public static String repeat(String s, int iterations) {
		if (s == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iterations; i++)
			sb.append(s);
		return sb.toString();
	}

	public static String formatDouble(double d) {
		return df.format(d);
	}

	public static String formatDouble(double d, String pattern) {
		DecimalFormat dfwp = new DecimalFormat(pattern);
		return dfwp.format(d);
	}

	public static String formatInt(int i, int size) {
		if (size > 0)
			return Helpers.pad(idf.format(i), size, Helpers.PAD_LEFT);
		return idf.format(i);
	}

	public static String formatInt(int i) {
		return formatInt(i, -1);
	}

	public static String formatInt(int i, String pattern) {
		return formatInt(i, pattern, -1);
	}

	public static String formatInt(int i, String pattern, int size) {
		DecimalFormat ifwp = new DecimalFormat(pattern);
		if (size > 0)
			return Helpers.pad(ifwp.format(i), size, Helpers.PAD_LEFT);
		return ifwp.format(i);
	}

	public static final int PAD_RIGHT = 0;
	public static final int PAD_LEFT = 1;
	public static final int PAD_CENTER = 2;

	/**
	 * Helper function to pad a string to a specified length and justification
	 */
	public static String pad(String s, int fill, int justified) {
		if (s == null)
			s = "x";
		String filler = repeat(" ", fill + 1);
		String t = "";
		switch (justified) {
		case PAD_RIGHT:
			if (s.length() < fill)
				t = s + filler.substring(1, fill - s.length() + 1);
			else
				t = s.substring(0, fill);
			break;
		case PAD_LEFT:
			if (s.length() < fill)
				t = filler.substring(1, fill - s.length() + 1) + s;
			else
				t = s.substring(0, fill);
			break;
		case PAD_CENTER:
			if (s.length() < fill)
				t = filler.substring(1, (fill / 2) - (s.length() / 2) + 1) + s;
			else
				t = s.substring(0, fill);
			break;
		}
		return t;

	}

	public static String pad(String s, String fillChar, int fill, int justified) {
		if (s == null)
			s = "x";
		String filler = repeat(fillChar, fill + 1);
		String t = "";
		switch (justified) {
		case PAD_RIGHT:
			if (s.length() < fill)
				t = s + filler.substring(1, fill - s.length() + 1);
			else
				t = s.substring(0, fill);
			break;
		case PAD_LEFT:
			if (s.length() < fill)
				t = filler.substring(1, fill - s.length() + 1) + s;
			else
				t = s.substring(0, fill);
			break;
		case PAD_CENTER:
			if (s.length() < fill)
				t = filler.substring(1, (fill / 2) - (s.length() / 2) + 1) + s;
			else
				t = s.substring(0, fill);
			break;
		}
		return t;

	}

	/**
	 * Helper function to pad a string to a specified length
	 */
	public static String pad(String s, int len) {
		return pad(s, len, 0);
	}

	/**
	 * Parses the string representation of a short integer and returns a valid short
	 * integer
	 *
	 * @params string representation of short integer
	 * @returns short integer representation of the string or -1 if the string is
	 *          not a valid short integer
	 */
	public static short parseShort(String s) {
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	/**
	 * Parses the string representation of a integer and returns a valid int
	 *
	 * @params string representation of int
	 * @returns int representation of the string or -1 if the string is not a valid
	 *          int
	 */
	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			if (Helpers.isNumeric(s)) {
				double d = Double.valueOf(s);
				return (int) d;
			}
			return -1;
		}
	}

	public static boolean isDateValid(String date, String pattern) {
		System.out.println("in isDateValid, date = " + date + ", pattern = " + pattern);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date myDate = sdf.parse(date);
			System.out.println("myDate = " + String.valueOf(myDate));
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
			return false;
		}
		return true;
	}

	public static ArrayList parseString(String s) {
		return parseString(s, ",");
	}

	public static ArrayList parseString(String s, String tokenChar) {
		ArrayList x = null;
		StringTokenizer st = new StringTokenizer(s, tokenChar);
		String token = "";

		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (x == null)
				x = new ArrayList();
			x.add(token);
		}
		return x;
	}

	public static String buildMessage3(String msg, String[] args) {
		StringBuffer msgBuffer = new StringBuffer(2000);
		StringTokenizer st = new StringTokenizer(msg, "{", true);
		String token = "";
		boolean openBracket = false;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (token.equals("{")) {
				openBracket = true;
			} else if (token.startsWith("0}")) {
				msgBuffer.append(args[0]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("1}")) {
				msgBuffer.append(args[1]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("2}")) {
				msgBuffer.append(args[2]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("3}")) {
				msgBuffer.append(args[3]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("4}")) {
				msgBuffer.append(args[4]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("5}")) {
				msgBuffer.append(args[5]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("6}")) {
				msgBuffer.append(args[6]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("7}")) {
				msgBuffer.append(args[7]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("8}")) {
				msgBuffer.append(args[8]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else if (token.startsWith("9}")) {
				msgBuffer.append(args[9]);
				msgBuffer.append(token.substring(2));
				openBracket = false;
			} else {
				if (openBracket) {
					openBracket = false;
					msgBuffer.append("{");
				}
				msgBuffer.append(token);
			}
		}

		return msgBuffer.toString().trim();
	}

	public static String loadFromFile(String messageFileName) {

		FileReader frdrParms;
		LineNumberReader lnrdrParms;
		String lineBuffer = "";

		String msgTemplate = "";

		try {
			frdrParms = new FileReader(messageFileName);
			lnrdrParms = new LineNumberReader(frdrParms);

			try {
				String carrageRtn = "\r\n";
				int lineNumber = 0;
				while ((lineBuffer = lnrdrParms.readLine()) != null) {
					if (lineNumber == 0) {
						if (lineBuffer.trim().startsWith("<html>"))
							carrageRtn = "";
					}
					msgTemplate += lineBuffer + carrageRtn;
					lineNumber++;
				}
				lnrdrParms.close();
				frdrParms.close();
			} catch (IOException ioBadFileLine) {
				// Usage error: Error reading line from file: {messageFileName}
				System.out.println("Error.0018 File name: " + messageFileName + ", " + ioBadFileLine.getMessage());
				return null;
			}
			lnrdrParms.close();
			frdrParms.close();
		} catch (IOException ioBadFile) {
			// Usage error: Bad file name specified: {messageFileName}
			System.out.println("Error.0018 File name: " + messageFileName + ", " + ioBadFile.getMessage());
			return null;
		}

		return msgTemplate;
	}

	public static String getDate() {
		String months[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		/* Get today's date for last published date */
		Calendar calendar = new GregorianCalendar();
		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " + months[calendar.get(Calendar.MONTH)] + " "
				+ String.valueOf(calendar.get(Calendar.YEAR));
	}

	public static String convertXMLEntities(String s) {
		return convertXMLEntities(s, true);
	}

	public static String convertXMLEntities(String s, boolean convertHtmlElements) {
		if (s == null)
			return "null";
		String t = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("'"))
				t += "&apos;";
			else if (s.substring(i, i + 1).equals("\""))
				t += "&quot;";
			else if (s.substring(i, i + 1).equals("<") && convertHtmlElements)
				t += "&lt;";
			else if (s.substring(i, i + 1).equals(">") && convertHtmlElements)
				t += "&gt;";
			else if (s.substring(i, i + 1).equals("&"))
				t += "&amp;";
			else
				t += s.substring(i, i + 1);
		}
		return t;
	}

	public static String formatDate(String date, String fromPattern) {
		return formatDate(date, fromPattern, "EEEE MMMM dd, yyyy");
	}

	/*
	 * <p>Returns a string representation of a date that has been passed as a String
	 * with a pattern described in the "fromPattern" parameter and converts to a
	 * string with a pattern described in the "toPattern" parameter.</p> <p> To
	 * specify the pattern use a <em>time pattern</em> string. In this pattern, all
	 * ASCII letters are reserved as pattern letters, which are defined as the
	 * following:</p> <blockquote> <pre> Symbol Meaning Presentation Example ------
	 * ------- ------------ ------- G era designator (Text) AD y year (Number) 1996
	 * M month in year (Text & Number) July & 07 d day in month (Number) 10 h hour
	 * in am/pm (1~12) (Number) 12 H hour in day (0~23) (Number) 0 m minute in hour
	 * (Number) 30 s second in minute (Number) 55 S millisecond (Number) 978 E day
	 * in week (Text) Tuesday D day in year (Number) 189 F day of week in month
	 * (Number) 2 (2nd Wed in July) w week in year (Number) 27 W week in month
	 * (Number) 2 a am/pm marker (Text) PM k hour in day (1~24) (Number) 24 K hour
	 * in am/pm (0~11) (Number) 0 z time zone (Text) Pacific Standard Time ' escape
	 * for text (Delimiter) '' single quote (Literal) ' </pre> </blockquote> <p>The
	 * count of pattern letters determine the format.</p> <p>
	 * <strong>(Text)</strong>: 4 or more pattern letters--use full form, &lt;
	 * 4--use short or abbreviated form if one exists. <p>
	 * <strong>(Number)</strong>: the minimum number of digits. Shorter numbers are
	 * zero-padded to this amount. Year is handled specially; that is, if the count
	 * of 'y' is 2, the Year will be truncated to 2 digits.</p> <p> <strong>(Text &
	 * Number)</strong>: 3 or over, use text, otherwise use number.</p> <p> Any
	 * characters in the pattern that are not in the ranges of ['a'..'z'] and
	 * ['A'..'Z'] will be treated as quoted text. For instance, characters like ':',
	 * '.', ' ', '#' and '@' will appear in the resulting time text even they are
	 * not embraced within single quotes.</p> <p> A pattern containing any invalid
	 * pattern letter will result in a thrown exception during formatting or
	 * parsing.</p>
	 * 
	 * <p> <strong>Examples Using the US Locale:</strong></p> <blockquote> <pre>
	 * Format Pattern Result -------------- ------- "yyyy.MM.dd G 'at' hh:mm:ss z"
	 * ->> 1996.07.10 AD at 15:08:56 PDT "EEE, MMM d, ''yy" ->> Wed, July 10, '96
	 * "h:mm a" ->> 12:08 PM "hh 'o''clock' a, zzzz" ->> 12 o'clock PM, Pacific
	 * Daylight Time "K:mm a, z" ->> 0:00 PM, PST "yyyyy.MMMMM.dd GGG hh:mm aaa" ->>
	 * 1996.July.10 AD 12:08 PM </pre> </blockquote>
	 * 
	 * @param date The string representation of the date to be formatted.
	 * 
	 * @param fromPattern The pattern of the input date field, e.g., yyyy-MM-dd
	 * 
	 * @param toPattern The pattern of the output date string
	 * 
	 * @returns String The string representation of the inputted date with the
	 * pattern described in the "toPattern" parameter
	 */

	public static String formatDate(String date, String fromPattern, String toPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(fromPattern);
		try {
			Date myDate = sdf.parse(date);
			sdf.applyPattern(toPattern);
			return sdf.format(myDate);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
			return null;
		}
	}

	public static String formatDate(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.format(date);
		} catch (Exception pe) {
			System.out.println(pe.getMessage());
			return null;
		}
	}

	public static String formatDate(java.util.Date date, String toPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(toPattern);
		try {
			return sdf.format(date);
		} catch (Exception pe) {
			System.out.println(pe.getMessage());
			return null;
		}
	}

	public static String getTimestamp() {
		return formatTime(new java.util.Date(), "yyyy-MM-dd-HH.mm.ss.SSSSSS");
	}

	public static String formatTime(Date date, String toPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(toPattern);
		return sdf.format(date);
	}

	public static String formatTMS(java.sql.Timestamp tms) {
		return formatTMS(tms, "yyyy-MM-dd-HH.mm.ss.SSSSSS");
	}
	
	public static String formatTMS(String tms) {
		if (tms==null) return null;
		if (tms.length()<11) return tms;
		//format yyyy-mm-dd hh:mm:ss[.fffffffff]
		return tms.substring(0, 10)+" "+tms.substring(11, 13)+":"+tms.substring(14, 16)+":"+tms.substring(17, tms.length());
	}

	public static String formatTMS(java.sql.Timestamp tms, String toPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(toPattern);
		return sdf.format(tms);
	}

	public static java.util.Date parseDate(String myDate) {
		return parseDate(myDate, "yyyy-MM-dd");
	}

	public static java.util.Date parseDate(String myDate, String datePattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		try {
			return sdf.parse(myDate);
		} catch (ParseException pe) {
			return null;
		}
	}

	public static String convertSql(String s) {
		String t = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("\u00ac")) {
				t += "<>";
				i++;
			} else {
				t += s.substring(i, i + 1);
			}
		} // end - for (int i=0; i<s.length(); i++)
		return t;
	}

	/**
	 * Convience function. XSLT does not allow variable to change or be manipulated
	 * after they are assigned. Therefore, incrementing the value of a variable is
	 * also not possible. Although the XSLT "count()" function may be used, this is
	 * limited to being able to count the one time instances of a element (e.g.,
	 * <xsl:variable name="x" select="count(row/status='4')". The following are a
	 * set of functions that may be called within a XSLT stylesheet to set either
	 * operate on a single counter (get, set, incrementCounter()) or an array of
	 * counters.
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param i
	 */
	public void setCounter(int i) {
		counter = i;
	}

	public void setCounter(String num) {
		setCounter(Integer.parseInt(num));
	}

	/**
	 * @param i
	 */
	public int incrementCounter() {
		return ++counter;
	}

	/**
	 * @param i
	 */
	public int incrementCounter(int i) {
		counter += i;
		return counter;
	}

	/**
	 * @param i
	 */
	public int incrementCounter(String i) {
		return incrementCounter(Integer.parseInt(i));
	}

	public void setCounters(int size) {
		counters = new int[size]; // create an array of integers

		// assign a 0 to each array element
		for (int i = 0; i < counters.length; i++) {
			counters[i] = 0;
		}
	}

	public void setCounters(int size, int qty) {
		counters = new int[size]; // create an array of integers
		// assign a value to each array element and print
		for (int i = 0; i < counters.length; i++) {
			counters[i] = qty;
		}
	}

	public void setCounters(int size, String qty) {
		counters = new int[size]; // create an array of integers
		// assign a value to each array element and print
		for (int i = 0; i < counters.length; i++) {
			counters[i] = Integer.parseInt(qty);
		}
	}

	public void incrementCounters(int position) {
		counters[position]++;
	}

	//
	public void incrementCounters(int position, int qty) {
		counters[position] += qty;
	}

	public void incrementCounters(int position, String qty) {
		counters[position] += Integer.parseInt(qty);
	}

	public int[] getCounters() {
		return counters;
	}

	public int getCounters(int position) {
		return counters[position];
	}

	public int getCounters(String position) {
		return counters[Integer.parseInt(position)];
	}

	public static double roundCurrency(double val) {
		return Math.round(val * 100d) / 100d;
	}

	/**
	 * Round a double value to a specified number of decimal places.
	 *
	 * @param val    the value to be rounded.
	 * @param places the number of decimal places to round to.
	 * @return val rounded to places decimal places.
	 */
	public static double roundD(double val, int places) {
		long factor = (long) Math.pow(10, places);

		// Shift the decimal the correct number of places
		// to the right.
		val = val * factor;

		// Round to the nearest integer.
		long tmp = Math.round(val);

		// Shift the decimal the correct number of places
		// back to the left.
		return (double) tmp / factor;
	}

	/* remove trailing whitespace */
	// public static String rtrim(String source) {
	// return source.replaceAll("\\s+$", "");
	// }
	/**
	 * Trims specified string from left.
	 * 
	 * @param s
	 */
	public static String ltrim(String s) {
		if (s == null) {
			return null;
		}

		int index = 0;
		int len = s.length();

		while (index < len && Character.isWhitespace(s.charAt(index))) {
			index++;
		}

		return (index >= len) ? "" : s.substring(index);
	}

	/**
	 * Trims specified string from right.
	 * 
	 * @param s
	 */
	public static String rtrim(String s) {
		if (s == null) {
			return null;
		}

		int len = s.length();
		int index = len;

		while (index > 0 && Character.isWhitespace(s.charAt(index - 1))) {
			index--;
		}

		return (index <= 0) ? "" : s.substring(0, index);
	}

	public static String escapeSingleQuote(String s, String escapeChar) {
		if (s == null)
			return "null";
		String t = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("'"))
				t += escapeChar + "'";
			// else if (s.substring(i, i+1).equals("\"")) t+= "&quot;";
			// else if (s.substring(i, i+1).equals("<")) t+= "&lt;";
			// else if (s.substring(i, i+1).equals(">")) t+= "&gt;";
			// else if (s.substring(i, i+1).equals("&")) t+= "&amp;";
			else
				t += s.substring(i, i + 1);
		}
		return t;
	}

	public static String escapeSingleQuote(String s) {
		return escapeSingleQuote(s, "\\");
	}

	public static String trimUtf16(String myString) {
		Pattern unicode = Pattern.compile("[^\\x00-\\x7F]",
				Pattern.UNICODE_CASE | Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
		Matcher matcher = unicode.matcher(myString);
		myString = matcher.replaceAll(" ");
		return myString;
	}

	public static String[] parseLine(String csvLine) {
		return parseLine(csvLine, ' ', ' ');
	}

	public static String[] parseLine(String csvLine, char separators) {
		return parseLine(csvLine, separators, ' ');
	}

	public static String[] parseLine(String cvsLine, char separators, char customQuote) {

		char DEFAULT_SEPARATOR = ',';
		char DEFAULT_QUOTE = '"';

		List<String> result = new ArrayList<String>();

		// if empty, return!
		if (cvsLine == null || cvsLine.isEmpty()) {
			return null;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						// curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString().trim());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString().trim());

		String[] resultArray = new String[result.size()];
		resultArray = result.toArray(resultArray);

		return resultArray;
	}
	
	/**
	 * Replaces placeholders in the input string with corresponding environment variable values.
	 * <p>
	 * This method searches for substrings enclosed in curly brackets (e.g., {variableName})
	 * within the input string and replaces them with the values of environment variables that match
	 * the variable names. If an environment variable with the specified name is not found,
	 * the placeholder is left unchanged.
	 * </p>
	 * <p><b>Explanation:</b></p>
	 * <ol>
	 * <li>The <pre>Pattern</pre> and <pre>Matcher</pre> classes are used to search for all occurrences 
	 * of variables enclosed in curly brackets.</li>
	 * <li>The regex <pre>\\{(\\w+)\\}</pre> matches any text inside curly brackets (e.g., 
	 * <pre>{sslTrustStoreLocation}</pre>).</li>
	 * <li>The <pre>matcher.group(1)</pre> extracts the variable name (without the curly brackets).</li>
	 * <li>The <pre>System.getenv(variable)</pre> retrieves the value of the environment variable.</li>
	 * <li>If the environment variable doesn't exist, the placeholder is kept unchanged.</li>
	 * <li>The <pre>appendReplacement()</pre> method replaces the found placeholder with the environment 
	 * variable's value, and <pre>appendTail()</pre> appends any remaining text.</li>
	 * </ol>
	 * <p>This approach allows dynamic replacement of any variable enclosed in curly brackets with 
	 * the corresponding environment variable at runtime.</p>
	 *
	 * @param input the input string containing placeholders in the format {variableName}
	 * @return a new string where all placeholders are replaced with environment variable values,
	 *         or left unchanged if the environment variable does not exist
	 * @throws NullPointerException if the input string is null
	 * 
	 * <pre>
	 * Example:
	 * String input = "The path is {pathVar} and the user is {userVar}";
	 * System.getenv("pathVar") returns "/usr/local/path";
	 * System.getenv("userVar") returns "admin";
	 * String result = replaceVariables(input);
	 * Result: "The path is /usr/local/path and the user is admin"
	 * </pre>
	 */
    public static String replaceVariables(String input) {
        // Regular expression to match any text within curly brackets
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(input);
        StringBuffer resultString = new StringBuffer();

        // Iterate through all matches
        while (matcher.find()) {
            // Get the variable name without the curly brackets
            String variable = matcher.group(1);

            // Look up the variable in the environment
            String replacement = System.getenv(variable);

            // If the variable doesn't exist in the environment, keep the placeholder
            if (replacement == null) {
                replacement = matcher.group(0); // {variable}
            }

            // Append the replacement (or original if no env var is found)
            matcher.appendReplacement(resultString, replacement);
        }
        
        // Append the rest of the string
        matcher.appendTail(resultString);

        return resultString.toString();
    }

}