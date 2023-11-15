package cars.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.job4j.cars.Main;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.service.ImageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageService imageService;

    @Test
    void whenGetRequestImageIdThenResponseOkWithByteArray() throws Exception {
        ImageDto imageDto = new ImageDto("name", new byte[1]);

        when(imageService.getImageDtoById(any(Integer.class))).thenReturn(imageDto);

        MvcResult mvcResult = mockMvc.perform(get("/images/1"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsByteArray()).isEqualTo(new byte[1]);
    }

    @Test
    void whenGetRequestDefaultImageThenResponseOkWithByteArray() throws Exception {
        ImageDto imageDto = new ImageDto("name", new byte[1]);

        when(imageService.getDefaultImageDto()).thenReturn(imageDto);

        MvcResult mvcResult = mockMvc.perform(get("/images/defaultImage"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsByteArray()).isEqualTo(new byte[1]);
    }

}