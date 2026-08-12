# Graph

1. vertex or Node

![img.png](img/img.png)

2. Edge or connection

![img_1.png](img/img_1.png)

1. [ ] Note : No limit of edge

![img_3.png](img/img_3.png)

* Weight edge
Ex: used in google maps and network routing for defining traffics

![img_4.png](img/img_4.png)
![img_5.png](img/img_5.png)

# Adjacency Matrix Representation

If an Undirected Graph G consists of n vertices then the adjacency matrix of a graph is n x n matrix A = [aij] and
defined by - aij = 1 {if there is a path exists from Vi to Vj}
1. bidirectional (symmetric matrix)

![img_6.png](img/img_6.png)
2. weight edge

![img_8.png](img/img_8.png)
3. unidirectional (Lost symmertic matric) 

![img_7.png](img/img_7.png)

# Adjacency List

Adjacency List is a method of representing graphs in list form, or it can be defined as a format used to represent graphs as an array of linked lists

hash map representation
![img_9.png](img/img_9.png)

# Big O

* Space complexity

![img_10.png](img/img_10.png)

1. When adding new vertex 

![img_11.png](img/img_11.png)
![img_12.png](img/img_12.png)

2. Adding edge b/w vertex

![img_13.png](img/img_13.png)


3. Removing Edge b/w vertex

![img_15.png](img/img_15.png)
Adjacency List we need to find b O(1) and iterate through each edge in b and same with f(vertex)
![img_14.png](img/img_14.png)

4. Remove a vertex 

![img_16.png](img/img_16.png)
* In Adjacency List we need to check ever vertex edge list and remove edge before (we need to touch every vertex and edge)
* In Adjacency matrix we need to remove (F) and rewrite the array

![img_17.png](img/img_17.png)

1. [ ] Notes : In Adjacency matrix we need to maintain zero(0) so if we have billion record (Ex: users graph: IF each user's have 1000 friend we would have millions zero for each user's)






