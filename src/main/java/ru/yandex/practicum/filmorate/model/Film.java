package ru.yandex.practicum.filmorate.model;

import lombok.Data;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private String releaseDate;
    private Long duration;
}
