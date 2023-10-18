package com.d103.dddev.api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.user.repository.dto.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Integer> {
	Optional<UserDto> findBygithubId(Integer githubId);
	Optional<UserDto> findByRefreshToken(String refreshToken);
	Optional<UserDto> findByIdNotAndNickname(int id, String nickname);	// id != not and nickname = nickname

}
