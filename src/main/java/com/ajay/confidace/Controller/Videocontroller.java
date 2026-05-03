package com.ajay.confidace.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@RequestMapping("/Video")
public class Videocontroller {

    // 🔹 Folder where videos will be stored
    private static final String UPLOAD_DIR = "D:\\Resumeg\\video\\";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {

        try {
            // 🔹 Get original file name
            String fileName = file.getOriginalFilename();


            // 🔹 Create full path
            String filePath = UPLOAD_DIR + fileName;


            // 🔹 Save file to disk
            File dest = new File(filePath);
          //file.transferTo(dest);


            // 🔥 MANUAL STREAMING STARTS HERE
            InputStream is = file.getInputStream();
            FileOutputStream fos = new FileOutputStream(dest);

            byte[] buffer = new byte[8192]; // 8KB buffer (good balance)
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            // 🔹 IMPORTANT: close resources
            fos.close();
            is.close();

            return ResponseEntity.ok("Video uploaded successfully: " + fileName);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<String>/* esponseEntity<InputStreamResource>*/ downloadVideo(@PathVariable String fileName) throws IOException {

        System.out.println("inside download controler ");
        // 🔹 File path
        String filePath = UPLOAD_DIR + fileName+".mp4";

        File file = new File(filePath);

        System.out.println(filePath);
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(fileBytes);
        if (!file.exists()) {         // 🔹 Check file exists
            System.out.println("file not exist");
            return ResponseEntity.notFound().build();
        }
        System.out.println("file exist");
        // 🔹 Create InputStream (streaming)
        InputStream inputStream = new FileInputStream(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(base64);
       /* return ResponseEntity.ok()

                // 🔹 Open in browser instead of force download
                //  .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)

*//*
        Value	            Behavior
        inline          	Opens in browser (video plays 🎥)
        attachment      	Forces download ⬇️*//*

                // 🔹 for forecefully download
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)

                // 🔹 Set video content type (important)
                .contentType(MediaType.parseMediaType("video/mp4"))

                // 🔹 Return stream
                .body(new InputStreamResource(inputStream));*//*👉

“When we return InputStreamResource, Spring internally reads the stream and writes it to the HTTP response using message converters, so we don’t need to call read() explicitly.”
*/    }

  //Streaming controller where video can go forward
    @GetMapping("/stream/{fileName}")
    public /*ResponseEntity<byte[]>*/ ResponseEntity<org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody> streamVideo(
            @PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        System.out.println("Streaming video...");

        String filePath = UPLOAD_DIR + fileName + ".mp4";
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outputStream -> {
                outputStream.write("File not found".getBytes());
            });
        }

        long fileSize = file.length();

        // 🔥 Default range
        long start = 0;
        long end = fileSize - 1;

        // 🔥 If browser sends range
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            start = Long.parseLong(ranges[0]);

            if (ranges.length > 1) {
                end = Long.parseLong(ranges[1]);
            }
        }

        long contentLength = end - start + 1;


        InputStream inputStream = new FileInputStream(file);
        inputStream.skip(start);

        // 🔥 STREAMING (memory efficient)
        org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody stream = outputStream -> {

            byte[] buffer = new byte[8192]; // 8KB chunk
            long bytesRemaining = contentLength;
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer, 0,
                    (int) Math.min(buffer.length, bytesRemaining))) != -1) {

                outputStream.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;

                if (bytesRemaining <= 0) break;
            }

            inputStream.close();
        };

        // 🔥 Read only required bytes
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(start);
       // reading all at once then share to the browse
     /*   byte[] data = new byte[(int) contentLength];
        raf.readFully(data);
        raf.close();*/

        return ResponseEntity.status(206) // 🔥 PARTIAL CONTENT
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + fileSize)
                .body(stream);

      /*  return ResponseEntity.status(206) // 🔥 VERY IMPORTANT
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + fileSize)
                .body(data);*/



    }

}
