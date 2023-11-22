package io.github.nishadchayanakhawa.testestimatehub.tests.apitests;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.model.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.Role;
import io.github.nishadchayanakhawa.testestimatehub.model.User;

public class CoverageTests {

	@Test
	public void userClassTests() {
		User user = new User();
		user.setRoles(Set.of(Role.TESTER, Role.TEST_LEAD));
		Assertions.assertThat(user.isAccountNonExpired()).isTrue();
		Assertions.assertThat(user.isAccountNonLocked()).isTrue();
		Assertions.assertThat(user.isCredentialsNonExpired()).isTrue();
		Assertions.assertThat(user.isEnabled()).isTrue();
		Assertions.assertThat(user.getAuthorities()).hasSize(2);
	}

	@Test
	public void complexityEnumTests() {
		Assertions.assertThat(Complexity.LOW.getCode()).isEqualTo("LOW");
		Assertions.assertThat(Complexity.LOW.getDisplayValue()).isEqualTo("Low");
	}
}