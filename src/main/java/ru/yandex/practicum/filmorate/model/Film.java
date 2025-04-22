package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    private String name;
    private String description;
    private String releaseDate;
    private Long duration;
}
