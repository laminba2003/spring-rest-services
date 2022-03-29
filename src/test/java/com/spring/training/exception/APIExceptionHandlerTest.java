package com.spring.training.exception;

import com.spring.training.controller.PersonController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class APIExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void testHandleRequestException() throws Exception {
        RequestException exception = new RequestException("error message",
                HttpStatus.INTERNAL_SERVER_ERROR);
        when(personController.getPerson(any()))
                .thenThrow(exception);
        mockMvc.perform(get("/persons/{id}", 1L))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException("error message");
        when(personController.getPerson(any()))
                .thenThrow(exception);
        mockMvc.perform(get("/persons/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleNumberFormatException() throws Exception {
        NumberFormatException exception = new NumberFormatException();
        when(personController.getPerson(any()))
                .thenThrow(exception);
        mockMvc.perform(get("/persons/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(mock(MethodParameter.class), mock(BindingResult.class)) {
                    public String getMessage() {
                        return "error message";
                    }
                };
        given(personController.getPerson(any()))
                .willAnswer(invocationOnMock -> {
                    throw exception;
                });
        mockMvc.perform(get("/persons/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

}
