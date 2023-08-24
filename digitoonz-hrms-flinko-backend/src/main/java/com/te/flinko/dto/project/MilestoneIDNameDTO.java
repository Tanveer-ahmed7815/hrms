package com.te.flinko.dto.project;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MilestoneIDNameDTO {
    private String mileStoneObjectId;
    private String milestoneName;
}
