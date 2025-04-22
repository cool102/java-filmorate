package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film updatedFilm) {
        if (updatedFilm.getId() == null || !films.containsKey(updatedFilm.getId())) {
            log.warn("Обновление фильма не удалось: некорректный id {}", updatedFilm.getId());
            throw new ValidationException("Фильм с id: " + updatedFilm.getId() + " не найден.");
        }
        validateFilm(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Обновлён фильм: {}", updatedFilm);
        return updatedFilm;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка валидации: пустое название фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Ошибка валидации: описание превышает 200 символов");
            throw new ValidationException("Максимальная длина описания фильма — 200 символов");
        }
        LocalDate parsed = getParsedReleased(film.getReleaseDate());
        if (parsed.isBefore(EARLIEST_RELEASE_DATE)) {
            log.warn("Ошибка валидации: дата релиза до 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        Long duration = film.getDuration();
        Duration durationParsed = Duration.ofMinutes(duration);
        if (durationParsed == null || durationParsed.isNegative() || durationParsed.isZero()) {
            log.warn("Ошибка валидации: отрицательная или нулевая продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private LocalDate getParsedReleased(String releaseDate) {
        return LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private long getNextId() {
        return films.keySet().stream()

                .mapToLong(Long::longValue)

                .max()

                .orElse(0L) + 1;
    }
}