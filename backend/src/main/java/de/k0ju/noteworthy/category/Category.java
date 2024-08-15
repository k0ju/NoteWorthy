package de.k0ju.noteworthy.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "prepend_icon", length = 40, nullable = false)
	private String prependIcon;
	@Column(name = "name", length = 255, nullable = false)
	private String name;
	@Column(name = "append_icon_1", length = 10, nullable = false)
	private String appendIcon1;
	@Column(name = "append_icon_2", length = 10, nullable = false)
	private String appendIcon2;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrependIcon() {
		return prependIcon;
	}
	public void setPrependIcon(String prependIcon) {
		this.prependIcon = prependIcon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppendIcon1() {
		return appendIcon1;
	}
	public void setAppendIcon1(String appendIcon1) {
		this.appendIcon1 = appendIcon1;
	}
	public String getAppendIcon2() {
		return appendIcon2;
	}
	public void setAppendIcon2(String appendIcon2) {
		this.appendIcon2 = appendIcon2;
	}	
}