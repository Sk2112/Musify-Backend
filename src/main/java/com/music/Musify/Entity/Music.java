package com.music.Musify.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "musicTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Music {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "music_id")
  private Long musicId;

  private String title;
  private String description;
  private String artist;
  private String fact;
  private String contentType;
  private String filepath;
  private String imagePath;
}
