package service;

import sample.SampleBolt1;
import sample.SampleBolt2;
import sample.SampleSpout;

public class SORTest implements Runnable {

    public static void main(String[] args) {

        SORTest sor = new SORTest();
        EventProcessingService eventService = new EventProcessingService();


        for (int i = 0; i < 5; i++) {
            Topology.TopologyBuilder builder = new Topology.TopologyBuilder();
            Spout spout = new SampleSpout();
            Bolt bolt1 = new SampleBolt1();
            Bolt bolt2 = new SampleBolt2();
            builder.addSpout(spout).addBolt(bolt1, "SampleSpout").addBolt(bolt2, "SampleBolt1");
            Topology topology = builder.build();
            eventService.register(topology);

        }


        Services.getInstance().register(eventService);

        sor.start();




    }

    static volatile boolean running = true;

    public void start() {
        // create other SOR related services here. each service will register itself with the services class.
        Runtime.getRuntime().addShutdownHook(new Thread(this));
        Services.getInstance().start();
    }

    public void stop() {
        Services.getInstance().stop();
        running = false;
    }


    public void run() {
        stop();

    }




}
