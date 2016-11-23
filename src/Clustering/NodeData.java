/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clustering;

import weka.core.Instance;

/**
 *
 * @author husni
 */
public class NodeData {
    public int dataIndex;
    public Instance data;
    
    public NodeData(Instance data, int dataIndex) {
        this.data = data;
        this.dataIndex = dataIndex;
    }
}
