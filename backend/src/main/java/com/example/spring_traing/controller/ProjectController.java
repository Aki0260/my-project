package com.example.spring_traing.controller;

import com.example.spring_traing.DTO.ProjectCreateRequest;
import com.example.spring_traing.DTO.ProjectResponse;
import com.example.spring_traing.Service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //このクラスはHTTPリクエストを受け付けるコントローラです
@RequestMapping("/api/projects") //このコントローラが処理するリクエストのベースURLを指定しています。ここでは、"/api/projects"というパスにマッピングされています。
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping //POst/api/projectsに対するHTTP POSTリクエストを処理するメソッドであることを示しています。
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody ProjectCreateRequest request) {

        ProjectResponse response = projectService.createProject(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}") //GET/api/projects/{id}に対するHTTP GETリクエストを処理するメソッドであることを示しています。{id}は、URLパスの一部として指定されるプロジェクトのIDを表します。
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long id) {

        ProjectResponse response = projectService.getProject(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping //全てのプロジェクトを取得するためのHTTP GETリクエストを処理するメソッドであることを示しています。
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        List<ProjectResponse> responses = projectService.getAllProjects();

        return ResponseEntity.ok(responses);
    }
}