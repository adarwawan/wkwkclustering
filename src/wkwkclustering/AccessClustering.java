/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wkwkclustering;

import Clustering.MyKMeans;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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
        System.out.println("Ler "+filename);
        DataSource source = new DataSource(filename);
        data = source.getDataSet();
        MyKMeans kmeans = new MyKMeans();
 
        kmeans.setSeed(10);

        //important parameter to set: preserver order, number of cluster.
        kmeans.buildClusterer(data);
        System.out.println(kmeans.toString());
        
        eval = new ClusterEvaluation();
        eval.setClusterer(kmeans);
        eval.evaluateClusterer(new Instances(data));

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
