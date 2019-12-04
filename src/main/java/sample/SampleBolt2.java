package sample;

import event.Event;
import service.AbstractBolt;

public class SampleBolt2 extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("Sample Bolt2" + Thread.currentThread().getName() + event);

        // no emit so event terminates


        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
