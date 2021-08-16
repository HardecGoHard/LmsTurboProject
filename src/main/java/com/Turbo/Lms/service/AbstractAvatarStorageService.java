package com.Turbo.Lms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public abstract class AbstractAvatarStorageService<A, E> {
    private static final Logger logger = LoggerFactory.getLogger(UserAvatarStorageService.class);

    protected abstract Optional<A> findAvatarImageInRepository(Long id);

    protected abstract E findEntityInRepository(Long id);

    protected abstract A createNewAvatarImage(String contentType, String filename, E entity);

    protected abstract void saveAvatarImageToRepository(A avatarImage);

    protected abstract Optional<String> getContentTypeMap(Long id);

    protected abstract String getPath();

    protected abstract String getFilename(A avatarImage);

    protected abstract void setContentType(A avatarImage, String contentType);

    protected abstract Optional<byte[]> getAvatarImageMap(Long id);

    public void save(Long id, String contentType, InputStream is) {
        Optional<A> opt = findAvatarImageInRepository(id);
        A avatarImage;
        String filename ;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            E entity = findEntityInRepository(id);
            avatarImage = createNewAvatarImage( contentType, filename, entity);
        } else {
            avatarImage = opt.get();
            filename = getFilename(avatarImage);
            setContentType(avatarImage,contentType);
        }

        saveAvatarImageToRepository(avatarImage);

        try (OutputStream os = Files.newOutputStream(Path.of(getPath(), filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    protected byte[] readFileImage(String filename){
        try {
            return Files.readAllBytes(Path.of(getPath(), filename));
        } catch (IOException ex) {
            logger.error("Can't read file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    public Optional<String> getContentTypeByEntity(Long id) {
        return getContentTypeMap(id);
    }

    public Optional<byte[]> getAvatarImageByEntity(Long id) {
        return getAvatarImageMap(id);
    }
}
