package com.ibm.wfm.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.wfm.beans.JsonNaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNode;

public class Tester {

	public static void main(String[] args) {

        String originalString = "\"Cross Communication's Professionals' are responsible for executing all Communications disciplines within a geography, market or country. This encompasses external and stakeholder communications, including media, analyst, influencer relations and social engagement, as well as employee and executive communications. They act as a steward for IBM's brand and reputation and ensures consistency of messaging across all internal and external audiences. This includes the protection and enhancement of the IBM brand through the application of sound issues and crisis management practices. They take business direction from the Cross Communications Leader for the geography or market, and acts as a trusted advisor by providing strategic advice and counsel aimed at improving IBM's reputation among all target audiences and building brand favorability and product consideration. At a functional level, the role takes direction from a senior Communications leader. The professional develops and executes a Communications strategy to support business objectives and key plays, such as Signature Moments. Develop a comprehensive Communications strategy to support the business objectives at a Geo, market or country, and aligned to the national agenda. Create compelling campaigns underpinned by corporate storytelling and aimed at influencing media and IT analysts, drive positive impact with clients, business partners, industry bodies, governments, influencers, and alumni. Work in collaboration with Government & Regulatory Affairs, Corporate Social Responsibility, Sales, and the Business Partner organization. Contribute to building and sustaining a cohesive team environment by providing clarity, direction, fostering a culture of excellence, cross collaboration and recognition. Act as a spokesperson to IBM's key audiences and communicate IBM's position convincingly across all platforms and mediums. Possess an in depth awareness and understanding of IBM's business strategy, offerings, and the competitive landscape with the ability to speak with authority. This includes undertaking competitive positioning to ensure that IBM's value proposition and competitive differentiators are clearly communicated. Some professionals will develop and execute sound issues and crisis management programs at speed to reduce any negative impact on IBM's brand and reputation and take part as the representative on the Crisis Management Team at a market or country level. Use social and digital media to amplify IBM's message, including the use of paid media. Show an applied understanding of audience segmentation and messaging development, multichannel communication program design, and content delivery through technology platforms and tools. Set and track progress against communications measures of success for brand, geography, market or country. Develop internal communications campaigns designed to connect all IBMers to the company through our corporate character and the IBM Values and Practices. In collaboration with Human Resources, define and deliver a program of work aimed at driving employee engagement across the IBM workforce in a market or country. Focuses on Individual/Team Objectives and Developing Professional Effectiveness. Skills:\n"
        		+ "\n"
        		+ "Environment's:\n"
        		+ "Professional knowledge related to incumbent's position, team, and department. Requires ability to absorb professional knowledge quickly and develop skills.\n"
        		+ "\n"
        		+ "Communication/Negotiation:\n"
        		+ "Draw upon professional concepts to collaborate with others to carry out assigned duties. Negotiation is required.\n"
        		+ "\n"
        		+ "Problem Solving's:\n"
        		+ "Recognize job-related problems. analyze causes using existing techniques or tools, prepare and recommend solution alternatives. Challenge the validity of given procedures and processes with the intent to enhance and improve. \n"
        		+ "\n"
        		+ "Contribution/Leadership:\n"
        		+ "Works on professional projects;work is often reviewed for developmental purposes. Understand the standard mission of the professional group and vision in own area of competence. May directly influence people in own project. Position may require coordination of activities of less experienced or less knowledgeable team members. \n"
        		+ "\n"
        		+ "Impact on Business/Scope:\n"
        		+ "Accountable for individual or team results. May contribute by supporting activities that are subject to business measurements, impact customer \n"
        		+ "satisfaction, or impact immediate costs or expenses.\"";

        // Replace single quotes with two single quotes
        String modifiedString = originalString.replace("'", "''");

        // Print the modified string
        System.out.println("@@Original String: \n" + originalString);
        System.out.println("@@Modified String: \n" + modifiedString);
        
        System.out.println("Something to trigger a rebuild");

	}
	
