package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AnalyticsService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.AnlyticsOverviewDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectOverViewDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectStatusCountDto;
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
        int projectCount = projectRepository.getActiveProjectCount(from, to);
        List<ProjectStatusCountDto> projectStatusCount = projectRepository.getActiveProjectCountByStatus(from, to);
        ProjectStatusCountDto presalesStage = projectStatusCount
                .stream().filter(project -> Arrays.asList(
                        "presales",
                        "presalesPD",
                        "preSalesQS",
                        "preSalesN",
                        "preSalesC",
                        "preSalesL")
                .contains(project.getProjectStatus().toString()))
                .findAny().orElse(null);
        ProjectOverViewDto projectOverView = new ProjectOverViewDto();
        projectOverView.setTotalProjects(projectCount);
        if (presalesStage != null) {
            projectOverView.setLeadsPending(presalesStage.getProjectCount());
            projectOverView.setLeadsOngoing(projectCount - presalesStage.getProjectCount());
            MathContext mc = new MathContext(2);
            BigDecimal rate = BigDecimal.valueOf(projectOverView.getLeadsOngoing()).divide(BigDecimal.valueOf(projectCount), mc).multiply(new BigDecimal(100));
            projectOverView.setLeadConversion(rate);
        } else {
            projectOverView.setLeadsOngoing(projectCount);
            projectOverView.setLeadConversion(new BigDecimal("100.00"));
        }

        return new Response(ResponseMessage.SUCCESS, projectOverView);
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
                return null;
            } catch (ParseException e) {
                return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
            }
        } else return null;
    }
}
