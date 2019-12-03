package sample;

import service.AbstractSpout;
import service.Spout;

public class SampleSpout extends AbstractSpout {

    public void process()
    {
        System.out.println("Sample Spout " + Thread.currentThread().getName());

        emit("SampleSpout" , new SampleEvent());
    }

}
