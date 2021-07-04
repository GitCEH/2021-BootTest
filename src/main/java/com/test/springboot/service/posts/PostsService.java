package com.test.springboot.service.posts;

import com.test.springboot.domain.posts.Posts;
import com.test.springboot.domain.posts.PostsRepository;
import com.test.springboot.web.dto.PostsListResponseDto;
import com.test.springboot.web.dto.PostsResponseDto;
import com.test.springboot.web.dto.PostsSaveRequestDto;
import com.test.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        //데이터베이스에 쿼리를 날리는 부분이 없는 이유는 JPA의 영속성 컨텍스트 때문임
        //영속성 컨텍스트 : 엔티티를 영구 저장하는 환경 (일종의 논리적 개념)
        //Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다. => 더티 체킹
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(),requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    //readOnly=true 를 주면 트랙잭션 범위는 유지하되, 조회기능만 남겨서 조회속도가 개선되기 때문에 등록,수정,삭제 기능이 없는 서비스 메소드에서 유용함
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    //글 삭제 API
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        postsRepository.delete(posts);
    }
}
