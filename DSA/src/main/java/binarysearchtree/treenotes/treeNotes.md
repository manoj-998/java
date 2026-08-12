Tree: There is not rule that binary binarysearchtree should have node two nodes

# `Binary Search Tree`
![img.png](img/img.png)

* Nodes left and right

![img_1.png](img/img_1.png)

* Nested representation how actual object looks like

![img_2.png](img/img_2.png)

* **Full Binary binarysearchtree :**

Every node has either 0 or 2 child nodes, i.e., left and right or no children

![img_3.png](img/img_3.png)

Not Full if root node has only one node in right or left

![img_4.png](img/img_4.png)

* **Perfect binary binarysearchtree :**

All nodes have exactly two children, and all leaf nodes are at the same level

![img_5.png](img/img_5.png)
![img_6.png](img/img_6.png)

* **Complete binarysearchtree:**

All levels, except possibly the last, are filled, and all nodes are as left as possible

ex: Complete binarysearchtree if we are filling the binarysearchtree in all level with any gaps

![img_7.png](img/img_7.png)
![img_8.png](img/img_8.png)
![img_9.png](img/img_9.png)
![img_10.png](img/img_10.png)


* Parent Node 

![img_11.png](img/img_11.png)

* Child Node

![img_12.png](img/img_12.png)

* Leaf node 

![img_14.png](img/img_14.png)

Note: This is not binarysearchtree only one parent for child node

![img_13.png](img/img_13.png)

## **Binary search binarysearchtree**

Left value is less and right value is more

A Binary Search Tree is a data structure used in computer science for organizing and storing data in a sorted manner. Each node in a Binary Search Tree has at most two children, a left child and a right child, with the left child containing values less than the parent node and the right child containing values greater than the parent node. This hierarchical structure allows for efficient searching, insertion, and deletion operations on the data stored in the binarysearchtree.

![img_15.png](img/img_15.png)

## Time complexity calculation 

To number of nodes in a binarysearchtree

Formula : 2^level-1

* in Level 1 = 2^1-1 = 1
* In level 2 = 2^2-1 = 3
* In level 3 = 2^3-1 = 7
* In level 4 = 2^4-1 = 16

As we go larger in nodes the -1 is insignificant

Binary search binarysearchtree operation 
* To find or add 35 value it is 4 steps 
* To find or add 76 value it is 2 steps

![img_16.png](img/img_16.png)

O(log N) achieved in average case 

![img_17.png](img/img_17.png)

Worst case Big O(N) if binary search binarysearchtree does not fork (This is similar to linked List)

![img_21.png](img/img_21.png)

Operation Average and Worst case for binary search binarysearchtree

![img_19.png](img/img_19.png)


**Linked list V/S Binary search binarysearchtree**

![img_22.png](img/img_22.png)

In details of binary search binarysearchtree 

![img_23.png](img/img_23.png)






