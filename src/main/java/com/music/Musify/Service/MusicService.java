package com.music.Musify.Service;

import com.music.Musify.Entity.Music;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MusicService {

// save the music

     Music uploadMusic(MultipartFile file, String title, String description) throws IOException;


     // get all the music files
     public void deleteMusic(Long musicId);

     List<Music> getAllMusic();
}

