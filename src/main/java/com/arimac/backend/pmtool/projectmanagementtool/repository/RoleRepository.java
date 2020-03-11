package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectRole;

public interface RoleRepository {
    ProjectRole getRolebyId(int projectRoleId);

    interface SubTaskRepository {
    }
}
