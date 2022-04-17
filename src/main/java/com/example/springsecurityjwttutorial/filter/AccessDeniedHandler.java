package com.example.springsecurityjwttutorial.filter;

import com.example.springsecurityjwttutorial.dto.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        AuthenticationResponse apiResponse = new AuthenticationResponse(false, null, null, "Access Denied");
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, apiResponse);
        outputStream.flush();

    }

}
