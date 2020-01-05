package io.github.cepr0.demo.auth.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "email")
@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
public class User {

	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Short version;

	@Column(nullable = false, length = 64)
	private String name;

	@NaturalId
	@Column(length = 64, nullable = false)
	private String email;

	private String password;

	@Column(columnDefinition = "varchar(255) default ''")
	private String avatarUrl;

	@Column(length = 64)
	private String googleId;

	@Enumerated(EnumType.STRING)
	@Column(length = 5, nullable = false)
	private Role role = Role.USER;

	public enum Role implements GrantedAuthority {
		USER, ADMIN;

		@Override
		public String getAuthority() {
			return this.name();
		}
	}
}
