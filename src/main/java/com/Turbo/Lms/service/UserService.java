package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.PasswordResetTokenRepository;
import com.Turbo.Lms.dao.RoleRepositorty;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.PasswordResetToken;
import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService {
    private static final int EXPIRATION_RESET_TOKEN = 2; //hours

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepositorty roleRepositorty;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordTokenRepository;


    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       RoleRepositorty roleRepositorty,
                       PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepositorty = roleRepositorty;
        this.passwordEncoder = passwordEncoder;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    public List<UserDto> findAll() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }

    public void delete(UserDto userDto) {
        userRepository.delete(userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("User not found!")));
    }

    public void save(UserDto userDto) {
        userRepository.save(userMapper.toUserFromDto(userDto));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public boolean isEnrolled(Long userId, Long courseId) {
        Optional<User> user = userRepository.findIfUserIsEnrolledOnCourse(userId, courseId);
        return user.isPresent();
    }


    public List<UserDto> findUsersNotAssignedToCourse(Long courseId) {
        return userMapper.convertToDtoList(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return userMapper.convertToDtoList(userRepository.findByUsernameLike(username));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return userMapper.convertToDtoList(new ArrayList<>(userRepository.getUsersOfCourse(id)));
    }

    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(userMapper::toUserDto).orElse(null);
    }

    public void registerUser(UserDto userDto) {
        Set<Role> roles = Set.of(roleRepositorty.findRoleByName(RoleType.STUDENT).get());
        userDto.setRoles(roles);
        save(userDto);
    }

    public UserDto findUserByUsername(String remoteUser) {
        Optional<User> user = userRepository.findUserByUsername(remoteUser);
        return user.map(userMapper::toUserDto).orElse(null);
    }

    public void createPasswordResetTokenForUser(UserDto userDto, String token) {
        /* Если для пользователя уже есть токен для восстановления,
         то удаляем старый и помещаем на его место новый
         */
        if (passwordTokenRepository.findByUser_Id(userDto.getId()) != null){
            passwordTokenRepository.deleteByUser_Id(userDto.getId());
        }
        User user = userRepository.findById(userDto.getId()).get();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, EXPIRATION_RESET_TOKEN);
        Date expirationDate = cal.getTime();
        PasswordResetToken myToken = new PasswordResetToken(token,user,expirationDate);
        passwordTokenRepository.save(myToken);
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public void changeUserPassword(Long userId, String password) {
        User user = userRepository.findById(userId).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public UserDto findUserByPasswordResetToken(String token) {
        Optional<User> user = userRepository.findById(passwordTokenRepository.findByToken(token).getUser().getId());
        return user.map(userMapper::toUserDto).orElse(null);
    }
}
