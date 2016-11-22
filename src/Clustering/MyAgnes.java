package Clustering;

import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 *
 * @author Husni Munaya
 */
public class MyAgnes extends AbstractClusterer  {
    private int numClusters = 2;
    private DistanceFunction distanceFunction = new EuclideanDistance();
    
    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }
    
    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public void buildClusterer(Instances data) {
        
    }

    @Override
    public int numberOfClusters() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
