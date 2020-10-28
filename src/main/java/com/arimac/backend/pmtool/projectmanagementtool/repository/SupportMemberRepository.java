package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember.SupportMemberDetails;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_SupportMember;

import java.util.List;

public interface SupportMemberRepository {
    Project_SupportMember getSupportMember(String memberId, String projectId);
    void addSupportMember(Project_SupportMember project_supportMember);
    void changeStatusOfSupportMember(String memberId, String projectId, boolean status);
    List<SupportMemberDetails> getSupportMemberByProject(String projectId);
}
