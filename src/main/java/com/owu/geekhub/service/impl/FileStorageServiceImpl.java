package com.owu.geekhub.service.impl;

import com.owu.geekhub.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${upload.path}")
    private Path fileRootLocation;

    private Path fileRoot;

    @Override
    public void storeFile(MultipartFile file) {

    }

    @Override
    public Resource loadFile(String fileName) throws MalformedURLException {
        Path imageDir = Paths.get(System.getProperty("user.dir") + fileRootLocation
                + File.separator + File.separator + "usersPicture");
        try {
            Path file = imageDir.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                System.out.println("++++++++++++++++++++++++++++File exists!!!!!!+++++++++++++++++++++++++++");
                return resource;
            } else {
                System.out.println("++++++++++++++++++++++++++++File doesn't exists!!!!!!+++++++++++++++++++++++++++");
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }

//        if (Files.exists(Paths.get(System.getProperty("user.dir") + fileRootLocation
//                + File.separator + File.separator + "usersPicture"))){
//            System.out.println("++++++++++++++++++++++++++++File exists!!!!!!+++++++++++++++++++++++++++");
//        }else {
//            System.out.println("++++++++++++++++++++++++++++File doesn't exists!!!!!!+++++++++++++++++++++++++++");
//        }
    }
}
