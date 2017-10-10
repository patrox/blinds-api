package pl.najda.blinds.api;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final BlindsApiConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
