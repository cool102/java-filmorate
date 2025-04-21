package ru.yandex.practicum.filmorate;

import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.testng.Assert.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Максимальная длина описания фильма — 200 символов")
    public void testFilmWithTooLongDescriptionShouldFail() {
        FilmController controller = new FilmController();
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("1".repeat(201));
        film.setReleaseDate(String.valueOf(LocalDate.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        film.setDuration(Duration.ofMinutes(120));
        controller.create(film);
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Дата рождения не может быть в будущем")
    public void testUserWithFutureBirthdateShouldFail() {
        UserController controller = new UserController();
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setBirthdate(String.valueOf(LocalDate.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        controller.create(user);
    }

    @org.testng.annotations.Test
    public void testUserWithEmptyNameDefaultsToLogin() {
        UserController controller = new UserController();
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login123");
        user.setName("");
        user.setBirthdate(String.valueOf(LocalDate.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        User created = controller.create(user);
        assertEquals(created.getName(), "login123");
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public void testFilmReleaseDateTooEarlyShouldFail() {
        FilmController controller = new FilmController();
        Film film = new Film();
        film.setName("Old Film");
        film.setDescription("Desc");
        film.setReleaseDate(String.valueOf(LocalDate.from(LocalDate.of(1800, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        film.setDuration(Duration.ofMinutes(100));
        controller.create(film);
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Продолжительность фильма должна быть положительным числом")
    public void testFilmWithNegativeDurationShouldFail() {
        FilmController controller = new FilmController();
        Film film = new Film();
        film.setName("Test");
        film.setDescription("Valid");
        film.setReleaseDate(String.valueOf(LocalDate.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        film.setDuration(Duration.ofMinutes(-90));
        controller.create(film);
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Электронная почта не может быть пустой и должна содержать символ @")
    public void testUserWithInvalidEmailShouldFail() {
        UserController controller = new UserController();
        User user = new User();
        user.setEmail("invalidemail.com");
        user.setLogin("login");
        user.setBirthdate(String.valueOf(LocalDate.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        controller.create(user);
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "Логин не может быть пустым или содержать пробелы")
    public void testUserWithLoginContainingSpacesShouldFail() {
        UserController controller = new UserController();
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("invalid login");
        user.setBirthdate(String.valueOf(LocalDate.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        controller.create(user);
    }

    @org.testng.annotations.Test(expectedExceptions = ValidationException.class)
    public void testEmptyFilmRequestShouldFail() {
        FilmController controller = new FilmController();
        controller.create(new Film());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyUserRequestShouldFail() {
        UserController controller = new UserController();
        controller.create(new User());
    }
}
