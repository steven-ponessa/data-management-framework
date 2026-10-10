package com.ibm.wfm.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.beans.taxonomies.TaxonomyBranchStatus;
import com.ibm.wfm.beans.taxonomies.TaxonomyBranchValidationResult;
import com.ibm.wfm.beans.taxonomies.TaxonomyMatchMode;

/**
 * Represents a node within an N-ary taxonomy hierarchy and provides
 * utilities for:
 *
 * <ul>
 *   <li>Node lookup by code and level</li>
 *   <li>Depth-first traversal</li>
 *   <li>Taxonomy branch validation</li>
 *   <li>Node existence validation</li>
 *   <li>Parent-child relationship (arc) validation</li>
 *   <li>Recovery of valid root-to-leaf hierarchy paths</li>
 * </ul>
 *
 * <p>
 * Branch validation distinguishes between invalid nodes and invalid
 * hierarchy relationships, making it suitable for validating
 * Workforce Management Framework (WFM) taxonomies such as JRS,
 * Geography, Skills, and similar hierarchical reference structures.
 * 
 * Note:
 * | Method                    | Expected Input                                        |
 * | ------------------------- | ----------------------------------------------------- |
 * | `validateBranch()`        | Complete root-to-leaf path                            |
 * | `validatePartialBranch()` | Any contiguous branch segment ending at a known level |
 */
public class NaryTreeNode implements NaryTreeNodeInterface<NaryTreeNode> {
	private String code = null;
	private String fullKey = null;
	private String description = null;
	protected int level = -1;
	private ArrayList<NaryTreeNode> children = null;
	//private List<? extends NaryTreeNode> children;
	@JsonIgnore
	private NaryTreeNode parentNode = null;
	
	public static void main (String[] args) {
		
	}

	public NaryTreeNode() {
	}

	public NaryTreeNode(String code) {
		this(code, null, null, -1, null);
	}

	public NaryTreeNode(String code, String description) {
		this(code, description, null, -1, null);
	}

	public NaryTreeNode(String code, String description, String fullKey) {
		this(code, description, fullKey, -1, null);
	}

	public NaryTreeNode(String code, String description, String fullKey, int level) {
		this(code, description, fullKey, level, null);
	}

	public NaryTreeNode(NaryTreeNode n) {
		this(n.getCode(), n.getDescription(), n.getFullKey(), n.getLevel(), n.getParentNode());
	}

	public NaryTreeNode(String code, String description, String fullKey, int level, NaryTreeNode parentNode) {
		this.code = code;
		this.description = description;
		this.fullKey = fullKey;
		this.level = level;
		this.parentNode = parentNode;
	}

	public void println() {
		System.out.println(this.code + " - " + this.description);
	}

	public void addChild(NaryTreeNode n) {
		if (children == null)
			children = new ArrayList<NaryTreeNode>();
		n.setParentNode(this);
		children.add(n);
	}

