---
title: Extended Relational Model
permalink: /en/exploring-utilities/extended-relational-model
abstract: >- 
  To have the ability to perform reporting and analytics across the enterprise, there needs to exist a common core set of reference data/dimensions. However, this does not allow
  variations or nuances in the reference data across different business units in the enterprise. The "Extended Relational Model" allows business units to extend and/or override
  enterprise data standard (EDS) dimensions.
---
# Extending a relational data model

The “extended dimensional model” design pattern/strategy covers both the breath of the dimension, i.e. the number of attributes contained in the dimension; and the depth of the dimensions (rows).  It also enables overrides, however, we currently don’t have a need to do but it is good to know we can.

![extended-relational-model-v-oo]({{ site.baseurl }}/assets/images/docs/extended-relational-model-v-oo.png){: style="width:50%;"}

The diagram above shows a simple object model from an OO design alongside a database model. If we were in an object-oriented environment, extending data objects would be as simple as using subclasses and inheritance.  Inheritance is a process in which a class (child/subclasses) inherits all the state and behavior of another class (parent/superclass) and is one of the largest benefits found in object-oriented systems.  In the OO model, we can create an instance of the `Dependent` object and get access to the object's properties, e.g., `attributeZ`, and the parent attributes, e.g., `attributeA`, and can override the inherited parent object's values, e.g., `setAttributeB()`.

<pre name="code" class="java">
Dependent dependent = new Dependent();
dependent.getAttributeA();
dependent.getAttributeZ();
dependent.setAttributeB("override value");
</pre>

The trick is to get inheritance to work in a non OO environment like COS or a relational database management system.   

Looking at inheritance with a bit of abstraction, it provides a mean of classifying objects and allows for the commonality of objects to be taken advantage of.  Which is what we are looking to take advantage of, the commonality provided by the standardized dimension.

Inheritance establishes the relationship between classes where one class is the parent class of another; in other words, there is a "**is-a**" relationship between the two classes.  Subclasses usually consist of several kinds of modifications or customization to their respective superclasses.  In our case we are concerned with the addition of new instance variables or attributes and overriding existing attributes.  Said simply, children classes inherit the attributes of their parent classes and have the ability to override them.

Applying this concept to relational tables, we are able to implement inheritance on any "is-a" relationship between ER Entities. By using a **full outer join** between tables with an established “is-a” relationship, you create an environment where the Parent table is the super class and the Dependent table is the subclass.  Thereafter the **COALESCE** function enables the subclass or dependent table to “override” attribute or column values of the superclass or parent table.  The COALESCE function returns the value of the first non-null expression in a list of expressions.  The arguments are evaluated in the order in which they are specified, and the result of the function is the first argument that is not null. The result is null only if all arguments are null.  

<pre name="code" class="sql">
CREATE VIEW Extended_Dimension_V
AS
SELECT COALESCE(P.COL_A, D.COL_A) AS COL_A
       -- Overeritten Parent's COL_B value
     , COALESCE(D.COL_B, P.COL_B) AS COL_B
       -- In this example Parent's COL_C can not be overwritten
     , P.COL_C
       -- Extend the dimeension's breath (attributes)
     , D.COL_X
     , D.COL_Y
     , D.COL_Z
FROM   Parent     P
FULL OUTER JOIN  
       Dependent  D
   ON  P.COL_A = D.COL_A
;
</pre>

By creating a child dimension extension table that is linked to its parent in a “is-a” relationship based on the natural key, we can create a view that extends the dimension.  The view would use the COALESCE function when specifying the value of a column where the first attribute would be from the child/dependent table and the second attribute would be from the parent table.  This would allow for true inheritance, where the subclass could inherit or override the parent's attribute value; with the exception of the column used to establish the foreign key relationship (i.e. the natural key).

So, the view here would allow the breath of the dimension to be extended with attributes COL_X, Y and Z.  The depth of the dimension can be extended by simply adding rows to the dependent table where the natural key doesn’t exist in the parent table.  Finally, in this example, we are allowing COL_B to be overridden within a subclass’ed instance/row.

# The extended model in action

_put an example of DDL and result sets on the extended model_
