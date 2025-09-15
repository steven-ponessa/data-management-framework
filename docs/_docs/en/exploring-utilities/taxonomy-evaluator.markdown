---
title: Taxonomy Evaluator
permalink: /en/exploring-utilities/taxonomy-evaluator
abstract: >- # this means to ignore newlines until "baseurl:"
  Taxonomies are a means of organize and classify things and are common in government, science, and business. Corporate taxonomies are the hierarchical classification of entities of interest to an enterprise, organization or administration, used to classify products, services, customers, people, documents, digital assets and other information.  This system allows a collection of data that exist in operational systems to be evaluated against a standardized taxonomy, of arbitrary complexity, and identify nodes and relationship between nodes (arcs) that are not contained within the given taxonomy.
---

## Summary

One way that enterprises derive value from the data that abounds within their organizations is by logically and consistently categorizing and sub-categorizing the data within structures that represent how their organizations do or should operate.  These resulting taxonomies become a common vocabulary for the collection of concepts that are organized in a hierarchy, based on the hierarchical relationship between the terms.

Once these taxonomies are defined, organizations often have to verify that data within their operational systems aligns to the structure of the taxonomies.  This data integrity checking process is complex since it has to check if a node itself is valid and if the node is valid within the context of its parent nodes or lineage.  The complexity increases as the taxonomies themselves become more and more complex.

This method efficiently evaluates whether a collection of related data items matches a branch within a standardized taxonomy.  Additionally, for each input branch that is in some way invalid, the method reports back exactly which nodes and lineages are not valid compared to the standardized taxonomy.

## Algorithm Overview
The solution receives as input, at a minimum,
- Records that make up the taxonomy (key, value(s), and parentage)
- Records containing the data being validated.  This data must contain the keys for the nodes that make up the taxonomy branches being validated.
- List of positions or attribute names of the keys for the nodes, within the data being validated, that make up the taxonomy branches being validated.

The solution first reads a serialized taxonomy, from a varying number of source formats, and populates an N-array tree.  N-array, also referred to as n-ary, trees are a collection of nodes where each node is a data structure that consists of data and a list of references to its children nodes.   The “n” in the n-ary tree refers to the fact that each node may have as many children as is required.  The n-ary tree may also go down as many levels as are required to support the structure it is representing or modeling.

To support easy navigation back up through any given branch, the n-array tree also contains a reference back to its immediate parent (null for the case of the root node) and an attribute that contains the concatenated key of all parent nodes.

Once the taxonomy has been loaded, the method reads through a second set of data records, that also can be in a varying number of source formats (e.g., JSON, CSV, Excel), where each record contains attributes that represent nodes within the taxonomy. 

Using the list of positions or attribute names, the method builds the branch that will be validated against the taxonomy.

Thereafter, a series of searches are performed.  The method uses a recursive decent parsing algorithm to navigate through the tree looking for nodes, branches, or sub-branches; exiting the recursion when a node or branch is found.  The recursive decent and references maintained to children nodes optimizes the performance.  Additionally, the search may be performed as “depth-first” (top to bottom, left to right) or “breath-first” (left to right, top to bottom), depending on the distribution of node values. If the node distribution is more likely to have valid branches, a depth-first decent is optimal.  If the data integrity tends to degrade at the lower levels of the taxonomy, breath-first is optimal.  If the regression method is not explicitly requested at invocation time, the method begins with depth-first decent, monitoring the rate that branches and nodes within those branches are found.  If it determines that a high percentage of upper nodes are found compared to leaf nodes, it switches the decent strategy to “breath-first”.

Starting from the leaf node of the branch being searched for, the method performs a search using the full key, which is a concatenation of all the keys within the branch.  If this is found, the record is marked found and the process moves onto the next record.  If it is not found, it checks if the node itself is valid.  Once it either finds or fails to find the node, it then repeats for the leaf minus 1 level, until it either finds a sub-branch or reaches the leaf node.  As the process finds or doesn’t find nodes and arcs, it records this to report back.


## Example

The following example sets up a sample standard/control taxonomy that is implemented as a n-ary tree with valid nodes a thru x.  Nodes y and z are intentionally omitted to serve as erroneous values within a branch.  Thereafter, a collection of records is processed where the offsets or column/attribute names that correspond to the keys for nodes at different levels of the taxonomy (i.e., branch) have been pre-established (i.e., passed in as a parameter).  For each record in the collection, the process will build a branch based on the pre-established offsets or names and evaluate if the branch exists and, if not, it will evaluate and record which nodes and arcs of the branch are valid and invalid.

