package event;

import service.Service;
import service.Services;

public class EventProcessingService implements Service {

    public enum Events { List , Order , Execution , MarketData}

    public EventProcessingService()
    {
        Services.getInstance().register(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void stop() {

    }


}
