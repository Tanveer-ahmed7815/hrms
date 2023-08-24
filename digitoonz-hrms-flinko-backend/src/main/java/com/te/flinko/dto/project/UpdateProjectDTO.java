package com.te.flinko.dto.project;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.te.flinko.common.project.ProjectManagementConstants.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProjectDTO {
    @NotNull(message = PROJECT_ID_NOT_ENTERED)
    private Long projactId;
    @NotBlank(message = PROJECT_NAME_NOT_ENTERED)
    private String projectName;
    @NotNull(message = PROJECT_CLIENT_ID_NOT_ENTERED)
    private Long clientNameCciId;
    @NotNull(message = START_DATE_IS_NOT_ENTERED)
    private LocalDate startDate;
    private String projectDescription;
    @NotNull(message = PROJECT_MANAGER_ID_NOT_ENTERED)
    private Long projectManagerEpiId;
    @NotNull(message = REPORTING_MANAGER_ID_NOT_ENTERED)
    private Long reportingManagerEpiId;
}
