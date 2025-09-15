package com.ibm.wfm.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import org.hibernate.mapping.Map;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.JsonNaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNode;

public class JsonNaryTree {
	
	public static JsonNaryTreeNode findStatic(JsonNaryTreeNode keyNode, JsonNaryTreeNode root, boolean useFullKey) {
		LinkedList<JsonNaryTreeNode> stack = new LinkedList<>();
		LinkedList<JsonNaryTreeNode> list = new LinkedList<>();
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
			JsonNaryTreeNode node = stack.pollLast();
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
	
				for (JsonNaryTreeNode item : node.getChildren()) {
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
	
	public static boolean toCsvHeader(JsonNaryTreeNode root, int level, String delimiter, String header, ArrayList<String> rows) throws IllegalArgumentException, IllegalAccessException {

		Field[] fields = root.getClass().getDeclaredFields();
		for (Field field: fields) {
			//if (field.getName().endsWith("Desc")) descriptionNm=field.getName();
			if (field.getType()==ArrayList.class || field.getType()==JsonNaryTreeNode.class) {}
			else {
				DbColumn dbColumn = field.getAnnotation(DbColumn.class);
				
				if (dbColumn==null || (!dbColumn.isId() && dbColumn.foreignKeySeq()<1)) {
					if (field.getName().equals("attributes")) {
						System.out.println(field.getName());
						field.setAccessible(true);
						
						//HashMap myMap = (HashMap)field.get(new HashMap());
					}
					else header+= (header.trim().length()>0?",":"")+field.getName();
				}
			}
		}
		
		if (root.getChildren() != null) {
			toCsvHeader(root.getChildren().get(0), level+1, delimiter, header, rows);
		}
		else {
			rows.add(header);
		}
		return true;		
	}
	
	public void toCsv(JsonNaryTreeNode root, StringBuffer csv) {
		if (root == null)
			return;
		csv.append(root.getCode()); //+"\t"+root.getDescription());
		if (root.getAttributes().size()>0) {
			Collection<Object> values = root.getAttributes().values();
			ArrayList<Object> al = new ArrayList<>(values);
			Set<String> keys = root.getAttributes().keySet();

			// Get keys and values
			for (Object value : al) {
				csv.append("\t").append(String.valueOf(value));
			}
			
		}
		if (root.getChildren() != null) {
			csv.append("\t");
			for (JsonNaryTreeNode child : root.getChildren()) {
				toCsv(child, csv);
			}
		}
		csv.append(System.lineSeparator());
	}	
	
	public static boolean toCsv(JsonNaryTreeNode root, ArrayList<String> rows) {
		return toCsv(root, 0, ",", "", true, rows);
	}
	
	public static boolean toCsv(JsonNaryTreeNode root, int level, String delimiter, String key, boolean doubleQuoteStrings, ArrayList<String> rows) {
		
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
			for (JsonNaryTreeNode child : root.getChildren()) {
				toCsv(child, level+1, delimiter, key, doubleQuoteStrings, rows);
			}
		}
		else {
			rows.add(key);
		}
		return true;
	}
}
