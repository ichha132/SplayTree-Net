public class BST_SplayTree<Key extends Comparable<Key>, Value> {

    public Node root;   // root of the BST

    // BST helper node data type
    private class Node {
        private int key;            // key
        private Node left, right;   // left and right subtrees

        public Node(int key) {
            this.key = key;
        }
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void insert(int key) {
        if (root == null) {         //=> Tree is null
            root = new Node(key);
            return;
        }
        //New node will always be inserted in the root.
        root = splay(root, key);
        int cmp = key - root.key;
        // Insert new node at root
        if (cmp < 0) {          //Go to left subtree
            Node n = new Node(key);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (cmp > 0) {     //Go to right subtree
            Node n = new Node(key);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }
    }


    /***************************************************************************
     *  Splay tree Search.
     ***************************************************************************/

    public int search_cost(int u) {
        int cost = 0;
        Node node = this.root;
        while (node != null && node.key != u) {
            if (u < node.key) {
                node = node.left;
                cost++;
            } else {
                node = node.right;
                cost++;
            }
        }
        if (node.key == u) {
            return cost;
        }
        return -1;          //u does not exist in the tree
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.

    public Node splay(Node h, int key) {
        if (h == null) return null;     //Node h does not exist

        int cmp1 = key - h.key;

        if (cmp1 < 0) {
            if (h.left == null) {
                return h;       // key not in tree, so we're done
            }
            int cmp2 = key - h.left.key;
            if (cmp2 < 0) {     //Left-left case => 2 times right rotate
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h); //Right rotate
            } else if (cmp2 > 0) {//Left-Right case => Right rotate then Left rotate
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left); //Left rotate
            }

            if (h.left == null) return h;
            else
                return rotateRight(h);   //Right Rotate
        } else if (cmp1 > 0) {

            if (h.right == null) {      // key not in tree, so we're done
                return h;
            }

            int cmp2 = key - h.right.key;
            if (cmp2 < 0) {             //Right-Left case
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);  //Right Rotate
            } else if (cmp2 > 0) {        //Right-Right case
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);      //Left rotate
            }

            if (h.right == null) return h;
            else
                return rotateLeft(h);   //Left rotate
        } else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    public void printPreorder(Node node) {
        if (node == null)
            return;

        /* first print data of node */
        System.out.print(node.key + " ");

        /* then recur on left sutree */
        printPreorder(node.left);

        /* now recur on right subtree */
        printPreorder(node.right);
    }
}