{: style="text-align: center;" }
![Example Taxonomy]({{ site.baseurl }}/assets/images/docs/taxonomy-ex1.svg){: style="width: 75%;"}

The diagram above shows a sample taxonomy of nodes with key values a-x.  Below is a sample of records that could be processed.  Within the context of this method and diagram, nodes are represented as the circles and arcs are the lines that connect them.  We also consider the n-ary tree starting at level 0 and descending downward to level n, in this diagram level 3.

{: style="text-align: center;" }
![Example Taxonomy]({{ site.baseurl }}/assets/images/docs/taxonomy-ex2.svg){: style="width: 75%;"}

The next diagram shows a set of sample branches that may be contained in the data being validated.

![Example Taxonomy]({{ site.baseurl }}/assets/images/docs/taxonomy-ex3.svg){: style="width: 100%;"}

**Record 1**  
This record contains the key for nodes 0-3 as a, b, e, and n.  This branch is found within the taxonomy and the method returns that it was found.

**Record 2**  
This record contains the key for nodes 0-3 as a, b, e, and p.  This branch is not found within the taxonomy.  The method finds and reports back that all the nodes (a, b, e, and p) are valid, the keys for the branch containing the leaf node being searched (a, b, f, and p), the lowest valid code (based on the nodes and arcs: a, b, and e), and that there were no invalid nodes and a single invalid arc at level 2 (between e and p).

**Record 3**  
This record contains the key for nodes 0-3 as a, b, e, and y.  This branch is not found within the taxonomy.  The method reports back that the node at level 3 (y) is invalid, the lowest valid node (based on the nodes and arcs: a, b, and e), and that there is an invalid node at level 3 (y) and a single invalid arc at level 2 (between e and non-existent node y).

**Record 4**   
This record contains the key for nodes 0-3 as a, b, y, and p.  This branch is not found within the taxonomy.  The method reports back that the node at level 2 (y) is invalid, the keys for the branch containing the leaf node being searched (a, b, f, and p), the lowest valid node (based on the nodes and arcs: a and b), and that there is invalid node at level 2 (y) and two invalid arcs at level 1 (between b and non-existent node y) and level 2 (between y-p)).

**Recording invalid nodes and arcs**  
<p>The solution needed to be able to identify and record instances of invalid nodes and arcs, however, since there is no limit to the depth (or height) of the tree supporting the taxonomy, it needed a flexible and efficient way to record these occurrences.  To accomplish this, the solution sets node and arc errors as 2<sup><b>l</b></sup> where <b>l</b> is the level where the error has been identified and adds that value to a variable that accumulates these values for nodes and arcs.  When processing for a particular branch completes, the value for the node and arc error accumulator will either be zero (no errors) or a positive integer.  By making the error power of 2, the bitwise AND (&) operator can be used to determine the levels with invalid nodes or arcs. </p>


![Level Error Codes]({{ site.baseurl }}/assets/images/docs/level-error-codes.svg){: #custom-id style="text-align: center; width: 50%;"}

Looking again at the example taxonomy, we see the following levels and error codes for each.

![Example Taxonomy]({{ site.baseurl }}/assets/images/docs/taxonomy-ex4.svg){: #custom-id style="text-align: center; width: 100%;"}

<p>Therefore, when a node or arc error is discovered 2<sup><i>level</i></sup> is added to the node or arc error accumulator. Thereafter, the object that holds the evaluation results for the branch contains a method report if a node or arc level is valid or invalid based on the accumulator (status) and the level being asked about.</p>


<pre name="code" class="java">

public boolean isLevelValid(int level, int status) {
  int levelValue = (int)Math.pow(2,level);
  if ((status & levelValue) == levelValue) return false;
  else return true;
}

</pre>

<p>This method takes the accumulated sum of errors and the level code being interrogated and uses a bitwise AND (&) comparator.  If the bitwise operator returns the level value (2<sup>level</sup>) then the node or arc contains an error at that level.</p>

<p>Suppose the branch for a record has invalid nodes at level 1 and 2:<br/>

invalidNodeAccumulator = 2<sup>1</sup> + 2<sup>2</sup> = 2 (0010) + 4 (0100) = <b>6</b> (0110)</p>

![Level Error Codes]({{ site.baseurl }}/assets/images/docs/level-error-codes-example.svg){: #custom-id style="text-align: center; width: 65%;"}