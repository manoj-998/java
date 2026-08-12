# Heaps

A Heap is a complete binary tree data structure that satisfies the heap property: for every node, the value of its
children is less than or equal to its own value. Heaps are usually used to implement priority queues, where the
smallest (or largest) element is always at the root of the tree.

* A heap will always be complete tree (without gaps)
* The tree is always balanced


![img.png](img/img.png)

* In heaps we can have duplicate

![img_1.png](img/img_1.png)

#   * Types

1. max heap

![img_2.png](img/img_2.png)

2. Min heap

![img_3.png](img/img_3.png)

# Heap representation

1.[ ] Note : To implement this we will use Array

Common method used while implementing

1. without leaving Zero index
2. With leaving Zero index

![img_4.png](img/img_4.png)

* Finding children logic
  int leftIndex=2*parentIndex
  int RightIndex=2*parentIndex*1

![img_5.png](img/img_5.png)

* Finding parent from child
  int index= childIndex/2
  ![img_6.png](img/img_6.png)


* Inserting into a heap
  First add it in the last and start swapping with the parent if greater
  ![img_7.png](img/img_7.png)

Will checks and swaps 100 and 99
![img_8.png](img/img_8.png)

[ ] Notes :

1. Remove item in heap

* While removing from the heap we only remove 1 element in the heap even if it's duplicate

![img.png](img/img10.png)
![img_1.png](img/img_11.png)

* Bring last element in the index to root node

![img_2.png](img/img_12.png)

* start swapping the root node with child node if the parent node is greater

  ![img_3.png](img/img_13.png)
  ![img_4.png](img/img_14.png)

##### Priority Queue  :

heap is the most efficient DS for priority queue

![img_5.png](img/img_15.png)

Ref link : https://www.geeksforgeeks.org/priority-queue-using-binary-heap/


