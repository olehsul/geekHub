package com.owu.geekhub.controllers;


import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class TestRestAPIs {
    @Autowired
    private UserDao userDao;

    @GetMapping("/api/test/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String userAccess() {
        return ">>> User Contents!";
    }

    @GetMapping("/api/test/pm")
    @PreAuthorize("hasRole('ROLE_PM') or hasRole('ROLE_ADMIN')")
    public String projectManagementAccess() {
        return ">>> Project Management Board";
    }

    @GetMapping("/api/test/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/save-photo")
    public void savePhoto(@RequestPart MultipartFile file) throws IOException {
        System.out.println("inside-save-photo-------------");
        BufferedImage image = ImageIO.read(file.getInputStream());
            if (image != null) {
                User user = userDao.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                String baseDir = System.getProperty("user.dir");
                File uploadFolder = new File(baseDir + uploadPath + File.separator + "usersPicture");
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + file.getOriginalFilename();
                final File targetFile = new File(baseDir + uploadPath + File.separator + "usersPicture" + File.separator + resultFileName);
                targetFile.createNewFile();
                file.transferTo(targetFile);
                user.setProfileImage(resultFileName);
                userDao.save(user);

        }
    }

}
