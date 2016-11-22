package Clustering;

import java.util.ArrayList;
import java.util.Random;
import weka.clusterers.RandomizableClusterer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import static weka.core.pmml.PMMLUtils.pad;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adarwawan
 */
public class MyKMeans extends RandomizableClusterer{

    // Data
    private int numCluster = 3; // Jumlah cluster, Default = 2
    private Instances clusterCentroids; // holds the cluster centroids
    private int[] clusterSizes; // banyak instances tiap centroid
    private int maxIterations = 500; // iterasi maksimal
    private int iterations = 0; // track jumlah iterasi
    protected DistanceFunction distFunction = new EuclideanDistance();
    private int[] clusterAssignments; // instance ke-i itu cluster ke berapa
    private double[] squaredErrors;
    
    public MyKMeans() {
        super();
        
        int seed_default = 10;
        setSeed(10);
    }
    
    @Override
    public void buildClusterer(Instances data) throws Exception {
        Instances instances = new Instances(data);
        int instanceSize = instances.numInstances();
        if ( instanceSize == 0 ){
            throw new RuntimeException("The dataset should not be empty");
        }
        
        distFunction.setInstances(instances);
        squaredErrors = new double[numCluster];
        
        // Algoritma k-Means
        // 1. Tentuin "numCluster" centroid awal. random
        clusterCentroids = new Instances(instances, numCluster);
        clusterAssignments = new int[instances.numInstances()];
        
        // Randomisasi
        Random rand = new Random(getSeed());
        int instIdx;
        ArrayList<Integer> chooseIdx = new ArrayList<>();
        for (int i = 0; i < numCluster; i++) {
            do {
                instIdx = rand.nextInt(instanceSize);
            } while (chooseIdx.contains(instIdx));
            chooseIdx.add(instIdx);
            clusterCentroids.add(instances.instance(instIdx));
        }
        
        // 2. repeat
        // 3.   (re)assign tiap objek ke cluster yang objek nya similar (pake ED)
        // 4.   update centroid baru
        // 5. until no change
        
        boolean converged = false;
        Instances[] tempI = new Instances[numCluster];
        
        iterations = 0;
        while (!converged) {
            iterations++;
            converged = true;
            for (int i = 0; i < instanceSize; i++) {
                Instance toCluster = instances.instance(i);
                int newC = clusterProcessedInstance(toCluster, true);
                if (newC != clusterAssignments[i]) {
                    converged = false;
                }
                clusterAssignments[i] = newC;
            }
            
            // update centroids
            clusterCentroids = new Instances(instances, numCluster);
            for (int i = 0; i < numCluster; i++) {
                tempI[i] = new Instances(instances, 0);
            }
            for (int i = 0; i < instanceSize; i++) {
               tempI[clusterAssignments[i]].add(instances.instance(i));
            }
            for (int i = 0; i < numCluster; i++) {
                moveCentroid(i, tempI[i], true);
            }
            
            if (iterations == maxIterations) {
                converged = true;
            }
            
            if (!converged) {
                squaredErrors = new double[numCluster];
            }
            
            clusterSizes = new int[numCluster];
            for (int i = 0; i < numCluster; i++) {
                clusterSizes[i] = tempI[i].numInstances();
            }
        }
        
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numCluster;
    }

    private int clusterProcessedInstance(Instance data, boolean updateErrors) {
        double minDist = Integer.MAX_VALUE;
        int bestCluster = 0;
        for (int i = 0; i < numCluster; i++) {
            double dist = distFunction.distance(data, clusterCentroids.instance(i));
            if (dist < minDist) {
                minDist = dist;
                bestCluster = i;
            }
        }
        if (updateErrors) {
            if(distFunction instanceof EuclideanDistance){
                //Euclidean distance to Squared Euclidean distance
                minDist *= minDist;
            }
            squaredErrors[bestCluster] += minDist;
        }
        return bestCluster;
    }

    private double[] moveCentroid(int centroidIdx, Instances members, boolean updateClusterInfo) {
        double[] vals = new double[members.numAttributes()];
        
        for (int i = 0; i < members.numAttributes(); i++) {
            vals[i] = members.meanOrMode(i);
        }
        Instance inst = new DenseInstance(1.0, vals);
        clusterCentroids.add(inst);
        return vals;
    }
    
