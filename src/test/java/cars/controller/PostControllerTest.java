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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.job4j.cars.Main;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class PostControllerTest {

    private final ControllerTestUtil testUtil = new ControllerTestUtil();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;
    @MockBean
    private BodyService bodyService;
    @MockBean
    private BrandService brandService;
    @MockBean
    private CarModelService carModelService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private ColorService colorService;
    @MockBean
    private EngineSizeService engineSizeService;
    @MockBean
    private FuelTypeService fuelTypeService;
    @MockBean
    private TransmissionService transmissionService;
    private MockHttpSession mockHttpSession;
    private MockMultipartFile testFile;

    @BeforeEach
    void init() {
        mockHttpSession = new MockHttpSession();
        testFile = new MockMultipartFile("file", "file", MediaType.IMAGE_JPEG_VALUE, new byte[] {1});
    }

    @Test
    void whenGetRequestPostsAllThenGetAllNotSoldPostsPage() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());

        mockCarAttributesGathering(post);
        when(postService.findAllNotSold()).thenReturn(List.of(post));

        this.mockMvc.perform(get("/posts/all"))
                .andDo(print())
                .andExpect(model().attributeExists("brandsModels"))
                .andExpect(model().attributeExists("bodies"))
                .andExpect(model().attributeExists("transmissions"))
                .andExpect(model().attributeExists("fuelTypes"))
                .andExpect(model().attributeExists("engineSizes"))
                .andExpect(model().attributeExists("colors"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", List.of(post)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"));
        verify(postService).findAllNotSold();
    }

    @Test
    void whenGetRequestPostsAllWithSearchDtoThenGetPostsByCriteriaPage() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());

        ArgumentCaptor<PostSearchDto> searchCaptor = ArgumentCaptor.forClass(PostSearchDto.class);
        mockCarAttributesGathering(post);
        when(postService.findAllByCriteria(any(PostSearchDto.class))).thenReturn(List.of(post));

        this.mockMvc.perform(get("/posts/all").param("imageExists", "true"))
                .andDo(print())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", List.of(post)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"));
        verify(postService).findAllByCriteria(searchCaptor.capture());
        assertThat(searchCaptor.getValue().isImageExists()).isTrue();
        assertThat(searchCaptor.getValue().getCar()).isNull();
    }

    @Test
    @WithMockUser
    void whenGetRequestPostsCreateThenGetCreatePage() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());

        mockHttpSession.setAttribute("user", testUtil.makeUser());
        mockCarAttributesGathering(post);

        this.mockMvc.perform(get("/posts/create").session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attributeExists("brandsModels"))
                .andExpect(model().attributeExists("bodies"))
                .andExpect(model().attributeExists("transmissions"))
                .andExpect(model().attributeExists("fuelTypes"))
                .andExpect(model().attributeExists("engineSizes"))
                .andExpect(model().attributeExists("colors"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/create"));
    }

    @Test
    void whenGetRequestPostsIdThenGetPostPage() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());

        when(postService.findById(any(Integer.class))).thenReturn(Optional.of(post));

        this.mockMvc.perform(get("/posts/1"))
                .andDo(print())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/one"));
    }

    @Test
    void whenGetRequestPostsWrongIdThenGetErrorPage() throws Exception {
        when(postService.findById(any(Integer.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/posts/1"))
                .andDo(print())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Объявление не найдено."))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    void whenGetRequestPostsEditIdThenGetPostEditPage() throws Exception {
        Post post = testUtil.makePost(testUtil.makeUser(), testUtil.makeCar());

        when(postService.findById(any(Integer.class))).thenReturn(Optional.of(post));
        mockCarAttributesGathering(post);

        this.mockMvc.perform(get("/posts/edit/1"))
                .andDo(print())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/edit"));
    }

    @Test
    void whenGetRequestPostsEditWrongIdThenGetErrorPage() throws Exception {
        when(postService.findById(any(Integer.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/posts/edit/1"))
                .andDo(print())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Объявление не найдено."))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    void whenPostRequestPostsCreateThenSavePostAndRedirectAllPostsPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(Map.of("description", "description", "price", "100"));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<ImageDto> imageCaptor = ArgumentCaptor.forClass(ImageDto.class);
        mockHttpSession.setAttribute("user", testUtil.makeUser());
        when(postService.save(any(), any())).thenReturn(Optional.of(new Post()));

        this.mockMvc.perform(multipart("/posts/create")
                        .file(testFile)
                        .params(params)
                        .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/posts/all"));
        verify(postService).save(postCaptor.capture(), imageCaptor.capture());
        assertThat(postCaptor.getValue().getDescription()).isEqualTo("description");
        assertThat(postCaptor.getValue().getPrice()).isEqualTo(100L);
        assertThat(imageCaptor.getValue().getName()).isEqualTo("file");
        assertThat(imageCaptor.getValue().getContent()).isEqualTo(new byte[] {1});
    }

    @Test
    void whenPostRequestPostsCreateWithWrongParamsThenGetErrorPage() throws Exception {
        mockHttpSession.setAttribute("user", testUtil.makeUser());
        when(postService.save(any(), any())).thenReturn(Optional.empty());

        this.mockMvc.perform(multipart("/posts/create").file(testFile).session(mockHttpSession))
                .andDo(print())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Произошла ошибка при создании объявления."))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    void whenPostRequestPostsEditThenUpdatePostAndRedirectUsersPostsPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(Map.of("description", "description", "price", "100"));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<ImageDto> imageCaptor = ArgumentCaptor.forClass(ImageDto.class);
        when(postService.update(any(), any())).thenReturn(true);

        this.mockMvc.perform(multipart("/posts/edit")
                        .file(testFile)
                        .params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/posts"));
        verify(postService).update(postCaptor.capture(), imageCaptor.capture());
        assertThat(postCaptor.getValue().getDescription()).isEqualTo("description");
        assertThat(postCaptor.getValue().getPrice()).isEqualTo(100L);
        assertThat(imageCaptor.getValue().getName()).isEqualTo("file");
        assertThat(imageCaptor.getValue().getContent()).isEqualTo(new byte[] {1});
    }

    @Test
    void whenPostRequestPostsEditWithWrongParamsThenGetErrorPage() throws Exception {
        when(postService.update(any(), any())).thenReturn(false);

        this.mockMvc.perform(multipart("/posts/edit").file(testFile))
                .andDo(print())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Произошла ошибка при обновлении объявления."))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    private void mockCarAttributesGathering(Post post) {
        when(carModelService.findAll()).thenReturn(List.of(post.getCar().getCarModel()));
        when(brandService.findAll()).thenReturn(List.of(post.getCar().getBrand()));
        when(bodyService.findAll()).thenReturn(List.of(post.getCar().getBody()));
        when(transmissionService.findAll()).thenReturn(List.of(post.getCar().getTransmission()));
        when(fuelTypeService.findAll()).thenReturn(List.of(post.getCar().getEngine().getFuelType()));
        when(engineSizeService.findAll()).thenReturn(List.of(post.getCar().getEngine().getEngineSize()));
        when(colorService.findAll()).thenReturn(List.of(post.getCar().getColor()));
        when(categoryService.findAll()).thenReturn(List.of(post.getCar().getCategory()));
    }

}