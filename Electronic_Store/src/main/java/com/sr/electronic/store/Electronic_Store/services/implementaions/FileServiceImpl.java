package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.exceptions.BadApiRequestException;
import com.sr.electronic.store.Electronic_Store.services.FileService;
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

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException{
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename : {}",originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filenameWithExtension = filename+extension;
        String fullPathWithFileName = path + filenameWithExtension;

        logger.info("full path with name : {}",fullPathWithFileName);
        if(extension.equalsIgnoreCase(".png")|| extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")){

            //file save
            logger.info("File extension is : {}",extension);
            File folder = new File(path);
            if(!folder.exists()){
                //create folder
                folder.mkdirs();
            }

            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return filenameWithExtension;
        } else {
            throw new BadApiRequestException("File with this"+extension+" not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
