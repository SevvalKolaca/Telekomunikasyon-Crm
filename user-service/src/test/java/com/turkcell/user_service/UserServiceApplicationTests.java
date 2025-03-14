package com.turkcell.user_service;

import com.turkcell.user_service.security.JwtTokenFilter;
import io.github.ergulberke.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceApplicationTests {

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private JwtTokenFilter jwtTokenFilter;

	@Test
	void contextLoads() {
		assertThat(jwtTokenProvider).isNotNull();
	}

}
