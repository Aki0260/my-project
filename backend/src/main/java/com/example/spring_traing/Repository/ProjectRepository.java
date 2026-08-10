package com.example.spring_traing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_traing.Entity.Project;
//JpaRepository<Project, Long>は、ProjectというEntityを操作し、LongはIDの型としている。これだけで、springが基本的なDB操作を用意してくれる
public interface ProjectRepository extends JpaRepository<Project, Long>{ 
}
