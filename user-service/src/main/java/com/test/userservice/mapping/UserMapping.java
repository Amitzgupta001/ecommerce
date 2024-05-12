package com.test.userservice.mapping;

import com.test.userservice.constant.CustomBeanUtil;
import com.test.userservice.dto.request.UpdateUserRequest;
import com.test.userservice.dto.request.UserRequest;
import com.test.userservice.dto.response.UserResponse;
import com.test.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.lang.reflect.InvocationTargetException;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapping {

    default User updateUser(User user, UpdateUserRequest userRequest) {
        try {
            CustomBeanUtil.copyPropertiesNotNull(user, userRequest);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    User requestToEntity(UserRequest userRequest);

    UserResponse entityToResponse(User user);

}
