function InteractiveD3TreeObj(treeHeight = 500, treeWidth = 1700, nodeSpacing = 210) {

    /**=========================================================================
     * Set up global varialbe, tree initial size and offset and build tree
     * 
     * @author  Steven J. Ponessa
     */
    let margin = { top: 0, right: 90, bottom: 0, left: 50 },
        width = treeWidth - margin.right - margin.left,
        height = treeHeight - margin.top - margin.bottom,
        nodeSpace = nodeSpacing;
  
  
    let i = 0,
        duration = 750,
        root, tree, tooltipDiv, diagonal, svg;
  
    let excludeProperties = ['code', 'description', 'effTms', 'expirTms', 'rowStatusCd', 'parent', 'x', 'y', 'id', 'x0', 'y0']
  
    let select2Data = [];
    let select2DataObject = [];
  
    let id3Tree = function (selection) {
        selection.each(function (data) {
  
            tree = d3.layout.tree()
                .size([height, width]);
  
            //Add tooltip div
            tooltipDiv = d3.select("body").append("div")
                .attr("class", "tooltip")
                .style("opacity", 1e-6);
  
            diagonal = d3.svg.diagonal()
                .projection(function (d) { return [d.y, d.x]; });
  
            svg = selection.append("svg")
                .attr("width", width + margin.right + margin.left)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
  
            root = Array.isArray(data) ? data[0] : data;
            root.x0 = height / 2;
            root.y0 = 0;
  
            root.children.forEach(collapse);
            //this.update(root);
            update(root);
        })
    }
  
    function clearAll(d) {
        d.class = "";
        if (d.children)
            d.children.forEach(clearAll);
        else if (d._children)
            d._children.forEach(clearAll);
    }
  
  
    /**=========================================================================
     * function recursively clears all class names for all nodes in the tree
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
  
  
    /**=========================================================================
     * function recursively collapses the node and all children branches (using 
     * recursion). 
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function collapse(d) {
        if (d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }
  
    /**=========================================================================
     * function recursively collapse all nodes that where not "found" (i.e., 
     * have the class name of "found".
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function collapseAllNotFound(d) {
        if (d.children) {
            if (d.class !== "found") {
                d._children = d.children;
                d._children.forEach(collapseAllNotFound);
                d.children = null;
            } else
                d.children.forEach(collapseAllNotFound);
        }
    }
  
    /**=========================================================================
     * function recursively expand the node and all children branches (using 
     * recursion). 
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function expandAll(d) {
        if (d._children) {
            d.children = d._children;
            d.children.forEach(expandAll);
            d._children = null;
        } else if (d.children)
            d.children.forEach(expandAll);
    }
  
    /**=========================================================================
     * function toggle children on click. This is performed by simply swapping
     * the .children (expanded/displayed) and ._children (collapsed/hidden)
     * attribute values and setting the other to null.
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function toggle(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        }
        else {
            d.children = d._children;
            d._children = null;
        }
        clearAll(root);
        update(d);
    }
  
  
    /**=========================================================================
     * function show the node's tooltip popup panel
     * 
     * @author  Steven J. Ponessa
     * @returns void
     */
    function mouseover() {
        tooltipDiv.transition()
            .duration(300)
            .style("opacity", 1);
    }
  
    /**=========================================================================
     * function populate and position the node's tooltip popup panel
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function mousemove(d) {
        let popupTxt = '<b>';
        if (typeof (d.code) !== "undefined") {
            popupTxt += '(' + d.code
            if (typeof (d.usageRule) !== "undefined" && typeof (d.complianceCd) !== "undefined" && d.complianceCd != null && d.usageRule == 'CCR') {
                popupTxt += '/' + d.complianceCd + '*';
            }
            else if (typeof (d.complianceCd) !== "undefined" && d.complianceCd != null) {
                popupTxt += '/' + d.complianceCd;
            }
            popupTxt += ") "
        };
        popupTxt += d.name || d.description;
        popupTxt += '</b>'
  
        const props = Object.getOwnPropertyNames(d);
        props.forEach(prop => {
            if (!(excludeProperties.includes(prop))) {
                if (prop == '_children' && d[prop] && d[prop].length)
                    popupTxt += "<br/>" + prop + " : " + d[prop].length
                else if (prop == 'children' && d[prop] && d[prop].length)
                    popupTxt += "<br/>" + prop + " : " + d[prop].length
                else
                    popupTxt += "<br/>" + prop + " : " + d[prop]
            }
        })
  
  
        tooltipDiv[0][0].innerHTML = popupTxt;
        tooltipDiv
            //.text(popupTxt)
            .style("left", (d3.event.pageX) + "px")
            .style("top", (d3.event.pageY) + "px");
    }
  
    /**=========================================================================
     * function hide the node's tooltip popup panel
     * 
     * @author  Steven J. Ponessa
     * @returns void
     */
    function mouseout() {
        tooltipDiv.transition()
            .duration(300)
            .style("opacity", 1e-6);
    }
  
    /**=========================================================================
     * function resets the tree to its initial state
     *
     * @author  Steven J. Ponessa
     * @returns void
     */
    function resetTree() {
        clearAll(root);
        collapse(root);
        toggle(root);
        update(root);
    }
  
    /**=========================================================================
     * function recursively searches d3 tree based on the name contained in the 
     * search field. The search can either be based on an exact match or a 
     * RegEx pattern (i.e., containing).  If a node is found it sets the found
     * node and each of the found node's ancestors (baased on the node's parent
     * attribute) with the class name "found".  This class name is used by
     * the `update()` function to set set all nodes and leading arcs as red.
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node
     * @returns void
     */
    function searchTree(d, searchText = "", searchField = "d.name", matchType = 0, regexFlags = '') {
        if (d.children)
            d.children.forEach(child => searchTree(child, searchText, searchField, matchType, regexFlags));
        else if (d._children)
            d._children.forEach(child => searchTree(child, searchText, searchField, matchType, regexFlags));
        var searchFieldValue = eval(searchField);
  
        var criterias = [];
        if (Array.isArray(searchText))
            criterias = searchText;
        else
            criterias.push(searchText);
  
        for (criteria of criterias) {
  
            let theCriteria, theMatchType;
            if (criteria.text) {
                theCriteria = criteria.text;
                theMatchType = matchType;
            }
            else {
                theCriteria = criteria;
                //theMatchType = 0;
                theMatchType = matchType;
            }
  
            const regEx = new RegExp(theCriteria, regexFlags);
            if (searchFieldValue &&
                (theMatchType == 0 && regEx.test(searchFieldValue) ||
                    theMatchType == 1 && searchFieldValue == criteria.text)
            ) {
                // Walk parent chain
                var ancestors = [];
                var parent = d;
                while (typeof (parent) !== "undefined") {
                    ancestors.push(parent);
                    //console.log(parent);
                    parent.class = "found";
                    parent = parent.parent;
                }
                //console.log(ancestors);
            }
        }
    }
  
  
    /**=========================================================================
     * function draws the tree. The d3.tree layout implements the Reingold-Tilford
     * algorithm for efficient, tidy arrangement of layered nodes. 
     * See https://reingold.co/tidier-drawings.pdf
     * 
     * @author  Steven J. Ponessa
     * @parm    d3 node (root)
     * @returns void
     */
    //var update = function(source) {
    function update(source) {
  
        // Compute the new tree layout.
        let nodes = tree.nodes(root).reverse(),
            links = tree.links(nodes);
  
        // Normalize for fixed-depth. Assign y vaalue of each based on the depth of the node
        nodes.forEach(function (d) { d.y = d.depth * nodeSpace; });
  
        // Update the nodes…
        let node = svg.selectAll("g.node")
            .data(nodes, function (d) { return d.id || (d.id = ++i); });
  
        // Enter any new nodes at the parent's previous position.
        let nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .attr("transform", function (d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
            .on("mouseover", mouseover)
            .on("mousemove", function (d) { mousemove(d); })
            .on("mouseout", mouseout)
            .on("click", toggle);
  
        //note - recall that ._children means collasped (i.e., expandable) node
        nodeEnter.append("circle")
            .attr("r", 1e-6)
            .style("fill", function (d) { return d._children ? "lightsteelblue" : "#fff"; });
  
        nodeEnter.append("text")
            .attr("x", function (d) { return d.children || d._children ? -10 : 10; })
            .attr("dy", ".35em")
            .attr("text-anchor", function (d) { return d.children || d._children ? "end" : "start"; })
            //.text(function (d) { return d.name || d.description ; })
            .text(function (d) {
                nodeText = "";
                if (typeof (d.code) !== "undefined") {
                    nodeText = '(' + d.code
                    if (typeof (d.usageRule) !== "undefined" && typeof (d.complianceCd) !== "undefined" && d.complianceCd != null && d.usageRule == 'CCR') {
                        nodeText += '/' + d.complianceCd + '*';
                    }
                    else if (typeof (d.complianceCd) !== "undefined" && d.complianceCd != null) {
                        nodeText += '/' + d.complianceCd;
                    }
                    nodeText += ") "
                };
                nodeText += d.name || d.description;
                return nodeText;
            })
            .style("fill-opacity", 1e-6);
  
        // Transition nodes to their new position.
        let nodeUpdate = node.transition()
            .duration(duration)
            .attr("transform", function (d) { return "translate(" + d.y + "," + d.x + ")"; });
  
        nodeUpdate.select("circle")
            .attr("r", 4.5)
            .style("fill", function (d) {
                if (d.class === "found") {
                    return "#ff4136"; //red
                } else if (d._children) {
                    return "lightsteelblue";
                } else {
                    return "#fff";
                }
            })
            .style("stroke", function (d) {
                if (d.class === "found") {
                    return "#ff4136"; //red
                }
            });
  
        nodeUpdate.select("text")
            .style("fill-opacity", 1);
  
        // Transition exiting nodes to the parent's new position.
        let nodeExit = node.exit().transition()
            .duration(duration)
            .attr("transform", function (d) { return "translate(" + source.y + "," + source.x + ")"; })
            .remove();
  
        nodeExit.select("circle")
            .attr("r", 1e-6);
  
        nodeExit.select("text")
            .style("fill-opacity", 1e-6);
  
        // Update the links…
        let link = svg.selectAll("path.link")
            .data(links, function (d) { return d.target.id; });
  
        // Enter any new links at the parent's previous position.
        link.enter().insert("path", "g")
            .attr("class", "link")
            .attr("d", function (d) {
                let o = { x: source.x0, y: source.y0 };
                return diagonal({ source: o, target: o });
            });
  
        // Transition links to their new position.
        link.transition()
            .duration(duration)
            .attr("d", diagonal)
            .style("stroke", function (d) {
                if (d.target.class === "found") {
                    return "#ff4136";
                }
            });
  
        // Transition exiting nodes to the parent's new position.
        link.exit().transition()
            .duration(duration)
            .attr("d", function (d) {
                let o = { x: source.x, y: source.y };
                return diagonal({ source: o, target: o });
            })
            .remove();
  
        // Stash the old positions for transition.
        nodes.forEach(function (d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }
  
    /**=========================================================================
     * function recursively pushes every node (in JSON tree) id and name into 
     * the global data structure select2Tax at the level where the node exists.
     * 
     * @author  Steven J. Ponessa
     * @parm    Node (must be d3 compliant containing id and text)
     * @returns void
     */
    function select2TaxCollectName(d, level) {
        if (d.children)
            d.children.forEach(item => select2TaxCollectName(item, level+1));
        else if (d._children)
            d._children.forEach(item => select2TaxCollectName(item, level+1));
        select2Tax[level].children.push({
            "id": d.code,
            "text": d.name || d.description
        });
    }
  
    /**=========================================================================
     * function recursively pushes every node (in JSON tree) name into 
     * the global data structure select2Data 
     * 
     * @author  Steven J. Ponessa
     * @parm    Node (must be d3 compliant containing name)
     * @returns void
     */
    function select2DataCollectName(d) {
        if (d.children)
            d.children.forEach(select2DataCollectName);
        else if (d._children)
            d._children.forEach(select2DataCollectName);
        select2Data.push(d.name || d.description);
    }
  
    function removeUndefinedElements(array) {
        return array.filter(function(element) {
            return element !== undefined;
        });
    }
    
  
    id3Tree.expandTree = function () {
        expandAll(root);
        update(root);
    }
  
    id3Tree.collapseTree = function () {
        collapse(root);
        update(root);
    }
  
    id3Tree.doSearch = function(searchCriteria, searchField = "d.name", matchType=0, regexFlags="i") {
        if (searchCriteria) {
            clearAll(root);
            expandAll(root);
            update(root);
  
            searchTree(root, searchCriteria, searchField, matchType, regexFlags);
  
            root.children.forEach(collapseAllNotFound);
            update(root);
        }
        else {
            resetTree();
        }
    }
  
    id3Tree.resetTree = resetTree;
  
    id3Tree.initSelect2 = function(select2Tax, select2Id = "#searchName", searchField = "d.name", idMatchType) {
        select2DataCollectName(root); //push all node text to select2Data[]
    
        select2Data.sort(function(a, b) {
                if (a > b) return 1; // sort
                if (a < b) return -1;
                return 0;
            })
            .filter(function(item, i, ar) {
                return ar.indexOf(item) === i;
            }) // remove duplicate items
            .filter(function(item, i, ar) {
                select2DataObject.push({
                    "id": i,
                    "text": item
                });
        });
    
        select2TaxCollectName(root,0);
    
        select2Tax.forEach(itm => itm.children.sort(function(a, b) {
                if (a.text > b.text) return 1; // sort
                if (a.text < b.text) return -1;
                return 0;
            })
        );
    
        //Get the select2 search element (#searchName) and assign it
        // the data (select2Tax for taxonomy or select2DataObject plain)
        // and set the CSS class. 
        // data: select2DataObject, when not using taxonomy
        $(select2Id).select2({
            data: select2Tax,
            containerCssClass: "search",
            multiple: "multiple"
        });
  
        /**=========================================================================
         * Anonymous function invoked when select2 option selected
         * select2-selecting Fired when a choice is being selected in the dropdown, 
         * but before any modification has been made to the selection. This event is 
         * used to allow the user to reject selection by calling event.preventDefault()
         * 
         * @author  Steven J. Ponessa
         * @parm    event
         * @returns void
         */
        $(select2Id).on("select2-selecting", function(e) {
            clearAll(root);
            expandAll(root);
            update(root);
  
            //searchField = "d.name";
            
            var data = $(select2Id).select2("data");
            if (Array.isArray(data))
                data.push(e.object);
            else
                data = e.object.text;
            
            //searchText = e.object.text;
            searchText = data;
  
            var matchTypeEl = document.getElementById(idMatchType)
            if (matchTypeEl && matchTypeEl.checked) {
                matchType = 0;
            }
            else {
                matchType = 1;
            }
            
            searchTree(root, searchText, searchField, matchType);
            root.children.forEach(collapseAllNotFound);
            update(root);
            //invokeSearch("d.name", e.object.text)
        })
  
  
        $(select2Id).on("select2-removed", function(e) {
            clearAll(root);
            expandAll(root);
            update(root);
  
            //searchField = "d.name";
            
            var data = $(select2Id).select2("data");
            if (Array.isArray(data))
                data.push(e.object);
            else
                data = e.object.text;
            
            //searchText = e.object.text;
            searchText = removeUndefinedElements(data);
  
            var matchTypeEl = document.getElementById(idMatchType)
            if (matchTypeEl && matchTypeEl.checked) {
                matchType = 0;
            }
            else {
                matchType = 1;
            }
            
            searchTree(root, searchText, searchField, matchType);
            root.children.forEach(collapseAllNotFound);
            update(root);
        })        
    }
  
    return id3Tree;
  }
  
  //export default InteractiveD3TreeObj;