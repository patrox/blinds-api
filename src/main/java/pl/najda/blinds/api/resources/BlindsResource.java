package pl.najda.blinds.api.resources;

import pl.najda.blinds.api.dao.BlindsDao;
import pl.najda.blinds.api.model.Blind;
import pl.najda.blinds.api.model.BlindCommand;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/blinds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlindsResource {

    private final BlindsDao blindsDao;

    public BlindsResource(BlindsDao blindsDao) {
        this.blindsDao = blindsDao;
    }

    @GET
    public Collection<Blind> getBlinds() {
        return blindsDao.getBlinds();
    }

    @POST
    public Blind addBlind(Blind blind) {
        blindsDao.add(blind);
        return blind;
    }

    @GET
    @Path("test")
    public String getEnum(@NotNull @QueryParam("blindCommand") BlindCommand blindCommand) {
        return blindCommand.toString();
    }

}