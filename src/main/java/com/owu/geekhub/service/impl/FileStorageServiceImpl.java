package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${upload.path:#{null}}")
    private Path fileRootLocation;

    @Autowired
    private UserDao userDao;

    @Value("${upload.path:#{null}}")
    private String uploadPath;

    @Override
    public void storeFile(MultipartFile file) {

    }

    @Override
    public Resource loadFile(String fileName) {
        Path imageDir = Paths.get(System.getProperty("user.dir") + File.separator + fileRootLocation
                + File.separator + "usersPicture");
        Resource resource = null;
        try {
            Path file = imageDir.resolve(fileName);
            resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                log.debug("++++++++++++++++++++++++++++File exists!!!!!!+++++++++++++++++++++++++++");
                return resource;
            } else {
                log.debug("++++++++++++++++++++++++++++File doesn't exists!!!!!!+++++++++++++++++++++++++++");
                throw new FileNotFoundException("FAIL!");
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (MalformedURLException e) {
            log.error("Url doesn't exist" + e.getMessage());
        }
        return resource;
    }

    @Override
    public void savePhoto(MultipartFile file) throws IOException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (image != null) {
            User user = userDao.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            File uploadFolder = new ClassPathResource("static" + File.separator
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
}
