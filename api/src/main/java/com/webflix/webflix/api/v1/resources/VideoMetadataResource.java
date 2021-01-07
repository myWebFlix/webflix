package com.webflix.webflix.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import com.webflix.webflix.models.entities.VideoMetadataEntity;
import com.webflix.webflix.services.beans.VideoMetadataBean;
import com.webflix.webflix.services.config.RestConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;


@Log
@ApplicationScoped
@Path("/videos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS")
public class VideoMetadataResource {

	private Logger log = Logger.getLogger(VideoMetadataResource.class.getName());

	@Inject
	private RestConfig restConfig;

	@Inject
	private VideoMetadataBean videoMetadataBean;

	@GET
	public Response getVideoMetadata(@HeaderParam("ID-Token") String idTokenString) {

		String userId = videoMetadataBean.manageUser(idTokenString);

		if (userId != null) {

			List<VideoMetadataEntity> vmes = videoMetadataBean.getVideoMetadata();

			System.out.println("User ID: " + userId);

			return Response.ok(vmes).build();

		} else {

			return Response.status(Response.Status.UNAUTHORIZED).build();

		}
	}

	@GET
	@Path("/{videoMetadataId}")
	public Response getVideoMetadata(@PathParam("videoMetadataId") Integer videoMetadataId) {

		VideoMetadataEntity vme = videoMetadataBean.getVideoMetadata(videoMetadataId);

		if (vme == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
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
