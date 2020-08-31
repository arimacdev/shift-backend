package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AnalyticsService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.AnlyticsOverviewDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.AspectSummary;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectOverViewDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectStatusCountDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.PerformanceEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private static final String ALL = "all";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private int dateCount = 0;
    private String previousFromDate = null;
    private String previousToDate = null;

    public AnalyticsServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Object getOrgOverview(String userId, String from, String to) {
        Object error = this.dateCheck(from,to);
        if (error instanceof ErrorMessage)
            return error;
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        int userCount = userRepository.getActiveUserCount(from, to);
        int projectCount = projectRepository.getActiveProjectCount(from, to);
        int ActiveTaskCount = taskRepository.getActiveTaskCount(from, to);
        int closedTaskCount = taskRepository.getClosedTaskCount(from, to);

        AnlyticsOverviewDto anlyticsOverviewDto = new AnlyticsOverviewDto();
        anlyticsOverviewDto.setActiveUsers(userCount);
        anlyticsOverviewDto.setActiveProjects(projectCount);
        anlyticsOverviewDto.setActiveTasks(ActiveTaskCount);
        anlyticsOverviewDto.setClosedTasks(closedTaskCount);
        anlyticsOverviewDto.setTotalTasks(ActiveTaskCount+closedTaskCount);

        return new Response(ResponseMessage.SUCCESS, anlyticsOverviewDto);

    }

    @Override
    public Object getProjectOverview(String userId, String from, String to) {
        Object error = this.dateCheck(from,to);
        if (error instanceof ErrorMessage)
            return error;
        int projectCountCurrent = projectRepository.getActiveProjectCount(from, to);
        int projectCountPrevious = projectRepository.getActiveProjectCount(previousFromDate,previousToDate);//

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
//        } else {
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
            aspectSummary.setPercentage(givenRate.add(new BigDecimal("100")));
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

    private Object dateCheck(String from, String to){
        Date fromDate;
        Date toDate;
        if (!from.equals(ALL) || !to.equals(ALL)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                fromDate = dateFormat.parse(from);
                toDate = dateFormat.parse(to);
                if (fromDate.after(toDate) || toDate.before(fromDate))
                    return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
                this.dateCount = (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
                this.previousFromDate = dateFormat.format(new Date(fromDate.getTime() + this.dateCount*(1000*60*60*24L)));
                this.previousToDate = dateFormat.format(new Date(toDate.getTime() - this.dateCount*(1000*60*60*24L)));
            return null;
            } catch (ParseException e) {
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
            }
        } else return null;
    }

}
