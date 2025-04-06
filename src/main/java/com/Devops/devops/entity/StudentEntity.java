package com.Devops.devops.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "students")
public class StudentEntity {

    @Id
    private String id;
    private String name;
    private String department;
    private String section;
    private String year;
    private String joinedDate;
}
