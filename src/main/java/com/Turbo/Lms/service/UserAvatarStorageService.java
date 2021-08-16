package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.UserAvatarImageRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.UserAvatarImage;
import com.Turbo.Lms.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class UserAvatarStorageService {

    private static final Logger logger = LoggerFactory.getLogger(UserAvatarStorageService.class);

    private final UserAvatarImageRepository userAvatarImageRepository;

    private final UserRepository userRepository;

    @Value("${user.avatar.storage.path}")
    private String path;

    @Autowired
    public UserAvatarStorageService(UserAvatarImageRepository userAvatarImageRepository, UserRepository userRepository) {
        this.userAvatarImageRepository = userAvatarImageRepository;
        this.userRepository = userRepository;
    }

    public void save(Long userId, String contentType, InputStream is) {
        Optional<UserAvatarImage> opt = userAvatarImageRepository.findAvatarImageByUser_Id(userId);
        UserAvatarImage userAvatarImage;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            User user = userRepository.findById(userId)
                    .orElseThrow(IllegalArgumentException::new);
            userAvatarImage = new UserAvatarImage(null, contentType, filename, user);
        } else {
            userAvatarImage = opt.get();
            filename = userAvatarImage.getFilename();
            userAvatarImage.setContentType(contentType);
        }
        userAvatarImageRepository.save(userAvatarImage);

        try (OutputStream os = Files.newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    public Optional<String> getContentTypeByUser(Long userId) {
        return userAvatarImageRepository.findAvatarImageByUser_Id(userId)
                .map(UserAvatarImage::getContentType);
    }

    public Optional<byte[]> getAvatarImageByUser(Long userId) {
        return userAvatarImageRepository.findAvatarImageByUser_Id(userId)
                .map(UserAvatarImage::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }
}
