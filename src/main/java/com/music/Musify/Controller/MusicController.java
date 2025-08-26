package com.music.Musify.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.Musify.Entity.Music;
import com.music.Musify.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/music")
@RestController
@CrossOrigin("*")
public class MusicController {

    private final MusicService musicService;


    public MusicController(MusicService musicService, ObjectMapper objectMapper) {
        this.musicService = musicService;
    }

    // Save Music
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMusic(
            @RequestPart("title") String title,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            Music savedMusic = musicService.uploadMusic(file, title, null);
            System.out.println("Saved in Database Successfully");
            return ResponseEntity.ok(savedMusic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Music upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Music>> getAllMusic() {
        return ResponseEntity.ok(musicService.getAllMusic());
    }
}
