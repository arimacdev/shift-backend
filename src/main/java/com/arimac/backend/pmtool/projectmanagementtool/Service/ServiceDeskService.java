package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddTicket;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.RequestKey;

public interface ServiceDeskService {
    Object createSupportTicket(AddTicket addTicket);
    Object requestNewServiceKey(RequestKey requestKey);
}
