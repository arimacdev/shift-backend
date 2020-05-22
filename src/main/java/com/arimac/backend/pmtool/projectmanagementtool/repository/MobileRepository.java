package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Mobile;

public interface MobileRepository {
    Mobile getMobileStatus(String platform);
}
