# Tree: 

There is no rule that tree should have node two nodes

# `Binary Search Tree`

* Object representation

<img src="img/img.png" width="400px">

* Nodes left and right

<img src="img/img_1.png" width="400px">

* Nested representation how actual object looks like

<img src="img/img_2.png" width="400px">

* **Full Binary binarysearchtree :**

Every node has either 0 or 2 child nodes, i.e., left and right or no children

<img src="img/img_3.png" width="400px">

Not Full if root node has only one node in right or left

<img src="img/img_4.png" width="400px">

* **Perfect binary binarysearchtree :**

All nodes have exactly two children, and all leaf nodes are at the same level

<img src="img/img_5.png" width="400px">
<img src="img/img_6.png" width="400px">

* **Complete binarysearchtree:**

All levels, except possibly the last, are filled, and all nodes are as left as possible

ex: Complete binarysearchtree if we are filling the binarysearchtree in all level without any gaps

<img src="img/img_7.png" width="400px">
<img src="img/img_8.png" width="400px">
<img src="img/img_9.png" width="400px">
<img src="img/img_10.png" width="400px">

* Parent Node

<img src="img/img_11.png" width="400px">

* Child Node

<img src="img/img_12.png" width="400px">

* Leaf node

<img src="img/img_14.png" width="400px">

Note: This is not binarysearchtree only one parent for child node

<img src="img/img_13.png" width="400px">

## **Binary search binarysearchtree**

Left value is less and right value is more

A Binary Search Tree is a data structure used in computer science for organizing and storing data in a sorted manner.
Each node in a Binary Search Tree has at most two children, a left child and a right child, with the left child
containing values less than the parent node and the right child containing values greater than the parent node. This
hierarchical structure allows for efficient searching, insertion, and deletion operations on the data stored in the
binarysearchtree.

<img src="img/img_15.png" width="400px">

## Time complexity calculation

The number of nodes in a binarysearchtree

Formula : 2^{level}-1

* in Level 1 = 2^1-1 = 1
* In level 2 = 2^2-1 = 3
* In level 3 = 2^3-1 = 7
* In level 4 = 2^4-1 = 16

As we go larger in nodes the -1 is insignificant

Binary search binarysearchtree operation

* To find or add 35 value it is 4 steps
* To find or add 76 value it is 2 steps

<img src="img/img_16.png" width="400px">

* O(log N) achieved in average case

<img src="img/img_17.png" width="400px">

* Worst case Big O(N) if binary search binarysearchtree does not fork (This is similar to linked List)

<img src="img/img_21.png" width="400px">

Operation Average and Worst case for binary search binarysearchtree

<img src="img/img_19.png" width="400px">

**Linked list V/S Binary search binarysearchtree**

<img src="img/img_22.png" width="400px">



### Removing item in binary tree

Case 1 : If there is only one child we bring the child node to parent node

Ex: in below image 27 will be replaced by 25 node 

<img src="img/img27.png" width="250px">

Case 2 : If there are multiple child we copy the lowest value child from right current Node

<img src="img/img_25.png" width="250px">

2.2 After copying we will remove 28 leaf node from bottom

<img src="img/img_26.png" width="250px">




1. [ ] In details of binary search binarysearchtree

<img src="img/img_23.png" width="800px">