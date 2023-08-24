package com.te.flinko.dto.project;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectDetailsBasicDTO {
    private Long projectId;
    private String  projectOwner;
    private String projectName;
    private String projectDescription;
    private String clientName;
    private LocalDate startDate;
    private String projectManager;
    private String status;
    private String reportingManager;
}
