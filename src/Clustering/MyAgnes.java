package Clustering;

import java.util.ArrayList;
import java.util.List;
import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Husni Munaya
 */
public class MyAgnes extends AbstractClusterer  {
    // Default cluster number
    private int numClusters = 2;
    
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
                    Double oldDistance = calculateSingleDistance(clusters.get(jMin), clusters.get(kMin));
                    Double currentDistance = calculateSingleDistance(clusters.get(j), clusters.get(k));
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
    
    public double calculateSingleDistance(ClusterNode first, ClusterNode second) {
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

    @Override
    public void buildClusterer(Instances data) {
        calculateDistanceMatrix(data, distanceFunction);
        initCluster(data);
        agglomerate(data);
        System.out.println("hmm");
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numClusters;
    }
    
    public void calculateDistanceMatrix(Instances data, DistanceFunction distanceFunction) {
        distances = new Double[data.numInstances()][data.numInstances()];
        
        distanceFunction.setInstances(data);
//        distanceFunction.setOptions(new String[] {"-D"});
        
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                distances[i][j] = distanceFunction.distance(data.get(i), data.get(j));
                distances[j][i] = distances[i][j];
            }
        }
    }
}
