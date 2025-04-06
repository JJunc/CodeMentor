package com.codementor.post.service;

import com.codementor.exception.ImageSaveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir; // 실제 파일이 저장된 경로

    public Map<String, String> saveImage(MultipartFile imageFile) {
        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path savePath = Paths.get(uploadDir, fileName);
            Files.copy(imageFile.getInputStream(), savePath);

            String imageUrl = "/images/" + fileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", imageUrl);
            result.put("filename", fileName);
            return result;
        } catch (IOException e) {
            throw new ImageSaveException("이미지 저장 중 오류 발생", e);
        }
    }

    public void deleteImageFile(String filename) {
        Path filePath = Paths.get(uploadDir, filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("이미지 삭제 실패: {}", filename, e);
        }
    }

}
