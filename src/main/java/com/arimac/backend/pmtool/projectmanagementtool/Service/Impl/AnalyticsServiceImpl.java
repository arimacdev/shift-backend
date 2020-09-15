package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AnalyticsService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Task.TaskRateResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserActivityDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserDetailedAnalysis;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserNumberDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private static final String ALL = "all";
    private static final String ID = "id";
    private static final String ORGANIZATION_ADMIN	 = "ORGANIZATION_ADMIN";
    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String ADMIN = "ADMIN";
    private static final String WORKLOAD = "WORKLOAD";
    private static final String USER	 = "USER";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ActivityLogRepository activityLogRepository;
    private final IdpUserService idpUserService;
    private int dateCount = 0;
    private String previousFromDate = null;
    private String previousToDate = null;

    public AnalyticsServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository, ActivityLogRepository activityLogRepository, IdpUserService idpUserService, IdpUserService idpUserService1) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.activityLogRepository = activityLogRepository;
        this.idpUserService = idpUserService1;
    }

    @Override
    public Object getOrgOverview(String userId, String from, String to) {
        Object error = this.dateCheck(from,to,false);
        if (error instanceof ErrorMessage)
            return error;
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        UserNumberDto userCount = userRepository.getActiveUserCount(from, to);
        ProjectNumberDto projectNumber = projectRepository.getProjectNumbers(from, to);
        int ActiveTaskCount = taskRepository.getActiveTaskCount(from, to);
        int closedTaskCount = taskRepository.getClosedTaskCount(from, to);

        AnlyticsOverviewDto anlyticsOverviewDto = new AnlyticsOverviewDto();
        anlyticsOverviewDto.setActiveUsers(userCount.getTotalUsers());
        anlyticsOverviewDto.setSlackActivatedUsers(userCount.getSlackActivated());
        anlyticsOverviewDto.setActiveProjects(projectNumber.getActiveProjects());
        anlyticsOverviewDto.setTotalProjects(projectNumber.getTotalProjects());
        anlyticsOverviewDto.setActiveTasks(ActiveTaskCount);
        anlyticsOverviewDto.setClosedTasks(closedTaskCount);
        anlyticsOverviewDto.setTotalTasks(ActiveTaskCount+closedTaskCount);

        return new Response(ResponseMessage.SUCCESS, anlyticsOverviewDto);

    }

    @Override
    public Object getProjectOverview(String userId, String from, String to) {
        Object error = this.dateCheck(from,to,true);
        if (error instanceof ErrorMessage)
            return error;
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        int projectCountCurrent = projectRepository.getActiveProjectCount(from, to);
        int projectCountPrevious = projectRepository.getActiveProjectCount(previousFromDate,previousToDate);

        ProjectStatusCountDto pendingCurrent = this.getPreSalesProjectStatusCount(projectRepository.getActiveProjectCountByStatus(from, to));
        ProjectStatusCountDto pendingPrevious = this.getPreSalesProjectStatusCount(projectRepository.getActiveProjectCountByStatus(previousFromDate, previousToDate));
        if (pendingCurrent == null)
            pendingCurrent = new ProjectStatusCountDto();
        if (pendingPrevious == null)
            pendingPrevious = new ProjectStatusCountDto();

        ProjectOverViewDto projectOverView = new ProjectOverViewDto();
        projectOverView.setTotalProjects(getAspectSummary(projectCountCurrent, projectCountPrevious));
//        if (pendingCurrent!= null && pendingPrevious != null){
//            projectOverView.setLeadsPending(getAspectSummary(pendingCurrent.getProjectCount(), pendingPrevious.getProjectCount()));
//            projectOverView.setLeadsOngoing(getAspectSummary((projectCountCurrent - pendingCurrent.getProjectCount()), (projectCountPrevious - pendingPrevious.getProjectCount())));
//            projectOverView.setLeadConversion(getConversionPercentage((projectCountCurrent - pendingCurrent.getProjectCount()), projectCountCurrent, (projectCountPrevious - pendingPrevious.getProjectCount()), projectCountPrevious ));
//        }
//         if (pendingCurrent == null && pendingPrevious == null){
//            projectOverView.setLeadsPending(new AspectSummary<>(this.dateCount));// check this case
//            projectOverView.setLeadsOngoing(new AspectSummary<>(this.dateCount));
//            projectOverView.setLeadConversion(new AspectSummary<>(this.dateCount));
//        } else {
             projectOverView.setLeadsPending(getAspectSummary(pendingCurrent.getProjectCount(), pendingPrevious.getProjectCount()));
             projectOverView.setLeadsOngoing(getAspectSummary((projectCountCurrent - pendingCurrent.getProjectCount()), (projectCountPrevious - pendingPrevious.getProjectCount())));
             projectOverView.setLeadConversion(getConversionPercentage((projectCountCurrent - pendingCurrent.getProjectCount()), projectCountCurrent, (projectCountPrevious - pendingPrevious.getProjectCount()), projectCountPrevious ));

//         }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, projectOverView);
    }

    @Override
    public Object getProjectSummary(String userId, String from, String to, Set<String> status, Set<String> project, ProjectSummaryTypeEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex) {
        Object error = this.dateCheck(from,to,false);
        if (error instanceof ErrorMessage)
            return error;
//        if ((key == null || key.isEmpty()) )
//            return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
        if ((status.size() > 1 && status.contains(ALL)) || (project.size() > 1 && status.contains(ALL)))
            return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
        for (String projectStatus: status ){
            if (!ProjectStatusEnum.contains(projectStatus) && !projectStatus.equals(ALL))
                return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
        }
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return new ErrorMessage("Invalid Start/End Index", HttpStatus.BAD_REQUEST);
        int limit = endIndex - startIndex;
        if (limit > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
       List<ProjectSummaryDto> summaryList = projectRepository.getProjectSummary(from, to, status, project, orderBy, orderType,startIndex, limit);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, summaryList);
    }

    @Override
    public Object getDetailedProjectDetails(String userId, String from, String to, ProjectDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex) {
        Object error = this.dateCheck(from,to,false);
        if (error instanceof ErrorMessage)
            return error;
        int limit = endIndex - startIndex;
        if (limit > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        LinkedHashMap<String, ProjectDetailAnalysis> projectDetailsMap =  projectRepository.getDetailedProjectDetails(from, to, orderBy, orderType, startIndex, limit);
        for (Map.Entry<String, ProjectDetailAnalysis> projectMap : projectDetailsMap.entrySet()){
        List<String> projectTaskIds = projectRepository.getProjectTaskIds(projectMap.getKey());
            if (!projectTaskIds.isEmpty()) {
                int changeStatusCount = activityLogRepository.getStatusChangeTaskCountOfTasks(projectTaskIds, from, to);
                int reOpenCount = activityLogRepository.getReOpenCountOfTasks(projectTaskIds, from, to);
                projectMap.getValue().setEngagement(projectMap.getValue().getTaskCount() + projectMap.getValue().getClosedCount() * 5 + changeStatusCount * 4 - reOpenCount * 5);
            }
        }
        List<ProjectDetailAnalysis> list = new ArrayList<>(projectDetailsMap.values());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, list);
    }

    @Override
    public Object getTaskRate(String userId, String from, String to, ChartCriteriaEnum criteria) {
        Object error = this.dateCheck(from,to,false);
        if (error instanceof ErrorMessage)
            return error;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(from, formatter);
        LocalDate endDate = LocalDate.parse(to, formatter);

        HashMap<String, Integer> taskCreationMap = taskRepository.getTaskCreationByDate(from, to, criteria);
        HashMap<String, Integer> taskCompletionMap = activityLogRepository.getClosedTaskCount(from, to, criteria);

        List<TaskRateResponse> rateResponses = new ArrayList<>();

        switch (criteria){
            case DAY:
                endDate = endDate.plusDays(1);
                while (!startDate.equals(endDate)) {
                    TaskRateResponse taskRateResponse = new TaskRateResponse();
                    taskRateResponse.setDate(startDate.toString());
                    this.getTaskRateResponse(taskCreationMap, taskCompletionMap, startDate.toString(), taskRateResponse);
                    rateResponses.add(taskRateResponse);
                    startDate = startDate.plusDays(1);
                }
                break;
            case MONTH:
                while (startDate.getMonthValue() <= endDate.getMonthValue()){
                    TaskRateResponse taskRateResponse = new TaskRateResponse();
                    String yearMonth = startDate.getYear() + "-" + startDate.toString().split("-")[1];
                    taskRateResponse.setDate(yearMonth);
                    this.getTaskRateResponse(taskCreationMap, taskCompletionMap, yearMonth, taskRateResponse);
                    rateResponses.add(taskRateResponse);
                    startDate = startDate.plusMonths(1);
                }
                break;
            case YEAR:
                while (startDate.getYear() <= endDate.getYear()){
                    TaskRateResponse taskRateResponse = new TaskRateResponse();
                    taskRateResponse.setDate(String.valueOf(startDate.getYear()));
                    this.getTaskRateResponse(taskCreationMap, taskCompletionMap, String.valueOf(startDate.getYear()), taskRateResponse);
                    rateResponses.add(taskRateResponse);
                    startDate = startDate.plusYears(1);
                }

        }
       return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, rateResponses);
    }

    @Override
    public Object getMemberActivity(String userId, String from, String to, ChartCriteriaEnum criteria) {
        Object error = this.dateCheck(from,to,false);
        if (error instanceof ErrorMessage)
            return error;
        if (userRepository.getUserByUserId(userId) == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(from, formatter);
        LocalDate endDate = LocalDate.parse(to, formatter);

        HashMap<String,UserActivityDto> userActivityMap = userRepository.getUserActivity(from,to,criteria);
        List<UserActivityDto> userActivityDtos = new ArrayList<>();

        switch (criteria){
            case DAY:
                endDate = endDate.plusDays(1);
                while (!startDate.equals(endDate)) {
                    if (userActivityMap.containsKey(startDate.toString()))
                        userActivityDtos.add(userActivityMap.get(startDate.toString()));
                    else {
                        UserActivityDto userActivity = new UserActivityDto();
                        userActivity.setDate(startDate.toString());
                        userActivityDtos.add(userActivity);
                    }
                    startDate = startDate.plusDays(1);
                }
                break;
            case MONTH:
                while (startDate.getMonthValue() <= endDate.getMonthValue()){
                    String yearMonth = startDate.getYear() + "-" + startDate.toString().split("-")[1];
                    if (userActivityMap.containsKey(yearMonth))
                        userActivityDtos.add(userActivityMap.get(yearMonth));
                    else {
                        UserActivityDto userActivity = new UserActivityDto();
                        userActivity.setDate(yearMonth);
                        userActivityDtos.add(userActivity);
                    }
                    startDate = startDate.plusMonths(1);
                }
                break;
            case YEAR:
                while (startDate.getYear() <= endDate.getYear()){
                    if (userActivityMap.containsKey(String.valueOf(startDate.getYear())))
                        userActivityDtos.add(userActivityMap.get(String.valueOf(startDate.getYear())));
                    else {
                        UserActivityDto userActivity = new UserActivityDto();
                        userActivity.setDate(String.valueOf(startDate.getYear()));
                        userActivityDtos.add(userActivity);
                    }
                    startDate = startDate.plusYears(1);
                }

        }

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userActivityDtos);
    }


    @Override
    public Object getDetailedUserDetails(String userId, UserDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex, Set<String> userList) {
        int limit = endIndex - startIndex;
        if (limit > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userRepository.getUserByUserId(userId);
        if (userList.size() > 1 && userList.contains(ALL))
            return new ErrorMessage(ResponseMessage.INVALID_FILTER_QUERY, HttpStatus.BAD_REQUEST);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<UserDetailedAnalysis> detailedUserList = userRepository.getDetailedUserDetails(orderBy, orderType, startIndex, limit, userList);
        for (UserDetailedAnalysis userDetail : detailedUserList){
            try {
                setUserRole(idpUserService.getAllUserRoleMappings(userDetail.getIdpUserId(), true), userDetail);
            } catch (Exception e){
                userDetail.setUserRole(USER);
            }
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, detailedUserList);
    }

    private void setUserRole(JSONArray roleList, UserDetailedAnalysis userDetail){
        List<String> userRoleList = new ArrayList<>();
        for (int i = 0; i < roleList.length(); i++) {
            JSONObject userRole = roleList.getJSONObject(i);
            userRoleList.add(userRole.getString(ID));
        }
        if (userRoleList.contains(ORGANIZATION_ADMIN))
            userDetail.setUserRole(ORGANIZATION_ADMIN);
        else if (userRoleList.contains(SUPER_ADMIN))
            userDetail.setUserRole(SUPER_ADMIN);
        else if (userRoleList.contains(ADMIN))
            userDetail.setUserRole(ADMIN);
        else if (userRoleList.contains(WORKLOAD))
            userDetail.setUserRole(WORKLOAD);
        else userDetail.setUserRole(USER);
    }


    private void getTaskRateResponse(HashMap<String, Integer> taskCreationMap, HashMap<String, Integer> taskCompletionMap , String filteredDate, TaskRateResponse taskRateResponse){
        if (taskCreationMap.containsKey(filteredDate)){
            taskRateResponse.setTaskCreationCount(taskCreationMap.get(filteredDate));
        }
        if (taskCompletionMap.containsKey(filteredDate)){
            taskRateResponse.setTaskCompletionCount(taskCompletionMap.get(filteredDate));
        }
    }


    private AspectSummary<Integer> getAspectSummary(int current, int previous){
        AspectSummary<Integer> aspectSummary = new AspectSummary<>(current);
        aspectSummary.setDays(this.dateCount);
//        if (current != null &&  preSalesCountTotal != null){
            aspectSummary.setValue(current);
            aspectSummary.setPercentage(getPercentage(current,previous));
            if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) > 0){
                aspectSummary.setPerformance(PerformanceEnum.increase);
            } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) < 0){ // not invoked
                aspectSummary.setPerformance(PerformanceEnum.decrease);
            } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) == 0){
                aspectSummary.setPerformance(PerformanceEnum.neutral);
            }
