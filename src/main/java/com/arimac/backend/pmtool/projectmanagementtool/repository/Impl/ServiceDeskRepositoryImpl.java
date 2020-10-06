package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.ServiceTicket;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ServiceDeskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class ServiceDeskRepositoryImpl implements ServiceDeskRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceDeskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addServiceTicket(ServiceTicket serviceTicket) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ServiceTicket(ticketId,description,projectId,reporter,ticketStatus,severity,ticketCreation) VALUES (?,?,?,?,?,?,?)");
                preparedStatement.setString(1, serviceTicket.getTicketId());
                preparedStatement.setString(2, serviceTicket.getDescription());
                preparedStatement.setString(3, serviceTicket.getProjectId());
                preparedStatement.setString(4, serviceTicket.getReporter());
                preparedStatement.setInt(5, serviceTicket.getTicketStatus());
                preparedStatement.setInt(6, serviceTicket.getSeverity());
                preparedStatement.setTimestamp(7, serviceTicket.getTicketCreation());
                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
