---
title: Slow Changing Dimensions
permalink: /en/exploring-utilities/slow-changing-dimensions
abstract: >- 
  (Type 2) slow changing dimensions maintain an historical view of what rows looked like at any given point in time.
---

## Motivation
Used when every state of an entity must be recorded and/or linked to a measurement over time. Dimension state information captured over time is needed to allow for aggregation for a specific time period against changing state information. For example, if an employee changed departments mid-year and we were aggregating yearly labor by department. Without preserving history all the employees hours would aggregate to his/her final department instead of being aggregated against the department the employee was in at the time the labor was claimed.

## Implementation
Slowly Changing Dimension Type 2 preserves history. This methodology generates a new record for every change of a dimension attribute, so that complete historical changes can be tracked correctly. Each row contains an "effective" and "expiration" date or timestamp of the row, depending on the required granularity. Therefore instead of overwriting old data, new data is written as a new record that becomes effective at the current date or timestamp and an expiration date/timestamp of "9999-12-31-23.59.59.999999" and the row with the old data is expired. By setting a distinguishable, non-null expiration date we can always see the current state of the dimension by looking WHERE expiration date is this distinguishable data/timestamp AND we can create a unique index using the expiration date. Although it is not required, Type 2 Slow Varying Dimensions usually use a surrogate key in lieu of the natural key, however, a unique index of the dimension is the natural key plus the effective date or timestamp.

The diagram below tries to show the concept.  For each natural key a new segment (row) is generated every time ANY of the record state attributes change.  Each segment has a effective and expiration date or timestamp.

![Slow Changing Dimensions]({{ site.baseurl }}/assets/images/docs/scd.png){: style="width:50%;"}

When implementing this pattern, the designer should consider the following items:

## Structure

![Slow Changing Dimensions]({{ site.baseurl }}/assets/images/docs/scd-Type2.gif){: style="width:25%;"}

## Consequences
Using this pattern when you need to see how a dimension row changes over time and/or if you need to aggregate data based on the business attribute value at the time that the business measurement is recorded.

For example, if you are aggregating hours claimed by departments, it would have to be noted when an employee has changed departments so that the yearly aggregated hours would be credited to the department the employee was in at the time the labor was worked.

This pattern can however exponentially increase the size of the table since every state of every dimension is persistently maintained.

From a design/implementation perspective, there are two main consequences of implementing this pattern:

1. Data type of the range columns
There are several time-based data types – date, time, timestamp, etc. – the data type chosen should support the business requirements. For example, if the data warehouse is populated multiple time through a given day, and the fact row must point to a dimension row that appears exactly as it did when the fact row was created, the designer must choose a data type that is more granular than Date.

1. Range column values are inclusive
An inclusive range provides for easier SQL querying for finding the active row for a given time by using the BETWEEN predicate as well as more intuitive representation of the range.

1. Database triggers should be used to maintain these columns to ensure proper and consistent population regardless of how the data is maintained.

1. As database triggers are not invoked when executing database utilities, such as Load, proper default values or values in the utility input must be considered

## SCD Management Using Triggers

SCD provide ... but is sometimes difficult to manage the effective and expiration date/timestamp of the record.  The following set of triggers takes the management of the SDC out of the hands of the user/programmer/DBA, encapsulating the management complexities. The triggers:

