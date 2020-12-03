package com.webflix.webflix.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {

	@GET
	@Path("/demo")
	public Response getInfo() {
		String message = "{" +
				"\"clani\": [\"mb0484\", \"rc8309\"]," +
				"\"opis_projekta\": \"Aplikacija za nalaganje, brskanje ter streamanje filmov ali videov.\"," +
				"\"mikrostoritve\": [\"http://34.89.197.212:8080/v1/videos\", \"http://34.107.92.162:8080/v1/streams\", \"http://35.198.86.215:8080/v1/users\"]," +
				"\"github\": [\"https://github.com/myWebFlix/webflix\", \"https://github.com/myWebFlix/video-stream\", \"https://github.com/myWebFlix/users\"]," +
				"\"travis\": []," +
				"\"dockerhub\": [\"https://hub.docker.com/r/webflix/webflix\", \"https://hub.docker.com/r/webflix/video-stream\", \"https://hub.docker.com/r/webflix/users\"]" +
				"}";

		return Response.status(Response.Status.OK).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