	@Override
	public List<NaryTreeNode> getChildren() {
		return children;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setChildren(ArrayList<NaryTreeNode> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFullKey() {
		return fullKey;
	}

	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}

	public NaryTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(NaryTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	/**
	 * Performs a depth-first search of this node and its descendants,
	 * returning the first node that matches the supplied predicate.
	 * <p>
	 * The search order is:
	 * <ol>
	 *   <li>Evaluate the current node.</li>
	 *   <li>Recursively evaluate each child subtree in order.</li>
	 * </ol>
	 * The first matching node encountered is returned.
	 *
	 * @param matcher Predicate used to determine whether a node matches.
	 * @return An {@link Optional} containing the first matching node,
	 *         or {@link Optional#empty()} if no matching node exists.
	 */
	public Optional<NaryTreeNode> findFirst(Predicate<NaryTreeNode> matcher) {
	    if (matcher.test(this)) {
	        return Optional.of(this);
	    }

	    if (children == null || children.isEmpty()) {
	        return Optional.empty();
	    }

	    for (NaryTreeNode child : children) {
	        Optional<NaryTreeNode> found = child.findFirst(matcher);
	        if (found.isPresent()) {
	            return found;
	        }
	    }

	    return Optional.empty();
	}
	
	/**
	 * 
	 */
	public Optional<NaryTreeNode> findByValueAndLevel(
	        String value,
	        int level,
	        TaxonomyMatchMode matchMode
	) {
	    return findFirst(node ->
	        matches(node, value, matchMode)
	        && node.getLevel() == level
	    );
	}

	/**
	 * Searches this node and its descendants for a node with the specified
	 * code and hierarchy level.
	 * <p>
	 * Code comparisons are case-insensitive and ignore leading and trailing
	 * whitespace. Level comparisons must match exactly.
	 *
	 * <pre>
	 * Example:
	 *     findByCodeAndLevel("E19", 4)
	 * </pre>
	 *
	 * @param code  Node code to locate.
	 * @param level Expected hierarchy level.
	 * @return An {@link Optional} containing the matching node,
	 *         or {@link Optional#empty()} if no matching node is found.
	 */
	public Optional<NaryTreeNode> findByCodeAndLevel(String code, int level) {
	    return findByValueAndLevel(code, level, TaxonomyMatchMode.CODE);
	}
	public Optional<NaryTreeNode> findByCodeAndLevelOrig(String code, int level) {
	    return findFirst(node ->
	        Objects.equals(normalize(node.getCode()), normalize(code))
	        && node.getLevel() == level
	    );
	}

	/**
	 * Normalizes a code value for case-insensitive comparisons.
	 * <p>
	 * Normalization performs:
	 * <ul>
	 *   <li>Null preservation</li>
	 *   <li>Leading/trailing whitespace removal</li>
	 *   <li>Conversion to upper case using {@link Locale#ROOT}</li>
	 * </ul>
	 *
	 * @param value Value to normalize.
	 * @return Normalized value, or {@code null} if the input was null.
	 */
	private static String normalize(String value) {
	    return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
	}
	
	/**
	 * Validates a taxonomy branch represented as an ordered list of node codes.
	 * <p>
	 * The validation begins at the current node (typically the taxonomy root)
	 * and traverses the hierarchy level-by-level, verifying that:
	 * <ol>
	 *   <li>Each node exists at the expected level.</li>
	 *   <li>Each node is a direct child of the previously matched node.</li>
	 * </ol>
	 *
	 * <p>
	 * Validation distinguishes between:
	 * <ul>
	 *   <li><b>INVALID_NODE</b> - The supplied code does not exist at the expected level.</li>
	 *   <li><b>INVALID_ARC</b> - The supplied code exists at the expected level but is not
	 *       connected to the previously matched node.</li>
	 * </ul>
	 *
	 * <p>
	 * If validation fails, the result contains:
	 * <ul>
	 *   <li>The level at which validation failed.</li>
	 *   <li>The offending code value.</li>
	 *   <li>The portion of the branch successfully matched.</li>
	 *   <li>The valid branch for the leaf node, if one can be located.</li>
	 * </ul>
	 *
	 * <p>
	 * Example valid branch:
	 *
	 * <pre>
	 * 0: 10J00
	 * 1: 15BS
	 * 2: 17OTH
	 * 3: EXE019
	 * 4: E19
	 * 5: 000100-E0007
	 * </pre>
	 *
	 * @param branchCodes Ordered list of node codes representing a path through
	 *                    the taxonomy hierarchy.
	 *
	 * @return A {@link TaxonomyBranchValidationResult} containing validation
	 *         status, diagnostics, and matched branch information.
	 *
	*/
	public TaxonomyBranchValidationResult validateBranch(List<String> branchCodes) {
	    int leafLevel = branchCodes.size() - 1;
	    return validatePartialBranch(branchCodes, leafLevel, TaxonomyMatchMode.CODE);
	}	
	public TaxonomyBranchValidationResult validateBranch(
	        List<String> branchValues,
	        TaxonomyMatchMode matchMode
	) {
	    int leafLevel = branchValues.size() - 1;

	    return validatePartialBranch(branchValues, leafLevel, matchMode);
	}	
	
	/**
	 * Creates a validation result describing either an invalid node or
	 * an invalid parent-child relationship (arc).
	 *
	 * <p>
	 * The method determines whether the failure occurred because:
	 * <ul>
	 *   <li>The node does not exist at the expected level
	 *       ({@link TaxonomyBranchStatus#INVALID_NODE})</li>
	 *   <li>The node exists but is not a child of the previously matched node
	 *       ({@link TaxonomyBranchStatus#INVALID_ARC})</li>
	 * </ul>
	 *
	 * <p>
	 * When possible, the method also attempts to locate the valid taxonomy
	 * branch for the requested leaf node so that callers can compare the
	 * supplied branch with the correct hierarchy.
	 *
	 * @param failedLevel      Hierarchy level where validation failed.
	 * @param failedCode       Node code that caused validation failure.
	 * @param matchedBranch    Successfully matched portion of the branch.
	 * @param inputBranchCodes Original branch supplied for validation.
	 *
	 * @return A populated validation result describing the failure.
	 *
	 */
	private TaxonomyBranchValidationResult invalidNodeOrArc(
	        int failedLevel,
	        String failedValue,
	        List<NaryTreeNode> matchedBranch,
	        List<String> inputBranchValues,
	        TaxonomyMatchMode matchMode
	) {
	    boolean nodeExistsAtLevel =
	        findByValueAndLevel(failedValue, failedLevel, matchMode).isPresent();

	    TaxonomyBranchStatus status = nodeExistsAtLevel
	        ? TaxonomyBranchStatus.INVALID_ARC
	        : TaxonomyBranchStatus.INVALID_NODE;

	    String fieldName = matchMode == TaxonomyMatchMode.CODE
	        ? "code"
	        : "description";

	    String message = nodeExistsAtLevel
	        ? "Node " + fieldName + " exists at level " + failedLevel
	            + " but is not a child of the prior matched node"
	        : "Node " + fieldName + " does not exist at level " + failedLevel;

	    List<NaryTreeNode> validBranchForLeaf =
	        findValidBranchForLeaf(
	            inputBranchValues.get(inputBranchValues.size() - 1),
	            inputBranchValues.size() - 1,
	            matchMode
	        ).orElse(List.of());

	    return new TaxonomyBranchValidationResult(
	        false,
	        status,
	        failedLevel,
	        failedValue,
	        message,
	        List.copyOf(matchedBranch),
	        validBranchForLeaf
	    );
	}
	
	/**
	 * Finds the complete valid taxonomy branch for a specified leaf node.
	 *
	 * <p>
	 * The returned branch contains every node from the taxonomy root to the
	 * specified leaf node, inclusive.
	 *
	 * <p>
	 * This method is commonly used when branch validation fails in order to
	 * return the correct hierarchy for comparison.
	 *
	 * <pre>
	 * Example:
	 *
	 * Leaf:
	 *     000100-E0007
	 *
	 * Returns:
	 *     10J00
	 *       -> 15BS
	 *       -> 17OTH
	 *       -> EXE019
	 *       -> E19
	 *       -> 000100-E0007
	 * </pre>
	 *
	 * @param leafCode  Code of the leaf node.
	 * @param leafLevel Expected hierarchy level of the leaf node.
	 *
	 * @return An {@link Optional} containing the complete root-to-leaf path,
	 *         or {@link Optional#empty()} if the leaf cannot be found.
	 *
	*/
	public Optional<List<NaryTreeNode>> findValidBranchForLeaf(
	        String leafValue,
	        int leafLevel,
	        TaxonomyMatchMode matchMode
	) {
	    return findValidBranchEndingAt(leafValue, leafLevel, matchMode);
	}
	/*
	 * orig
	 *
	public Optional<List<NaryTreeNode>> findValidBranchForLeafOrig(
	        String leafCode,
	        int leafLevel
	) {
	    return findValidBranchEndingAt(leafCode, leafLevel);
	}
	*/	

	/**
	 * Performs a depth-first search of the taxonomy hierarchy to locate a
	 * target node and build the complete path from the root node.
	 *
	 * <p>
	 * The supplied path list acts as a traversal stack:
	 * <ul>
	 *   <li>Nodes are added as recursion descends.</li>
	 *   <li>Nodes are removed during backtracking when a branch does not
	 *       contain the target.</li>
	 * </ul>
	 *
	 * <p>
	 * Upon successful completion, the path contains the ordered root-to-node
	 * hierarchy.
	 *
	 * @param node         Current node being evaluated.
	 * @param targetCode   Target node code.
	 * @param targetLevel  Target hierarchy level.
	 * @param path         Mutable path accumulator.
	 *
	 * @return {@code true} if the target node was found; otherwise
	 *         {@code false}.
	 *	
	*/
	private boolean findPath(
	        NaryTreeNode node,
	        String targetCode,
	        int targetLevel,
	        List<NaryTreeNode> path
	) {
	    path.add(node);

	    if (Objects.equals(normalize(node.getCode()), targetCode)
	            && node.getLevel() == targetLevel) {
	        return true;
	    }

	    List<NaryTreeNode> children = node.getChildren();

	    if (children != null) {
	        for (NaryTreeNode child : children) {
	            if (findPath(child, targetCode, targetLevel, path)) {
	                return true;
	            }
	        }
	    }

	    path.remove(path.size() - 1);
	    return false;
	}	
	
	/**
	 * Validates a partial taxonomy branch that terminates at a known leaf level.
	 *
	 * <p>
	 * Unlike {@link #validateBranch(List)}, which expects a complete root-to-leaf
	 * path, this method validates a branch fragment whose starting level is
	 * inferred from the supplied leaf level.
	 *
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Full taxonomy:
	 *
	 * 0 Brand
	 * 1 Growth Platform
	 * 2 Service Line
	 * 3 Practice
	 * 4 Service Area
	 * 5 JRS
	 *
	 * Input:
	 *     [17OTH, EXE019, E19]
	 *     leafLevel = 4
	 *
	 * Inferred levels:
	 *     2 Service Line = 17OTH
	 *     3 Practice     = EXE019
	 *     4 Service Area = E19
	 * </pre>
	 *
	 * <p>
	 * Validation performs the following checks:
	 * <ol>
	 *   <li>Determines the starting level based on the supplied leaf level.</li>
	 *   <li>Locates the starting node within the taxonomy.</li>
	 *   <li>Traverses downward through the hierarchy validating each child node.</li>
	 *   <li>Distinguishes between invalid nodes and invalid parent-child
	 *       relationships (arcs).</li>
	 * </ol>
	 *
	 * <p>
	 * If validation fails, the result contains:
	 * <ul>
	 *   <li>The level at which validation failed.</li>
	 *   <li>The offending code value.</li>
	 *   <li>The successfully matched portion of the branch.</li>
	 *   <li>The valid taxonomy branch for the supplied leaf node when available.</li>
	 * </ul>
	 *
	 * @param branchCodes Ordered list of node codes representing a partial
	 *                    taxonomy branch.
	 * @param leafLevel   Hierarchy level of the final node in the branch.
	 *
	 * @return A {@link TaxonomyBranchValidationResult} containing validation
	 *         status and diagnostic information.
	 */	
	public TaxonomyBranchValidationResult validatePartialBranch(
	        List<String> branchValues,
	        int leafLevel
	) {
	    return validatePartialBranch(branchValues, leafLevel, TaxonomyMatchMode.CODE);
	}
	public TaxonomyBranchValidationResult validatePartialBranch(
	        List<String> branchValues,
	        int leafLevel,
	        TaxonomyMatchMode matchMode
	) {
	    if (branchValues == null || branchValues.isEmpty()) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.EMPTY_INPUT,
	            -1,
	            null,
	            "Branch input is empty",
	            List.of(),
	            List.of()
	        );
	    }

	    int startLevel = leafLevel - branchValues.size() + 1;

	    if (startLevel < 0) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.INVALID_NODE,
	            startLevel,
	            null,
	            "Branch contains more levels than possible for the supplied leaf level",
	            List.of(),
	            List.of()
	        );
	    }

	    String firstValue = branchValues.get(0);

	    Optional<List<NaryTreeNode>> matchingPath =
	        findValidBranchEndingAt(firstValue, startLevel, matchMode);

	    if (matchingPath.isEmpty()) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.INVALID_NODE,
	            startLevel,
	            firstValue,
	            "Starting node does not exist at level " + startLevel,
	            List.of(),
	            List.of()
	        );
	    }

	    List<NaryTreeNode> fullPathToStart = matchingPath.get();
	    NaryTreeNode current = fullPathToStart.get(fullPathToStart.size() - 1);

	    List<NaryTreeNode> matchedBranch = new ArrayList<>();
	    matchedBranch.add(current);

	    for (int i = 1; i < branchValues.size(); i++) {
	        int expectedLevel = startLevel + i;
	        String expectedValue = branchValues.get(i);

	        Optional<NaryTreeNode> matchingChild =
	            findMatchingChild(current, expectedValue, expectedLevel, matchMode);

	        if (matchingChild.isPresent()) {
	            current = matchingChild.get();
	            matchedBranch.add(current);
	            continue;
	        }

	        boolean nodeExistsAtLevel =
	            findByValueAndLevel(expectedValue, expectedLevel, matchMode).isPresent();

	        TaxonomyBranchStatus status = nodeExistsAtLevel
	            ? TaxonomyBranchStatus.INVALID_ARC
	            : TaxonomyBranchStatus.INVALID_NODE;

	        String fieldName = matchMode == TaxonomyMatchMode.CODE ? "code" : "description";

	        String message = nodeExistsAtLevel
	            ? "Node " + fieldName + " exists at level " + expectedLevel
	                + " but is not a child of the prior matched node"
	            : "Node " + fieldName + " does not exist at level " + expectedLevel;

	        List<NaryTreeNode> validBranchForLeaf =
	            findValidBranchForLeaf(
	                branchValues.get(branchValues.size() - 1),
	                leafLevel,
	                matchMode
	            ).orElse(List.of());

	        return new TaxonomyBranchValidationResult(
	            false,
	            status,
	            expectedLevel,
	            expectedValue,
	            message,
	            List.copyOf(matchedBranch),
	            validBranchForLeaf
	        );
	    }

	    return new TaxonomyBranchValidationResult(
	        true,
	        TaxonomyBranchStatus.VALID,
	        -1,
	        null,
	        "Branch is valid",
	        List.copyOf(matchedBranch),
	        findValidBranchForLeaf(
	            branchValues.get(branchValues.size() - 1),
	            leafLevel,
	            matchMode
	        ).orElse(List.copyOf(matchedBranch))
	    );
	}	
	


	/**
	 * Searches the direct children of a parent node for a child having the
	 * specified code and hierarchy level.
	 *
	 * <p>
	 * Code comparisons are case-insensitive and ignore leading and trailing
	 * whitespace. Level comparisons must match exactly.
	 *
	 * <p>
	 * This method validates a single parent-child relationship and is used
	 * during branch validation to determine whether a node is reachable from
	 * the currently matched node.
	 *
	 * @param parent Parent node whose immediate children will be searched.
	 * @param code   Expected child node code.
	 * @param level  Expected child hierarchy level.
	 *
	 * @return An {@link Optional} containing the matching child node if found;
	 *         otherwise {@link Optional#empty()}.
	 */	
	private Optional<NaryTreeNode> findMatchingChild(
	        NaryTreeNode parent,
	        String value,
	        int level,
	        TaxonomyMatchMode matchMode
	) {
	    List<NaryTreeNode> children = parent.getChildren();

	    if (children == null || children.isEmpty()) {
	        return Optional.empty();
	    }

	    return children.stream()
	        .filter(child ->
	            matches(child, value, matchMode)
	            && child.getLevel() == level
	        )
	        .findFirst();
	}
	/*
	 * orig
	 */
	private Optional<NaryTreeNode> findMatchingChild(
	        NaryTreeNode parent,
	        String code,
	        int level
	) {
	    List<NaryTreeNode> children = parent.getChildren();

	    if (children == null || children.isEmpty()) {
	        return Optional.empty();
	    }

	    return children.stream()
	        .filter(child ->
	            Objects.equals(normalize(child.getCode()), normalize(code))
	            && child.getLevel() == level
	        )
	        .findFirst();
	}
	
	/**
	 * Finds the complete taxonomy path ending at the specified node.
	 *
	 * <p>
	 * The search locates the first node matching the supplied code and level
	 * and returns the full hierarchy path from the taxonomy root to that node.
	 *
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Input:
	 *     code  = "E19"
	 *     level = 4
	 *
	 * Result:
	 *     Brand
	 *       -> Growth Platform
	 *       -> Service Line
	 *       -> Practice
	 *       -> E19
	 * </pre>
	 *
	 * <p>
	 * This method is commonly used to:
	 * <ul>
	 *   <li>Locate the starting point for partial branch validation.</li>
	 *   <li>Retrieve the valid hierarchy for diagnostic reporting.</li>
	 *   <li>Resolve a node's complete ancestry.</li>
	 * </ul>
	 *
	 * @param code  Node code to locate.
	 * @param level Expected hierarchy level of the node.
	 *
	 * @return An {@link Optional} containing the root-to-node path if found;
	 *         otherwise {@link Optional#empty()}.
	 */	
	/*
	 * Orig
	 */
	public Optional<List<NaryTreeNode>> findValidBranchEndingAt(
	        String value,
	        int level,
	        TaxonomyMatchMode matchMode
	) {
	    List<NaryTreeNode> path = new ArrayList<>();

	    boolean found = findPath(this, value, level, matchMode, path);

	    return found ? Optional.of(path) : Optional.empty();
	}
	public Optional<List<NaryTreeNode>> findValidBranchEndingAtOrig(
	        String code,
	        int level
	) {
	    List<NaryTreeNode> path = new ArrayList<>();

	    boolean found = findPath(this, normalize(code), level, path);

	    return found ? Optional.of(path) : Optional.empty();
	}	
	
	/*
	 * description-specific convenience method
	 */
	public Optional<NaryTreeNode> findByDescriptionAndLevel(String description, int level) {
	    return findByValueAndLevel(description, level, TaxonomyMatchMode.DESCRIPTION);
	}
	
	/*
	 * 
	 */
	private boolean findPath(
	        NaryTreeNode node,
	        String targetValue,
	        int targetLevel,
	        TaxonomyMatchMode matchMode,
	        List<NaryTreeNode> path
	) {
	    path.add(node);

	    if (matches(node, targetValue, matchMode)
	            && node.getLevel() == targetLevel) {
	        return true;
	    }

	    List<NaryTreeNode> children = node.getChildren();

	    if (children != null) {
	        for (NaryTreeNode child : children) {
	            if (findPath(child, targetValue, targetLevel, matchMode, path)) {
	                return true;
	            }
	        }
	    }

	    path.remove(path.size() - 1);
	    return false;
	}
	
	/*
	 * matches
	 */
	private static boolean matches(
	        NaryTreeNode node,
	        String expectedValue,
	        TaxonomyMatchMode matchMode
	) {
	    String actualValue = switch (matchMode) {
	        case CODE -> node.getCode();
	        case DESCRIPTION -> node.getDescription();
	    };

	    return Objects.equals(
	        normalize(actualValue),
	        normalize(expectedValue)
	    );
	}
	
	/*
	 * validateBranchByLevels
	 */
	public TaxonomyBranchValidationResult validateBranchByLevels(
		    List<String> branchValues,
		    List<Integer> branchLevels,
		    TaxonomyMatchMode matchMode
	) {
	    if (branchValues == null || branchLevels == null || branchValues.isEmpty()) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.EMPTY_INPUT,
	            -1,
	            null,
	            "Branch input is empty",
	            List.of(),
	            List.of()
	        );
	    }

	    if (branchValues.size() != branchLevels.size()) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.INVALID_NODE,
	            -1,
	            null,
	            "branchValues and branchLevels must have the same size",
	            List.of(),
	            List.of()
	        );
	    }

	    List<NaryTreeNode> matchedBranch = new ArrayList<>();

	    Optional<List<NaryTreeNode>> startPath =
	        findValidBranchEndingAt(
	            branchValues.get(0),
	            branchLevels.get(0),
	            matchMode
	        );

	    if (startPath.isEmpty()) {
	        return new TaxonomyBranchValidationResult(
	            false,
	            TaxonomyBranchStatus.INVALID_NODE,
	            branchLevels.get(0),
	            branchValues.get(0),
	            "Starting node does not exist at level " + branchLevels.get(0),
	            List.of(),
	            List.of()
	        );
	    }

	    NaryTreeNode current = startPath.get().get(startPath.get().size() - 1);
	    matchedBranch.add(current);

	    for (int i = 1; i < branchValues.size(); i++) {
	        String expectedValue = branchValues.get(i);
	        int expectedLevel = branchLevels.get(i);

	        Optional<List<NaryTreeNode>> descendantPath =
	            findDescendantPath(
	                current,
	                expectedValue,
	                expectedLevel,
	                matchMode
	            );

	        if (descendantPath.isPresent()) {
	            List<NaryTreeNode> path = descendantPath.get();

	            // Add only the requested matching node, not skipped intermediates.
	            NaryTreeNode matchedNode = path.get(path.size() - 1);
	            matchedBranch.add(matchedNode);

	            current = matchedNode;
	            continue;
	        }

	        boolean nodeExistsAtLevel =
	            findByValueAndLevel(expectedValue, expectedLevel, matchMode).isPresent();

	        TaxonomyBranchStatus status = nodeExistsAtLevel
	            ? TaxonomyBranchStatus.INVALID_ARC
	            : TaxonomyBranchStatus.INVALID_NODE;

	        String fieldName = matchMode == TaxonomyMatchMode.CODE
	            ? "code"
	            : "description";

	        String message = nodeExistsAtLevel
	            ? "Node " + fieldName + " exists at level " + expectedLevel
	                + " but is not a descendant of the prior matched node"
	            : "Node " + fieldName + " does not exist at level " + expectedLevel;

	        List<NaryTreeNode> validBranchForLeaf =
	            findValidBranchForLeaf(
	                branchValues.get(branchValues.size() - 1),
	                branchLevels.get(branchLevels.size() - 1),
	                matchMode
	            ).orElse(List.of());

	        return new TaxonomyBranchValidationResult(
	            false,
	            status,
	            expectedLevel,
	            expectedValue,
	            message,
	            List.copyOf(matchedBranch),
	            validBranchForLeaf
	        );
	    }

	    return new TaxonomyBranchValidationResult(
	        true,
	        TaxonomyBranchStatus.VALID,
	        -1,
	        null,
	        "Branch is valid",
	        List.copyOf(matchedBranch),
	        findValidBranchForLeaf(
	            branchValues.get(branchValues.size() - 1),
	            branchLevels.get(branchLevels.size() - 1),
	            matchMode
	        ).orElse(List.copyOf(matchedBranch))
	    );
	}	
	
	/*
	 * findDependentPath
	 */
	private Optional<List<NaryTreeNode>> findDescendantPath(
	        NaryTreeNode startNode,
	        String targetValue,
	        int targetLevel,
	        TaxonomyMatchMode matchMode
	) {
	    List<NaryTreeNode> path = new ArrayList<>();

	    boolean found = findDescendantPath(
	        startNode,
	        targetValue,
	        targetLevel,
	        matchMode,
	        path
	    );

	    return found ? Optional.of(path) : Optional.empty();
	}
	
	private boolean findDescendantPath(
	        NaryTreeNode node,
	        String targetValue,
	        int targetLevel,
	        TaxonomyMatchMode matchMode,
	        List<NaryTreeNode> path
	) {
	    path.add(node);

	    if (matches(node, targetValue, matchMode)
	            && node.getLevel() == targetLevel) {
	        return true;
	    }

	    List<NaryTreeNode> children = node.getChildren();

	    if (children != null) {
	        for (NaryTreeNode child : children) {
	            if (findDescendantPath(
	                    child,
	                    targetValue,
	                    targetLevel,
	                    matchMode,
	                    path
	            )) {
	                return true;
	            }
	        }
	    }

	    path.remove(path.size() - 1);
	    return false;
	}

}
