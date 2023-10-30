package com.d103.dddev.api.ground.service;

import java.util.Map;
import java.util.Optional;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundService {
	GroundDto createGround(String groundName, UserDto userDto, RepositoryDto repositoryDto) throws Exception;
	Optional<GroundDto> getGroundByRepoId(Integer repoId) throws Exception;
	Optional<GroundDto> getGroundInfo(Integer groundId) throws Exception;
}