package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FolderService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderFileList;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Folder.FolderTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
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
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;

    public FolderServiceImpl(FolderRepository folderRepository, UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository, ProjectFileRepository projectFileRepository, TaskFileRepository taskFileRepository, UtilsService utilsService) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectFileRepository = projectFileRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createFolder(String projectId, String userId, FolderDto folderDto) {
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (folderDto.getParentFolder()!= null){
            Folder folder = folderRepository.getFolderById(folderDto.getParentFolder());
            if (folder == null)
                return new ErrorMessage(ResponseMessage.PARENT_FOLDER_NOT_EXISTS, HttpStatus.NOT_FOUND);
        }
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        Folder folder = new Folder();
        folder.setFolderId(utilsService.getUUId());
        folder.setProjectId(projectId);
        folder.setFolderName(folderDto.getFolderName());
        folder.setFolderCreator(userId);
        folder.setFolderCreatedAt(utilsService.getCurrentTimestamp());
        folder.setParentFolder(folderDto.getParentFolder());
        folder.setFolderType(FolderTypeEnum.PROJECT);

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
        Folder folder = folderRepository.getFolderById(folderId);
        if (folder == null)
            return new ErrorMessage(ResponseMessage.FOLDER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<Folder> folders = folderRepository.getSubFoldersOfFolder(folderId);
        List<?> files;
        if (folder.getFolderType().equals(FolderTypeEnum.TASK))
            files = taskFileRepository.getFolderTaskFiles(folderId);
        else
        files = projectFileRepository.getFolderProjectFiles(folderId);
        FolderFileList folderFileList = new FolderFileList();
        folderFileList.setFiles(files);
        folderFileList.setFolders(folders);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, folderFileList);
    }

    @Override
    public Object updateFolder(String userId, String projectId, String folderId, FolderDto folderDto) {
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        //TODO CHECK HERE
        Folder folder = folderRepository.getFolderById(folderId);
        if (folder == null)
            return new ErrorMessage(ResponseMessage.FOLDER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (folder.getFolderType().equals(FolderTypeEnum.TASK))
            return new ErrorMessage(ResponseMessage.CANNOT_UPDATE_TASK_FOLDER, HttpStatus.UNPROCESSABLE_ENTITY);
        folderRepository.updateFolder(folderDto, folderId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
