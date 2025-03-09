package com.codementor.utils.file;

import groovy.util.logging.Slf4j;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${temp.upload.dir}")
    private String tempUploadDir;

    private final ServletContext servletContext;

    public String ckFileTempUpload(MultipartHttpServletRequest request){

        MultipartFile uploadFile = request.getFile("upload");
        assert uploadFile != null;

        String originalFileName = uploadFile.getOriginalFilename();
        assert originalFileName != null;
        String saveFileName = getFetchFileName(originalFileName);

        String realPath = servletContext.getRealPath(tempUploadDir);
        String savePath = realPath + saveFileName;
        uploadFile(savePath,uploadFile);
        log.info("Save ck upload File. originalFileName-{} , saveFileName-{}", originalFileName,saveFileName);
        // Spring boot resource relative path
        return request.getContextPath() + tempUploadDir + saveFileName;
    }

    private String getFetchFileName(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }
    
    private void uploadFile(String savePath, MultipartFile uploadFile) {
        File file = new File(savePath);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload the file", e);
        }
    }
}