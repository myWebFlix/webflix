package com.fri.webflix.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "video_metadata")
@NamedQueries(value =
		{
				@NamedQuery(name = "VideoMetadataEntity.getAll",
						query = "SELECT vm FROM VideoMetadataEntity vm")
		})
public class VideoMetadataEntity {
	// Fields

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title")
	private String title;

	// Getters & Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
