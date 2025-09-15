---
title: Ladder Processing Algorithm
permalink: /en/exploring-utilities/ladder-processing-algorithm
abstract: >- 
  A ladder processing algorithm can be used to compare and identify deltas in any two sets of data.
---

# Overview

A ladder processing algorithm can be used to compare and identify deltas in any two sets of data provided the sets of data

- Have a set of attributes that uniquely identify a row of data (primary key or natural key)
- Are sorted in ascending order by these identifying attributes

The theory behind the algorithm is that if a primary/natural key exists in the first or older file and not in the second or newer file, then that record has been deleted.  If a primary/natural key exists in the second/newer file and not in the first/older file, then that record has been inserted.  And if a primary/natural key exists in both file and any part of the remaining attributes differ, then that record has been updated.

Ladder processing gets its name for the way that the program processes down the first/old and second/new set of data together, based on the value of the primary/natural key; like descending the rungs of a ladder.  

The processing begins by reading the first record from both sets of data then starting a processing loop.  If the key value from the old dataset is less than the key value from the new dataset; the process writes the old dataset record as being a deletion and then reads the next record from the old dataset.  If the key value from the old dataset is greater than the key value from the new dataset; the process writes the new dataset record as being an insertion and then reads the next record from the new dataset.  If the key values are equal, the process compares the entire record from both datasets, if they are not equal; the process writes the new dataset record as being an update and reads the next record from both the old and new dataset.

Finally, if either the old or new dataset is exhausted before the other, the remaining records from the non-exhausted data set are written out.  If the new dataset has completely processed and there are records remaining in the old dataset, each of these old dataset records are written out as deletions.  Conversely, if the old dataset has completely processed, each of the remaining new dataset records are written out as insertions.

Consider the following example:

<table border="1">
    <thead>
        <tr>
            <th colspan="2"><b>Old file</b></th>
            <th></th>
            <th colspan="2"><b>New file</b></th>
            <th colspan="5"></th>
        </tr>
        <tr>
            <td>Key</td>
            <td>Attributes</td>
            <td>&nbsp;&nbsp;</td>
            <td>Key</td>
            <td>Attributes</td>
            <td>&nbsp;&nbsp;</td>
            <td>Old key</td>
            <td>is</td>
            <td>New key</td>
            <td>Action</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td align="center">A</td>
            <td align="center">1</td>
            <td></td>
            <td align="center">B</td>
            <td align="center">6</td>
            <td></td>
            <td align="center">A</td>
            <td align="center">&lt;</td>
            <td align="center">B</td>
            <td align="center">Write A as delete; read from old dataset;</td>
        </tr>
        <tr>
            <td align="center">B</td>
            <td align="center">2</td>
            <td></td>
            <td align="center">C</td>
            <td align="center">7</td>
            <td></td>
            <td align="center">B</td>
            <td align="center">=</td>
            <td align="center">B</td>
            <td align="center">Compare records; B2&lt;&gt;B6; Write B6 as update; read from both datasets;</td>
        </tr>
        <tr>
            <td align="center">D</td>
            <td align="center">3</td>
            <td></td>
            <td align="center">D</td>
            <td align="center">3</td>
            <td></td>
            <td align="center">D</td>
            <td align="center">&gt;</td>
            <td align="center">C</td>
            <td align="center">Write C7 as insert; read from new dataset;</td>
        </tr>
        <tr>
            <td align="center">F</td>
            <td align="center">4</td>
            <td></td>
            <td align="center" valign="top">F</td>
            <td align="center" valign="top">8</td>
            <td></td>
            <td align="center">D</td>
            <td align="center">=</td>
            <td align="center">D</td>
            <td align="center">Compare records; D3=D3 (no delta); read from both datasets;</td>
        </tr>
        <tr>
            <td align="center" valign="top">G</td>
            <td align="center" valign="top">5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td align="center">F</td>
            <td align="center">=</td>
            <td align="center">F</td>
            <td align="center">Compare records; F4&lt;&gt;F8; Write F8 as update; read from both datasets;</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td align="center">G</td>
            <td align="center"></td>
            <td align="center"><i>null</i></td>
            <td align="center">New dataset exhausted; Write remaining old records (G) as deletes</td>
        </tr>
    </tbody>
</table>

Additionally, this algorithm has the advantage of speed and a small memory footprint.  The speed comes from its simplicity.  It is read, compare, and write.  A complete delta list can be compiled nearly as fast as the datasets can be read.  And, since the program handles at most two records at a time (1 from the old dataset and 1 from the new); it has a very small memory footprint; unlike other methods that require the complete sets to be realized in memory before the comparisons can be performed.