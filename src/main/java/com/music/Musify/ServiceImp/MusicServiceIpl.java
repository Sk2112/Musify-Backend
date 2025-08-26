package com.music.Musify.ServiceImp;


import com.music.Musify.Entity.GeminiResponse;
import com.music.Musify.Entity.Music;
import com.music.Musify.Repository.MusicRepo;
import com.music.Musify.Service.CloudinaryService;
import com.music.Musify.Service.GeminiService;
import com.music.Musify.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MusicServiceIpl implements MusicService {

    @Autowired
    private final MusicRepo musicRepo;
//    private final Cloudinary cloudinary;
    private final GeminiService geminiService;
    private final CloudinaryService cloudinaryService;


    @Autowired
    public MusicServiceIpl(MusicRepo musicRep, GeminiService geminiService, CloudinaryService cloudinaryService) {
        this.musicRepo = musicRep;
        this.cloudinaryService = cloudinaryService;
        this.geminiService = geminiService;
    }

    @Override
    public Music uploadMusic(MultipartFile file, String title, String description) throws IOException {
        // Upload file to Cloudinary
        String cloudinaryUrl = cloudinaryService.uploadFile(file);

        String finalDescription = description;
        String artist = "";
        String fact = "";

        // If no description provided, use Gemini AI
        if (description == null || description.isBlank()) {
            GeminiResponse aiData = geminiService.fetchSongDetails(title);
            finalDescription = aiData.getDescription();
            artist = aiData.getArtist();
            fact = aiData.getFact();
        }

        Music music = Music.builder()
                .title(title)
                .description(finalDescription)
                .artist(artist)
                .fact(fact)
                .contentType(file.getContentType())
                .filepath(cloudinaryUrl)
                .imagePath("/assets/default.png") // default thumbnail
                .build();

        return musicRepo.save(music);
    }


    @Override
    public List<Music> getAllMusic() {
        return musicRepo.findAll();
    }
}