	private static JsonNaryTreeNode fromFlatJsonArrayNode(ArrayNode arrayNode, String[] keys, String dummyTopNodeNm) {
		//Stream<?> arrayNodeStream = StreamSupport.stream(arrayNode.spliterator(), false);
		//return processJsonArrayAsStream(arrayNodeStream, keys, dummyTopNodeNm);
		
		JsonNaryTreeNode root = null;
		int nodeLevel=0;
		if (dummyTopNodeNm!=null && dummyTopNodeNm.trim().length()>0) {
			if (root==null) root = new JsonNaryTreeNode(dummyTopNodeNm,dummyTopNodeNm,nodeLevel);
			nodeLevel=1;
		}
		
		String[] currentKey = new String[keys.length];
		JsonNaryTreeNode lastObjectFound=null;
		JsonNaryTreeNode lastRoot=null;
		
		//Process each row returned
		for (JsonNode jsonNode: arrayNode) {
			
			List<JsonNaryTreeNode> nodes = new ArrayList<>();
			//Create a list of objects from the row.
			for (Iterator<String> iter = jsonNode.fieldNames(); iter.hasNext();) {
				String fieldNm = iter.next();
				JsonNode value = jsonNode.get(fieldNm);
				
				int keyPos = isKey(fieldNm,keys);
				if (keyPos>=0) {
					currentKey[keyPos] = value.asText().trim();
					JsonNaryTreeNode node = new JsonNaryTreeNode(value.asText().trim(), getFullKey(currentKey, keyPos),keyPos);
					nodes.add(node);
				}
				else {
					nodes.get(nodes.size()-1).addAttribute(fieldNm, value.asText());
				}
			} //end
			
			//Loop through list of objects and add them to the tax/list as appropriate
			int i = 0;
			for (JsonNaryTreeNode node: nodes) {
				if (root==null) {
					root = node;
					lastObjectFound=node;
					lastRoot=node;
				}
				else {
					JsonNaryTreeNode tempNode = JsonNaryTree.findStatic(node, root, true);
					if (tempNode==null) {
						if (i==0) {
							root.addChild(node);
							lastRoot = node;
						}
						else lastObjectFound.addChild(node);
						if (i < nodes.size()-1) lastObjectFound = node; 
					}
					else {
						if (i < nodes.size()-1) lastObjectFound = tempNode;
					}
				}
				System.out.println(node.toString());
				i++;
			} //end - for (JsonNaryTreeNode node: nodes)
		}
		return root;	
	}
	
	public static String getFullKey(String[] currentKey, int keyPos) {
		String fullKey="";
		for (int i=0; i<=keyPos; i++) fullKey+= (i==0?"":":") + currentKey[i];
		return fullKey;
	}
	
	public static JsonNaryTreeNode processJsonArrayAsStream(Stream<?> list, String[] keys, String dummyTopNodeNm) {
		
		JsonNaryTreeNode root = null;
		int nodeLevel=0;
		if (dummyTopNodeNm!=null && dummyTopNodeNm.trim().length()>0) {
			if (root==null) root = new JsonNaryTreeNode(dummyTopNodeNm,null, nodeLevel);
			nodeLevel=1;
		}
		
		String[] currentKey = new String[keys.length];
		
		list.forEach(item -> {			
			ObjectNode objectNode = (ObjectNode)item;
			
			for (Iterator<String> iter = objectNode.fieldNames(); iter.hasNext();) {
				String fieldNm = iter.next();
				JsonNode value = objectNode.get(fieldNm);
				
				int keyPos = isKey(fieldNm,keys);
				if (keyPos>=0) {
					//Check if node for the key has been created
					if (!value.asText().equals(currentKey[keyPos])) {
						JsonNaryTreeNode node = new JsonNaryTreeNode(value.asText().trim(),null, keyPos);
						//root.addAttribute("X", "Y");
					}
				}
				System.out.println(fieldNm + ":"+value.asText());
			}
			
		});
		return root;
	}
	
	private static int isKey(String fieldNm, String[] keys) {
		for (int i=0; i<keys.length; i++) {
			if (keys[i].trim().equalsIgnoreCase(fieldNm.trim())) return i;
		}
		return -1;
	}
	
    private static void addToHierachy(Node root, Object[] row) {
        Node current = root;

        // Go through each column in the row
        for(Object col : row) {
            // If this column is a string, then it is a Branch node, not a value one
            // (It might be better to iterate through the array using a counter instead 
            //  and change this condition to say "if it isn't the last column"...)
            if(col instanceof String) {
                // Get (or create) the child node for this column
                current = current.getOrCreateChild((String) col);
            } else {
                // Otherwise, set the value
                current.setValue((Integer) col);
            }
        }
    }

