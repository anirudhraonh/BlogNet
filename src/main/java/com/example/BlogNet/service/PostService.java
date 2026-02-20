package com.example.BlogNet.service;

import com.example.BlogNet.dto.PostRequestDTO;
import com.example.BlogNet.dto.PostResponseDTO;
import com.example.BlogNet.entity.Post;
import com.example.BlogNet.entity.User;
import com.example.BlogNet.exception.DuplicateUserException;
import com.example.BlogNet.exception.ResourceNotFoundExcecption;
import com.example.BlogNet.exception.UnauthorizedException;
import com.example.BlogNet.repository.PostRepository;
import com.example.BlogNet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    //Create post
    public PostResponseDTO savePost(PostRequestDTO postRequestDTO){
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setBody(postRequestDTO.getBody());
        post.setAuthor(getCurrentUser());

        Post savedPost = postRepository.save(post);
        return convertToResponseDTO(savedPost);
    }
    //Update post
    public PostResponseDTO updatePost(Long id,PostRequestDTO postRequestDTO) {
        //Fetch post
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExcecption("Post not found with id: "+id));

        //Authorization
        if(!post.getAuthor().getId().equals(getCurrentUser().getId())){
            throw new UnauthorizedException("You are not the author of post with id:" + id);
        }

        //Update
        post.setTitle(postRequestDTO.getTitle());
        post.setBody(postRequestDTO.getBody());
        postRepository.save(post);

        return convertToResponseDTO(post);
    }

    //Fetch All Posts
    public List<PostResponseDTO> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        for (Post post : posts){
            postResponseDTOList.add(convertToResponseDTO(post));
        }
        return postResponseDTOList;
    }

    // Find post
    public PostResponseDTO findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExcecption("Post not found with id: " + id));
        return convertToResponseDTO(post);
    }

    //Delete post
    public void deletePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExcecption("Post not found with id: "+ id));
        if (!getCurrentUser().getRole().equals(ROLE_ADMIN) && !post.getAuthor().getId().equals(getCurrentUser().getId()))
            throw new UnauthorizedException("You cannot delete someone else's post.");
        postRepository.deleteById(id);
    }

    //Pagination
    public Page<PostResponseDTO> getPostsPaginated(int page,int size, String sortBy){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.map((post) -> convertToResponseDTO(post));
    }

    //HELPER METHODS
    private PostResponseDTO convertToResponseDTO(Post post){
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setBody(post.getBody());
        postResponseDTO.setId(post.getId());
        postResponseDTO.setCreatedAt(post.getCreatedAt());
        postResponseDTO.setAuthorId(post.getAuthor().getId());
        postResponseDTO.setAuthorName(post.getAuthor().getUsername());

        return postResponseDTO;
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundExcecption("Unknown user."));
    }
}
