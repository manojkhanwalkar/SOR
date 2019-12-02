package service;

import java.util.ArrayList;
import java.util.List;

public class Services {

    static class ServiceHolder {
        static Services services = new Services();
    }

    public static Services getInstance() {
        return ServiceHolder.services;
    }

    private Services() {

    }

    volatile List<Service> services = new ArrayList<>();

    public void register(Service service) {
        if (!services.contains(service)) {
            services.add(service);
        }
    }

    protected void start() {

        System.out.println("Starting SOR services");
        services.stream().forEach(s -> s.init());

        services.stream().

    forEach(s->s.start());
    }


    protected void stop()
    {

        System.out.println("Stopping SOR services");
        services.stream().forEach(s->s.destroy());
        services.stream().forEach(s->s.stop());
    }


}
