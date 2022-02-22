package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.category.SubCategoryRepository;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.contest.PostContestRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.ContestRequestDto;
import com.example.gachonhub.payload.response.ContestListResponseDto;
import com.example.gachonhub.payload.response.ContestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        Page<PostContest> contestList = contestRepository.findAll(request);
        return ContestListResponseDto.fromPagable(contestList);
    }

    public ContestResponseDto getContest(Long id) {
        PostContest contest = findPostContestById(id);
        contest.updateHit();
        return ContestResponseDto.fromContest(contest);
    }

    public void createContest(User user, ContestRequestDto dto) {
        SubCategory subCategoryById = categoryService.findSubCategoryById(dto.getId());
        String url = amazonS3Service.uploadFile(dto.getImage());
        PostContest contest = dto.toEntity(user, subCategoryById, url);
        contestRepository.save(contest);
    }

    public void updateContest(User user, ContestRequestDto dto) {
        PostContest contest = findPostContestById(dto.getId());
        isCorrectAuthor(user.getId(), contest.getUser().getId());
        SubCategory subCategoryById = categoryService.findSubCategoryById(dto.getId());
        String url = amazonS3Service.uploadFile(dto.getImage());
        dto.updateContest(contest, subCategoryById, url);
        contestRepository.save(contest);
    }


    public void deleteContest(User user, Long id) {
        PostContest contest = findPostContestById(id);
        isCorrectAuthor(user.getId(), contest.getId());
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
