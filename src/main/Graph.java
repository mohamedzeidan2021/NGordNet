package main;

import java.util.*;

public class Graph extends HashMap<Integer, TreeSet<Integer>> {

    HashMap<Integer, Integer> edges;

    Graph() {
        edges = new HashMap<>();
    }

    //WILL BE CALLED IN HYPONYM HANDLER

    //wordNet will grab words and call these methods to put that shit in

    public void addNode(int wordID, TreeSet<Integer> adj) {

        //check if in adj list
        if (!this.containsKey(wordID)) {
            this.put(wordID, adj);
        } else {
            //add the values to it
            TreeSet<Integer> oldVals = this.get(wordID);
            this.remove(wordID);
            oldVals.addAll(adj);
            this.put(wordID, oldVals);
        }

    }

    public void printGraph() {
        for (Integer wordID : this.keySet()) {
            TreeSet<Integer> adj = this.get(wordID);
            System.out.print("WordID: " + wordID + " - Adjacency List: ");
            for (Integer adjacentWordID : adj) {
                System.out.print(adjacentWordID + " ");
            }
            System.out.println(); // Move to the next line for the next wordID
        }
    }


    public Set<Integer> getChildren(Set<Integer> iDforWord) {
        HashSet<Integer> res = new HashSet<>();

        //ID for word is just the ID FOR THE WORD MF
        for (int id : iDforWord) {
            findChildrenRecursively(id, res);
        }

        return res;
    }

    public void findChildrenRecursively(int id, Set<Integer> res) {

        res.add(id);

        if (this.containsKey(id)) {

            TreeSet<Integer> adjacents = this.get(id);
            for (int adj : adjacents) {
                findChildrenRecursively(adj, res);
                res.add(adj);
            }
        }
    }
}
