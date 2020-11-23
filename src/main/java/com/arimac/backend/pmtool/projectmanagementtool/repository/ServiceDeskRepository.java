package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.ServiceTicket;

public interface ServiceDeskRepository {
    void addServiceTicket(ServiceTicket serviceTicket);
}
