package ru.job4j.cars.controller;

import java.io.IOException;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.*;
import ru.job4j.cars.utilities.SearchValidator;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final BodyService bodyService;
    private final BrandService brandService;
    private final CarModelService carModelService;
    private final CategoryService categoryService;
    private final ColorService colorService;
    private final EngineSizeService engineSizeService;
    private final FuelTypeService fuelTypeService;
    private final TransmissionService transmissionService;

    private final SearchValidator searchValidator;

    @GetMapping("/all")
    public String getAllPosts(Model model, @ModelAttribute PostSearchDto searchDto, BindingResult br) {
        addFormAttributesToModel(model);
        searchValidator.validate(searchDto, br);
        boolean searchActive = searchIsActiveCheck(br);
        if (!searchActive) {
            model.addAttribute("posts", postService.findAllNotSold());
        } else {
            model.addAttribute("posts", postService.findAllByCriteria(searchDto));
        }
        return "posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        addFormAttributesToModel(model);
        return "posts/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Объявление не найдено.");
            return "errors/404";
        }
        model.addAttribute("post", postOptional.get());
        return "posts/one";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(Model model, @PathVariable int id) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Объявление не найдено.");
            return "errors/404";
        }
        addFormAttributesToModel(model);
        model.addAttribute("post", postOptional.get());
        return "posts/edit";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, @RequestParam MultipartFile file, Model model)
            throws IOException {
        Optional<Post> savedPost = postService.save(post,
                new ImageDto(file.getOriginalFilename(), file.getBytes()));
        if (savedPost.isEmpty()) {
            model.addAttribute("message", "Произошла ошибка при создании объявления.");
            return "errors/404";
        }
        return "redirect:/posts/all";
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute Post post, @RequestParam MultipartFile file, Model model)
            throws IOException {
        boolean isUpdated = postService.update(post,
                new ImageDto(file.getOriginalFilename(), file.getBytes()));
        if (!isUpdated) {
            model.addAttribute("message", "Произошла ошибка при обновлении объявления.");
            return "errors/404";
        }
        return "redirect:/users/posts";
    }

    private Model addFormAttributesToModel(Model model) {
        Collection<CarModel> carModels = carModelService.findAll();
        Map<Brand, List<CarModel>> brandsModels = new LinkedHashMap<>();
        brandService.findAll().forEach(brand ->
                brandsModels.put(
                        brand,
                        carModels.stream().filter(cm -> cm.getBrandId() == brand.getId()).toList()));
        model.addAttribute("brandsModels", brandsModels);
        model.addAttribute("bodies", bodyService.findAll());
        model.addAttribute("transmissions", transmissionService.findAll());
        model.addAttribute("fuelTypes", fuelTypeService.findAll());
        model.addAttribute("engineSizes", engineSizeService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return model;
    }

    private boolean searchIsActiveCheck(BindingResult bindingResult) {
        return bindingResult.getErrorCount() < PostSearchDto.class.getDeclaredFields().length;
    }

}