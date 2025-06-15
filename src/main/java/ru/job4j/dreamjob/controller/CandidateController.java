package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.SimpleCandidateService;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final SimpleCandidateService simpleCandidateService =
            SimpleCandidateService.getInstance();

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", simpleCandidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "candidates/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate) {
        simpleCandidateService.save(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var candidateOptional = simpleCandidateService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message",
                    "Кандидат с указанным Id не найден");
            return "errors/404";
        }
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, Model model) {
        var isUpdate = simpleCandidateService.update(candidate);
        if (!isUpdate) {
            model.addAttribute("message",
                    "Кандидат с указанным Id не найден");
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = simpleCandidateService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message",
                    "Кандидат с указанным Id не найден");
        }
        return "redirect:/candidates";
    }

}
