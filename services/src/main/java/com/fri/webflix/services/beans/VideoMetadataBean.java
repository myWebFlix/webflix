package com.fri.webflix.services.beans;

import com.fri.webflix.models.entities.VideoMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class VideoMetadataBean {

	@PersistenceContext(unitName = "webflix-jpa")
	private EntityManager em;

	public List<VideoMetadataEntity> getVideoMetadata(){

		TypedQuery<VideoMetadataEntity> query = em.createNamedQuery("VideoMetadataEntity.getAll", VideoMetadataEntity.class);

		return query.getResultList();
	}

	public VideoMetadataEntity getVideoMetadata(Integer id) {

		VideoMetadataEntity vme = em.find(VideoMetadataEntity.class, id);

		if (vme == null) {
			throw new NotFoundException();
		}

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
