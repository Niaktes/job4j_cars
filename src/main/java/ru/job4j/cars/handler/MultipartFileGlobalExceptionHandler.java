package ru.job4j.cars.handler;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MultipartFileGlobalExceptionHandler {

    @ExceptionHandler (value = {IOException.class})
    public String ioExceptionHandle(Exception e, Model model) {
        log.error(e.getMessage(), e);
        model.addAttribute("message", "Ошибка при работе с объявлением!");
        return "errors/404";
    }

}