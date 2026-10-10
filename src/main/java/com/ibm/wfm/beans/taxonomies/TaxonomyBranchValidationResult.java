package com.ibm.wfm.beans.taxonomies;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.wfm.beans.NaryTreeNode;

public record TaxonomyBranchValidationResult(
		boolean valid,
		TaxonomyBranchStatus status,
		int failedLevel,
		String failedCode,
		String message,
		List<NaryTreeNode> matchedBranch,
		List<NaryTreeNode> validBranchForLeaf
	) {
	public String validBranchAsText(String delimiter) {
	    if (validBranchForLeaf == null || validBranchForLeaf.isEmpty()) {
	        return "";
	    }

	    return validBranchForLeaf.stream()
	        .map(node ->
	            "(" + nullToBlank(node.getCode()) + ") "
	            + nullToBlank(node.getDescription())
	        )
	        .collect(Collectors.joining(delimiter));
	}
	
	public String[] validBranch2CdDescArray(List<NaryTreeNode> nodes) {
	    if (nodes == null || nodes.isEmpty()) {
	        return new String[0];
	    }

	    return nodes.stream()
	        .flatMap(node -> Stream.of(
	            nullToBlank(node.getCode()),
	            nullToBlank(node.getDescription())
	        ))
	        .toArray(String[]::new);
	}
	
	private static String nullToBlank(String value) {
	    return value == null ? "" : value;
	}
}