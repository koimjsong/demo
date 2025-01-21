package com.example.demo.controller.home;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private final RestTemplate restTemplate;

    public HomeApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET 예제
    @GetMapping("/posts")
    public ResponseEntity<Object> posts() {
        Object response = restTemplate.getForObject(BASE_URL + "/posts", Object.class);
        return ResponseEntity.ok(response);
    }

    // POST 예제
    @PostMapping("/posts")
    public ResponseEntity<Object> createPost(@RequestBody Map<String, Object> payload) {
        Object response = restTemplate.postForObject(BASE_URL + "/posts", payload, Object.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT 예제
    @PutMapping("/posts/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") int id, @RequestBody Map<String, Object> payload) {
        String url = BASE_URL + "/posts/" + id;

        Object post = restTemplate.getForObject(url, Map.class);
        if (post != null) {
            restTemplate.put(url, payload); // PUT 요청
            return ResponseEntity.ok(Map.of("message", "Post updated successfully", "id", id, "payload", payload));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Post ID does not exist",
                    "id", id
            ));
        }
    }

    // DELETE 예제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable("id") int id) {
        String url = BASE_URL + "/posts/" + id;

        Object post = restTemplate.getForObject(url, Map.class);

        if (post != null) {
            restTemplate.delete(url);
            return ResponseEntity.ok(Map.of(
                    "message", "Post deleted successfully",
                    "id", id
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Post ID does not exist",
                    "id", id
            ));
        }
    }
}
