package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FileUploadService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskFileRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final AmazonS3 amazonS3Client;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;

    public FileUploadServiceImpl(AmazonS3 amazonS3Client, ProjectRepository projectRepository, TaskRepository taskRepository, TaskFileRepository taskFileRepository, UtilsService utilsService) {
        this.amazonS3Client = amazonS3Client;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object uploadFileToTask(String userId, String projectId, String taskId, FileUploadEnum fileType, MultipartFile multipartFiles) throws IOException {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null){
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
            List<String> fileUrlList = new ArrayList<>();
//            for (MultipartFile currentMultipartFile : multipartFiles){
                String taskUrl = fileQueue(multipartFiles, fileType);
                fileUrlList.add(taskUrl);
                TaskFile taskFile = new TaskFile();
                taskFile.setTaskFileId(utilsService.getUUId());
                taskFile.setTaskId(taskId);
                taskFile.setTaskFileName(multipartFiles.getOriginalFilename());
                taskFile.setTaskFileUrl(taskUrl);
                taskFile.setTaskFileCreator(userId);
                taskFile.setTaskFileDate(utilsService.getCurrentTimestamp());
                taskFileRepository.uploadTaskFile(taskFile);
//            }

            return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, fileUrlList);
        }

        private String fileQueue(MultipartFile multipartFile, FileUploadEnum fileType){
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile, fileType);
                return uploadFile(file, fileName);
            } catch (Exception e){
                throw new PMException("File upload failed", HttpStatus.BAD_REQUEST);
            }
        }

        private String generateFileName(MultipartFile multipartFile, FileUploadEnum fileType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fileType.toString());
        stringBuilder.append("_");
        stringBuilder.append(new Date().getTime());
        stringBuilder.append("_");
        stringBuilder.append(multipartFile.getOriginalFilename());

        return  stringBuilder.toString();
        }

        private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
            File convertFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convertFile;
        }

        private String uploadFile(File file, String filePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ENVConfig.AWS_BUCKET_NAME, filePath, file);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        putObjectRequest.withMetadata(metadata);
        amazonS3Client.putObject(putObjectRequest);
        String fileUploadedUrl = String.format("https://%s.s3-%s.amazonaws.com/%s", ENVConfig.AWS_BUCKET_NAME, ENVConfig.AWS_REGION, filePath);
        file.delete();
        logger.info("File uploaded to S3 successfully {}", fileUploadedUrl);
        return fileUploadedUrl;

        }
}
