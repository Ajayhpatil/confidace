package com.ajay.confidace.Controller;


import com.ajay.confidace.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

//Returing json only
@RestController
@RequestMapping("/api")
public class controller {



    @PostMapping("/add")
    public respone Useraddition (@RequestBody user userobj){

        System.out.println("feature");

        respone responeobj =new respone();
        responeobj.setRespone("user added1");
        System.out.println(userobj.getUser()+
                userobj.getPassword());

        return responeobj;

    };

    @PostMapping("/upload-pdf")
    public String uploadPdf(@RequestParam("file") MultipartFile file) {

        try {
            // folder path (change as per your system)
            String folderPath = "D:\\Resumeg\\hhhhhhhhhhhhhhhhhh";



            // create folder if not exists
            File dir = new File(folderPath);
            if (!dir.exists()) {
               dir.mkdirs();
            }

            String filePath = folderPath + "\\" + UUID.randomUUID().toString()+".pdf";
            // file path
           // String filePath = folderPath + file.getOriginalFilename();


            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(file.getBytes());
            }

            // convert to bytes and save


            return "PDF uploaded successfully: " + filePath;

        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/download/{policyNo}")
    public ResponseEntity<byte[]> getPdf(@PathVariable String policyNo) throws IOException {

        // Example: dynamic file path
        String filePath = "D:\\Resumeg\\hhhhhhhhhhhhhhhhhh\\" + policyNo + ".pdf";

        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("PDF not found");
        }

        byte[] pdfBytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + policyNo + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}


// returning view instead of json


