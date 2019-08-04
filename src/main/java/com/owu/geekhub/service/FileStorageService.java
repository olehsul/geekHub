package com.owu.geekhub.service;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageService {
    void storeFile (MultipartFile file);
    Resource loadFile(String fileName) throws MalformedURLException;
    void savePhoto(MultipartFile file) throws IOException;
}
