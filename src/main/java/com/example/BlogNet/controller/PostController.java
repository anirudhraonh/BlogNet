package com.example.BlogNet.controller;

import com.example.BlogNet.dto.PostRequestDTO;
import com.example.BlogNet.dto.PostResponseDTO;
import com.example.BlogNet.entity.Post;
import com.example.BlogNet.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    //GET all posts
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    //GET post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id){
        PostResponseDTO postResponseDTO = postService.findById(id);
        return ResponseEntity.ok(postResponseDTO);
    }

    //POST create new post
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO){
        PostResponseDTO postResponseDTO = postService.savePost(postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    //DELETE post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    //PAGINATION
    @GetMapping("/paginated")
    public ResponseEntity<Page<PostResponseDTO>> getPostsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
            ){
        Page<PostResponseDTO> posts = postService.getPostsPaginated(page,size,sortBy);
        return ResponseEntity.ok(posts);
    }
}
