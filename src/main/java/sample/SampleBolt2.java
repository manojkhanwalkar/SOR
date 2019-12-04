package sample;

import event.Event;
import service.AbstractBolt;

public class SampleBolt2 extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("Sample Bolt2" + Thread.currentThread().getName() + event);

        // no emit so event terminates



    }

}
