package com.example.spring_traing.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_traing.DTO.ProjectCreateRequest;
import com.example.spring_traing.DTO.ProjectResponse;
import com.example.spring_traing.DTO.ProjectUpdateRequest;
import com.example.spring_traing.Service.ProjectService;

@RestController //このクラスはHTTPリクエストを受け付けるコントローラです
@RequestMapping("/api/projects") //このコントローラが処理するリクエストのベースURLを指定しています。ここでは、"/api/projects"というパスにマッピングされています。
@CrossOrigin(origins = "http://localhost:5173") //このコントローラが処理するリクエストに対して、指定されたオリジン（ここではhttp://localhost:3000）からのクロスオリジンリクエストを許可することを示しています。
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

    //PUT/api/projects/{id}に対するHTTP PUTリクエストを処理するメソッドであることを示しています。
    // {id}は、URLパスの一部として指定されるプロジェクトのIDを表します。
    @PutMapping("/{id}")
    //<ProjectResponse>は、HTTPレスポンスのボディに含まれるデータの型を指定しています。ここでは、ProjectResponse型のデータが返されることを示しています。
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectUpdateRequest request) {

        ProjectResponse response
                = projectService.updateProject(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping //全てのプロジェクトを取得するためのHTTP GETリクエストを処理するメソッドであることを示しています。
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        List<ProjectResponse> responses = projectService.getAllProjects();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
