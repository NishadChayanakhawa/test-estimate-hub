package io.github.nishadchayanakhawa.testestimatehub.model;

//import sections
//java utils
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
//spring security
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//jpa validations
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
//lombok
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: User<br>
 * <b>Description</b>: User entity integrated with spring security.<br>
 * 
 * @author nishad.chayanakhawa
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TEH_USER", uniqueConstraints = {
		// username must be unique
		@UniqueConstraint(name = "TEH_USER_UNQ_USERNAME", columnNames = "username"),
		// email must be unique
		@UniqueConstraint(name = "TEH_USER_UNQ_EMAIL", columnNames = "email") })
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
	// id
	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Long id;

	// username
	@Getter(AccessLevel.NONE)
	@NotBlank(message = "username " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String username;

	// password
	@Getter(AccessLevel.NONE)
	private String password;

	// first name
	@NotBlank(message = "firstName " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String firstName;

	// last name
	@NotBlank(message = "lastName " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String lastName;

	// email
	@NotBlank(message = "email " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String email;

	// user roles
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "TEH_USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "ROLE")
	@Enumerated(EnumType.STRING)
	@NotEmpty(message = "roles " + TestEstimateHubConstants.NON_EMPTY_MESSAGE)
	private Set<Role> roles = new HashSet<>();

	/**
	 * <b>Method Name</b>: getAuthorities<br>
	 * <b>Description</b>: Return <br>
	 * 
	 * @return {@link java.util.Collection Collection} of
	 *         {@link org.springframework.security.core.GrantedAuthority
	 *         GrantedAuthority}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
	}

	/**
	 * <b>Method Name</b>: getPassword<br>
	 * <b>Description</b>: Get user password.<br>
	 * 
	 * @return password as {@link java.lang.String String}
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
	 * <b>Method Name</b>: getUsername<br>
	 * <b>Description</b>: Get username.<br>
	 * 
	 * @return username as {@link java.lang.String String}
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * <b>Method Name</b>: isAccountNonExpired<br>
	 * <b>Description</b>: flag to indicate non-expired account.<br>
	 * 
	 * @return defaulted to true
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * <b>Method Name</b>: isAccountNonLocked<br>
	 * <b>Description</b>: flag to indicate non-expired account.<br>
	 * 
	 * @return defaulted to true
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * <b>Method Name</b>: isCredentialsNonExpired<br>
	 * <b>Description</b>: Flag to indicate non-expired credentials<br>
	 * 
	 * @return defaulted to true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * <b>Method Name</b>: isEnabled<br>
	 * <b>Description</b>: Flag to indicate enabled account.<br>
	 * 
	 * @return defaulted to true
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}