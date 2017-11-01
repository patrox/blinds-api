package pl.najda.blinds.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pl.najda.blinds.api.dao.BlindsDao;
import pl.najda.blinds.api.dao.InMemoryBlindsDao;
import pl.najda.blinds.api.resources.BlindsControlResource;
import pl.najda.blinds.api.resources.BlindsResource;
import pl.najda.blinds.api.rpi.RpiExecutor;

public class BlindsApiApplication extends Application<BlindsApiConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BlindsApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "Blinds API";
    }

    @Override
    public void initialize(final Bootstrap<BlindsApiConfiguration> bootstrap) {
        bootstrap.getObjectMapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    @Override
    public void run(final BlindsApiConfiguration configuration,
                    final Environment environment) {
        final BlindsDao blindsDao = new InMemoryBlindsDao();
        final BlindsResource blindsResource = new BlindsResource(blindsDao);
        final BlindsControlResource blindsControlResource = new BlindsControlResource(new RpiExecutor(1), blindsDao);

        environment.jersey().register(blindsResource);
        environment.jersey().register(blindsControlResource);
    }

}
