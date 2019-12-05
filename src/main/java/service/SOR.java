package service;

import processor.*;

public class SOR implements Runnable {

    public static void main(String[] args) {

        SOR sor = new SOR();
        EventProcessingService eventService = new EventProcessingService();


       // for (int i=0;i<5;i++) {
            Topology.TopologyBuilder builder = new Topology.TopologyBuilder();
            Spout spout = new ListSpout();
            Bolt listSlicerBolt = new ListSlicerBoltSync();
            Bolt bolt = new SysOutBoltSync();
            builder.addSpout(spout).addBolt(listSlicerBolt, "ListSpout").addBolt(new OrderSlicerBoltSync(), "ListSlicerBoltSync")
                    .addBolt(new ExecutionCreatorBoltSync(),"OrderSlicerBoltSync")
                    .addBolt(bolt,"ExecutionCreatorBoltSync");
            Topology topology = builder.build();
            eventService.register(topology);

      //  }



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