    public String toString() {
        if (clusterCentroids == null) {
            return "No clusterer built yet!";
        }

        int maxWidth = 0;
        int maxAttWidth = 0;
        boolean containsNumeric = false;
        for (int i = 0; i < numCluster; i++) {
            for (int j = 0; j < clusterCentroids.numAttributes(); j++) {
                if (clusterCentroids.attribute(j).name().length() > maxAttWidth) {
                    maxAttWidth = clusterCentroids.attribute(j).name().length();
                }
                if (clusterCentroids.attribute(j).isNumeric()) {
                    containsNumeric = true;
                    double width = Math.log(Math.abs(clusterCentroids.instance(i).value(j))) / Math.log(10.0);
                    if (width < 0) {
                        width = 1;
                    }
                // decimal + # decimal places + 1
                    width += 6.0;
                    if ((int) width > maxWidth) {
                        maxWidth = (int) width;
                    }
                }
            }
        }

        for (int i = 0; i < clusterCentroids.numAttributes(); i++) {
            if (clusterCentroids.attribute(i).isNominal()) {
                Attribute a = clusterCentroids.attribute(i);
                for (int j = 0; j < clusterCentroids.numInstances(); j++) {
                    String val = a.value((int) clusterCentroids.instance(j).value(i));
                    if (val.length() > maxWidth) {
                        maxWidth = val.length();
                    }
                }
                for (int j = 0; j < a.numValues(); j++) {
                    String val = a.value(j) + " ";
                    if (val.length() > maxAttWidth) {
                        maxAttWidth = val.length();
                    }
                }
            }
        }

    // check for size of cluster sizes
    for (double m_ClusterSize : clusterSizes) {
        String size = "(" + m_ClusterSize + ")";
        if (size.length() > maxWidth) {
            maxWidth = size.length();
        }
    }

    String plusMinus = "+/-";
    maxAttWidth += 2;
    if (maxAttWidth < "Attribute".length() + 2) {
      maxAttWidth = "Attribute".length() + 2;
    }

    if (maxWidth < "Full Data".length()) {
      maxWidth = "Full Data".length() + 1;
    }

    if (maxWidth < "missing".length()) {
      maxWidth = "missing".length() + 1;
    }

    StringBuffer temp = new StringBuffer();
    temp.append("\nkMeans\n======\n");
    temp.append("\nNumber of iterations: " + iterations);


    temp.append("\n\nFinal cluster centroids:\n");
    temp.append(pad("Cluster#", " ", (maxAttWidth + (maxWidth * 2 + 2))
      - "Cluster#".length(), true));

    temp.append("\n");
    temp
      .append(pad("Attribute", " ", maxAttWidth - "Attribute".length(), false));

    temp
      .append(pad("Full Data", " ", maxWidth + 1 - "Full Data".length(), true));

    // cluster numbers
    for (int i = 0; i < numCluster; i++) {
      String clustNum = "" + i;
      temp.append(pad(clustNum, " ", maxWidth + 1 - clustNum.length(), true));
    }
    temp.append("\n");

    // cluster sizes
    String cSize = "(" + Utils.sum(clusterSizes) + ")";
    temp.append(pad(cSize, " ", maxAttWidth + maxWidth + 1 - cSize.length(),
      true));
    for (int i = 0; i < numCluster; i++) {
      cSize = "(" + clusterSizes[i] + ")";
      temp.append(pad(cSize, " ", maxWidth + 1 - cSize.length(), true));
    }
    temp.append("\n");

    temp.append(pad("", "=",
      maxAttWidth
        + (maxWidth * (clusterCentroids.numInstances() + 1)
          + clusterCentroids.numInstances() + 1), true));
    temp.append("\n");

    for (int i = 0; i < clusterCentroids.numAttributes(); i++) {
      String attName = clusterCentroids.attribute(i).name();
      temp.append(attName);
      for (int j = 0; j < maxAttWidth - attName.length(); j++) {
        temp.append(" ");
      }

      String strVal;
      String valMeanMode;
      // full data
      
      for (int j = 0; j < numCluster; j++) {
        if (clusterCentroids.attribute(i).isNominal()) {
          if (clusterCentroids.instance(j).isMissing(i)) {
            valMeanMode =
              pad("missing", " ", maxWidth + 1 - "missing".length(), true);
          } else {
            valMeanMode =
              pad(
                (strVal =
                  clusterCentroids.attribute(i).value(
                    (int) clusterCentroids.instance(j).value(i))), " ",
                maxWidth + 1 - strVal.length(), true);
          }
        } else {
          if (clusterCentroids.instance(j).isMissing(i)) {
            valMeanMode =
              pad("missing", " ", maxWidth + 1 - "missing".length(), true);
          } else {
            valMeanMode =
              pad(
                (strVal =
                  Utils.doubleToString(clusterCentroids.instance(j).value(i),
                    maxWidth, 4).trim()), " ", maxWidth + 1 - strVal.length(),
                true);
          }
        }
        temp.append(valMeanMode);
      }
      temp.append("\n");
    }

    temp.append("\n\n");
    return temp.toString();
  }
}