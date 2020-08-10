import java.util.Scanner;

public class LazySplayTree {
    BST_SplayTree original_tree=new BST_SplayTree();          //original BST
    int threshold_cost;         //a
    int[] communication_requests;     //nx2 matrix, with n communication request pairs
    BST_SplayTree splayTree=new BST_SplayTree();
    public LazySplayTree(BST_SplayTree original_tree, BST_SplayTree splayTree,int threshold_cost, int[] communication_requests)
    {
        this.original_tree=original_tree;
        System.out.println("original Tree:");
        original_tree.printPreorder(original_tree.root);
        System.out.println();
        this.threshold_cost=threshold_cost;
        this.communication_requests=communication_requests;
        this.splayTree=splayTree;
        /* Print statements for testing
//        this.original_tree.root=this.original_tree.splay(this.original_tree.root,7);
//        System.out.println("original tree after change");
//        this.original_tree.printPreorder(this.original_tree.root);
//        System.out.println("splay Tree");
//        this.splayTree.printPreorder(this.splayTree.root);
         */
    }
    int total_routing_cost=0;
    int epoch_routing_cost=0;
    int adjustment_cost=0;
    public void laziness_prop()
    {
        int iter=0;
        System.out.println("Final tree:");
        laziness_prop_recur(this.original_tree,iter);
        System.out.println();
        System.out.println("total_routing_cost="+ total_routing_cost);
        System.out.println("epoch_routing_cost="+ epoch_routing_cost);
        System.out.println("adjustment_cost=" + adjustment_cost);
    }
    private void laziness_prop_recur(BST_SplayTree tree, int iter)
    {
        if (iter>=communication_requests.length)
        {
            tree.printPreorder(tree.root);
            return;
        }
        int u=communication_requests[iter];
        int cost=tree.search_cost(u);
        /*Print statements for testing
//        System.out.println("tree before splaying :");
//        tree.printPreorder(tree.root);
//        this.splayTree.root=this.splayTree.splay(this.splayTree.root,u);
//        System.out.println("tree after splaying :");
//        tree.printPreorder(tree.root);
         */
        this.splayTree.root=this.splayTree.splay(this.splayTree.root,u);
        total_routing_cost=total_routing_cost+cost;
        epoch_routing_cost=epoch_routing_cost+cost;
       // System.out.println("epoch cost="+epoch_routing_cost);
        if(epoch_routing_cost>=threshold_cost)
        {
            adjustment_cost+=threshold_cost;
            epoch_routing_cost=epoch_routing_cost-threshold_cost;
            laziness_prop_recur(this.splayTree.copy(),++iter);
        }
        else
        {
            laziness_prop_recur(tree,++iter);
        }
    }

    public static void main(String[] args)
    {
        /* Input Type
        Line 1--> number of nodes in the input tree (eg. m)
        Line 2--> list keys of all nodes present in the tree (m integers)
        Line 3--> number of search queries (eg, n)
        Line 4 --> n integers (keys that are to be queried)
        Line 5--> threshold value (value of a or alpha) -- single integer
        Eg of input :-
        7
        5 2 3 7 1 10 0
        6
        0 3 1 2 5 2
        5
        Corresponding output:
        original Tree:
        0 1 7 2 5 3 10
        Final tree:
        5 3 2 1 0 7 10
        total_routing_cost=12
        epoch_routing_cost=2
        adjustment_cost=10
         */
        Scanner s=new Scanner(System.in);
        int totNodes=s.nextInt();
        BST_SplayTree input_tree=new BST_SplayTree();
        BST_SplayTree splayTree=new BST_SplayTree();
        for(int i=0;i<totNodes;i++)
        {
            int in=s.nextInt();
            input_tree.insert(in);
            splayTree.insert(in);
        }
        int tot_comm_req=s.nextInt();
        int[] communication_requests=new int[tot_comm_req];
        for(int i=0;i<tot_comm_req;i++)
        {
            communication_requests[i]=s.nextInt();
        }
        int threshold_value=s.nextInt();
        LazySplayTree original_tree=new LazySplayTree(input_tree,splayTree, threshold_value,communication_requests);
        original_tree.laziness_prop();
    }
}
