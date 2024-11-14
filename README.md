# Binary-Search-Tree-Implementation

Implements a self-balancing binary search tree and AVL tree.

Includes add, remove, iterator, and other functions to traverse the given tree. A self balancing tree maintains that no subtree of a given root has a depth more than 1 greater than the depth of the other subtree. In order for this to hold, rotations must be performed to make sure that balance is kept. These rotations may recurse up the tree and into the other subtree. 

Keeping balance in this way ensures that searches on the tree remain in O(logn) time complexity. 
