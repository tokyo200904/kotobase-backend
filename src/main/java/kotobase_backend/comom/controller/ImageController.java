package kotobase_backend.security.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {
    private final Path rootParent = Paths.get("D:/kotobase-data");

    @GetMapping("/api/v1/images/{fileName:.+}")
    public ResponseEntity<byte[]> readImage(@PathVariable String fileName) {
        try {
            Path filePath = rootParent.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                }
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(bytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}