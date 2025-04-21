package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.util.Date;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private Date releaseDate;
    private Duration duration;
}
