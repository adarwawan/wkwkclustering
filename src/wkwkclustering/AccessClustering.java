/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wkwkclustering;

import Clustering.MyAgnes;
import Clustering.MyKMeans;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.HierarchicalClusterer;
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
        
        switch (cluster_algo) {
            case 1: // weka KMeans
                {
                    SimpleKMeans kmeans = new SimpleKMeans();
                    kmeans.setSeed(10);
                    kmeans.setPreserveInstancesOrder(true);
                    kmeans.setNumClusters(3);
                    kmeans.buildClusterer(data);
                    System.out.println(kmeans.toString());
                    
                    int[] assignments = kmeans.getAssignments();
 
                    int i=0;
                    for(int clusterNum : assignments) {
                        System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
                        i++;
                    }
                    break;
                }
            case 2: // weka Hierarchical
                {
                    HierarchicalClusterer clusterer = new HierarchicalClusterer();
                    clusterer.setPrintNewick(true);
                    clusterer.buildClusterer(data);
                    System.out.println(clusterer.toString());
                    
//                    int[] assignments = hierarchical.getAssignments();
// 
//                    int i=0;
//                    for(int clusterNum : assignments) {
//                        System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
//                        i++;
//                    }
                    break;
                }
            case 3: // myAgnes
                MyAgnes myAgnes = new MyAgnes();
                Scanner reader = new Scanner(System.in);
                System.out.println("Jumlah cluster");
                int numCluster = reader.nextInt();
                
                myAgnes.setNumClusters(numCluster);
                myAgnes.buildClusterer(data);
                System.out.println(myAgnes.toString());
                break;
            case 4: // myKMeans
                {
                    MyKMeans kmeans = new MyKMeans();
                    kmeans.setSeed(10);
                    //important parameter to set: preserver order, number of cluster.
                    kmeans.buildClusterer(data);
                    System.out.println(kmeans.toString(data));
                    break;
                }
            default:
                break;
        }
        
    }
}
