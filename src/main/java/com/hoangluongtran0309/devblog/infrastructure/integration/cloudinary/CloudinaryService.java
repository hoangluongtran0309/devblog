package com.hoangluongtran0309.devblog.infrastructure.integration.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hoangluongtran0309.devblog.media.MediaServicePort;

@Service
public class CloudinaryService implements MediaServicePort {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, Object> upload(MultipartFile file, String folder) {
        try {
            var result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "use_filename", true,
                            "unique_filename", true,
                            "resource_type", "auto"));

            return Map.of(
                    "publicId", result.get("public_id"),
                    "url", result.get("secure_url"),
                    "resourceType", result.get("resource_type"),
                    "bytes", result.get("bytes"),
                    "originalFilename", result.get("original_filename"));
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary delete failed", e);
        }
    }
}
