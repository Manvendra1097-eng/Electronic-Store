package com.m2code.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    //    upload file
    String uploadFile(MultipartFile multipartFile, String path) throws IOException;

    //    read uploaded file
    InputStream renderFile(String path, String fileName) throws FileNotFoundException;
}
