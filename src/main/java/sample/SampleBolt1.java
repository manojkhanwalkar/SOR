package sample;

import event.Event;
import service.AbstractBolt;
import service.AbstractSpout;

public class SampleBolt1 extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("Sample Bolt " + Thread.currentThread().getName() + event);

        emit("SampleBolt1" , new SampleEvent());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
