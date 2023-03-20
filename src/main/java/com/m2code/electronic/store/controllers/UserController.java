package com.m2code.electronic.store.controllers;

import com.m2code.electronic.store.dtos.ApiResponseMessage;
import com.m2code.electronic.store.dtos.ImageApiResponse;
import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.dtos.UserDto;
import com.m2code.electronic.store.services.FileService;
import com.m2code.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FileService fileService;

    @Value("${users.image.path}")
    private String imageUploadPath;

    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    //    create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    //    update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        UserDto updateUser = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }


    //    delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("User with userId " + userId + " deleted")
                .success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //    get all user
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir));
    }

    //    get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    //    get user by email
    @GetMapping("email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    //    search user
    @GetMapping("search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        return ResponseEntity.ok(userService.searchUser(keyword));
    }

    //    upload user image
    @PostMapping("/upload/{userId}")
    public ResponseEntity<ImageApiResponse> uploadUserImage(@PathVariable String userId, @RequestParam("userImage") MultipartFile image) throws IOException {
        String userPhoto = fileService.uploadFile(image, imageUploadPath);
//        update image name in user
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(userPhoto);
        userService.updateUser(userDto, userId);
//        creating image api response
        ImageApiResponse imageApiResponse = ImageApiResponse.builder().fileName(userPhoto).message("File uploaded").status(HttpStatus.CREATED)
                .success(true).build();
        return new ResponseEntity<>(imageApiResponse, HttpStatus.CREATED);
    }

    //    serve user image
    @GetMapping("/image/{userId}")
    public void renderImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        InputStream inputStream = fileService.renderFile(imageUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