1. Set the effective and expiration data/timestamp of the new data (row)
1. If applicable, expires the old data record
1. Changes a delete record request to expire the current record
1. Changes an update record request to perform an insert (which then turn control over to the first two triggers to manage the effective and expiration data/timestamp of the new and old record.

## Trigger sample implementation
The DDL below shows a typical slow changing dimension table.  It contains

- A generated surrogate key (surrogate_KEY), which is also the table's primary key
- A composite natural key (natural_key_1, natural_key_2, the natural key is not required to be composite, a composite key was just used in this example)
- Effective and expiration timestamp, EFF_TMS and EXPIR_TMS, respectively
- A set of attributes (attr_1-3)
- A unique index on the natural key plus the EFF_TMS.

<pre name="code" class="sql">
CREATE TABLE schema.scd_type_2                                  
   (surrogate_KEY       INTEGER NOT NULL GENERATED BY DEFAULT   
      AS IDENTITY                                               
        (START WITH 1, INCREMENT BY 1, NO CACHE, NO CYCLE,      
         NO ORDER, MAXVALUE 2147483647, MINVALUE 1),            
    natural_key          dataType NOT NULL      
    EFF_TMS              TIMESTAMP NOT NULL                     
    EXPIR_TMS            TIMESTAMP NOT NULL                     
    attr                 dataType,       
    CONSTRAINT scd_type_2_KEY                                   
    PRIMARY KEY (surrogate_KEY))                                
  ...;                                                 

CREATE UNIQUE INDEX schema.scd_type_2_PK  
  ON schema.scd_type_2                    
   (surrogate_KEY)                
   CLUSTER                        
  ..;

CREATE UNIQUE INDEX schema.scd_type_2_UIX1
  ON schema.scd_type_2                    
   (natural_key  ASC
  , EFF_TMS)
  ..;
</pre>

### Trigger to set the Effective and Expiration Date on inserted records
The first trigger will fire before the insertion of a new record and set the effective and expiration timestamps. 

<pre name="code" class="sql">
CREATE TRIGGER schema.scd2_T1
 NO CASCADE BEFORE INSERT ON schema.scd_type_2                   
REFERENCING NEW AS NEWVAL                                      
FOR EACH ROW MODE DB2SQL                                       
 SET EFF_TMS   = CURRENT TIMESTAMP
  ,  EXPIR_TMS = '9999-12-31-23.59.59.999999'
;
</pre>

### Trigger to update the Expiration Timestamp of the prior record
This trigger fires after the insert and “expires” the prior record for the natural key.   That is, it sets the expiration timestamp of the prior record for the natural key to 1 microsecond less than the effective timestamp of the new record.

<pre name="code" class="sql">
CREATE TRIGGER schema.scd2_T2
 AFTER INSERT ON schema.scd_type_2                   
REFERENCING NEW AS N                       
FOR EACH ROW MODE DB2SQL                   
 UPDATE schema.scd_type_2                 
  SET EXPIR_TMS= N.EFF_TMS - 1 MICROSECOND 
WHERE natural_key_1  = N.natural_key_1     
  AND natural_key_2  = N.natural_key_2     
  AND EXPIR_TMS      = '9999-12-31-23.59.59.999999'
  AND EFF_TMS        < N.EFF_TMS
;
</pre>

### Trigger to change deletion into expiring current record
The **INSTEAD OF** clause will be used to intercept and convert deletion requests into updates that simply expires the current record for the natural key.  Note however that the “INSTEAD OF” clause only works against views and therefore must be run against the world wide full view of the table.

<pre name="code" class="sql">
CREATE TRIGGER schema.scd2_T3
INSTEAD OF DELETE ON schema.scd_type_2_V
REFERENCING OLD AS O                                  
FOR EACH ROW MODE DB2SQL 
    UPDATE schema.scd_type_2  
    SET    EXPIR_TMS     = CURRENT TIMESTAMP
    WHERE  natural_key_1 = O.natural_key_1     
      AND  natural_key_2 = O.natural_key_2     
      AND  EXPIR_TMS     = '9999-12-31-23.59.59.999999'  
;
</pre>



### Trigger to change updates into inserts
For the final trigger, the INSTEAD OF clause will be used to intercept and convert update requests into inserts of new records.  This trigger acts as a safeguard in the event an authorized user attempts to make an SQL update to the SCD.

<pre name="code" class="sql">
CREATE TRIGGER schema.scd2_T4
INSTEAD OF UPDATE ON schema.scd_type_2_V
REFERENCING NEW AS N                                 
FOR EACH ROW MODE DB2SQL 
    INSERT INTO schema.scd_type_2  
    (natural_key_1, natural_key_2, attr_1, attr_2, attr_2) 
	VALUES
	(N.natural_key_1, N.natural_key_2, N.attr_1, N.attr_2, N.attr_2) 
;
</pre>

