package com.example.apiserver.service;

import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.entity.Gender;
import com.example.apiserver.entity.Provider;
import com.example.apiserver.entity.User;
import com.example.apiserver.entity.UserRole;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.exception.UnauthorizedException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.SocialLoginDataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService extends BaseService<User, Long> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialLoginService socialLoginService;
    private final S3Service s3Service;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected BaseRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Transactional
    public UserResponse signUp(UserRequest.SignUp request) {
        // 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }

        // 이메일 중복 체크
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new BadRequestException("이미 등록된 이메일입니다");
        }

        // 휴대폰 번호 중복 체크
        String normalizedPhone = normalizePhone(request.getPhone());
        if (normalizedPhone != null && userRepository.existsByPhoneAndDeletedAtIsNull(normalizedPhone)) {
            throw new BadRequestException("이미 등록된 휴대폰 번호입니다");
        }

        // 생년월일 파싱
        LocalDate birthDate = null;
        if (request.getBirthDate() != null && !request.getBirthDate().isBlank()) {
            try {
                birthDate = LocalDate.parse(request.getBirthDate(), DATE_FORMATTER);
            } catch (Exception e) {
                throw new BadRequestException("생년월일 형식이 올바르지 않습니다 (YYYY-MM-DD)");
            }
        }

        // 성별 파싱
        Gender gender = null;
        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                gender = Gender.valueOf(request.getGender().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("성별은 male, female, other 중 하나여야 합니다");
            }
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(normalizedPhone)
                .birthDate(birthDate)
                .gender(gender)
                .role(UserRole.USER)
                .provider(Provider.LOCAL)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    public User validateLogin(String email, String password) {
        User user = findByEmail(email);

        // 소셜 로그인 사용자는 비밀번호가 없을 수 있음
        if (user.getProvider() != Provider.LOCAL) {
            throw new UnauthorizedException("소셜 로그인으로 가입한 계정입니다");
        }

        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다");
        }

        return user;
    }

    @Transactional
    public SocialLoginResult socialLogin(UserRequest.SocialLogin request) {
        Provider provider;
        try {
            provider = Provider.valueOf(request.getProvider().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("지원하지 않는 소셜 로그인 제공자입니다");
        }

        if (provider == Provider.LOCAL) {
            throw new BadRequestException("일반 로그인은 /auth/login을 사용하세요");
        }

        // 소셜 로그인 API에서 사용자 정보 가져오기
        SocialLoginService.SocialUserInfo socialUserInfo = socialLoginService.getUserInfo(provider, request.getAccessToken());

        if (socialUserInfo.getEmail() == null || socialUserInfo.getEmail().isBlank()) {
            throw new BadRequestException("소셜 로그인에서 이메일 정보를 가져올 수 없습니다");
        }

        // 기존 사용자 확인
        User user = userRepository.findByEmailAndProviderAndDeletedAtIsNull(socialUserInfo.getEmail(), provider)
                .orElse(null);

        boolean isNewUser = false;
        if (user == null) {
            // 신규 사용자 생성 (소셜 로그인 시 필수 정보가 없을 수 있음)
            isNewUser = true;
            user = User.builder()
                    .email(socialUserInfo.getEmail())
                    .name(socialUserInfo.getName() != null ? socialUserInfo.getName() : "사용자")
                    .provider(provider)
                    .role(UserRole.USER)
                    .phone(socialUserInfo.getPhone())
                    .profileImageUrl(socialUserInfo.getProfileImage())
                    // birthDate, gender는 소셜에서 가져올 수 있으면 설정, 없으면 null
                    .birthDate(SocialLoginDataParser.parseBirthDate(socialUserInfo.getBirthYear(), socialUserInfo.getBirthDay()))
                    .gender(SocialLoginDataParser.parseGender(socialUserInfo.getGender()))
                    .build();
            user = userRepository.save(user);
        } else {
            // 기존 사용자 정보 업데이트 (프로필 이미지 등)
            if (socialUserInfo.getProfileImage() != null && !socialUserInfo.getProfileImage().isBlank()) {
                user.updateProfileImageUrl(socialUserInfo.getProfileImage());
            }
        }

        return new SocialLoginResult(UserResponse.from(user), isNewUser);
    }


    public static class SocialLoginResult {
        private final UserResponse userResponse;
        private final boolean isNewUser;

        public SocialLoginResult(UserResponse userResponse, boolean isNewUser) {
            this.userResponse = userResponse;
            this.isNewUser = isNewUser;
        }

        public UserResponse getUserResponse() {
            return userResponse;
        }

        public boolean isNewUser() {
            return isNewUser;
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));
    }

    public UserResponse getProfile(Long userId) {
        User user = findById(userId);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UserRequest.UpdateProfile request) {
        User user = findById(userId);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.updateName(request.getName());
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String normalizedPhone = normalizePhone(request.getPhone());
            // 다른 사용자가 사용 중인지 확인
            if (normalizedPhone != null && userRepository.existsByPhoneAndDeletedAtIsNull(normalizedPhone)) {
                User existingUser = userRepository.findByEmailAndDeletedAtIsNull(user.getEmail())
                        .orElse(null);
                if (existingUser == null || !existingUser.getId().equals(userId)) {
                    throw new BadRequestException("이미 사용 중인 휴대폰 번호입니다");
                }
            }
            user.updatePhone(normalizedPhone);
        }

        if (request.getBirthDate() != null && !request.getBirthDate().isBlank()) {
            try {
                LocalDate birthDate = LocalDate.parse(request.getBirthDate(), DATE_FORMATTER);
                user.updateBirthDate(birthDate);
            } catch (Exception e) {
                throw new BadRequestException("생년월일 형식이 올바르지 않습니다 (YYYY-MM-DD)");
            }
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                Gender gender = Gender.valueOf(request.getGender().toUpperCase());
                user.updateGender(gender);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("성별은 male, female, other 중 하나여야 합니다");
            }
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            // 다른 사용자가 사용 중인지 확인
            if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
                User existingUser = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                        .orElse(null);
                if (existingUser == null || !existingUser.getId().equals(userId)) {
                    throw new BadRequestException("이미 사용 중인 이메일입니다");
                }
            }
            user.updateEmail(request.getEmail());
        }

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse completeSocialProfile(Long userId, UserRequest.CompleteSocialProfile request) {
        User user = findById(userId);

        // 소셜 로그인 사용자만 가능
        if (user.getProvider() == Provider.LOCAL) {
            throw new BadRequestException("일반 회원가입 사용자는 이 API를 사용할 수 없습니다");
        }

        // 필수 정보가 이미 모두 있는지 확인
        if (user.getPhone() != null && user.getBirthDate() != null && user.getGender() != null) {
            throw new BadRequestException("이미 모든 필수 정보가 입력되어 있습니다");
        }

        // 휴대폰 번호 설정
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String normalizedPhone = normalizePhone(request.getPhone());
            // 다른 사용자가 사용 중인지 확인
            if (normalizedPhone != null && userRepository.existsByPhoneAndDeletedAtIsNull(normalizedPhone)) {
                User existingUser = userRepository.findByPhoneAndDeletedAtIsNull(normalizedPhone);
                if (existingUser != null && !existingUser.getId().equals(userId)) {
                    throw new BadRequestException("이미 사용 중인 휴대폰 번호입니다");
                }
            }
            user.updatePhone(normalizedPhone);
        }

        // 생년월일 설정
        if (request.getBirthDate() != null && !request.getBirthDate().isBlank()) {
            try {
                LocalDate birthDate = LocalDate.parse(request.getBirthDate(), DATE_FORMATTER);
                user.updateBirthDate(birthDate);
            } catch (Exception e) {
                throw new BadRequestException("생년월일 형식이 올바르지 않습니다 (YYYY-MM-DD)");
            }
        }

        // 성별 설정
        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                Gender gender = Gender.valueOf(request.getGender().toUpperCase());
                user.updateGender(gender);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("성별은 male, female, other 중 하나여야 합니다");
            }
        }

        // 필수 정보 검증
        if (user.getPhone() == null || user.getBirthDate() == null || user.getGender() == null) {
            throw new BadRequestException("모든 필수 정보를 입력해주세요 (휴대폰 번호, 생년월일, 성별)");
        }

        return UserResponse.from(user);
    }

    @Transactional
    public void changePassword(Long userId, UserRequest.ChangePassword request) {
        User user = findById(userId);

        if (user.getProvider() != Provider.LOCAL) {
            throw new BadRequestException("소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다");
        }

        if (user.getPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("현재 비밀번호가 일치하지 않습니다");
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteUser(Long userId, UserRequest.DeleteUser request) {
        User user = findById(userId);

        // 일반 회원가입 사용자는 비밀번호 확인 필요
        if (user.getProvider() == Provider.LOCAL) {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new BadRequestException("비밀번호를 입력해주세요");
            }

            if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new UnauthorizedException("비밀번호가 올바르지 않습니다");
            }
        }

        // 소프트 삭제
        user.softDelete();
    }

    @Transactional
    public String uploadProfileImage(Long userId, MultipartFile image) {
        User user = findById(userId);
        
        // 기존 프로필 이미지 URL 가져오기 (삭제용)
        String oldImageUrl = user.getProfileImageUrl();

        // S3 업로드
        String profileImageUrl = s3Service.uploadFile(image, "profile/" + userId);
        
        // 기존 이미지 삭제 (TODO: 이미지 삭제 정책 결정 필요 - 현재는 즉시 삭제)
        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            s3Service.deleteFile(oldImageUrl);
        }

        // DB 업데이트
        user.updateProfileImageUrl(profileImageUrl);
        
        return profileImageUrl;
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = findById(userId);
        
        // 기존 프로필 이미지 URL 가져오기
        String oldImageUrl = user.getProfileImageUrl();

        // S3에서 이미지 삭제 (TODO: 이미지 삭제 정책 결정 필요 - 현재는 즉시 삭제)
        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            s3Service.deleteFile(oldImageUrl);
        }

        // DB에서 이미지 URL 제거
        user.updateProfileImageUrl(null);
    }

    @Transactional
    public void updateProfileImageUrl(Long userId, String profileImageUrl) {
        User user = findById(userId);
        user.updateProfileImageUrl(profileImageUrl);
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        // 하이픈 제거
        return phone.replace("-", "");
    }

    // 관리자용 메서드들
    public Page<UserResponse> getUsersForAdmin(int page, int limit, String search, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<User> users;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                users = userRepository.findAllWithSearchOrderByUpdatedAt(search, pageable);
                break;
            case "name":
                users = userRepository.findAllWithSearchOrderByName(search, pageable);
                break;
            case "email":
                users = userRepository.findAllWithSearchOrderByEmail(search, pageable);
                break;
            default:
                users = userRepository.findAllWithSearch(search, pageable);
        }
        
        return users.map(UserResponse::from);
    }

    @Transactional
    public UserResponse createUserForAdmin(UserRequest.CreateAdmin request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new BadRequestException("이미 등록된 이메일입니다");
        }

        // 휴대폰 번호 중복 체크
        String normalizedPhone = normalizePhone(request.getPhone());
        if (normalizedPhone != null && userRepository.existsByPhoneAndDeletedAtIsNull(normalizedPhone)) {
            throw new BadRequestException("이미 등록된 휴대폰 번호입니다");
        }

        // 생년월일 파싱
        LocalDate birthDate = LocalDate.parse(request.getBirthDate(), DATE_FORMATTER);

        // 성별 파싱
        Gender gender = Gender.valueOf(request.getGender().toUpperCase());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(normalizedPhone)
                .birthDate(birthDate)
                .gender(gender)
                .role(UserRole.USER)
                .provider(Provider.LOCAL)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    @Transactional
    public UserResponse updateUserForAdmin(Long userId, UserRequest.UpdateAdmin request) {
        User user = findById(userId);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.updateName(request.getName());
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String normalizedPhone = normalizePhone(request.getPhone());
            // 다른 사용자가 사용 중인지 확인
            if (normalizedPhone != null && userRepository.existsByPhoneAndDeletedAtIsNull(normalizedPhone)) {
                User existingUser = userRepository.findByPhoneAndDeletedAtIsNull(normalizedPhone);
                if (existingUser != null && !existingUser.getId().equals(userId)) {
                    throw new BadRequestException("이미 사용 중인 휴대폰 번호입니다");
                }
            }
            user.updatePhone(normalizedPhone);
        }

        if (request.getBirthDate() != null && !request.getBirthDate().isBlank()) {
            try {
                LocalDate birthDate = LocalDate.parse(request.getBirthDate(), DATE_FORMATTER);
                user.updateBirthDate(birthDate);
            } catch (Exception e) {
                throw new BadRequestException("생년월일 형식이 올바르지 않습니다 (YYYY-MM-DD)");
            }
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                Gender gender = Gender.valueOf(request.getGender().toUpperCase());
                user.updateGender(gender);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("성별은 male, female, other 중 하나여야 합니다");
            }
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            // 다른 사용자가 사용 중인지 확인
            if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
                User existingUser = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                        .orElse(null);
                if (existingUser != null && !existingUser.getId().equals(userId)) {
                    throw new BadRequestException("이미 사용 중인 이메일입니다");
                }
            }
            user.updateEmail(request.getEmail());
        }

        return UserResponse.from(user);
    }

    @Transactional
    public void deleteUserForAdmin(Long userId) {
        User user = findById(userId);
        
        // 프로필 이미지가 있으면 S3에서 삭제
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isBlank()) {
            s3Service.deleteFile(user.getProfileImageUrl());
        }
        
        // 소프트 삭제
        user.softDelete();
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

