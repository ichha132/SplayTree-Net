import java.util.Scanner;

public class LazySplayNet {
    BST_SplayNet original_tree=new BST_SplayNet();          //original BST
    int threshold_cost;                 //a (alpha)
    int[][] communication_requests;     //nx2 matrix, with n communication request pairs
    BST_SplayNet splayNet=new BST_SplayNet();
    public LazySplayNet(BST_SplayNet original_tree, BST_SplayNet splayNet,int threshold_cost, int[][] communication_requests)
    {
        /*
        Initially 'original tree' an 'splayNet' are the same, they are taken separately as arguments to prevent
        problems arising due to "pass by reference" ===> But the problem does not get solved
         */

        this.original_tree=original_tree;                   //never changes
        System.out.println("original Tree:");
        original_tree.printPreorder(original_tree.root);
        System.out.println();
        this.threshold_cost=threshold_cost;
        this.communication_requests=communication_requests;
        this.splayNet=splayNet;                             //changes after every query (u,v)

        /*  Print statements for testing
//        this.original_tree.commute(1,7);
//        System.out.println("original tree after change");
//        this.original_tree.printPreorder(this.original_tree.root);
//        System.out.println("splay Net");
//        this.splayNet.printPreorder(this.splayNet.root);
         */
    }
    int adjustment_cost=0;              //stores cost taken for adjusting
    int total_routing_cost=0;           //stores cost taken to route all pairs of queries given in input
    int epoch_routing_cost=0;           //stores cost taken to route pairs of queries given in input till it reaches "a"
    public void laziness_prop()
    {
        int iter=0;
        System.out.println("Final tree:");
        laziness_prop_recur(this.original_tree,iter);       //function calling of the recursive function
        System.out.println("total_routing_cost="+ total_routing_cost);
        System.out.println("epoch_routing_cost="+ epoch_routing_cost);
        System.out.println("adjustment_cost=" + adjustment_cost);
    }
    private void laziness_prop_recur(BST_SplayNet tree, int iter)
    {
         /*  Print statements for testing
//        System.out.print("tree that entered: ");
//        tree.printPreorder(tree.root);
//        System.out.println();
          */
        if (iter>=communication_requests.length)        //Base condition (or escape condition) for recursion
        {
            tree.printPreorder(tree.root);              //printing final tree
            System.out.println();
            return;
        }
        int u=communication_requests[iter][0];          //stores u
        int v=communication_requests[iter][1];          //stores v
        int cost=tree.cost(u,v);                        //calculates cost to travel from u to v
        this.splayNet.commute(u,v);                     //makes changes in the splayNet

         /*  Print statements for testing
        //System.out.println("cost for "+u+" "+v+" ="+cost);
        //System.out.println("splayNet="+splayNet+" "+splayNet.root.key);
        this.splayNet.commute(u,v);
        //System.out.println("splayNet after commute of "+u+" "+v);
        //this.splayNet.printPreorder(this.splayNet.root);
        //System.out.println();
          */

        total_routing_cost=total_routing_cost+cost;
        epoch_routing_cost=epoch_routing_cost+cost;
        System.out.println(epoch_routing_cost);
        if(epoch_routing_cost>=threshold_cost)              //checks is epoch_cost exceeds 'a'
        {
             adjustment_cost+=threshold_cost;
             epoch_routing_cost=epoch_routing_cost-threshold_cost;
             laziness_prop_recur(splayNet,++iter);          //if epoch_cost exceeds 'a' changed tree is used for next query
            //PASS BY REFERENCE ISSUE
        }
        else
        {
             /*  Print statements for testing
//            System.out.println("Normal tree after commute of "+u+" "+v);
//            tree.printPreorder(tree.root);
//            System.out.println();
              */
            laziness_prop_recur(tree,++iter);               //if epoch_cost does not exceed 'a', tree is not changed, same tree is used for next query
        }
    }
    public static void main(String[] args)
    {
        /* Input Type
        Line 1--> number of nodes in the input tree (eg. m)
        Line 2--> list keys of all nodes present in the tree (m integers)
        Line 3--> number of search queries (eg, n)
        Line 4 to line (n+4)--> n pairs of integers in next n lines
        Line (n+5)--> threshold value (value of a or alpha) -- single integer
        Eg of input :-
        7
        5 2 3 7 1 10 0
        4
        0 3
        1 2
        2 5
        1 7
        5
        Corresponding output:
        original Tree:
        5 2 1 0 3 7 10
        Final tree:
        1 0 7 5 2 3 10
        total_routing_cost=9
        epoch_routing_cost=4
        adjustment_cost=5
         */
        Scanner s=new Scanner(System.in);
        int totNodes=s.nextInt();
        BST_SplayNet input_tree=new BST_SplayNet();
        BST_SplayNet splayNet=new BST_SplayNet();
        for(int i=0;i<totNodes;i++)
        {
            int in=s.nextInt();
            input_tree.insert(in);
            splayNet.insert(in);
        }
        int tot_comm_req=s.nextInt();
        int[][] communication_requests=new int[tot_comm_req][2];
        for(int i=0;i<tot_comm_req;i++)
        {
            communication_requests[i][0]=s.nextInt();
            communication_requests[i][1]=s.nextInt();
        }
        int threshold_value=s.nextInt();
        LazySplayNet original_tree=new LazySplayNet(input_tree,splayNet,threshold_value,communication_requests);
        original_tree.laziness_prop();
    }
}
