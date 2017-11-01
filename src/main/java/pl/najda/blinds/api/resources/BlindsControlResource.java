package pl.najda.blinds.api.resources;

import pl.najda.blinds.api.dao.BlindsDao;
import pl.najda.blinds.api.rpi.BlindsController;
import pl.najda.blinds.api.model.Blind;
import pl.najda.blinds.api.model.BlindCommand;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/blinds/control")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlindsControlResource {

    private final BlindsController blindsController;
    private final BlindsDao blindsDao;

    public BlindsControlResource(BlindsController blindsController, BlindsDao blindsDao) {
        this.blindsController = blindsController;
        this.blindsDao = blindsDao;
    }

    @Path("{blindName}/{blindCommand}")
    @POST
    public Response executeCommand(@PathParam("blindName") String blindName, @PathParam("blindCommand") BlindCommand blindCommand) {
        final Blind blind = blindsDao.getBlind(blindName).orElseThrow(() -> new WebApplicationException("Blind named: '" + blindName + "' was not found!", 404));
        final Collection<String> executeResults = blindsController.execute(blind.getChannels(), blindCommand);

        return Response.ok(executeResults).build();
    }

    @GET
    public Response getCurrentChannel() {
        return Response.ok(blindsController.getCurrentChannel()).build();
    }

}