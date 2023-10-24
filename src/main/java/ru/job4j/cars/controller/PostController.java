package ru.job4j.cars.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.*;

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

    @GetMapping("/all")
    public String getAllPosts(Model model, @ModelAttribute PostSearchDto searchDto) {
        addFormAttributesToModel(model);
        boolean searchActive = searchDto.getCar() != null
                || searchDto.getHighestPrice() > 0
                || searchDto.getLowestPrice() > 0
                || searchDto.getPostCreatedBeforeDays() > 0
                || searchDto.isImageExists();
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
            model.addAttribute("message", "Объявление с указанным id не найдено.");
            return "errors/404";
        }
        model.addAttribute("post", postOptional.get());
        return "posts/one";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, @RequestParam MultipartFile file, Model model) {
        try {
            postService.save(post, new ImageDto(file.getOriginalFilename(), file.getBytes()));
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка при создании объявления!");
            return "errors/404";
        }
        return "redirect:/posts/all";
    }

    private Model addFormAttributesToModel(Model model) {
        Map<Brand, List<CarModel>> brandsModels = new LinkedHashMap<>();
        brandService.findAll().forEach(brand -> brandsModels.put(
                brand, carModelService.findAllByBrandId(brand.getId()).stream().toList()));
        model.addAttribute("brandsModels", brandsModels);
        model.addAttribute("bodies", bodyService.findAll());
        model.addAttribute("transmissions", transmissionService.findAll());
        model.addAttribute("fuelTypes", fuelTypeService.findAll());
        model.addAttribute("engineSizes", engineSizeService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return model;
    }

}