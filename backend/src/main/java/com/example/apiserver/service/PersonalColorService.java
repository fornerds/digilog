package com.example.apiserver.service;

import com.example.apiserver.dto.personalcolor.PersonalColorColorResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorRequest;
import com.example.apiserver.dto.personalcolor.PersonalColorResponse;
import com.example.apiserver.entity.Image;
import com.example.apiserver.entity.PersonalColorColor;
import com.example.apiserver.entity.PersonalColorDiagnosis;
import com.example.apiserver.entity.PersonalColorDiagnosisColor;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.ImageRepository;
import com.example.apiserver.repository.PersonalColorColorRepository;
import com.example.apiserver.repository.PersonalColorDiagnosisRepository;
import com.example.apiserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalColorService extends BaseService<PersonalColorDiagnosis, Long> {

    private final PersonalColorDiagnosisRepository diagnosisRepository;
    private final PersonalColorColorRepository colorRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final JsonConverter jsonConverter;

    @Override
    protected BaseRepository<PersonalColorDiagnosis, Long> getRepository() {
        return diagnosisRepository;
    }

    public List<PersonalColorColorResponse> getColors() {
        List<PersonalColorColor> colors = colorRepository.findAllActive();
        return colors.stream()
                .map(c -> PersonalColorColorResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .hexCode(c.getHexCode())
                        .category(c.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    public PersonalColorResponse getDiagnosis(Long diagnosisId) {
        PersonalColorDiagnosis diagnosis = findById(diagnosisId);
        
        List<PersonalColorColor> matchingColors = diagnosis.getDiagnosisColors().stream()
                .filter(dc -> "matching".equals(dc.getType()) && !dc.isDeleted())
                .map(dc -> dc.getColor())
                .collect(Collectors.toList());
        
        List<PersonalColorColor> nonMatchingColors = diagnosis.getDiagnosisColors().stream()
                .filter(dc -> "nonMatching".equals(dc.getType()) && !dc.isDeleted())
                .map(dc -> dc.getColor())
                .collect(Collectors.toList());

        return PersonalColorResponse.from(diagnosis, matchingColors, nonMatchingColors, jsonConverter);
    }

    // 관리자용 메서드들
    public Page<PersonalColorColorResponse> getColorsForAdmin(int page, int limit, String category, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<PersonalColorColor> colors;
        
        switch (sortBy != null ? sortBy : "id") {
            case "name":
                colors = colorRepository.findAllActiveOrderByName(category, pageable);
                break;
            case "category":
                colors = colorRepository.findAllActiveOrderByCategory(category, pageable);
                break;
            default:
                colors = colorRepository.findAllActive(category, pageable);
        }
        
        return colors.map(c -> PersonalColorColorResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .hexCode(c.getHexCode())
                .category(c.getCategory())
                .build());
    }

    public PersonalColorColorResponse getColorForAdmin(Long colorId) {
        PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다"));
        
        return PersonalColorColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .category(color.getCategory())
                .build();
    }

    @Transactional
    public PersonalColorColorResponse createColorForAdmin(PersonalColorRequest.CreateColor request) {
        PersonalColorColor color = PersonalColorColor.builder()
                .name(request.getName())
                .hexCode(request.getHexCode())
                .category(request.getCategory())
                .build();
        
        PersonalColorColor savedColor = colorRepository.save(color);
        return PersonalColorColorResponse.builder()
                .id(savedColor.getId())
                .name(savedColor.getName())
                .hexCode(savedColor.getHexCode())
                .category(savedColor.getCategory())
                .build();
    }

    @Transactional
    public PersonalColorColorResponse updateColorForAdmin(Long colorId, PersonalColorRequest.UpdateColor request) {
        PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다"));
        
        if (request.getName() != null) {
            color.updateName(request.getName());
        }
        if (request.getHexCode() != null) {
            color.updateHexCode(request.getHexCode());
        }
        if (request.getCategory() != null) {
            color.updateCategory(request.getCategory());
        }
        
        return PersonalColorColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .category(color.getCategory())
                .build();
    }

    @Transactional
    public void deleteColorForAdmin(Long colorId) {
        PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다"));
        
        // 다른 진단에서 사용 중인지 확인
        List<PersonalColorDiagnosis> diagnoses = diagnosisRepository.findAllByIsDeletedFalse().stream()
                .filter(d -> d.getDiagnosisColors().stream()
                        .anyMatch(dc -> !dc.isDeleted() && dc.getColor().getId().equals(colorId)))
                .collect(Collectors.toList());
        
        if (!diagnoses.isEmpty()) {
            throw new BadRequestException("사용 중인 색상은 삭제할 수 없습니다");
        }
        
        color.softDelete();
        colorRepository.save(color); // soft delete 후 저장
    }

    public Page<PersonalColorResponse> getDiagnosesForAdmin(int page, int limit, String search, Long userId, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<PersonalColorDiagnosis> diagnoses;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                diagnoses = diagnosisRepository.findAllWithFiltersOrderByUpdatedAt(userId, search, pageable);
                break;
            default:
                diagnoses = diagnosisRepository.findAllWithFilters(userId, search, pageable);
        }
        
        return diagnoses.map(diagnosis -> {
            List<PersonalColorColor> matchingColors = diagnosis.getDiagnosisColors().stream()
                    .filter(dc -> "matching".equals(dc.getType()) && !dc.isDeleted())
                    .map(PersonalColorDiagnosisColor::getColor)
                    .collect(Collectors.toList());
            
            List<PersonalColorColor> nonMatchingColors = diagnosis.getDiagnosisColors().stream()
                    .filter(dc -> "nonMatching".equals(dc.getType()) && !dc.isDeleted())
                    .map(PersonalColorDiagnosisColor::getColor)
                    .collect(Collectors.toList());
            
            return PersonalColorResponse.from(diagnosis, matchingColors, nonMatchingColors, jsonConverter);
        });
    }

    @Transactional
    public PersonalColorResponse createDiagnosisForAdmin(PersonalColorRequest.CreateDiagnosis request) {
        User user = userRepository.findByIdAndIsDeletedFalse(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        String typeDescriptionsJson = jsonConverter.toJson(request.getTypeDescriptions());
        String makeupTipsJson = jsonConverter.toJson(request.getMakeupTips());

        Image contouringGuideImage = null;
        if (request.getContouringGuideUrl() != null && !request.getContouringGuideUrl().isBlank()) {
            contouringGuideImage = imageRepository.findByUrlAndIsDeletedFalse(request.getContouringGuideUrl())
                    .orElse(null);
        }

        PersonalColorDiagnosis diagnosis = PersonalColorDiagnosis.builder()
                .user(user)
                .personalColor(request.getPersonalColor())
                .typePercentage(request.getTypePercentage())
                .typeDescriptions(typeDescriptionsJson)
                .labL(request.getLabValues().getL())
                .labA(request.getLabValues().getA())
                .labB(request.getLabValues().getB())
                .matchingColorsTitle(request.getMatchingColors().getTitle())
                .matchingColorsDescription(request.getMatchingColors().getDescription())
                .nonMatchingColorsTitle(request.getNonMatchingColors().getTitle())
                .nonMatchingColorsDescription(request.getNonMatchingColors().getDescription())
                .contouringGuideImage(contouringGuideImage)
                .makeupTips(makeupTipsJson)
                .build();

        PersonalColorDiagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);

        // 어울리는 색상 연결
        for (Long colorId : request.getMatchingColors().getColorIds()) {
            PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                    .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다: " + colorId));
            PersonalColorDiagnosisColor diagnosisColor = PersonalColorDiagnosisColor.builder()
                    .diagnosis(savedDiagnosis)
                    .color(color)
                    .type("matching")
                    .build();
            savedDiagnosis.getDiagnosisColors().add(diagnosisColor);
        }

        // 안 어울리는 색상 연결
        for (Long colorId : request.getNonMatchingColors().getColorIds()) {
            PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                    .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다: " + colorId));
            PersonalColorDiagnosisColor diagnosisColor = PersonalColorDiagnosisColor.builder()
                    .diagnosis(savedDiagnosis)
                    .color(color)
                    .type("nonMatching")
                    .build();
            savedDiagnosis.getDiagnosisColors().add(diagnosisColor);
        }

        List<PersonalColorColor> matchingColors = savedDiagnosis.getDiagnosisColors().stream()
                .filter(dc -> "matching".equals(dc.getType()) && !dc.isDeleted())
                .map(PersonalColorDiagnosisColor::getColor)
                .collect(Collectors.toList());
        
        List<PersonalColorColor> nonMatchingColors = savedDiagnosis.getDiagnosisColors().stream()
                .filter(dc -> "nonMatching".equals(dc.getType()) && !dc.isDeleted())
                .map(PersonalColorDiagnosisColor::getColor)
                .collect(Collectors.toList());

        return PersonalColorResponse.from(savedDiagnosis, matchingColors, nonMatchingColors, jsonConverter);
    }

    @Transactional
    public PersonalColorResponse updateDiagnosisForAdmin(Long diagnosisId, PersonalColorRequest.UpdateDiagnosis request) {
        PersonalColorDiagnosis diagnosis = findById(diagnosisId);

        if (request.getPersonalColor() != null) {
            diagnosis.updatePersonalColor(request.getPersonalColor());
        }
        if (request.getTypePercentage() != null) {
            diagnosis.updateTypePercentage(request.getTypePercentage());
        }
        if (request.getTypeDescriptions() != null) {
            diagnosis.updateTypeDescriptions(jsonConverter.toJson(request.getTypeDescriptions()));
        }
        if (request.getLabValues() != null) {
            diagnosis.updateLabValues(
                    request.getLabValues().getL(),
                    request.getLabValues().getA(),
                    request.getLabValues().getB()
            );
        }
        if (request.getMatchingColors() != null) {
            diagnosis.updateMatchingColors(
                    request.getMatchingColors().getTitle(),
                    request.getMatchingColors().getDescription()
            );
            // 기존 색상 관계 삭제
            diagnosis.getDiagnosisColors().removeIf(dc -> "matching".equals(dc.getType()));
            // 새 색상 연결
            if (request.getMatchingColors().getColorIds() != null) {
                for (Long colorId : request.getMatchingColors().getColorIds()) {
                    PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                            .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다: " + colorId));
                    PersonalColorDiagnosisColor diagnosisColor = PersonalColorDiagnosisColor.builder()
                            .diagnosis(diagnosis)
                            .color(color)
                            .type("matching")
                            .build();
                    diagnosis.getDiagnosisColors().add(diagnosisColor);
                }
            }
        }
        if (request.getNonMatchingColors() != null) {
            diagnosis.updateNonMatchingColors(
                    request.getNonMatchingColors().getTitle(),
                    request.getNonMatchingColors().getDescription()
            );
            // 기존 색상 관계 삭제
            diagnosis.getDiagnosisColors().removeIf(dc -> "nonMatching".equals(dc.getType()));
            // 새 색상 연결
            if (request.getNonMatchingColors().getColorIds() != null) {
                for (Long colorId : request.getNonMatchingColors().getColorIds()) {
                    PersonalColorColor color = colorRepository.findByIdAndIsDeletedFalse(colorId)
                            .orElseThrow(() -> new ResourceNotFoundException("색상을 찾을 수 없습니다: " + colorId));
                    PersonalColorDiagnosisColor diagnosisColor = PersonalColorDiagnosisColor.builder()
                            .diagnosis(diagnosis)
                            .color(color)
                            .type("nonMatching")
                            .build();
                    diagnosis.getDiagnosisColors().add(diagnosisColor);
                }
            }
        }
        if (request.getContouringGuideUrl() != null) {
            if (request.getContouringGuideUrl().isBlank()) {
                diagnosis.updateContouringGuideImage(null);
            } else {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(request.getContouringGuideUrl())
                        .orElse(null);
                diagnosis.updateContouringGuideImage(image);
            }
        }
        if (request.getMakeupTips() != null) {
            diagnosis.updateMakeupTips(jsonConverter.toJson(request.getMakeupTips()));
        }

        List<PersonalColorColor> matchingColors = diagnosis.getDiagnosisColors().stream()
                .filter(dc -> "matching".equals(dc.getType()) && !dc.isDeleted())
                .map(PersonalColorDiagnosisColor::getColor)
                .collect(Collectors.toList());
        
        List<PersonalColorColor> nonMatchingColors = diagnosis.getDiagnosisColors().stream()
                .filter(dc -> "nonMatching".equals(dc.getType()) && !dc.isDeleted())
                .map(PersonalColorDiagnosisColor::getColor)
                .collect(Collectors.toList());

        return PersonalColorResponse.from(diagnosis, matchingColors, nonMatchingColors, jsonConverter);
    }

    @Transactional
    public void deleteDiagnosisForAdmin(Long diagnosisId) {
        PersonalColorDiagnosis diagnosis = findById(diagnosisId);
        
        diagnosis.softDelete();
        diagnosisRepository.save(diagnosis); // soft delete 후 저장
        
        // TODO: 이미지 삭제 정책 결정 필요
        // 컨투어링 가이드 이미지 삭제는 정책 결정 후 구현
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

