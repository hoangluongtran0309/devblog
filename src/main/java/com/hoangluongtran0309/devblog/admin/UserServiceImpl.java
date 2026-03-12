package com.hoangluongtran0309.devblog.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean exists(Email email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUser(UserId userId) {
        logger.info("Retrieving user with ID: {}", userId.asString());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return user;
    }

    @Override
    public User createAdministrator(CreateUserParameters parameters) {
        logger.info("Creating administrator with email: {}", parameters.getEmail().asString());

        User user = User.createAdministrator(
                userRepository.nextId(),
                parameters.getUserName(),
                parameters.getEmail(),
                passwordEncoder.encode(parameters.getPassword()));

        logger.info("Administrator created with ID: {}", user.getId().asString());

        return userRepository.save(user);
    }

    @Override
    public void changePassword(UserId userId, ChangePasswordParameters parameters) {
        logger.info("Changing password for user ID: {}", userId.asString());

        User user = getUser(userId);

        if (!passwordEncoder.matches(parameters.getCurrentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        if (!parameters.getNewPassword().equals(parameters.getConfirmPassword())) {
            throw PasswordNotMatchException.confirmPasswordNotMatch();
        }

        user.changePassword(passwordEncoder.encode(parameters.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed successfully for user ID: {}", userId.asString());
    }
}
