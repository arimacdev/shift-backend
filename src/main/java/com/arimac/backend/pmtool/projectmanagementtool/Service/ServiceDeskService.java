package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddTicket;

public interface ServiceDeskService {
    Object createSupportTicket(AddTicket addTicket);
}