//        } else {;
//            aspectSummary.setPerformance(PerformanceEnum.neutral);
//        }
        return aspectSummary;
    }

    private AspectSummary<BigDecimal> getConversionPercentage(int givenOngoing, int givenTotalProjects, int previousOngoing, int previousTotalProjects){
        AspectSummary<BigDecimal> aspectSummary = new AspectSummary<>();
        aspectSummary.setDays(this.dateCount);
        MathContext mc = new MathContext(2);
        BigDecimal givenRate = getPercentage(givenOngoing, givenTotalProjects);
        BigDecimal previousRate = getPercentage(previousOngoing, previousTotalProjects);
        aspectSummary.setValue(givenRate);
        if (previousRate.compareTo(BigDecimal.ZERO) == 0)
            aspectSummary.setPercentage(givenRate.add(new BigDecimal("100"))); //Fix here
        else if (givenRate.compareTo(BigDecimal.ZERO) == 0)
            aspectSummary.setPercentage(previousRate.negate().add(new BigDecimal("-100")));
        else {
            BigDecimal performanceRate = givenRate.divide(previousRate, mc).multiply(new BigDecimal(100));
            aspectSummary.setPercentage(performanceRate);
        }
        if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) > 0){
            aspectSummary.setPerformance(PerformanceEnum.increase);
        } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) < 0){ // not invoked
            aspectSummary.setPerformance(PerformanceEnum.decrease);
        } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) == 0){
            aspectSummary.setPerformance(PerformanceEnum.neutral);
        }

        return aspectSummary;

    }

    private ProjectStatusCountDto getPreSalesProjectStatusCount(List<ProjectStatusCountDto> projectCountList){
        return  projectCountList
                .stream().filter(project -> Arrays.asList(
                        "presales",
                        "presalesPD",
                        "preSalesQS",
                        "preSalesN",
                        "preSalesC",
                        "preSalesL")
                        .contains(project.getProjectStatus().toString()))
                .findAny().orElse(null);
    }

    private BigDecimal getPercentage(int current, int previous){
        if (previous == 0 && current == 0)
            return new BigDecimal("0.00");
        else if (previous == 0 && current > 0)
            return new BigDecimal("100.00");
        MathContext mc = new MathContext(2);
         return BigDecimal.valueOf(current).divide(BigDecimal.valueOf(previous), mc).multiply(new BigDecimal(100)); //check here
    }

    private Object dateCheck(String from, String to, boolean setDate){
        Date fromDate;
        Date toDate;
        if (!from.equals(ALL) || !to.equals(ALL)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                fromDate = dateFormat.parse(from);
                toDate = dateFormat.parse(to);
                if (fromDate.after(toDate) || toDate.before(fromDate))
                    return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
                if (setDate) {
                    this.dateCount = (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
                    this.previousFromDate = dateFormat.format(new Date(fromDate.getTime() - this.dateCount * (1000 * 60 * 60 * 24L)));
                    this.previousToDate = dateFormat.format(new Date(toDate.getTime() - this.dateCount * (1000 * 60 * 60 * 24L)));
                }
            return null;
            } catch (ParseException e) {
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
            }
        } else return null;
    }

}
