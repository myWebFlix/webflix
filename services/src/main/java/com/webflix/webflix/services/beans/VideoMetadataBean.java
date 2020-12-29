package com.webflix.webflix.services.beans;

import com.webflix.webflix.models.entities.VideoMetadataEntity;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class VideoMetadataBean {

	private Logger log = Logger.getLogger(VideoMetadataBean.class.getName());

	@Inject
	private VideoMetadataBean videoMetadataBeanProxy;

	@PersistenceContext(unitName = "webflix-jpa")
	private EntityManager em;

	private Client httpClient;
	private String baseUrl;

	@PostConstruct
	private void init() {
		httpClient = ClientBuilder.newClient();
		baseUrl = "http://localhost:8081";
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
