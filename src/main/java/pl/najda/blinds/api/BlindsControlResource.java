package pl.najda.blinds.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/blinds/control/{blindName}/{blindCommand}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlindsControlResource {

    private final RpiExecutor rpiExecutor;
    private final BlindsDao blindsDao;

    public BlindsControlResource(RpiExecutor rpiExecutor, BlindsDao blindsDao) {
        this.rpiExecutor = rpiExecutor;
        this.blindsDao = blindsDao;
    }

    @POST
    public Response executeCommand(@PathParam("blindName") String blindName, @PathParam("blindCommand") BlindCommand blindCommand) {
        final Blind blind = blindsDao.getBlind(blindName).orElseThrow(() -> new WebApplicationException("Blind named: '" + blindName + "' was not found!", 404));
        rpiExecutor.execute(blind.getChannels(), blindCommand);

        return Response.ok().build();
    }

}