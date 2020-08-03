public class LazySplayTree {
    BST_SplayTree original_tree;          //original BST
    int threshold_cost;         //a
    int[] communication_requests;     //nx2 matrix, with n communication request pairs
    BST_SplayTree splayTree=original_tree;
    public LazySplayTree(BST_SplayTree original_tree, int threshold_cost, int[] communication_requests)
    {
        this.original_tree=original_tree;
        this.threshold_cost=threshold_cost;
        this.communication_requests=communication_requests;
    }
    int total_routing_cost=0;
    int epoch_routing_cost=0;
    int adjustment_cost=0;
    public void laziness_prop()
    {
        int iter=0;
        laziness_prop_recur(original_tree,iter);
    }
    private void laziness_prop_recur(BST_SplayTree tree, int iter)
    {
        if (iter>=communication_requests.length)
        {
            return;
        }
        int u=communication_requests[iter];
        int cost=tree.search_cost(u);
        splayTree.splay(tree.root, u);
        total_routing_cost=total_routing_cost+cost;
        epoch_routing_cost=epoch_routing_cost+cost;
        if(epoch_routing_cost>=threshold_cost)
        {
            adjustment_cost+=threshold_cost;
            epoch_routing_cost=epoch_routing_cost-threshold_cost;
            laziness_prop_recur(splayTree,iter++);
        }
        else
        {
            laziness_prop_recur(tree,iter++);
        }
    }
}
