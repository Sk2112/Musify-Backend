package com.music.Musify.Repository;

import com.music.Musify.Entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepo  extends JpaRepository<Music,Long> {




}
