package com.arimac.backend.pmtool.projectmanagementtool.config;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private static final String BEARER = "Bearer ";

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  IdpUserService idpUserService;
    @Autowired
    private  UtilsService utilsService;

    public RequestInterceptor() {
    }


//    public RequestInterceptor(UserRepository userRepository, IdpUserService idpUserService, UtilsService utilsService) {
//        this.userRepository = userRepository;
//        this.idpUserService = idpUserService;
//        this.utilsService = utilsService;
//    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(7);
            logger.info("token {}", token);
            try {
                DecodedJWT jwt = JWT.decode(token);
                logger.info("jwt subject {}", jwt.getSubject());
                User user = userRepository.getUserByIdpUserId(jwt.getSubject());
                logger.info("INTERCWEPTOR USER {}", user );
                if (user == null){
                    User newUser = new User();
                    newUser.setUserId(utilsService.getUUId());
                    newUser.setIdpUserId(jwt.getSubject());
//                    newUser.setUsername(jwt.getp);
//                    user.setFirstName(userRegistrationDto.getFirstName());
//                    user.setLastName(userRegistrationDto.getLastName());
//                    user.setEmail(userRegistrationDto.getEmail());
                    return true;
                } else if (!user.getIsActive()){
                    response.sendError(401);
                    return false;
                } else
                return  true;
            } catch (Exception e){
                logger.info("JWT Exception", e);
                response.sendError(400);
                return false;
            }
        } else {
            response.sendError(400);
            return false;
        }
    }


}