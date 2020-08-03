// Java program to demonstrate insert operation in binary search tree
class BST_SplayNet {

    /* Class containing left and right child of current node and key value*/
    class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }
    }

    // Root of BST
    Node root;

    // Constructor
    BST_SplayNet() {
        root = null;
    }

    // This method mainly calls insertRec()
    void insert(int key) {
        root = insertRec(root, key);
    }

    /* A recursive function to insert a new key in BST */
    Node insertRec(Node root, int key) {

        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);

        /* return the (unchanged) node pointer */
        return root;
    }

    // This method mainly calls InorderRec()
    void inorder() {
        inorderRec(root);
    }

    // A utility function to do inorder traversal of BST
    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.key);
            inorderRec(root.right);
        }
    }

    public int cost(int u, int v) {
        Node node = this.root;
        Node common_ancestor;
        Node[] nodeSet = new BST_SplayNet.Node[2];
        //Property used => u<=common_ancester<=v always
        while (node != null && ((u > node.key && v > node.key) || (u < node.key && v < node.key))) {
            if (u > node.key && v > node.key)     // if current_node<u<=v ... Go right
            {
                node = node.right;
            } else if (u < node.key && v < node.key)//if u<=v<current_node ... Go left
            {
                node = node.left;
            }
        }
        int cost = 0;
        common_ancestor = node;        //nodeSet[0]=common_ancester
        while (node != null && node.key != u)        //Finding Node(u)
        {
            if (u < node.key) {
                node = node.left;
                cost++;
            } else {
                node = node.right;
                cost++;
            }
        }
        node = common_ancestor;    //nodeSet[1]=uNode
        while (node != null && node.key != v)        //Finding Node(u)
        {
            if (u < node.key) {
                node = node.left;
                cost++;
            } else {
                node = node.right;
                cost++;
            }
        }
        return cost;
    }

    public void commute(int u, int v)        //Assuming u amd v always exist in the tree && u<=v
    {
        Node nodeSet[] = findNodes(u, v);    //Node[0]=common_ancester; Node[1]=Node(u)
        BST_SplayNet.Node common_ancester = nodeSet[0];
        BST_SplayNet.Node uNode = nodeSet[1];
        common_ancester = splay(common_ancester, u);
        this.printPreorder(this.root);          //Print tree in preorder fashion
        System.out.println();
        if (u == v)
            return;
        uNode.right = splay(uNode.right, v);          //if v is not there, node closest to v will come to uNode
        this.printPreorder(this.root);
        System.out.println();
    }

    public BST_SplayNet.Node[] findNodes(int u, int v)        // Returns an array with common ancester of u an v and BST.Node of u
    {
        Node node = this.root;
        Node[] nodeSet = new BST_SplayNet.Node[2];
        //Property used => u<=common_ancester<=v always
        while (node != null && ((u > node.key && v > node.key) || (u < node.key && v < node.key))) {
            if (u > node.key && v > node.key)     // if current_node<u<=v ... Go right
            {
                node = node.right;
            } else if (u < node.key && v < node.key)//if u<=v<current_node ... Go left
            {
                node = node.left;
            }
        }
        nodeSet[0] = node;        //nodeSet[0]=common_ancester
        while (node != null && node.key != u)        //Finding Node(u)
        {
            if (u < node.key) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        nodeSet[1] = node;    //nodeSet[1]=uNode
        return nodeSet;
    }

    private BST_SplayNet.Node splay(BST_SplayNet.Node h, int key) {
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


    private BST_SplayNet.Node rotateRight(BST_SplayNet.Node h) {
        BST_SplayNet.Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private BST_SplayNet.Node rotateLeft(BST_SplayNet.Node h) {
        BST_SplayNet.Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    public void printPreorder(BST_SplayNet.Node node) {
        if (node == null)
            return;

        /* first print data of node */
        System.out.print(node.key + " ");

        /* then recur on left sutree */
        printPreorder(node.left);

        /* now recur on right subtree */
        printPreorder(node.right);
    }


    // Driver Program to test above functions

}