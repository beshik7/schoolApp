package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.List;

public interface AvatarService {
    Avatar uploadAvatar(Long id, MultipartFile file) throws IOException;
    Avatar getAvatarId(Long id);

    Avatar getAvatar(Long avatarId);

    List<Avatar> getPaginatedAvatars(Integer page, Integer size);
}
