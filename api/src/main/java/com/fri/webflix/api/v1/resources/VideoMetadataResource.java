package com.fri.webflix.api.v1.resources;

import com.fri.webflix.models.entities.VideoMetadataEntity;
import com.fri.webflix.services.beans.VideoMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/videos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoMetadataResource {

	@Inject
	private VideoMetadataBean videoMetadataBean;

	@GET
	public Response getVideoMetadata() {
		List<VideoMetadataEntity> vmes = videoMetadataBean.getVideoMetadata();

		return Response.ok(vmes).build();
	}

	@GET
	@Path("/{id}")
	public Response getVideoMetadata(@PathParam("id") Integer id) {

		VideoMetadataEntity vme = videoMetadataBean.getVideoMetadata(id);

		if (vme == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(vme).build();
	}

}
