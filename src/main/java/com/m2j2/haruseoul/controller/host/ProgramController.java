package com.m2j2.haruseoul.controller.host;

import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.entity.ProgramView;
import com.m2j2.haruseoul.service.CategoryService;
import com.m2j2.haruseoul.service.ProgramService.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller("hostProgramController")
@RequestMapping("host/program")
public class ProgramController {;

    @GetMapping("create")
    public String create() {
        return "host/program/create";
    }


    @Autowired
    private ProgramService programService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public String list(
//            @RequestParam(value = "hostId",required = true) Long hostId,
//            @RequestParam(value = "category", required = false) List<String> selectedCategories,
//            @RequestParam(value = "pg-id", required = false) List<Long> selectedIds,
//            @RequestParam(value = "status", required = false) List<String> selectedStatuses,
//            Model model
    ) {
//        List<ProgramView> programs= programService.getList(hostId, selectedCategories, selectedIds, selectedStatuses);
//        List<ProgramView> filteredPrograms = programService.getList(hostId,
//                                                            selectedCategories,
//                                                            selectedIds,
//                                                            selectedStatuses);
//
//        System.out.println(selectedIds);
//        System.out.println(selectedCategories);
//        System.out.println(selectedStatuses);
//        System.out.println(filteredPrograms);
//
//        List<Category> categories = categoryService.getList();
//        List<String> statuses = Arrays.asList("In Progress", "Unpublished", "Published");
//
//        model.addAttribute("programs", programs);
//        model.addAttribute("categories", categories);
//        model.addAttribute("statuses", statuses);
//        model.addAttribute("selectedIds", selectedIds);
//        model.addAttribute("selectedCategories", selectedCategories);
//        model.addAttribute("selectedStatuses", selectedStatuses);
//        model.addAttribute("filteredPrograms", filteredPrograms);
        return "host/program/list-ms";
    }

}
