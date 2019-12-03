package service;

import sample.SampleBolt1;
import sample.SampleBolt2;
import sample.SampleSpout;

public class SOR implements Runnable {

    public static void main(String[] args) {

        SOR sor = new SOR();

        Topology.TopologyBuilder builder = new Topology.TopologyBuilder();
        Spout spout = new SampleSpout();
        Bolt bolt1 = new SampleBolt1();
        Bolt bolt2 = new SampleBolt2();
        builder.addSpout(spout).addBolt(bolt1,"SampleSpout").addBolt(bolt2,"SampleBolt1");
        Topology topology = builder.build();
        EventProcessingService eventService = new EventProcessingService();
        eventService.register(topology);
        Services.getInstance().register(eventService);

        sor.start();

        Thread holder = new Thread(new Holder());

        holder.start();


    }

    static volatile boolean running = true;
    public void start()
    {
        // create other SOR related services here. each service will register itself with the services class.
        Runtime.getRuntime().addShutdownHook(new Thread(this));
        Services.getInstance().start();
    }

    public void stop()
    {
        Services.getInstance().stop();
        running = false;
    }


    public void run()
    {
        stop();

    }



}

class Holder implements Runnable{

    public void run()
    {
        while(true)
        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
