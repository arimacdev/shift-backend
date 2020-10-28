package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Project_SupportMember;

public interface SupportMemberRepository {
    Project_SupportMember getSupportMember(String memberId, String projectId);
    void addSupportMember(Project_SupportMember project_supportMember);
    void changeStatusOfSupportMember(String memberId, String projectId, boolean status);
}
