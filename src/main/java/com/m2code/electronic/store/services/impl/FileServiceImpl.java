package com.m2code.electronic.store.services.impl;

import com.m2code.electronic.store.exceptions.BadApiRequest;
import com.m2code.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    //    method to upload file
    @Override
    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("Original file name {}", originalFilename);

        String fileName = UUID.randomUUID().toString();

        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileNameWithExtension = fileName.concat(extension);

        String fullPathWithFileName = path.concat(fileNameWithExtension);

//        check extension and upload file
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
//        file upload

//            1. Check Folder is available
            File folder = new File(path);
            if (!folder.exists()) {
                boolean mkdirs = folder.mkdirs();
            }
//            copy file to folder
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPathWithFileName));

            return fileNameWithExtension;

        } else {
            throw new BadApiRequest("File with extension " + extension + " are not allowed");
        }

    }


    //    method to render file
    @Override
    public InputStream renderFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path.concat(File.separator).concat(fileName);
        return new FileInputStream(filePath);
    }
}
