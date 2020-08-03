public class LazySplayNet {
    BST_SplayNet original_tree;          //original BST
    int threshold_cost;         //a
    int adjustment_cost=0;
    int[][] communication_requests;     //nx2 matrix, with n communication request pairs
    BST_SplayNet splayNet=original_tree;
    public LazySplayNet(BST_SplayNet original_tree, int threshold_cost, int[][] communication_requests)
    {
        this.original_tree=original_tree;
        this.threshold_cost=threshold_cost;
        this.communication_requests=communication_requests;
    }
    int total_routing_cost=0;
    int epoch_routing_cost=0;
    public void laziness_prop()
    {
        int iter=0;
        laziness_prop_recur(original_tree,iter);
    }
    private void laziness_prop_recur(BST_SplayNet tree, int iter)
    {
        if (iter>=communication_requests.length)
        {
            return;
        }
        int u=communication_requests[iter][0];
        int v=communication_requests[iter][1];
        int cost=tree.cost(u,v);
        splayNet.commute(u,v);
        total_routing_cost=total_routing_cost+cost;
        epoch_routing_cost=epoch_routing_cost+cost;
        if(epoch_routing_cost>=threshold_cost)
        {
             adjustment_cost+=threshold_cost;
             epoch_routing_cost=epoch_routing_cost-threshold_cost;
             laziness_prop_recur(splayNet,iter++);
        }
        else
        {
            laziness_prop_recur(tree,iter++);
        }
    }
}
