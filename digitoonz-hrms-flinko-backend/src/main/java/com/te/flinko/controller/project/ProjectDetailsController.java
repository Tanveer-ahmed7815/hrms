package com.te.flinko.controller.project;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.project.ClientDropDownDTO;
import com.te.flinko.dto.project.CreateProjectDto;
import com.te.flinko.dto.project.UpdateProjectDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.project.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.te.flinko.common.project.ProjectManagementConstants.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/project")
@RestController
public class ProjectDetailsController extends BaseConfigController {
	
    private final ProjectDetailsService projectDetailsService;
    
    @GetMapping(path = "/client/drop-down")
	public ResponseEntity<SuccessResponse> getCompanyClients() {
		List<ClientDropDownDTO> companyClients = projectDetailsService.getCompanyClients(getCompanyId());
		if (!companyClients.isEmpty()) {
			log.info(CLIENTS_FETCHED);
			return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
					.message(CLIENTS_FETCHED).data(companyClients).build());
		} else {
			log.info(NO_CLIENTS_FOUND);
			return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
					.message(NO_CLIENTS_FOUND).data(companyClients).build());
		}
	}


    @GetMapping(path = "/")
    public ResponseEntity<SuccessResponse> getAllProjectDetailsBasicByCompanyId() {
        log.info("getAllProjectDetailsBasicById method, execution start");
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.builder()
                        .error(Boolean.FALSE)
                        .message(PROJECT_DETAILS_BASIC_INFO_PROVIDED)
                        .data(projectDetailsService.getAllProjectDetailsBasicByCompanyId(getCompanyId()))
                        .build());
    }

    @PostMapping(path = "/create-project")
    public ResponseEntity<SuccessResponse> createProject(@RequestBody @Valid CreateProjectDto createProjectDto) {
        log.info("createProject method, execution start");
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.builder()
                        .error(Boolean.FALSE)
                        .message(PROJECT_SAVED)
                        .data(projectDetailsService.createProject(createProjectDto, getCompanyId()))
                        .build());
    }

    @PutMapping(path = "/update-project")
    public ResponseEntity<SuccessResponse> updateProject(@RequestBody @Valid UpdateProjectDTO updateProjectDto) {
        log.info("updateProject method, execution start");
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.builder()
                        .error(Boolean.FALSE)
                        .message(PROJECT_UPDATED)
                        .data(projectDetailsService.updateProject(updateProjectDto))
                        .build());
    }

}
