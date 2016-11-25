/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clustering;

import java.util.ArrayList;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Husni Munaya
 */
public class MyAgnesTest {
    public static void main(String[] args) throws Exception {
        DataSource data = new DataSource("criminal-preprocessed.arff");
        Instances instances = data.getDataSet();
        
        MyAgnes agnes = new MyAgnes();
        agnes.setNumClusters(2);
        agnes.setLinkType(MyAgnes.COMPLETE);
        agnes.buildClusterer(instances);
        
        System.out.println(agnes);
    }
}
