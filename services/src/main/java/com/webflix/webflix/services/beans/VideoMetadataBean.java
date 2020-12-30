package com.webflix.webflix.services.beans;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.webflix.webflix.models.entities.VideoMetadataEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@RequestScoped
public class VideoMetadataBean {

	private Logger log = Logger.getLogger(VideoMetadataBean.class.getName());

	@Inject
	private VideoMetadataBean videoMetadataBeanProxy;

	@PersistenceContext(unitName = "webflix-jpa")
	private EntityManager em;

	private Client httpClient;
	private String baseUsersUrl;
	private String baseUrl;

	@PostConstruct
	private void init() {
		httpClient = ClientBuilder.newClient();
		baseUsersUrl = "http://martin.zoxxnet.com"; // "http://localhost:8090";
		baseUrl = "http://localhost:8090";
	}

	public String manageUser(String idTokenString) {
		HttpResponse userAuthResponse = null;

		try {
			HttpClient client = HttpClients.custom().build();
			HttpUriRequest request = RequestBuilder.get()
					.setUri(baseUsersUrl + "/users/v1/user") //.setUri(baseUrl + "/v1/user")
					.setHeader("ID-Token", idTokenString)
					.build();
			userAuthResponse = client.execute(request);

		} catch (Exception e) {
			System.out.println(e);
		}

		if (userAuthResponse != null && userAuthResponse.getStatusLine().getStatusCode() == 200) {

			try {
				HttpEntity entity = userAuthResponse.getEntity();
				String userId = EntityUtils.toString(entity);
				System.out.println("User ID: " + userId);

				return userId;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}

	public List<VideoMetadataEntity> getVideoMetadata(){

		TypedQuery<VideoMetadataEntity> query = em.createNamedQuery("VideoMetadataEntity.getAll", VideoMetadataEntity.class);

		return query.getResultList();
	}

	public VideoMetadataEntity getVideoMetadata(Integer id) {

		VideoMetadataEntity vme = em.find(VideoMetadataEntity.class, id);

		if (vme == null) {
			throw new NotFoundException();
		}

		// videoMetadataBeanProxy.getRating(id); // For testing

		return vme;
	}

	public VideoMetadataEntity createVideoMetadata(VideoMetadataEntity vme) {

		try {
			beginTx();
			em.persist(vme);
			commitTx();
		} catch (Exception e) {
			rollbackTx();
		}

		if (vme.getId() == null) {
			throw new RuntimeException("Entity was not persisted");
		}

		return vme;
	}

	public VideoMetadataEntity putVideoMetadata(Integer id, VideoMetadataEntity vme) {

		VideoMetadataEntity vme_old = em.find(VideoMetadataEntity.class, id);

		if (vme_old == null) {
			return null;
		}

		try {
			beginTx();
			vme.setId(vme_old.getId());
			vme = em.merge(vme);
			commitTx();
		} catch (Exception e) {
			rollbackTx();
		}

		return vme;
	}

	public boolean deleteVideoMetadata(Integer id) {

		VideoMetadataEntity vme = em.find(VideoMetadataEntity.class, id);

		if (vme != null) {
			try {
				beginTx();
				em.remove(vme);
				commitTx();
			} catch (Exception e) {
				rollbackTx();
			}
		} else
			return false;

		return true;
	}

	@Retry
	@Timeout(value = 2, unit = ChronoUnit.SECONDS)
	@CircuitBreaker(requestVolumeThreshold = 3)
	@Fallback(fallbackMethod = "getRatingFallback")
	public Integer getRating(Integer videoId) {

		log.info("Calling feedback service: getting rating.");

		try {
			return httpClient
					.target(baseUrl + "/v1/feedback/rating")
					.queryParam("videoId", videoId)
					.request().get(new GenericType<Integer>() {
					});
		}
		catch (WebApplicationException | ProcessingException e) {
			log.severe(e.getMessage());
			throw new InternalServerErrorException(e);
		}
	}

	public Integer getRatingFallback(Integer videoId) {
		return null;
	}

	// Transactions

	private void beginTx() {
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
	}

	private void commitTx() {
		if (em.getTransaction().isActive())
			em.getTransaction().commit();
	}

	private void rollbackTx() {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
	}

}
