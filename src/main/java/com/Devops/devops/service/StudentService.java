package com.Devops.devops.service;

import com.Devops.devops.dto.StudentDTO;
import org.springframework.http.ResponseEntity;

public interface StudentService {
     ResponseEntity<StudentDTO> addStudent(StudentDTO studentDTO);
}
