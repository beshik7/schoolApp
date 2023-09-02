package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
    public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
    @PostMapping(value = "/students/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Avatar uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        return avatarService.uploadAvatar(id, file);
    }
    @GetMapping("/avatar/{avatarId}/dB")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long avatarId) {
    Avatar avatar = avatarService.getAvatar(avatarId);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
    httpHeaders.setContentLength(avatar.getFileSize());
    return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
    }

    @GetMapping("/avatars")
    public List<Avatar> getPaginatedAvatars(@RequestParam Integer page, @RequestParam Integer size) {
        return avatarService.getPaginatedAvatars(page, size);
    }
    @GetMapping("/avatar/{avatarId}/from-drive")
    public void downloadAvatarFromDrive(@PathVariable Long avatarId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.getAvatar(avatarId);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
             response.setStatus(HttpStatus.OK.value());
             response.setContentType(avatar.getMediaType());
             response.setContentLength(Math.toIntExact(avatar.getFileSize()));
            is.transferTo(os);
        }

        }

    }

