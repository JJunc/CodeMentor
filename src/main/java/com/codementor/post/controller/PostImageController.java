package com.codementor.post.controller;

import com.codementor.post.repository.PostImageRepository;
import com.codementor.utils.file.FileService;
import com.codementor.utils.file.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class PostImageController {

    private final FileService fileService;

    @PostMapping("/image/upload")
    public ModelAndView uploadCKImage(MultipartHttpServletRequest request) {
        ModelAndView mav = new ModelAndView("jsonView");

        String uploadPath = fileService.ckFileTempUpload(request);
        mav.addObject("uploaded", true);
        mav.addObject("url", uploadPath);
        return mav;
    }
}

