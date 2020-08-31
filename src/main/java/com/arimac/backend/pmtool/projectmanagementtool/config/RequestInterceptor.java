package com.arimac.backend.pmtool.projectmanagementtool.config;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
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
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
            logger.info("request URI {}", request.getRequestURI());
            logger.info("jwt subject {}", jwt.getSubject());
            User user = userRepository.getUserByIdpUserId(jwt.getSubject());
            logger.info("INTERCEPTOR USER {}", user);
            JSONObject idpUser = idpUserService.getUserByIdpUserId(jwt.getSubject(), true);
            if (idpUser == null) {
                response.sendError(400);
                return false;
            }
            if (user == null) {
                try {
                    User newUser = new User();
                    newUser.setUserId(utilsService.getUUId());
                    newUser.setIdpUserId(jwt.getSubject());
                    newUser.setUsername(idpUser.getString("username"));
                    newUser.setFirstName(idpUser.getString("firstName"));
                    newUser.setLastName(idpUser.getString("lastName"));
                    newUser.setEmail(idpUser.getString("email"));
                    userRepository.createUser(newUser);
                    idpUserService.addUserAttributes(jwt.getSubject(), newUser.getUserId(), true);
                } catch (Exception e){
                    logger.info("Exception {}", e.getMessage());
                  //  idpUserService.deleteUserFromIdp(jwt.getSubject(), true);
                }

                //idpUserService.removeAllAssociatedUserSessions(jwt.getSubject(), true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", token);
                response.getWriter().write(String.valueOf(jsonObject));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(422);

                return false;
            } else if (!user.getIsActive()) {
                response.sendError(401);
                return false;
            } else {
                return  true;
            }
        } catch (Exception e) {
            logger.info("Exception", e);
//            if (jwt !=null)
//                idpUserService.deleteUserFromIdp(jwt.getSubject(), true);
            response.sendError(400);
            return false;
        }
    } else
        return false;
}


}
