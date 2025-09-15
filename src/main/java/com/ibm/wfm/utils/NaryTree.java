package com.ibm.wfm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNodeInterface;
import com.ibm.wfm.beans.SelfReferenceTaxonomyNodeInterface;

public class NaryTree {
	
	private NaryTreeNode rootNode = null;

	public static void main(String[] args) {
		
		boolean verbose = false;
		boolean validParams = true;
		String mode = "test";
		String delimiter = ",";
		String csvFileName = null;
		String outputFileName = null;
		String treeId = "treeId";
		boolean useFullkey = false;
		boolean formatHtml = false;
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-m") || args[optind].equalsIgnoreCase("-mode")) {
					mode = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-csv")) {
					csvFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-o") || args[optind].equalsIgnoreCase("-output")) {
					outputFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-d") || args[optind].equalsIgnoreCase("-delimiter")) {
					delimiter = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-id") || args[optind].equalsIgnoreCase("-treeId")) {
					treeId = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-formathtml") || args[optind].equalsIgnoreCase("-pretty")) {
					formatHtml = true;
				} else if (args[optind].equalsIgnoreCase("-verbose")) {
					verbose = true;
				} else if (args[optind].equalsIgnoreCase("-useFullkey")) {
					useFullkey = true;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("-help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.out.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}
		
		if (!mode.equalsIgnoreCase("csv2tree") && !mode.equalsIgnoreCase("test") && !mode.equalsIgnoreCase("tax2tree") && !mode.equalsIgnoreCase("tax2csv")) {
			System.out.println("Error: Invalid mode specified - "+mode);
			validParams = false;
		}
		
		if (mode.equalsIgnoreCase("csv2tree") && csvFileName==null) {
			System.out.println("Error: CSV file name must be specified when mode=csv2tree.");
			validParams = false;
		}

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Usage: com.ibm.gbs.utils.NaryTree"); // msgs.getText("Seat2PldbEntitlementEtl.usage"));
			System.out.println(" ");
			System.out.println("NaryTree Parameters");
			System.out.println("-------------------");
			System.out.println("[-m | -mode]     - Mode, csv2tree | tax2tree | tax2csv | test (default: csv2tree)");
			System.out.println("[-csv]           - CSV file name (mandatory with mode=test)");
			System.out.println("[-id | -treeId]  - Id to be used for tree element (default: treeId)");
			System.out.println("[-d | -delimiter] - With mode csv2tree, specifies the delimiter to use  (default: comma)");
			System.out.println("[-useFullKey]    - Use the full key from the taxonomy.  Used in cases where a code is not unique within the tree (however, the code must be unique within the branch).");
			System.out.println(" ");
			System.out.println("Output Parameters");
			System.out.println("-----------------");
			System.out.println("[-o | -output]    - Output file name. (default: system output)");
			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-h | -help]    - Display usage parameters.");
			System.out.println("[-verbose]      - Display verbose messages.");
			
			System.exit(-99);
		}
		
				
		System.out.println("Processing started at "+new java.util.Date());
		NaryTree naTree = new NaryTree();
		if (mode.equalsIgnoreCase("csv2tree")) {
			NaryTreeNode root = naTree.populateNaryTreeFromCsv(csvFileName, delimiter, useFullkey);
			
			List<String> tree = naTree.processTree2Ul(root);
			String html = "<ul id=\""+treeId+"\" class=\"nodeTree\">";
			for (String e : tree) html+=e;
			html+="</ul>";
			if (outputFileName==null) {
				if (formatHtml) System.out.println(Helpers.formatHtml(html));
				else System.out.println(html);
			}
			else {
				//System.out.println("Put in code to output formatted HTML to file.");
				if (formatHtml) FileHelpers.stringToFile(outputFileName, Helpers.formatHtml(html));
				else FileHelpers.stringToFile(outputFileName, html);
			}
		}
		else if (mode.equalsIgnoreCase("tax2tree")) {
			NaryTreeNode root = naTree.populateNaryTreeFromTax(csvFileName, ",");
			
			List<String> tree = naTree.processTree2Ul(root);
			String html = "<ul id=\""+treeId+"\" class=\"nodeTree\">";
			for (String e : tree) html+=e;
			html+="</ul>";
			if (outputFileName==null) {
				System.out.println(Helpers.formatHtml(html));
			}
			else {
				//FileHelpers.stringToFile(outputFileName, Helpers.formatHtml(html));
				FileHelpers.stringToFile(outputFileName, html);
			}
		}
		else if (mode.equalsIgnoreCase("tax2csv")) {
			NaryTreeNode root = naTree.populateNaryTreeFromTax(csvFileName, ",");
			ArrayList<String> rows = new ArrayList<String>();;
			//this.toCsv(nodeA, level, delimiter, key, doubleQuoteStrings, rows);		
			naTree.toCsv(root, rows);		
			for (String s : rows) {
				System.out.println(s);
			}
		}
		else if (mode.equalsIgnoreCase("test")) {
			naTree.runTest();
		}	
		System.out.println("Processing completed at "+new java.util.Date());
	}
	
	/*
	 * Emulates the Taxonomy tool logic.  Expects a delimited file (default CSV) that has two sections.
	 * The first section has a repeating set of <parent-key-value><delimiter><child-key-value>.  There 
	 * should be no headers used.
	 * 
	 * The second section begins with the word "nodes" in the first column and that is it.  Followed by
     * <node-key-value><delimiter><node-description>
     * 
     * For example, the format for MLB's American League would look like:
     * MLB, AL
     * AL, ALE
     * AL, ALC
     * AL, ALW
     * ALE, NYY
     * ALE, TB
     * ALE, BOS
     * ALE, TOR
     * ALE, BAL
     * ALC, MIN
     * ALC, CLE
     * ALC, CWS
     * ALC, KC
     * ALC, DET
     * ALW, HOU
     * ALW, OAK
     * ALW, TEX
     * ALW, LAA
     * ALW, SEA
     * nodes
     * MLB, Major League Baseball
     * AL, American League
     * ALE, American League East
     * ALC, American League Central
     * ALW, American League West
     * ALE, AL East
     * NYY, NY Yankees
     * TB, Tampa Bay
     * BOS, Boston
     * TOR, Toronto
     * BAL, Baltimore
     * ALC, AL Central
     * MIN, Minnesota
     * CLE, Cleveland
     * CWS, Chi White Sox
     * KC, Kansas City
     * DET, Detroit
     * ALW, AL West
     * HOU, Houston
     * OAK, Oakland
     * TEX, Texas
     * LAA, LA Angels
     * SEA, Seattle
     * 
     * see mlb-american-league-sample-taxonomy.csv
     * 
	 */
	
	public NaryTreeNode populateNaryTreeFromTax(String fileName, String delimiter) {
		NaryTreeNode rootNode = new NaryTreeNode("IBM", "IBM");
		FileReader frdr;
		try {
			frdr = new FileReader(fileName);

			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;

			int lineCnt=0;
			int coupleSize=0;
			boolean processingNodes = false;
			int nodeCnt = 0;
			while ((lineBuffer = lnrdr.readLine()) != null) {
				System.out.println(lineBuffer);
				if (lineBuffer.trim().startsWith("nodes")) {
					processingNodes=true;
				}
				if (processingNodes) {
					if (nodeCnt++ > 0) {
						String[] keys = lineBuffer.split(delimiter,-1);
						NaryTreeNode ntn = find(keys[0].trim(),rootNode);
						if (ntn==null) {
							System.out.println("ERROR: Missing node "+keys[0]);
						} 
						else {
							ntn.setDescription(keys[1].trim());
						}
					}					
				}
				else {
					String[] keys = lineBuffer.split(delimiter,-1);
					//keys[0] = keys[0].replaceAll("[^a-zA-Z0-9]", "").trim();
					NaryTreeNode ntn = find(keys[0],rootNode);
					if (ntn==null) {
						System.out.println("ERROR: Missing node "+keys[0]);
					} 
					else {
						ntn.addChild(new NaryTreeNode(keys[1].trim()));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		System.out.println("");
		return rootNode;		
	}	
	
	public NaryTreeNode populateNaryTreeFromTax(String fileName) {
		NaryTreeNode rootNode = new NaryTreeNode("IBM", "IBM Top");
		FileInputStream file;
		try {
			file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			 
			Map<Integer, List<String>> data = new HashMap<>();
			//int i = 0;
			//for (XSSFRow row : sheet) {
			for (int i=sheet.getFirstRowNum(); i<sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
			    data.put(i, new ArrayList<String>());
			    //for (Cell cell : row) {
			    int cellNum = row.getFirstCellNum();
			    for (int c=row.getFirstCellNum(); c<row.getLastCellNum(); c++) {
			    	XSSFCell cell = row.getCell(c);
			    	System.out.println(cell.getStringCellValue());
			    }
			    //i++;
			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootNode;
	}
	
	public NaryTreeNode populateNaryTreeFromCsv(String fileName) {
		return populateNaryTreeFromCsv(fileName, "~", false);
	}
		
	public NaryTreeNode populateNaryTreeFromCsv(String fileName, String delimiter) {
		return populateNaryTreeFromCsv(fileName, "~", false);
	}
	
	public NaryTreeNode populateNaryTreeFromCsv(String fileName, String delimiter, boolean useFullKey) {
		//NaryTreeNode rootNode = new NaryTreeNode("10J00", "Global Business Services", "10J00:",0);
		//NaryTreeNode rootNode = null;
		FileReader frdr;
		try {
			frdr = new FileReader(fileName);

			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;

			int lineCnt=0;
			int coupleSize=0;
			while ((lineBuffer = lnrdr.readLine()) != null) {
				StringBuffer fullKey = new StringBuffer();
				String[] keys = Helpers.parseLine(lineBuffer);
			     
				String key = "";
				NaryTreeNode[] keyValuePairs = new NaryTreeNode[10];
				int kvNum = 0;
				
				//Extract the key-value pairs from the CSV assuming the first column is the key and the second is the value
				if (lineCnt++>0) {
					if (lineCnt==2) coupleSize=keys.length/2;
					for (int i = 0; i < keys.length; i++) {
						key = keys[i];
						//if it is an odd # key
						if ((i+1)%2 == 1) {
							kvNum = i/2;
							fullKey.append(key).append(":");
							keyValuePairs[kvNum] = new NaryTreeNode((key==null || key.trim().equals("")?"-":key),null,fullKey.toString(),kvNum);
						}
						else {
							keyValuePairs[kvNum].setDescription(key==null || key.trim().equals("")?"none":key);
							//keyValuePairs[kvNum].println();
						}
						
					} //end - for (int i = 0; i < keys.length; i++) 
					
					for (int i=0; i<coupleSize; i++) {
						NaryTreeNode n = keyValuePairs[i];
						NaryTreeNode foundNode = null;
						if (rootNode!=null)
							foundNode = this.find(useFullKey?n.getFullKey():n.getCode(), rootNode, useFullKey);
						if (foundNode==null) {
							if (i==0) {
								if (rootNode==null)
									rootNode = new NaryTreeNode(n);
								else
									rootNode.addChild(n);
							}
							else {
								//NaryTreeNode parentNode = this.find(keyValuePairs[i-1].getCode(), rootNode);
								NaryTreeNode parentNode = this.find(useFullKey?keyValuePairs[i-1].getFullKey():keyValuePairs[i-1].getCode()
										, rootNode, useFullKey);
								if (parentNode==null) {
									System.out.println("What the F...");
									keyValuePairs[i-1].println();
									n.println();
								}
								else {
									parentNode.addChild(n);
								}
							}
						} //end - if (foundNode==null)
					} //end - for (int i=0; i<coupleSize; i++)
				} //end - if (lineCnt++>0)
			} //end - while ((lineBuffer = lnrdr.readLine()) != null)
			lnrdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		System.out.println("");
		return rootNode;		
	}
	
	public boolean runTest() {
		/*
		 * Build Tree 
		 *           A 
		 *      /    |\ 
		 *     B     C D
		 *    /|\   /| |
		     E F G H I J
		 */
		NaryTreeNode nodeA = new NaryTreeNode("A");
		NaryTreeNode nodeB = new NaryTreeNode("B");
		NaryTreeNode nodeC = new NaryTreeNode("C");
		NaryTreeNode nodeD = new NaryTreeNode("D");
		NaryTreeNode nodeE = new NaryTreeNode("E");
		NaryTreeNode nodeF = new NaryTreeNode("F");
		NaryTreeNode nodeG = new NaryTreeNode("G");
		NaryTreeNode nodeH = new NaryTreeNode("H");
		NaryTreeNode nodeI = new NaryTreeNode("I");
		NaryTreeNode nodeJ = new NaryTreeNode("J");

		nodeA.addChild(nodeB);
		nodeA.addChild(nodeC);
		nodeA.addChild(nodeD);
		nodeB.addChild(nodeE);
		nodeB.addChild(nodeF);
		nodeB.addChild(nodeG);
		nodeC.addChild(nodeH);
		nodeC.addChild(nodeI);
		nodeD.addChild(nodeJ);
		//NaryTree naTree = new NaryTree();
		
		/* Test to UL */
		List<String> tree = this.processTree2Ul(nodeA);
		String html = "<ul id=\"testTree\">";
		for (String e : tree) html+=e;
		html+="</ul>";
		System.out.println(Helpers.formatHtml(html));


		/* Test Pre-Order */
		List<NaryTreeNode> list = this.processTree(nodeA);
		String preOrderResults = "";
		for (NaryTreeNode n : list)
			preOrderResults += (n.getCode() + " ");
		System.out.println("Pre-Order Recursion: \t" + preOrderResults);
		
		System.out.println("");
		System.out.println("To CSV 1");
		int level = 0;
		String key = "";
		String delimiter = ",";
		boolean doubleQuoteStrings = false;
		ArrayList<String> rows = new ArrayList<String>();;
		//this.toCsv(nodeA, level, delimiter, key, doubleQuoteStrings, rows);		
		this.toCsv(nodeA, rows);		
		for (String s : rows) {
			System.out.println(s);
		}
		
		System.out.println("");
		System.out.println("To CSV 2");
		StringBuffer sb = new StringBuffer();
		this.toCsv(nodeA, sb);
		System.out.println(sb);
		
		/* Test Pre-Order with Iteration*/
		list = this.processTree(nodeA, NaryTreeNodeInterface.PRE_ORDER_ITERATE);
		String preOrderIterateResults = "";
		for (NaryTreeNode n : list)
			preOrderIterateResults += (n.getCode() + " ");
		System.out.println("Pre-Order Iterate: \t" + preOrderIterateResults);		

		/* Test Post-Order with Recursion*/
		list = this.processTree(nodeA, NaryTreeNodeInterface.POST_ORDER);
		String postOrderResults = "";
		for (NaryTreeNode n : list)
			postOrderResults += (n.getCode() + " ");
		System.out.println("Post-Order Recursion: \t" + postOrderResults);

		/* Test Post-Order with Iteration*/
		list = this.processTree(nodeA, NaryTreeNodeInterface.POST_ORDER_ITERATE);
		String postOrderIterateResults = "";
		for (NaryTreeNode n : list)
			postOrderIterateResults += (n.getCode() + " ");
		System.out.println("Post-Order Iterate: \t" + postOrderIterateResults);
		
		return true;
	}
	
	public static boolean toCsv(NaryTreeNode root, ArrayList<String> rows) {
		return toCsv(root, 0, ",", "", true, rows);
	}
	
	public static boolean toCsv(NaryTreeNode root, int level, String delimiter, String key, boolean doubleQuoteStrings, ArrayList<String> rows) {
		
		String encloseString = "";
		if (doubleQuoteStrings) encloseString = "\"";

		if (root == null) return false;
		
		/*
		if (delimiter.equals(",")) {
			key+=(key.length()==0?"":delimiter) + encloseString + root.getCode() + encloseString + delimiter + encloseString + root.getDescription() + encloseString ;
		}
		else
			key+=(key.length()==0?"":delimiter) + root.getCode() + delimiter + root.getDescription();
		*/
		key+=(key.length()==0?"":delimiter)+ DataMarshaller.getObjectValuesAsCsvString(root, delimiter, doubleQuoteStrings);
		
		if (root.getChildren() != null) {
			for (NaryTreeNode child : root.getChildren()) {
				toCsv(child, level+1, delimiter, key, doubleQuoteStrings, rows);
			}
		}
		else {
			rows.add(key);
		}
		return true;
	}
	
	public static boolean toCsvHeader(NaryTreeNode root, int level, String delimiter, String header, ArrayList<String> rows) {
		String codeNm = "code"+String.valueOf(level);
		String descriptionNm = "description"+String.valueOf(level);
		Field[] fields = root.getClass().getDeclaredFields();
		for (Field field: fields) {
			//if (field.getName().endsWith("Desc")) descriptionNm=field.getName();
			if (field.getType()==ArrayList.class || field.getType()==NaryTreeNode.class) {}
			else {
				DbColumn dbColumn = field.getAnnotation(DbColumn.class);
				
				if (dbColumn==null || (!dbColumn.isId() && dbColumn.foreignKeySeq()<1)) {
					header+= (header.trim().length()>0?",":"")+field.getName();
				}
			}
		}
		//if (!descriptionNm.equals("description"+String.valueOf(level)))
		//		codeNm = descriptionNm.substring(0, descriptionNm.length()-4)+"Cd";
		
		//header+= (header.trim().length()>0?",":"")+codeNm+", "+descriptionNm;
		if (root.getChildren() != null) {
			toCsvHeader(root.getChildren().get(0), level+1, delimiter, header, rows);
		}
		else {
			rows.add(header);
		}
		return true;		
	}

	public List<String> processTree2Ul(NaryTreeNode root) {
		List<String> list = new ArrayList<String>();
		toUlTree(root, list);
		return list;
	}
	
	public String processTree2Csv(NaryTreeNode root) {
		StringBuffer csv = new StringBuffer();;
		toCsv(root, csv);
		return csv.toString();
	}
	
	public List<NaryTreeNode> processTree(NaryTreeNode root) {
		return processTree(root, NaryTreeNodeInterface.PRE_ORDER);
	}

	public List<NaryTreeNode> processTree(NaryTreeNode root, int order) {
		List<NaryTreeNode> list = null;
		if (order == NaryTreeNodeInterface.PRE_ORDER) {
			list = new ArrayList<>();
			preOrder(root, list);
		} else if (order == NaryTreeNodeInterface.PRE_ORDER_ITERATE) {
			list = preOrderIterate(root);	
		} else if (order == NaryTreeNodeInterface.POST_ORDER) {
			list = new ArrayList<>();
			postOrder(root, list);
		} else if (order == NaryTreeNodeInterface.POST_ORDER_ITERATE) {
			list = postOrderIterate(root);
		}
		return list;
	}

	public void preOrder(NaryTreeNode root, List<NaryTreeNode> list) {
		if (root == null)
			return;
		list.add(root);
		if (root.getChildren() != null) {
			for (NaryTreeNode child : root.getChildren()) {
				preOrder(child, list);
			}
		}
	}
	
	public void toUlTree(NaryTreeNode root, List<String> list) {
		toUlTree(root, list, true);
	}
	
	public void toUlTree(NaryTreeNode root, List<String> list, boolean includeCode) {
		//preOrder logic
		if (root == null)
			return;
		list.add("<li>");
		
		list.add((includeCode?"<b>"+root.getCode()+"</b> - ":"")+Helpers.convertXMLEntities(root.getDescription(), false));

		if (root.getChildren() != null) {
			list.add("<ul>");
			//Collections.reverse(root.getChildren());
			for (NaryTreeNode child : root.getChildren()) {
				toUlTree(child, list, includeCode);
			}
			list.add("</ul>");
		}
		list.add("</li>");
	}
	
	public void toCsv(NaryTreeNode root, StringBuffer csv) {
		if (root == null)
			return;
		csv.append(root.getCode()+"\t"+root.getDescription());
		if (root.getChildren() != null) {
			csv.append("\t");
			for (NaryTreeNode child : root.getChildren()) {
				toCsv(child, csv);
			}
		}
		csv.append(System.lineSeparator());
	}	

	public List<NaryTreeNode> preOrderIterate(NaryTreeNode root) {
		LinkedList<NaryTreeNode> stack = new LinkedList<>();
		LinkedList<NaryTreeNode> list = new LinkedList<>();
		if (root == null) {
			return list;
		}

		stack.add(root);
		while (!stack.isEmpty()) {
			NaryTreeNode node = stack.pollLast();
			list.add(node);
			if (node.getChildren() != null) {
				Collections.reverse(node.getChildren());

				for (NaryTreeNode item : node.getChildren()) {
					stack.add(item);
				}
			}
		}
		return list;
	}
	
	public void postOrder(NaryTreeNode root, List<NaryTreeNode> list) {
		if (root == null)
			return;
		if (root.getChildren() != null) {
			for (NaryTreeNode child : root.getChildren()) {
				postOrder(child, list);
			}
		}
		list.add(root);
	}

	public List<NaryTreeNode> postOrderIterate(NaryTreeNode root) {
		List<NaryTreeNode> list = new ArrayList<NaryTreeNode>();
		if (root == null)
			return list;
		Stack<NaryTreeNode> stack = new Stack<NaryTreeNode>();
		stack.push(root);
		while (!stack.isEmpty()) {
			NaryTreeNode node = stack.pop();
			list.add(node);
			if (node.getChildren() != null) {
				for (NaryTreeNode child : node.getChildren()) {
					// for (int i=0; i<node.getChildren().size(); i++) {
					// NaryTreeNode child = node.getChildren().get(i);
					stack.push(child);
				}
			}
		}
		Collections.reverse(list);
		return list;
	}
	
	public NaryTreeNode find(String key, NaryTreeNode root) {
		return this.find(key, root, false);
		
	}
	
	public NaryTreeNode find(String key, NaryTreeNode root, boolean useFullKey) {
		LinkedList<NaryTreeNode> stack = new LinkedList<>();
		LinkedList<NaryTreeNode> list = new LinkedList<>();
		if (root == null) {
			return null;
		}
		//if (node.getCode().equals(key)) return node;
		if (useFullKey) {
			if (root.getFullKey().equals(key)) return root;
		}
		else {
			if (root.getCode().equals(key)) return root;
		}
	
		stack.add(root);
		while (!stack.isEmpty()) {
			NaryTreeNode node = stack.pollLast();
			//if (node.getCode().equals(key)) return node;
			if (useFullKey) {
				if (node.getFullKey().equals(key)) return node;
			}
			else {
				if (node.getCode().equals(key)) return node;
			}
			list.add(node);
			if (node.getChildren() != null) {
				Collections.reverse(node.getChildren());
	
				for (NaryTreeNode item : node.getChildren()) {
					//if (item.getCode().equals(key)) {
					if ((useFullKey && item.getFullKey().equals(key))
					 ||	(!useFullKey && item.getCode().equals(key))) {
						Collections.reverse(node.getChildren());
						return item;
					}
					stack.add(item);
				} //end-for (NaryTreeNode item : node.getChildren())
				Collections.reverse(node.getChildren());
			}
		}
		return null;
	}
	
	public static NaryTreeNode findStatic(NaryTreeNode keyNode, NaryTreeNode root, boolean useFullKey) {
		LinkedList<NaryTreeNode> stack = new LinkedList<>();
		LinkedList<NaryTreeNode> list = new LinkedList<>();
		if (root == null) {
			return null;
		}
		//if (node.getCode().equals(key)) return node;
		if (useFullKey) {
			if (root.getFullKey().equals(keyNode.getFullKey())) return root;
		}
		else {
			if (root.getCode().equals(keyNode.getCode())) return root;
		}
	
		stack.add(root);
		while (!stack.isEmpty()) {
			NaryTreeNode node = stack.pollLast();
			//if (node.getCode().equals(key)) return node;
			if (useFullKey) {
				if (node.getFullKey().equals(keyNode.getFullKey())) return node;
			}
			else {
				if (node.getCode().equals(keyNode.getCode())) return node;
			}
			list.add(node);
			if (node.getChildren() != null) {
				Collections.reverse(node.getChildren());
	
				for (NaryTreeNode item : node.getChildren()) {
					//if (item.getCode().equals(key)) {
					if ((useFullKey && item.getFullKey().equals(keyNode.getFullKey()))
					 ||	(!useFullKey && item.getCode().equals(keyNode.getCode()))) {
						Collections.reverse(node.getChildren());
						return item;
					}
					stack.add(item);
				} //end-for (NaryTreeNode item : node.getChildren())
				Collections.reverse(node.getChildren());
			}
		}
		return null;
	}	
	
	/*
	public boolean isLevelValid(int level, int status) {
		int levelValue = (int)Math.pow(2,level);
		if ((status & levelValue) == levelValue) return true;
		else return false;
	}
	*/

	public NaryTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(NaryTreeNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public static SelfReferenceTaxonomyNodeInterface buildTreeFromSelfReferencingTaxonomy(List<SelfReferenceTaxonomyNodeInterface> nodes) {
		SelfReferenceTaxonomyNodeInterface rootNode = null;
	    // Map to hold the nodes by their code
	    Map<String, SelfReferenceTaxonomyNodeInterface> nodeMap = new HashMap<>();
	
	    // Populate the map with nodes
	    for (SelfReferenceTaxonomyNodeInterface node : nodes) {
	        nodeMap.put(((SelfReferenceTaxonomyNodeInterface) node).getCode(), node);
	    }
	
	    // Iterate over the nodes to set the children
	    for (SelfReferenceTaxonomyNodeInterface node : nodes) {
	        String parentCode = node.getParentCd();
	        if (parentCode != null) {
	        	SelfReferenceTaxonomyNodeInterface parentNode = nodeMap.get(parentCode);
	            if (parentNode != null) {
	                ((NaryTreeNode)parentNode).addChild((NaryTreeNode)node);
	            } else {
	                System.err.println("Parent node with code " + parentCode + " not found for node with code " + node.getCode());
	                rootNode = node;
	            }
	        }
	        else {
	        	rootNode = node;
	        }
	    }
	    
        // Set the levels starting from the root node
        if (rootNode != null) {
            setLevels(rootNode, 0, rootNode.getCode());
        }
	
	    return rootNode;
	}
	
    private static void setLevels(SelfReferenceTaxonomyNodeInterface node, int level, String key) {
        node.setLevelNum(level);
        ((NaryTreeNode)node).setFullKey(key);
        if (((NaryTreeNode) node).getChildren()!=null) {
            for (NaryTreeNode child : ((NaryTreeNode) node).getChildren()) {
                setLevels((SelfReferenceTaxonomyNodeInterface)child, level + 1, key+":"+child.getCode());
            }
        }
    }
	    
    public static NaryTreeNode buildTreeFromSelfReferencingTaxonomyORIGINAL(List<SelfReferenceTaxonomyNodeInterface> nodes) {
        // Map to store each node by its code for easy access
        Map<String, NaryTreeNode> nodeMap = new HashMap<>();

        // First pass: create nodes without setting parent-child relationships
        for (SelfReferenceTaxonomyNodeInterface node : nodes) {
            NaryTreeNode treeNode = new NaryTreeNode();
            treeNode.setCode(node.getCode());
            treeNode.setDescription(String.valueOf(node.getName())); // getDescription
            //treeNode.setLevel(node.getLevelNum());
            nodeMap.put(node.getCode(), treeNode);
        }

        // Second pass: set parent-child relationships
        for (SelfReferenceTaxonomyNodeInterface node : nodes) {
            NaryTreeNode treeNode = nodeMap.get(node.getCode());
            String parentCode = node.getParentCd();
            if (parentCode != null) {
                NaryTreeNode parentNode = nodeMap.get(parentCode);
                if (parentNode != null) {
                    treeNode.setParentNode(parentNode);
                    parentNode.addChild(treeNode);
                } else {
                    // Handle case where parent node is not found
                    // You can throw an exception or handle it according to your requirements
                }
            }
        }

        // Find and return the root node(s)
        //return findRootNodes(nodeMap);
        // Find the root nodes and assign levels
        NaryTreeNode rootNode = findAndSetLevels(nodeMap);

        return rootNode;
    }
    
    private static NaryTreeNode findRootNodes(Map<String, NaryTreeNode> nodeMap) {
        NaryTreeNode rootNode = null;
        for (NaryTreeNode node : nodeMap.values()) {
            if (node.getParentNode() == null) {
                if (rootNode == null) {
                    rootNode = node;
                } else {
                    // If there are multiple root nodes, create a dummy root
                    NaryTreeNode dummyRoot = new NaryTreeNode();
                    dummyRoot.addChild(rootNode);
                    dummyRoot.addChild(node);
                    rootNode = dummyRoot;
                }
            }
        }
        return rootNode;
    }
    
    private static NaryTreeNode findAndSetLevels(Map<String, NaryTreeNode> nodeMap) {
        NaryTreeNode rootNode = null;
        for (NaryTreeNode node : nodeMap.values()) {
            if (node.getParentNode() == null) {
                if (rootNode == null) {
                    rootNode = node;
                } else {
                    // If there are multiple root nodes, create a dummy root
                    NaryTreeNode dummyRoot = new NaryTreeNode();
                    dummyRoot.addChild(rootNode);
                    dummyRoot.addChild(node);
                    rootNode = dummyRoot;
                }
            }
        }
        // If there's a rootNode, assign levels starting from 0
        if (rootNode != null) {
            assignLevels(rootNode, 0);
        }
        return rootNode;
    }

    private static void assignLevels(NaryTreeNode node, int level) {
        node.setLevel(level);
        if (node.getChildren() != null) {
            for (NaryTreeNode child : node.getChildren()) {
                assignLevels(child, level + 1);
            }
        }
    }
    

}
