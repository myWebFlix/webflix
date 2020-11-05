package com.fri.webflix.api.v1.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/videos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoResource {

	@GET
	public Response getVideo() {
		return Response.ok().build();
	}

	@GET
	@Path("/{videoId}")
	public Response getVideo(@PathParam("videoId") Integer videoId) {
		return Response.status(Response.Status.OK).entity(videoId + 100).build();
	}

}