    private static JSONArray convertToJSON(Node root) throws JSONException {
        // Use recursion to build the result JSON
        JSONArray array = new JSONArray();

        // Starting at this root, go through all of the child entries
        for(Map.Entry<String, Node> child : root.getChildren().entrySet()) {
            Node childNode = child.getValue();

            // New object for this entry...
            JSONObject object = new JSONObject();
            // Set the name
            object.put("name", child.getKey());

            // Set the value if it is present on this node
            if(childNode.getValue() != null) {
                object.put("value", childNode.getValue());
            }

            // Generate the child hierarchy if it has children
            if(!childNode.getChildren().isEmpty()) {
                JSONArray childHierachy = convertToJSON(childNode);
                object.put("children", childHierachy);
            }

            array.put(object);
        }

        return array;
    }

    // Class used to build the hierarchy
    static class Node {
        // The map of children, LABEL -> NODE
        private Map<String, Node> children = new HashMap<>();
        // The value (kept as null if this does not have a value set)
        private Integer value;

        public Node getOrCreateChild(String key) {
            Node node = children.get(key);

            if(node == null) {
                node = new Node();
                children.put(key, node);
            }

            return node;
        }

        public Map<String, Node> getChildren() {
            return children;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
	
	//public static boolean processStream(List list) {
	//	list.forEach(item -> {
	//		System.out.println(item.toString());
	//	});
	//	return true;
	//}
	
	public static boolean processStream(Stream<?> list) {
		list.forEach(item -> {
			System.out.println("item is instance of "+item.getClass());
			System.out.println(item.toString());
			String[] columns = null;
			if (item instanceof String) {
				columns = Helpers.parseLine((String)item);
			}
			if (columns!=null) {
				for (String column: columns) {
					System.out.println(column);
				}
			}
		});
		return true;
	}
	
	public static <T> String getHeaderRecursive(Class type,List<T> objects,String header) {
		T object = objects.get(0);
		if (((NaryTreeNode) object).getChildren()!=null) {
			Object o = ((NaryTreeNode) object).getChildren().get(0);
			header += getHeaderRecursive(o.getClass(), ((NaryTreeNode) object).getChildren(), header);
		}

        for (Field field : type.getDeclaredFields()) {
        	header+=((header.length()>1?",":"")+field.getName());
        }
        return header;

	}
	
	public static <T> void buildCsvForList(List<T> list) {
		
		for (T obj: list) {
			ArrayList<String> rows = new ArrayList<>();
			Tester.toCsv((NaryTreeNode) obj, rows);
			for (String row: rows) {
				System.out.println(row);
				//printWriter.write(System.lineSeparator());
			}
		}
		
		T object = list.get(0);
		String x = class2CsvString(object);
		
	}
	
    public static <T> String class2CsvString(T object) {
    	StringBuffer sb = new StringBuffer();
    	Field[] fields = object.getClass().getDeclaredFields();
    	for (Field field: fields) {
    		
    	}
    	return sb.toString();
    }
	
    //public static  boolean toCsv(NaryTreeNode root, ArrayList<String> rows) {
    public static <T extends NaryTreeNode> boolean toCsv(T root, ArrayList<String> rows) {
		return toCsv(root, 0, ",", "", true, rows);
	}
	
	public static <T extends NaryTreeNode> boolean toCsv(T root, int level, String delimiter, String key, boolean doubleQuoteStrings, ArrayList<String> rows) {
		
		String encloseString = "";
		if (doubleQuoteStrings) encloseString = "\"";

		if (root == null)
			return false;
		if (delimiter.equals(",")) {
			key+=(key.length()==0?"":delimiter) + encloseString + root.getCode() + encloseString + delimiter + encloseString + root.getDescription() + encloseString ;
		}
		else
			key+=(key.length()==0?"":delimiter) + root.getCode() + delimiter + root.getDescription();
		if (root.getChildren() != null) {
			for (T child : (List<T>)root.getChildren()) {
				System.out.println("**"+child.getClass().getName());
				toCsv(child, level+1, delimiter, key, doubleQuoteStrings, rows);
			}
		}
		else {
			rows.add(key);
		}
		return true;
	}

}
