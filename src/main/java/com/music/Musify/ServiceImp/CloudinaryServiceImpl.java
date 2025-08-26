package com.music.Musify.ServiceImp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.music.Musify.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("File upload to Cloudinary failed", e);
        }
    }
}
