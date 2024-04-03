package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.Role;
import com.sr.electronic.store.Electronic_Store.entities.User;
import com.sr.electronic.store.Electronic_Store.exceptions.ResourceNOtFoundException;
import com.sr.electronic.store.Electronic_Store.helper.Helper;
import com.sr.electronic.store.Electronic_Store.repositories.RoleRepository;
import com.sr.electronic.store.Electronic_Store.repositories.UserRepository;
import com.sr.electronic.store.Electronic_Store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${normal.role.id}")
    private String normalRoleId;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    //dto => entity
        User user = dtoToEntity(userDto);

        //fetch of normal and set it to user
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);

        User saveUser = userRepository.save(user);

    //entity => dto
        UserDto newDto = entityToDto(saveUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User is Registered with the given ID"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        //save data
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User is Registered with the given ID"));
        //delete user profile image
        String fullPath = imagePath + user.getImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("User image not found in folder");
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);
    }

    @Override
    public UserDto getByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User is Registered with the given ID"));
        return entityToDto(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //For sorting
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //For Paging default Start from Zero
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
        return pageableResponse;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNOtFoundException("User is not Registered with given Email ID !!"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user-> entityToDto(user)).toList();
        //or
//        List<UserDto> dtoList = users.stream().map(this::entityToDto).toList();
        return dtoList;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDto entityToDto(User saveUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(saveUser.getUserId())
//                .name(saveUser.getName())
//                .email(saveUser.getEmail())
//                .password(saveUser.getPassword())
//                .about(saveUser.getAbout())
//                .imageName(saveUser.getImageName())
//                .gender(saveUser.getGender()).build();
        return mapper.map(saveUser,UserDto.class);

    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .gender(userDto.getGender()).build();
        return mapper.map(userDto, User.class);
    }
}
