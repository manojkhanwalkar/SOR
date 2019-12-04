package sample;

import service.AbstractSpout;
import service.Spout;

public class SampleSpout extends AbstractSpout {

    public void process()
    {
        System.out.println("Sample Spout " + Thread.currentThread().getName());

        for (int i=0;i<10;i++) {
            emit("SampleSpout", new SampleEvent());

        }
    }

}
