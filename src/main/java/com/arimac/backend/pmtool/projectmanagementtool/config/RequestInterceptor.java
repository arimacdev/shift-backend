package com.arimac.backend.pmtool.projectmanagementtool.config;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final IdpUserService idpUserService;
    private final UtilsService utilsService;

    public RequestInterceptor(UserRepository userRepository, IdpUserService idpUserService, UtilsService utilsService) {
        this.userRepository = userRepository;
        this.idpUserService = idpUserService;
        this.utilsService = utilsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(7);
            logger.info("token {}", token);
            try {
                DecodedJWT jwt = JWT.decode(token);
                logger.info("jwt subject {}", jwt.getSubject());
//                User user = userRepository.getUserByIdpUserId(jwt.getSubject());
//                logger.info("INTERCWEPTOR USER {}", user );
//                if (user == null){
//                    User newUser = new User();
//                    newUser.setUserId(utilsService.getUUId());
//                    user.setIdpUserId(jwt.getSubject());
////                    user.setUsername(userName);
////                    user.setFirstName(userRegistrationDto.getFirstName());
////                    user.setLastName(userRegistrationDto.getLastName());
////                    user.setEmail(userRegistrationDto.getEmail());
//                } else if (!user.getIsActive()){
//                    response.sendError(401);
//                } else
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
