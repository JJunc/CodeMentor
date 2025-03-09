package com.codementor.utils.file;

import com.codementor.post.entity.PostImage;
import com.codementor.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final PostImageRepository postImageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${temp.upload.dir}")
    private String tempUploadDir;

    public String imageUpload(MultipartRequest request) throws IOException {

        // MultipartRequest에서 파일을 가져오기
        MultipartFile file = request.getFile("upload"); // CKEditor에서 전송한 파일 필드명

        // 파일 이름 정리 및 경로 설정
        String fileName = UUID.randomUUID() + StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(uploadDir, fileName);

        // 디렉토리가 없으면 생성
        Files.createDirectories(path.getParent());

        // 파일 저장
        Files.copy(file.getInputStream(), path);

        // 업로드된 파일 URL 반환
        return "/images/" + fileName; // 정적 파일 경로
    }

    public String tempImageUpload(MultipartRequest request) throws IOException {

        // MultipartRequest에서 파일을 가져오기
        MultipartFile file = request.getFile("upload"); // CKEditor에서 전송한 파일 필드명

        // 파일 이름 정리 및 경로 설정
        String fileName = UUID.randomUUID() + StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(uploadDir, fileName);

        // 디렉토리가 없으면 생성
        Files.createDirectories(path.getParent());

        // 파일 저장
        Files.copy(file.getInputStream(), path);

        // 업로드된 파일 URL 반환
        return "/images/" + fileName; // 정적 파일 경로
    }
}

