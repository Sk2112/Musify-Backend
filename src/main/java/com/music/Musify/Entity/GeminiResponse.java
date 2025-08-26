package com.music.Musify.Entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeminiResponse {

    private String artist;
    private String releaseDate;
    private String description;
    private String fact;
}
