package com.example.spring_traing.Service;

import com.example.spring_traing.DTO.ProjectCreateRequest;
import com.example.spring_traing.DTO.ProjectResponse;
import com.example.spring_traing.Entity.Project;
import com.example.spring_traing.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService { //このサービス層では、プロジェクトの作成、取得、一覧表示などのビジネスロジックを実装しています。

    //このserviceがprojectRepositoryを使えるようにするための宣言
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //新しいプロジェクトを作成するメソッド
    public ProjectResponse createProject(ProjectCreateRequest request) {

        Project project = new Project(); //新しいProjectエンティティのインスタンスを空で作成

        //以下では、リクエストから取得した各プロジェクトの属性をエンティティに移し替えています。
        project.setName(request.getName()); //リクエストから取得したプロジェクト名を設定
        project.setDescription(request.getDescription());
        project.setGoal(request.getGoal());
        project.setMotivation(request.getMotivation());
        project.setStatus(request.getStatus());
        project.setPriority(request.getPriority());
        project.setStartDate(request.getStartDate());
        project.setPlannedEndDate(request.getPlannedEndDate());
        project.setPlannedHours(request.getPlannedHours());

        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        //プロジェクトエンティティをデータベースに保存し、保存されたエンティティを返す
        Project savedProject = projectRepository.save(project);

        //保存されたプロジェクトエンティティをレスポンスDTOに変換して返す
        return toResponse(savedProject);
    }

    public ProjectResponse getProject(Long id) { //指定されたIDのプロジェクトを取得するメソッド

        Project project = projectRepository.findById(id) //指定されたIDでプロジェクトを検索
                .orElseThrow(() -> new RuntimeException("Project not found: " + id)); //もしプロジェクトが見つからなかった場合は例外をスロー

        return toResponse(project);
    }

    public List<ProjectResponse> getAllProjects() { //すべてのプロジェクトを取得するメソッド

        //1件ずつProjectエンティティをProjectResponse DTOに変換し、リストとして返す
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProjectResponse toResponse(Project project) { //ProjectエンティティをProjectResponse DTOに変換するメソッド

        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setGoal(project.getGoal());
        response.setMotivation(project.getMotivation());
        response.setStatus(project.getStatus());
        response.setPriority(project.getPriority());
        response.setStartDate(project.getStartDate());
        response.setPlannedEndDate(project.getPlannedEndDate());
        response.setActualEndDate(project.getActualEndDate());
        response.setPlannedHours(project.getPlannedHours());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        return response;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
