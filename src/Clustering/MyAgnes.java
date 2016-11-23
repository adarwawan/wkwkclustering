package Clustering;

import java.util.ArrayList;
import java.util.List;
import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.Drawable;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;

/**
 *
 * @author Husni Munaya
 */
public class MyAgnes extends AbstractClusterer implements OptionHandler  {
    public final static int SINGLE = 0;
    public final static int COMPLETE = 1;
    
    // Default cluster number
    private int numClusters = 2;
    
    // Default link type
    private int linkType = SINGLE;
    
    // Default distance function
    private DistanceFunction distanceFunction = new EuclideanDistance();
    
    // Distances matrix
    public Double distances[][];
    
    // Lists of Cluster
    ArrayList<ClusterNode> clusters;
    
    
    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }
    
    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
    
    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }
    
    public void initCluster(Instances data) {
        clusters = new ArrayList<>();
        for (int i = 0; i < data.numInstances(); i++) {
            ClusterNode node = new ClusterNode();
            node.leaf(true);
            node.setData(data.get(i));
            
            clusters.add(node);
        }
    }
    
    public void agglomerate(Instances data) {
        while (clusters.size() > 3) {
            // Find closest distance between cluster;
            int jMin = 0;
            int kMin = 1;

            for (int j = 0; j < clusters.size(); j++) {
                for (int k = j; k < clusters.size(); k++) {
                    if (j == k) {
                        continue;
                    }
                    Double oldDistance = null;
                    Double currentDistance = null;
                    
                    if (linkType == SINGLE) {
                        oldDistance = calculateSingleDistance(clusters.get(jMin), clusters.get(kMin));
                        currentDistance = calculateSingleDistance(clusters.get(j), clusters.get(k));
                    } else if (linkType == COMPLETE) {
                        oldDistance = calculateCompleteDistance(clusters.get(jMin), clusters.get(kMin));
                        currentDistance = calculateCompleteDistance(clusters.get(j), clusters.get(k));
                    }
                    
                    if (currentDistance < oldDistance) {
                        jMin = j;
                        kMin = k;
                    }
                }
            }

            ClusterNode cluster = new ClusterNode();
            cluster.leaf(false);
            cluster.addFirst(clusters.get(jMin));
            cluster.addSecond(clusters.get(kMin));
            clusters.add(cluster);

            int min = Math.min(jMin, kMin);
            int max = Math.max(jMin, kMin);

            clusters.remove(min);
            clusters.remove(max - 1);
        }
        
        
        for (ClusterNode c : clusters) {
            System.out.println(c.getItems());
        }
    }
    
    public Double calculateSingleDistance(ClusterNode first, ClusterNode second) {
        List<Instance> firstItems = first.getItems();
        List<Instance> secondItems = second.getItems();
        
        Double distance = Double.MAX_VALUE;
        for (int i = 0; i < firstItems.size(); i++) {
            for (int j = 0; j < secondItems.size(); j++) {
                Double currentDistance = distanceFunction.distance(firstItems.get(i), secondItems.get(j));
                if (currentDistance < distance) {
                    distance = currentDistance;
                }
            }
        }
        
        return distance;
    }
    
    public Double calculateCompleteDistance(ClusterNode first, ClusterNode second) {
        List<Instance> firstItems = first.getItems();
        List<Instance> secondItems = second.getItems();

        Double distance = Double.MIN_VALUE;
        for (int i = 0; i < firstItems.size(); i++) {
            for (int j = 0; j < secondItems.size(); j++) {
                Double currentDistance = distanceFunction.distance(firstItems.get(i), secondItems.get(j));
                if (currentDistance > distance) {
                    distance = currentDistance;
                }
            }
        }
        
        return distance;
    }

    @Override
    public void buildClusterer(Instances data) {
        calculateDistanceMatrix(data, distanceFunction);
        initCluster(data);
        agglomerate(data);
        System.out.println("hmm");
    }
    
    @Override
    public int clusterInstance(Instance instance) throws Exception {
        ClusterNode node = new ClusterNode();
        node.leaf(true);
        node.setData(instance);

        Double distance = Double.MAX_VALUE;
        int iMin = 0;
        for (int i = 0; i < clusters.size(); i++) {
            Double currentDistance = calculateSingleDistance(node, clusters.get(i));
            if (currentDistance < distance) {
                iMin = i;
            }
        }

        return iMin;
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numClusters;
    }
    
    public void calculateDistanceMatrix(Instances data, DistanceFunction distanceFunction) {
        distances = new Double[data.numInstances()][data.numInstances()];
        
        distanceFunction.setInstances(data);
        
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                distances[i][j] = distanceFunction.distance(data.get(i), data.get(j));
                distances[j][i] = distances[i][j];
            }
        }
    }
}
