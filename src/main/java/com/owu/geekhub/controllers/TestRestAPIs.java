package com.owu.geekhub.controllers;


import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class TestRestAPIs {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FileStorageService fileStorageService;

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
            File uploadFolder =  new ClassPathResource("static" + File.separator
                    + uploadPath + File.separator + "usersPicture").getFile();
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + file.getOriginalFilename();
            final File targetFile = new File(uploadFolder.getAbsolutePath()
                    + File.separator + resultFileName);
            targetFile.createNewFile();
            file.transferTo(targetFile);
            user.setProfileImage(resultFileName);
            userDao.save(user);

        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
