package Clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Husni Munaya
 */
public class ClusterNode {
    private boolean isLeaf;
    private ClusterNode firstNode;
    private ClusterNode secondNode;
    private NodeData data;
    
    public ClusterNode() {
        firstNode = null;
        secondNode = null;
        data = null;
    }
    
    public void setData(NodeData data) {
        this.data = data;
    }
    
    public void leaf(boolean bool) {
        this.isLeaf = bool;
    }
    
    public void addFirst(ClusterNode node) {
        this.firstNode = node;
    }
    
    public ClusterNode getFirst() {
        return firstNode;
    }
    
    public void addSecond(ClusterNode node) {
        this.secondNode = node;
    }
    
    public ClusterNode getSecond() {
        return secondNode;
    }
    
    public List<NodeData> getItems() {
        if (this.isLeaf) {
            return new ArrayList<>(Arrays.asList(data));
        }
        
        List<NodeData> fromFirst = this.firstNode.getItems();
        List<NodeData> fromSecond = this.secondNode.getItems();
        
        List<NodeData> items = new ArrayList<>();
        items.addAll(fromFirst);
        items.addAll(fromSecond);
        
        return items;
    }
}
