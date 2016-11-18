/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wkwkclustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 *
 * @author adarwawan
 */
public class AccessClustering {
    
    public Instances data;
    public ClusterEvaluation eval;
    public String[] options;
    public DensityBasedClusterer cl;
    
    public AccessClustering(String filename, int cluster_algo) throws Exception {
        BufferedReader datafile = readDataFile(filename); 
        System.out.println("Ler "+filename);
        data = new Instances(datafile);
        
        SimpleKMeans kmeans = new SimpleKMeans();
 
        kmeans.setSeed(10);

        //important parameter to set: preserver order, number of cluster.
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(3);
        kmeans.buildClusterer(data);

        eval = new ClusterEvaluation();
        eval.setClusterer(kmeans);
        eval.evaluateClusterer(data);

        System.out.println("Cluster Evaluation: "+eval.clusterResultsToString()); 
    }
    
    private BufferedReader readDataFile (String filename) {
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }
    
}
