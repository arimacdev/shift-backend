package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportMemberService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember.AddSupportMember;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_SupportMember;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SupportMemberRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SupportMemberServiceImpl implements SupportMemberService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SupportMemberRepository supportMemberRepository;
    private final UtilsService utilsService;

    public SupportMemberServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, SupportMemberRepository supportMemberRepository, UtilsService utilsService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.supportMemberRepository = supportMemberRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addSupportMember(String userId, AddSupportMember addSupportMember) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        Project_User projectUser = projectRepository.getProjectUser(addSupportMember.getProjectId(), userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (projectUser.getAssigneeProjectRole() != ProjectRoleEnum.owner.getRoleValue())
            return new ErrorMessage(ResponseMessage.USER_NOT_OWNER, HttpStatus.UNAUTHORIZED);
        if (userRepository.getUserByUserId(addSupportMember.getMemberId()) == null)
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_SupportMember supportMember = supportMemberRepository.getSupportMember(addSupportMember.getMemberId(), addSupportMember.getProjectId());
        if (supportMember == null){
            Project_SupportMember project_supportMember = new Project_SupportMember();
            project_supportMember.setProjectId(addSupportMember.getProjectId());
            project_supportMember.setAssigneeId(addSupportMember.getMemberId());
            project_supportMember.setAssignedBy(userId);
            project_supportMember.setAssignedAt(utilsService.getCurrentTimestamp());
            project_supportMember.setIsEnabled(true);
            supportMemberRepository.addSupportMember(project_supportMember);
        } else if (supportMember.getIsEnabled())
            return new ErrorMessage(ResponseMessage.SUPPORT_MEMBER_ALREADY_ASSIGNED, HttpStatus.CONFLICT);
        else
            supportMemberRepository.changeStatusOfSupportMember(addSupportMember.getMemberId(), addSupportMember.getProjectId(), true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
