package service;

import event.Event;
import graph.TransactionGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Topology {

    TransactionGraph graph = new TransactionGraph();

    private Topology(Spout spout, Map<String,Bolt> bolts)
    {
        this.spout = spout;
        this.bolts = bolts;
    }

    Spout spout ;
    Map<String,Bolt> bolts = new HashMap<>();

    public void execute()
    {
        spout.process();
    }

    public TransactionGraph getGraph() {
        return graph;
    }

    public void setEventService() {

        spout.setEventService(this);
        bolts.values().stream().forEach(b->b.setEventService(this));

    }

    public void forwardSync(String name, Event event) {

            graph.add(event);

            Bolt bolt = bolts.get(name);
            if (bolt!=null)
            {
                bolt.process(event);
            }
            else
            {
                System.out.println("Event cannot be forwarded for " + name);
            }


    }

    public void forward(String name, Event event) {

        CompletableFuture cf = CompletableFuture.runAsync(()->{
            Bolt bolt = bolts.get(name);
            if (bolt!=null)
            {
                bolt.process(event);
            }
            else
            {
                System.out.println("Event cannot be forwarded for " + name);
            }

        });

    }

    static class TopologyBuilder
    {

        Spout spout ;
        Map<String,Bolt> bolts = new HashMap<>();

        public  TopologyBuilder()
        {

        }

        public TopologyBuilder addSpout(Spout spout)
        {
            this.spout = spout;
            return this;
        }

        public TopologyBuilder addBolt(Bolt bolt, String input)
        {
            bolts.put(input,bolt);
            return this;
        }

        //TODO - prevent build being called repeatedly . check that a spout exists .
        public Topology build()
        {
            return new Topology(spout,bolts);
        }
    }

}
