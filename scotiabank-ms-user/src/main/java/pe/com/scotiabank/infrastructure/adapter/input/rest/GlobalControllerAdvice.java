package pe.com.scotiabank.infrastructure.adapter.input.rest;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.com.scotiabank.domain.exception.DuplicateUserException;
import pe.com.scotiabank.domain.exception.InvalidCredentialException;
import pe.com.scotiabank.domain.exception.NotFoundException;
import pe.com.scotiabank.domain.exception.UserNotFoundException;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.ErrorResponse;
import pe.com.scotiabank.utils.Constants;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static pe.com.scotiabank.utils.ErrorCatalog.GENERIC_ERROR;
import static pe.com.scotiabank.utils.ErrorCatalog.INVALID_USER;
import static pe.com.scotiabank.utils.ErrorCatalog.NOT_FOUND;
import static pe.com.scotiabank.utils.ErrorCatalog.USER_DUPLICATE;
import static pe.com.scotiabank.utils.ErrorCatalog.USER_NOT_FOUND;

@RestControllerAdvice
public class GlobalControllerAdvice  {

    @ExceptionHandler(DuplicateRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(DuplicateRequestException ex) {
        return ErrorResponse.builder()
                .code(USER_DUPLICATE.getCode())
                .message(USER_DUPLICATE.getTitle())
                .details(Collections.singletonList(ex.getMessage()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException() {
        return ErrorResponse.builder()
                .code(NOT_FOUND.getCode())
                .message(NOT_FOUND.getTitle())
                .details(List.of(NOT_FOUND.getDescription()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException() {
        return ErrorResponse.builder()
                .code(USER_NOT_FOUND.getCode())
                .message(USER_NOT_FOUND.getTitle())
                .details(List.of(USER_NOT_FOUND.getDescription()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidCredentialException.class)
    public ErrorResponse handleInvalidCredentialException() {
        return ErrorResponse.builder()
                .code(INVALID_USER.getCode())
                .message(INVALID_USER.getTitle())
                .details(List.of(INVALID_USER.getDescription()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateUserException(DuplicateUserException ex) {
        return ErrorResponse.builder()
                .code(USER_DUPLICATE.getCode())
                .message(USER_DUPLICATE.getTitle())
                .details(Collections.singletonList(ex.getMessage()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();

        return ErrorResponse.builder()
                .code(INVALID_USER.getCode())
                .message(INVALID_USER.getTitle())
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericError(Exception exception) {
        return ErrorResponse.builder()
                .code(GENERIC_ERROR.getCode())
                .message(GENERIC_ERROR.getTitle())
                .details(Collections.singletonList(exception.getMessage()))
                .timestamp(Constants.convertLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }

}