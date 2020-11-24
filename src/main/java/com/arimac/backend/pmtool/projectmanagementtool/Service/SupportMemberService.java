package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember.AddSupportMember;

public interface SupportMemberService {
    Object addSupportMember(String user, AddSupportMember addSupportMember);
    Object getSupportMemberByProject(String userId, String projectId);
    Object removeSupportMemberFromProject(String user, String memberId, String projectId);
}
