package com.demoboletto.service;

import com.demoboletto.domain.Collect;
import com.demoboletto.domain.User;
import com.demoboletto.exception.CommonException;
import com.demoboletto.exception.ErrorCode;
import com.demoboletto.repository.CollectRepository;
import com.demoboletto.repository.PictureRepository;
import com.demoboletto.repository.UserRepository;
import com.demoboletto.type.ESticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectService {

    private final CollectRepository collectRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final AWSS3Service awsS3Service;

    @Transactional
    public Collect saveCollect(Long userId, ESticker stickerType, MultipartFile frameFile) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String frameUrl = null;
        if (frameFile != null && !frameFile.isEmpty()) {
            try {
                frameUrl = awsS3Service.uploadFile(frameFile);  // S3에 이미지 업로드
            } catch (IOException e) {
                throw new CommonException(ErrorCode.UPLOAD_FILE_ERROR);
            }
        }

        Collect collect = Collect.builder()
                .user(user)
                .frameUrl(frameUrl)
                .stickerType(stickerType)
                .build();

        return collectRepository.save(collect);
    }

    public List<String> getCollectedFrames(Long userId) {
        List<Collect> collections = collectRepository.findByUserIdAndFrameUrlIsNotNull(userId);
        return collections.stream()
                .map(Collect::getFrameUrl)
                .collect(Collectors.toList());
    }

    public List<ESticker> getCollectedStickers(Long userId) {
        List<Collect> collections = collectRepository.findByUserIdAndStickerTypeIsNotNull(userId);
        return collections.stream()
                .map(Collect::getStickerType)
                .collect(Collectors.toList());
    }
}
