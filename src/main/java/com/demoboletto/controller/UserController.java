package com.demoboletto.controller;

import com.demoboletto.annotation.UserId;
import com.demoboletto.domain.Collect;
import com.demoboletto.dto.global.ResponseDto;
import com.demoboletto.dto.request.UserProfileUpdateDto;
import com.demoboletto.dto.response.GetUserInfoDto;
import com.demoboletto.service.CollectService;
import com.demoboletto.service.UserService;
import com.demoboletto.type.ESticker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 관련 API")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final CollectService collectService;

    @GetMapping("")
    @Operation(summary = "Get User Info", description = "유저의 닉네임과 이름 정보를 가져오는 API")
    public ResponseDto<GetUserInfoDto> getUserNameAndNickname(@UserId Long userId) {
        GetUserInfoDto userInfo = userService.getUserNameAndNickname(userId);
        return ResponseDto.ok(userInfo);
    }

    @PatchMapping("")
    public ResponseDto<ResponseDto<?>> updateUserProfile(@RequestBody @Validated UserProfileUpdateDto userProfileUpdateDto) {
        userService.updateUserProfile(userProfileUpdateDto);
        return ResponseDto.ok(ResponseDto.ok("프로필이 성공적으로 업데이트되었습니다."));
    }

    @GetMapping("/frames")
    @Operation(summary = "Get User collected Frames", description = "유저가 획득한 프레임과 개수를 가져오는 API")
    public ResponseDto<?> getCollectedFrames(@UserId Long userId) {
        List<String> frames = collectService.getCollectedFrames(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("frameCount", frames.size());
        response.put("frames", frames);
        return ResponseDto.ok(response);
    }

    @GetMapping("/stickers")
    @Operation(summary = "Get User collected Stickers", description = "유저가 획득한 스티커와 개수를 가져오는 API")
    public ResponseDto<?> getCollectedStickers(@UserId Long userId) {
        List<ESticker> stickers = collectService.getCollectedStickers(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("stickerCount", stickers.size());
        response.put("stickers", stickers);
        return ResponseDto.ok(response);
    }


    @PostMapping("/collect")
    @Operation(summary = "Post user collected Stickers & Frames", description = "userId, stickerType 정보를 받아 유저가 획득한 스티커와 프레임을 저장합니다.")
    public ResponseDto<?> saveCollect(
            @UserId Long userId,
            @RequestParam(required = false) ESticker stickerType,
            @RequestPart(value = "frameFile", required = false) MultipartFile frameFile) {
        Collect collect=collectService.saveCollect(userId, stickerType, frameFile);

        return ResponseDto.ok(collect);
    }

}
