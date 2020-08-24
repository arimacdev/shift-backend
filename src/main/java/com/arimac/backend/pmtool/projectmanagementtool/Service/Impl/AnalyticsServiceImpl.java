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
import org.joda.time.Days;
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
import java.util.concurrent.TimeUnit;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private static final String ALL = "all";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private int dateCount = 0;

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
        int projectCountGiven = projectRepository.getActiveProjectCount(from, to);
        int projectCountTotal = projectRepository.getActiveProjectCount(ALL,ALL);
        List<ProjectStatusCountDto> projectStatusCountGiven = projectRepository.getActiveProjectCountByStatus(from, to);
        List<ProjectStatusCountDto> projectStatusCountTotal = projectRepository.getActiveProjectCountByStatus(ALL, ALL);

        ProjectStatusCountDto presalesStage = projectStatusCountGiven
                .stream().filter(project -> Arrays.asList(
                        "presales",
                        "presalesPD",
                        "preSalesQS",
                        "preSalesN",
                        "preSalesC",
                        "preSalesL")
                .contains(project.getProjectStatus().toString()))
                .findAny().orElse(null);

        //ProjectOverViewDto projectOverView = new ProjectOverViewDto();

//        AspectSummary projectOverView = new AspectSummary();
//        projectOverView.setDays(presalesStage.getProjectCount());
//        projectOverView.setDays();
//
//
//        projectOverView.setTotalProjects();
//
//
//        projectOverView.setTotalProjects(projectCount);
//        if (presalesStage != null) {
//            projectOverView.setLeadsPending(presalesStage.getProjectCount());
//            projectOverView.setLeadsOngoing(projectCount - presalesStage.getProjectCount());
//            MathContext mc = new MathContext(2);
//            BigDecimal rate = BigDecimal.valueOf(projectOverView.getLeadsOngoing()).divide(BigDecimal.valueOf(projectCount), mc).multiply(new BigDecimal(100));
//            projectOverView.setLeadConversion(rate);
//        } else {
//            projectOverView.setLeadsOngoing(projectCount);
//            projectOverView.setLeadConversion(new BigDecimal("100.00"));
//        }

        return new Response(ResponseMessage.SUCCESS, null);
    }

//    private ProjectOverViewDto getProjectOverView(ProjectStatusCountDto preSalesCountGiven, ProjectStatusCountDto preSalesCountTotal){
//        if (projectStatusCount!= null){
//            AspectSummary aspectSummary = new AspectSummary();
//            aspectSummary.setDays(dateCount);
//            aspectSummary.setValue(projectStatusCount.getProjectCount());
//            aspectSummary.setPercentage();
//            aspectSummary.setPerformance();
//        } else {
//
//        }
//    }

    private AspectSummary getAspectSummary(ProjectStatusCountDto preSalesCountGiven, ProjectStatusCountDto preSalesCountTotal){
        AspectSummary aspectSummary = new AspectSummary();
        if (preSalesCountGiven != null &&  preSalesCountTotal != null){
            aspectSummary.setValue(preSalesCountGiven.getProjectCount());
            aspectSummary.setDays(this.dateCount);
            aspectSummary.setPercentage(getPercentage(preSalesCountGiven.getProjectCount(), preSalesCountTotal.getProjectCount()));
            if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) > 0){
                aspectSummary.setPerformance(PerformanceEnum.increase);
            } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) < 0){
                aspectSummary.setPerformance(PerformanceEnum.decrease);
            } else if (aspectSummary.getPercentage().compareTo(BigDecimal.ZERO) == 0){
                aspectSummary.setPerformance(PerformanceEnum.neutral);            }
        } else {

        }
        aspectSummary.setValue(preSalesCountGiven.getProjectCount());
        return null;
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

    private BigDecimal getPercentage(int value, int total){
        MathContext mc = new MathContext(2);
         return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(total), mc).multiply(new BigDecimal(100));
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
                this.dateCount = (int)( (fromDate.getTime() - toDate.getTime()) / (1000 * 60 * 60 * 24));
                return null;
            } catch (ParseException e) {
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
            }
        } else return null;
    }
}