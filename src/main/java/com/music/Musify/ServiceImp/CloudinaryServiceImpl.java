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


    @Override
    public void deleteFile(String fileUrl) {
        try {
            String[] parts = fileUrl.split("/");

            // Find "upload" index
            int uploadIndex = -1;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("upload")) {
                    uploadIndex = i;
                    break;
                }
            }
            if (uploadIndex == -1 || uploadIndex + 1 >= parts.length) {
                throw new RuntimeException("Invalid Cloudinary URL");
            }


            String fileNameWithExt = parts[parts.length - 1];
            String publicId = String.join("/", java.util.Arrays.copyOfRange(parts, uploadIndex + 1, parts.length));
            publicId = publicId.substring(0, publicId.lastIndexOf(".")); // remove extension

            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "auto"));
            System.out.println("Deleted Cloudinary file: " + publicId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }


}
