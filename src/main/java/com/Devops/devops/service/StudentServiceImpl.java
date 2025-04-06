package com.Devops.devops.service;

import com.Devops.devops.dto.StudentDTO;
import com.Devops.devops.entity.StudentEntity;
import com.Devops.devops.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<StudentDTO> addStudent(StudentDTO studentDTO) {
        StudentEntity studentEntity1 = StudentEntity.builder()
                .name(studentDTO.getName())
                .department(studentDTO.getDepartment())
                .year(studentDTO.getYear())
                .section(studentDTO.getSection())
                .joinedDate(LocalDateTime.now().toString())
                .build();
        StudentEntity resultEntity = studentRepository.save(studentEntity1);
        if(resultEntity!=null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(StudentDTO.builder()
                    .id(resultEntity.getId())
                    .name(resultEntity.getName())
                    .year(resultEntity.getYear())
                    .department(resultEntity.getDepartment())
                    .build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
