package com.owu.geekhub.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface FileStorageService {
    void storeFile (MultipartFile file);
    Resource loadFile(String fileName) throws MalformedURLException;
}
