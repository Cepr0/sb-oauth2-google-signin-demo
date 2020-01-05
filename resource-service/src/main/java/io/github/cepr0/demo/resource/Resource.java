package io.github.cepr0.demo.resource;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "resources")
@DynamicInsert
@DynamicUpdate
public class Resource {
	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Short version;

	@Enumerated(EnumType.STRING)
	@Column(length = 12, nullable = false)
	private ValueType type;

	@Column(length = 32, nullable = false)
	private String name;

	public enum ValueType {
		HIGH_VALUE, MIDDLE_VALUE, LOW_VALUE
	}
}
