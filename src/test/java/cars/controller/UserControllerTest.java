package cars.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import cars.util.ControllerTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.job4j.cars.Main;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class UserControllerTest {

    private final ControllerTestUtil testUtil = new ControllerTestUtil();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PostService postService;
    private MockHttpSession mockHttpSession;

    @BeforeEach
    void init() {
        mockHttpSession = new MockHttpSession();
    }

    @Test
    void whenGetRequestUsersRegisterThenReturnRegisterPage() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"));
    }

    @Test
    void whenGetRequestUsersLoginThenReturnLoginPage() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    @Test
    void whenGetRequestLogoutThenRedirectIndexPage() throws Exception {
        assertThat(mockHttpSession.isInvalid()).isFalse();
        this.mockMvc.perform(get("/users/logout").session(mockHttpSession))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        assertThat(mockHttpSession.isInvalid()).isTrue();
    }

    @Test
    void whenGetRequestUsersUpdateThenReturnUpdatePage() throws Exception {
        mockHttpSession.setAttribute("user", new User());

        this.mockMvc.perform(get("/users/update").session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update"));
    }

    @Test
    void whenGetRequestUsersPostsThenReturnPostsPageWithPosts() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());
        mockHttpSession.setAttribute("user", new User());

        when(postService.findAllByUserId(any(Integer.class))).thenReturn(List.of(post));

        this.mockMvc.perform(get("/users/posts").session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", List.of(post)))
                .andExpect(status().isOk())
                .andExpect(view().name("users/posts"));
    }

    @Test
    void whenPostRequestUsersRegisterThenSaveUserAndRedirectToLoginPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(Map.of("email", "testEmail", "password", "testPassword"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(any(User.class))).thenReturn(Optional.of(new User()));

        this.mockMvc.perform(post("/users/register").params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"));
        verify(userService).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("testEmail");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("testPassword");
    }

    @Test
    void whenPostRequestUsersRegisterWithWrongParamsThenGetErrorMessage() throws Exception {
        when(userService.save(any())).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/users/register"))
                .andDo(print())
                .andExpect(model().attribute("error", "Пользователь с таким email уже существует"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"));
    }

    @Test
    void whenPostRequestUsersLoginThenValidateUserAndRedirectIndexPage() throws Exception {
        User user = testUtil.makeUser();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(Map.of("email", user.getEmail(), "password", user.getPassword()));

        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        MvcResult mvcResult = mockMvc.perform(post("/users/login").params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andReturn();
        assertThat(mvcResult.getRequest().getSession().getAttribute("user")).isEqualTo(user);
    }

    @Test
    void whenPostRequestUsersLoginWithWrongParamsThenGetErrorMessage() throws Exception {
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/users/login"))
                .andDo(print())
                .andExpect(model().attribute("error", "Email или пароль введены не верно!"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    @Test
    void whenPostRequestUsersUpdateThenUpdateUserAndInvalidateAndRedirectLoginPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(Map.of("name", "testName", "phone", "testPhone"));
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        mockHttpSession.setAttribute("user", user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.update(any(User.class))).thenReturn(true);

        assertThat(mockHttpSession.isInvalid()).isFalse();
        this.mockMvc.perform(post("/users/update").params(params).session(mockHttpSession))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"));
        verify(userService).update(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(capturedUser.getName()).isEqualTo("testName");
        assertThat(capturedUser.getPhone()).isEqualTo("testPhone");
        assertThat(mockHttpSession.isInvalid()).isTrue();
    }

    @Test
    void whenPostRequestUsersUpdateWithWrongParamsThenGetErrorMessage() throws Exception {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        mockHttpSession.setAttribute("user", user);

        when(userService.update(any(User.class))).thenReturn(false);

        this.mockMvc.perform(post("/users/update").session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attribute("error", "Ошибка при обновлении данных пользователя."))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update"));
    }

    @Test
    void whenPostRequestUsersDeleteThenDeleteUserWithPostsAndInvalidateAndRedirectIndexPage()
            throws Exception {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        mockHttpSession.setAttribute("user", user);

        assertThat(mockHttpSession.isInvalid()).isFalse();
        this.mockMvc.perform(post("/users/delete").param("password", "password").session(mockHttpSession))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        assertThat(mockHttpSession.isInvalid()).isTrue();
        verify(postService).deleteAllByUser(user);
        verify(userService).deleteByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Test
    void whenPostRequestUsersDeleteWithWrongPasswordThenGetErrorMessage() throws Exception {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        mockHttpSession.setAttribute("user", user);

        this.mockMvc.perform(post("/users/delete").param("password", "pass").session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attribute("error",
                        "Введенный пароль не совпадает с паролем пользователя."))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update"));
    }

}