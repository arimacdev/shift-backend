package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.UserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class IdpUserServiceImpl implements IdpUserService {

    private static final Logger logger = LoggerFactory.getLogger(IdpUserServiceImpl.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String DEFAULT_PASSWORD = "123456";

    private static String clientAccessToken = null;
    private final RestTemplate restTemplate;

    public IdpUserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = String.format("%s:%s", ENVConfig.KEYCLOAK_ROLE_CLIENT_NAME, ENVConfig.KEYCLOAK_ROLE_CLIENT_SECRET);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }

    private HttpHeaders getIdpTokenHeader(){
        if (clientAccessToken == null)
            getClientAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    private void getClientAccessToken(){
        HttpHeaders httpHeaders = getHeader();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.KEYCLOAK_HOST);
        url.append("/auth/realms/");
        url.append(ENVConfig.KEYCLOAK_REALM);
        url.append("/protocol/openid-connect/token");
        logger.info("Access token URL : {}", url);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.POST, new HttpEntity<>(map, httpHeaders), String.class);
        clientAccessToken = new JSONObject(exchange.getBody()).getString("access_token");
    }

    @Override
    public JSONObject createUser(UserRegistrationDto userRegistrationDto, String UUID,  boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject payload = new JSONObject();
            payload.put("username", userRegistrationDto.getUserName());
            payload.put("enabled", true);
            payload.put("email", userRegistrationDto.getEmail());
            JSONArray credentials = new JSONArray();
            JSONObject user = new JSONObject();
            user.put("type", "password");
            user.put("value", userRegistrationDto.getPassword());
            user.put("temporary", true);
            credentials.put(user);
            payload.put("credentials", credentials);
            Map<String,String> attributes = new HashMap<>();
            attributes.put("userId", UUID);
            payload.put("attributes",attributes);

            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
            StringBuilder userCreateUrl = new StringBuilder();
            userCreateUrl.append(ENVConfig.KEYCLOAK_HOST);
            userCreateUrl.append("/auth/admin/realms/");
            userCreateUrl.append(ENVConfig.KEYCLOAK_REALM);
            userCreateUrl.append("/users");
            logger.info("User Create URL {}", userCreateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userCreateUrl.toString(), HttpMethod.POST, entity, String.class);
            //setTemporaryPassword(idpUserId, true);

            return getIdpUserId(httpHeaders, userRegistrationDto, true);

        }
            catch(HttpClientErrorException | HttpServerErrorException e) {
                String response = e.getResponseBodyAsString();
                logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                    getClientAccessToken();
                    return createUser(userRegistrationDto, UUID,false);
                } else if (e.getStatusCode() == HttpStatus.CONFLICT){
                    throw new PMException(new ErrorMessage(response, e.getStatusCode()));
                }
                throw new PMException(new ErrorMessage(response, e.getStatusCode()));
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new PMException(e);
              }
    }

    @Override
    public JSONObject getUserByIdpUserId(String idpUserId, boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, getIdpTokenHeader());
            StringBuilder userRetrieveUrl = new StringBuilder();
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_HOST);
            userRetrieveUrl.append("/auth/admin/realms/");
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_REALM);
            userRetrieveUrl.append("/users/");
            userRetrieveUrl.append(idpUserId);
            logger.info("User Retrieval Url : {}", userRetrieveUrl);
            ResponseEntity<String> idpUser = restTemplate.exchange(userRetrieveUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            return new JSONObject(idpUser.getBody());
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getUserByIdpUserId(idpUserId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }

    @Override
    public void updateUserPassword(String idpUserId, boolean firstRequest, String password) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject updatePasswordPayload = new JSONObject();
            JSONArray credentials = new JSONArray();
            JSONObject user = new JSONObject();
            user.put("type", "password");
            user.put("value", password);
            user.put("temporary", true);
            credentials.put(user);
            updatePasswordPayload.put("credentials", credentials);
            HttpEntity<Object> entity = new HttpEntity<>(updatePasswordPayload.toString(), httpHeaders);
            StringBuilder deactivateUrl = new StringBuilder();
            deactivateUrl.append(ENVConfig.KEYCLOAK_HOST);
            deactivateUrl.append("/auth/admin/realms/");
            deactivateUrl.append(ENVConfig.KEYCLOAK_REALM);
            deactivateUrl.append("/users/");
            deactivateUrl.append(idpUserId);
            logger.info("User Deactivate URL {}", deactivateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(deactivateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                updateUserPassword(idpUserId, false, password);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }



    @Override
    public void updateUserEmail(String idpUserId, String email, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject updatePayload = new JSONObject();
            updatePayload.put("email",email);

            HttpEntity<Object> entity = new HttpEntity<>(updatePayload.toString(), httpHeaders);
            StringBuilder userUpdateUrl = new StringBuilder();
            userUpdateUrl.append(ENVConfig.KEYCLOAK_HOST);
            userUpdateUrl.append("/auth/admin/realms/");
            userUpdateUrl.append(ENVConfig.KEYCLOAK_REALM);
            userUpdateUrl.append("/users/");
            userUpdateUrl.append(idpUserId);
            logger.info("User update URL {}", userUpdateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userUpdateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                updateUserEmail(idpUserId, email, false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void addUserAttributes(String idpUserId, String UUID, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject updatePayload = new JSONObject();
            Map<String,String> attributes = new HashMap<>();
            attributes.put("userId", UUID);
            updatePayload.put("attributes",attributes);
            HttpEntity<Object> entity = new HttpEntity<>(updatePayload.toString(), httpHeaders);
            StringBuilder userUpdateUrl = new StringBuilder();
            userUpdateUrl.append(ENVConfig.KEYCLOAK_HOST);
            userUpdateUrl.append("/auth/admin/realms/");
            userUpdateUrl.append(ENVConfig.KEYCLOAK_REALM);
            userUpdateUrl.append("/users/");
            userUpdateUrl.append(idpUserId);
            logger.info("User update URL {}", userUpdateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userUpdateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                addUserAttributes(idpUserId, UUID, false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }

    }

    @Override
    public void changeUserActiveSatatus(String idpUserId, boolean status, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject deactivatePayload = new JSONObject();
            deactivatePayload.put("enabled",status);

            HttpEntity<Object> entity = new HttpEntity<>(deactivatePayload.toString(), httpHeaders);
            StringBuilder deactivateUrl = new StringBuilder();
            deactivateUrl.append(ENVConfig.KEYCLOAK_HOST);
            deactivateUrl.append("/auth/admin/realms/");
            deactivateUrl.append(ENVConfig.KEYCLOAK_REALM);
            deactivateUrl.append("/users/");
            deactivateUrl.append(idpUserId);
            logger.info("User Deactivate URL {}", deactivateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(deactivateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                changeUserActiveSatatus(idpUserId, status,false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public JSONArray getAllRealmRoles(boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, getIdpTokenHeader());
            StringBuilder userRetrieveUrl = new StringBuilder();
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_HOST);
            userRetrieveUrl.append("/auth/admin/realms/");
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_REALM);
            userRetrieveUrl.append("/roles");
//            userRetrieveUrl.append(idpUserId);
            logger.info("User Retrieval Url : {}", userRetrieveUrl);
            ResponseEntity<String> idpUser = restTemplate.exchange(userRetrieveUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            return new JSONArray(idpUser.getBody());
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getAllRealmRoles(false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }

    @Override
    public void addRoleToUser(String idpUserId, UserRoleDto userRoleDto, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONArray addRolePayload = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("id", userRoleDto.getRoleId());
            role.put("name", userRoleDto.getRoleName());
            addRolePayload.put(role);

            HttpEntity<Object> entity = new HttpEntity<>(addRolePayload.toString(), httpHeaders);
            StringBuilder addRoleUrl = new StringBuilder();
            addRoleUrl.append(ENVConfig.KEYCLOAK_HOST);
            addRoleUrl.append("/auth/admin/realms/");
            addRoleUrl.append(ENVConfig.KEYCLOAK_REALM);
            addRoleUrl.append("/users/");
            addRoleUrl.append(idpUserId);
            addRoleUrl.append("/role-mappings/realm");
            logger.info("User Add Role URL {}", addRoleUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(addRoleUrl.toString(), HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                addRoleToUser(idpUserId, userRoleDto,false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void removerUserRole(String idpUserId, UserRoleDto userRoleDto, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONArray addRolePayload = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("id", userRoleDto.getRoleId());
            role.put("name", userRoleDto.getRoleName());
            addRolePayload.put(role);

            HttpEntity<Object> entity = new HttpEntity<>(addRolePayload.toString(), httpHeaders);
            StringBuilder addRoleUrl = new StringBuilder();
            addRoleUrl.append(ENVConfig.KEYCLOAK_HOST);
            addRoleUrl.append("/auth/admin/realms/");
            addRoleUrl.append(ENVConfig.KEYCLOAK_REALM);
            addRoleUrl.append("/users/");
            addRoleUrl.append(idpUserId);
            addRoleUrl.append("/role-mappings/realm");
            logger.info("User Add Role URL {}", addRoleUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(addRoleUrl.toString(), HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                addRoleToUser(idpUserId, userRoleDto,false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public JSONArray getAllUserRoleMappings(String idpUserId, boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, getIdpTokenHeader());
            StringBuilder roleMappingUrl = new StringBuilder();
            roleMappingUrl.append(ENVConfig.KEYCLOAK_HOST);
            roleMappingUrl.append("/auth/admin/realms/");
            roleMappingUrl.append(ENVConfig.KEYCLOAK_REALM);
            roleMappingUrl.append("/users/");
            roleMappingUrl.append(idpUserId);
            roleMappingUrl.append("/role-mappings/realm");
            logger.info("Role Mapping Url : {}", roleMappingUrl);
            ResponseEntity<String> userRoles = restTemplate.exchange(roleMappingUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            return new JSONArray(userRoles.getBody());
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getAllUserRoleMappings(idpUserId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }

    @Override
    public void removeAllAssociatedUserSessions(String idpUserId, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            HttpEntity<Object> entity = new HttpEntity<>(null, httpHeaders);
            StringBuilder sessionRemoveUrl = new StringBuilder();
            sessionRemoveUrl.append(ENVConfig.KEYCLOAK_HOST);
            sessionRemoveUrl.append("/auth/admin/realms/");
            sessionRemoveUrl.append(ENVConfig.KEYCLOAK_REALM);
            sessionRemoveUrl.append("/users/");
            sessionRemoveUrl.append(idpUserId);
            sessionRemoveUrl.append("/logout");
            logger.info("User Session Remove URL {}", sessionRemoveUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(sessionRemoveUrl.toString(), HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                removeAllAssociatedUserSessions(idpUserId, false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void deleteUserFromIdp(String idpUserId, boolean firstRequest) {
        try {
            HttpEntity<Object> userDeleteEntity = new HttpEntity<>(null, getIdpTokenHeader());
            StringBuilder userDeleteUrl = new StringBuilder();
            userDeleteUrl.append(ENVConfig.KEYCLOAK_HOST);
            userDeleteUrl.append("/auth/admin/realms/");
            userDeleteUrl.append(ENVConfig.KEYCLOAK_REALM);
            userDeleteUrl.append("/users/");
            userDeleteUrl.append(idpUserId);
            logger.info("User Delete URL {}", userDeleteUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userDeleteUrl.toString(), HttpMethod.DELETE, userDeleteEntity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
            getClientAccessToken();
            deleteUserFromIdp(idpUserId, false);
        }
        throw new PMException(e.getLocalizedMessage());
    } catch (Exception e) {
        throw new PMException(e.getMessage());
    }
    }


    private JSONObject getIdpUserId(HttpHeaders httpHeaders, UserRegistrationDto userRegistrationDto, boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, httpHeaders);
            StringBuilder userRetrieveUrl = new StringBuilder();
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_HOST);
            userRetrieveUrl.append("/auth/admin/realms/");
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_REALM);
            userRetrieveUrl.append("/users?username=");
            userRetrieveUrl.append(userRegistrationDto.getUserName());
            logger.info("User Retrieval Url : {}", userRetrieveUrl);
            ResponseEntity<String> userResult = restTemplate.exchange(userRetrieveUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            String response = userResult.getBody();
            JSONObject idpUser = new JSONObject();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String userName = jo.getString("username");
                if (userName.equals(userRegistrationDto.getUserName()))
                    idpUser = jo;
            }
            logger.info("Idp userID : {}", idpUser);
            return idpUser;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getIdpUserId(httpHeaders, userRegistrationDto, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    private void setTemporaryPassword(String idpUserId, boolean firstRequest){
        try {
            if (clientAccessToken == null)
                getClientAccessToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject credentailsPayload = new JSONObject();
            credentailsPayload.put("type", "password");
            credentailsPayload.put("value", DEFAULT_PASSWORD);
            credentailsPayload.put("temporary", true);

            HttpEntity<Object> entity = new HttpEntity<>(credentailsPayload.toString(), httpHeaders);
            StringBuilder passwordResetUrl = new StringBuilder();
            passwordResetUrl.append(ENVConfig.KEYCLOAK_HOST);
            passwordResetUrl.append("/auth/admin/realms/");
            passwordResetUrl.append(ENVConfig.KEYCLOAK_REALM);
            passwordResetUrl.append("/users/");
            passwordResetUrl.append(idpUserId);
            passwordResetUrl.append("/reset-password");

            logger.info("Password Reset URL {}", passwordResetUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(passwordResetUrl.toString(), HttpMethod.PUT, entity, String.class);
//            return getIdpUserId(httpHeaders, userRegistrationDto, true);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                setTemporaryPassword(idpUserId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }


}
