package com.webflix.webflix.api.v1.resources;

import com.webflix.webflix.models.entities.VideoMetadataEntity;
import com.webflix.webflix.services.beans.VideoMetadataBean;
import com.webflix.webflix.services.config.RestConfig;

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
	private RestConfig restConfig;

	@Inject
	private VideoMetadataBean videoMetadataBean;

	@GET
	public Response getVideoMetadata() {
		if (restConfig.getMaintenanceMode()) {
			return Response.ok("Maintenance in progress.").build();
		} else {
			List<VideoMetadataEntity> vmes = videoMetadataBean.getVideoMetadata();

			return Response.ok(vmes).build();
		}
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

	@POST
	public Response createVideoMetadata(VideoMetadataEntity vme) {

		if (vme.getTitle() == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			vme = videoMetadataBean.createVideoMetadata(vme);
		}

		return Response.status(Response.Status.OK).entity(vme).build();

	}

	@PUT
	@Path("{id}")
	public Response putVideoMetadata(@PathParam("id") Integer id, VideoMetadataEntity vme) {

		vme = videoMetadataBean.putVideoMetadata(id, vme);

		if (vme == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(vme).build();

	}

	@DELETE
	@Path("{id}")
	public Response deleteVideoMetadata(@PathParam("id") Integer id) {

		boolean deleted = videoMetadataBean.deleteVideoMetadata(id);

		if (deleted) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
