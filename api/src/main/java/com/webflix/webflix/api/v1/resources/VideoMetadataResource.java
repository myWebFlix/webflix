package com.webflix.webflix.api.v1.resources;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import com.webflix.webflix.models.entities.VideoMetadataEntity;
import com.webflix.webflix.services.beans.VideoMetadataBean;
import com.webflix.webflix.services.config.RestConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


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
		if (restConfig.getMaintenanceMode()) {
			return Response.ok("Maintenance in progress.").build();
		} else {
			List<VideoMetadataEntity> vmes = videoMetadataBean.getVideoMetadata();

			String CLIENT_ID = "304157826665-k1nt1phqk7qgsgk5m2hh6hlfkof6g5oe.apps.googleusercontent.com";

			HttpTransport transport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					// Specify the CLIENT_ID of the app that accesses the backend:
					.setAudience(Collections.singletonList(CLIENT_ID))
					// Or, if multiple clients access the backend:
					//.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
					.build();

			System.out.println(idTokenString);

			GoogleIdToken idToken = null;
			try {
				idToken = verifier.verify(idTokenString);
			} catch (Exception e) {}

			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				System.out.println("User ID: " + userId);

				return Response.ok(vmes).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
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
