package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AdminService;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Mobile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MobileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final MobileRepository mobileRepository;

    public AdminServiceImpl(MobileRepository mobileRepository) {
        this.mobileRepository = mobileRepository;
    }

    @Override
    public Object getMobileStatus(String platform, int version) {
        Mobile status = mobileRepository.getMobileStatus(platform);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, status);
    }

}
