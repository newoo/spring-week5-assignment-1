package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("UserController의")
class UserControllerTest {
    private final Long givenSavedId = 1L;
    private final Long givenUnsavedId = 100L;
    private final String givenName = "newoo";
    private final String givenEmail = "newoo@codesoom.com";
    private final String givenPassword = "codesoom123";

    private final String givenChangedName = "newoo2";
    private final String givenChangedEmail = "newoo2@codesoom.com";
    private final String givenChangedPassword = "codesoom789";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    private RequestBuilder requestBuilder;

    private User user;
    private UserData userData;

    private OutputStream outputStream;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String userJsonString;

    @BeforeEach
    void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userData = UserData.builder()
                .id(givenSavedId)
                .name(givenName)
                .email(givenEmail)
                .password(givenPassword)
                .build();

        user = mapper.map(userData, User.class);

        setUserJsonString(user);
    }

    private void setUserJsonString(Object value) throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, value);
        userJsonString = outputStream.toString();
    }

    @Nested
    @DisplayName("POST /users 요청에 대한 응답은")
    class Describe_response_of_post_users_request {
        @BeforeEach
        void setRequest() {
            requestBuilder = post("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJsonString);
        }

        @Test
        @DisplayName("201 Created와 생성된 유저정보를 가지고 있다.")
        void it_has_201_created_and_created_user() throws Exception {
            given(userService.createUser(any(UserData.class))).willReturn(user);

            mockMvc.perform(requestBuilder)
                    .andExpect(status().isCreated())
                    .andExpect(content().json(userJsonString));
        }
    }
}