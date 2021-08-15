package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.AvatarImageRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.AvatarImage;
import com.Turbo.Lms.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class AvatarStorageService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarStorageService.class);

    private final AvatarImageRepository avatarImageRepository;

    private final UserRepository userRepository;

    @Value("${file.storage.path}")
    private String path;

    @Autowired
    public AvatarStorageService(AvatarImageRepository avatarImageRepository, UserRepository userRepository) {
        this.avatarImageRepository = avatarImageRepository;
        this.userRepository = userRepository;
    }

    public void save(String username, String contentType, InputStream is) {
        Optional<AvatarImage> opt = avatarImageRepository.findAvatarImageByUser_Username(username);
        AvatarImage avatarImage;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(IllegalArgumentException::new);
            avatarImage = new AvatarImage(null, contentType, filename, user);
        } else {
            avatarImage = opt.get();
            filename = avatarImage.getFilename();
            avatarImage.setContentType(contentType);
        }
        avatarImageRepository.save(avatarImage);

        try (OutputStream os = Files.newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    public Optional<String> getContentTypeByUser(String username) {
        return avatarImageRepository.findAvatarImageByUser_Username(username)
                .map(AvatarImage::getContentType);
    }

    public Optional<byte[]> getAvatarImageByUser(String username) {
        return avatarImageRepository.findAvatarImageByUser_Username(username)
                .map(AvatarImage::getFilename)
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
