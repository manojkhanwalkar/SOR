package service;

import processor.*;

public class CHFGVAPMerger implements Runnable {

    public static void main(String[] args) {

        CHFGVAPMerger sor = new CHFGVAPMerger();
        EventProcessingService eventService = new EventProcessingService();


            Topology.TopologyBuilder builder = new Topology.TopologyBuilder();


            //
        //CHFGVAPSolrWriter

            Spout spout = new CHFFileReaderSpout();
            Bolt bolt = new CHFGVAPMergerBoltSync();
            Bolt bolt1 = new CHFGVAPSolrWriterBoltSync();

            builder.addSpout(spout).addBolt(bolt, "CHFGVAPMergerBoltSync").addBolt(bolt1,"CHFGVAPSolrWriter");

/*
        // Bolt listSlicerBolt = new ListSlicerBoltSync();
            builder.addSpout(spout).addBolt(listSlicerBolt, "ListSpout").addBolt(new OrderSlicerBoltSync(), "ListSlicerBoltSync")
                    .addBolt(new ExecutionCreatorBoltSync(),"OrderSlicerBoltSync")
                    .addBolt(bolt,"ExecutionCreatorBoltSync"); */


            Topology topology = builder.build();
            eventService.register(topology);




        Services.getInstance().register(eventService);

        sor.start();

        Thread holder = new Thread(new Holder1());

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

class Holder1 implements Runnable{

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
