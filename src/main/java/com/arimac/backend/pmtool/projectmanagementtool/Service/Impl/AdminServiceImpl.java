package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AdminService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.AddUserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.RealmRole;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Mobile;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MobileRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final IdpUserService idpUserService;
    private final UserRepository userRepository;
    private final MobileRepository mobileRepository;

    private final String NAME = "name";
    private final String ID = "id";

    public AdminServiceImpl(IdpUserService idpUserService, UserRepository userRepository, MobileRepository mobileRepository) {
        this.idpUserService = idpUserService;
        this.userRepository = userRepository;
        this.mobileRepository = mobileRepository;
    }

    @Override
    public Object getAllRealmRoles(String userId) {
        User admin = userRepository.getUserByUserId(userId);
        if (admin == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        JSONArray realmRoles = idpUserService.getAllRealmRoles(true);
        List<RealmRole> realmRoleList = new ArrayList<>();
        for (int i=0 ; i<realmRoles.length(); i++){
            JSONObject jsonRealmRole = realmRoles.getJSONObject(i);
            RealmRole realmRole = new RealmRole();
            realmRole.setId(jsonRealmRole.getString(ID));
            realmRole.setName(jsonRealmRole.getString(NAME));
            realmRoleList.add(realmRole);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, realmRoleList);
    }

    @Override
    public Object getMobileStatus(String platform, int version) {
        Mobile status = mobileRepository.getMobileStatus(platform);
        status.setCurrent_version(version);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, status);
    }

    @Override
    public Object addRoleToUser(String userId, AddUserRoleDto addUserRoleDto) {
        //CHECK ADMIN
        //Super Admin validation
        User admin = userRepository.getUserByUserId(userId);
        if (admin == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User user = userRepository.getUserByUserId(addUserRoleDto.getUserId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        idpUserService.addRoleToUser(user.getIdpUserId(), addUserRoleDto, true);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, addUserRoleDto);
    }

}
