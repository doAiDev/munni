package com.munni.controller;

import com.munni.model.Product;
import com.munni.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", Product.Category.values());
        return "admin/upload";
    }

    @PostMapping("/upload")
    public String upload(@ModelAttribute Product product,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         RedirectAttributes ra) {
        try {
            productService.save(product, imageFile);
            ra.addFlashAttribute("msg", "상품이 등록되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "등록 실패: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        productService.delete(id);
        ra.addFlashAttribute("msg", "삭제되었습니다.");
        return "redirect:/admin/dashboard";
    }
}
