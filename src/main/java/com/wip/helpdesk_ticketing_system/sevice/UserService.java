package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.User;
import java.util.List;

public interface UserService {
    User addUser(UserDto dto);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
}
