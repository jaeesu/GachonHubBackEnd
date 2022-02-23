package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.contest.PostContestRepository;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.ContestRequestDto;
import com.example.gachonhub.payload.response.ContestListResponseDto;
import com.example.gachonhub.payload.response.ContestResponseDto;
import com.example.gachonhub.payload.response.ContestSimpleResponseDto;
import com.example.gachonhub.util.ErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.gachonhub.util.ErrorUtil.NOT_CORRECT_USER_ID;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final PostContestRepository contestRepository;
    private final SubCategoryService categoryService;
    private final AmazonS3Service amazonS3Service;

    public ContestListResponseDto getContestList(int page) {
        PageRequest request = PageRequest.of(page, 15, Sort.by("id").descending());
        Page<PostContest> contest = contestRepository.findAll(request);
        return new ContestListResponseDto(contest);
    }

    public ContestResponseDto getContest(Long id) {
        PostContest contest = findPostContestById(id);
        return ContestResponseDto.fromEntity(contest);
    }

    public List<ContestSimpleResponseDto> getListById(Long id) {
        List<PostContest> limitListById = contestRepository.getLimitListById(id);
        return limitListById.stream()
                .map(x -> ContestSimpleResponseDto.fromEntity(x))
                .collect(Collectors.toList());
    }

    public void createContest(User user, ContestRequestDto dto) {
        String url = amazonS3Service.uploadFile(dto.getImage());
        SubCategory category = categoryService.findById(dto.getCategory());
        PostContest postContest = dto.toEntity(user, category, url);
        contestRepository.save(postContest);

    }

    public void updateContest(User user, ContestRequestDto dto) {
        PostContest contest = findPostContestById(dto.getId());
        isCorrectAuthor(user.getId(), contest.getUser().getId());
        String url = amazonS3Service.uploadFile(dto.getImage());
        SubCategory category = categoryService.findById(dto.getCategory());
        dto.updateEntity(contest, category, url);
        contestRepository.save(contest);
    }

    public void deleteContest(User user, Long id) {
        PostContest contest = findPostContestById(id);
        isCorrectAuthor(user.getId(), contest.getUser().getId());
        contestRepository.deleteById(id);
    }


    public PostContest findPostContestById(Long id) {
        return contestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }
    public void isCorrectAuthor(Long userId, Long postAuthorId) {
        if (!userId.equals(postAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }

}
