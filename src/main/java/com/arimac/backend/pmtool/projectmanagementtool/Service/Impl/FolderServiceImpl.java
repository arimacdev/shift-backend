package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FolderService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderFileList;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectFileRepository projectFileRepository;
    private final UtilsService utilsService;

    public FolderServiceImpl(FolderRepository folderRepository, UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository, ProjectFileRepository projectFileRepository, UtilsService utilsService) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectFileRepository = projectFileRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createFolder(String projectId, String userId, FolderAddDto folderAddDto) {
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (folderAddDto.getParentFolder()!= null){
            Folder folder = folderRepository.getFolderById(folderAddDto.getParentFolder());
            if (folder == null)
                return new ErrorMessage(ResponseMessage.PARENT_FOLDER_NOT_EXISTS, HttpStatus.NOT_FOUND);
        }
        Folder folder = new Folder();
        folder.setFolderId(utilsService.getUUId());
        folder.setProjectId(projectId);
        folder.setFolderName(folderAddDto.getFolderName());
        folder.setFolderCreator(userId);
        folder.setFolderCreatedAt(utilsService.getCurrentTimestamp());
        folder.setParentFolder(folderAddDto.getParentFolder());

        folderRepository.createFolder(folder);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getMainFolders(String userId, String projectId) {
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        List<Folder> mainFolders = folderRepository.getMainFolders(projectId);
        List<ProjectFile> mainFiles = projectFileRepository.getMainProjectFiles(projectId);
        FolderFileList folderFileList = new FolderFileList();
        folderFileList.setFiles(mainFiles);
        folderFileList.setFolders(mainFolders);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, folderFileList);
    }

    @Override
    public Object getFilesFoldersOfFolder(String userId, String projectId, String folderId) {
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        List<Folder> folders = folderRepository.getSubFoldersOfFolder(folderId);
        List<ProjectFile> files = projectFileRepository.getFolderProjectFiles(folderId);
        FolderFileList folderFileList = new FolderFileList();
        folderFileList.setFiles(files);
        folderFileList.setFolders(folders);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, folderFileList);
    }
}
