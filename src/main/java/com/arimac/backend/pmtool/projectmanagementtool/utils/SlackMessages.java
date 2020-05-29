package com.arimac.backend.pmtool.projectmanagementtool.utils;

public class SlackMessages {
    public static final String TASK_REMINDER_TITLE = "Task Reminder";
    public static final String TASK_ASSIGNMENT_TITLE = "New Task Assignment";
    public static final String SUB_TASK_CREATION_TITLE = "New Subtask Creation";
    public static final String SUB_TASK_DELETION_TITLE = "Subtask Deletion";
    public static  final String TASK_ASSIGNEE_UPDATE_TITLE ="Task Assignment Modification";
    public static  final String TASK_ASSIGNEE_DELETE_TITLE ="Task Assignment Modification";
    public static  final String TASK_FILE_UPLOAD_NOTIFICATION_TITLE ="Task File Upload";
    public static  final String TASK_MODIFICATION_TITLE ="Task Content Modification";
    public static  final String SUB_TASK_MODIFICATION_TITLE ="SubTask Content Modification";
    public static  final String TASK_REMINDER_GREETING =" A Reminder for the following Task";
    public static  final String TASK_ASSIGNMENT_GREETING =" You have been assigned to the following Task";
    public static  final String ADDRESS_GREETING =":wave: Hey There ";

    public static  final String SUB_TASK_ASSIGNMENT_GREETING =" A SubTask has been created for the following task";
    public static  final String SUB_TASK_MODIFICATION_GREETING =" A Subtask assigned to you has been modified";
    public static  final String SUB_TASK_DELETION_GREETING =" A Subtask assigned to you has been deleted";



    public static  final String TASK_ASSIGNMENT_TRANSITION_GREETING =" The following Task Assignment has been modified and has been assigned to you";
    public static  final String TASK_FILE_UPLOAD_GREETING =" A File has been uploaded to a task assigned to you";
    public static  final String TASK_MODIFICATION_GREETING =" A Task assigned to you has been modified";
    public static  final String TASK_DELETION_GREETING =" A Task assigned to you has been deleted";
    public static  final String TASK_ICON =":gear:   *Task:* ";
    public static  final String SUB_TASK_ICON =":clipboard:   *Subtask:* ";
    public static  final String PROJECT_ICON ="\n:briefcase:   *Project:* ";
    public static  final String TRANSITION_ICON = "\n:zap:   *Transition:* ";
    public static  final String ARROW_ICON = "  ⟶  ";
    public static  final String ASSIGNED_BY_ICON ="\n:speaking_head_in_silhouette:   *Assigned By:* ";
    public static  final String CREATED_BY_ICON ="\n:speaking_head_in_silhouette:   *Created By:* ";
    public static  final String DELETED_BY_ICON ="\n:put_litter_in_its_place:   *Deleted By:* ";
    public static  final String UPLOADED_BY_ICON ="\n:file_cabinet:   *Uploaded By:* ";
    public static  final String UPLOADED_FILE_ICON =":file_folder:   *Uploaded File:* ";
    public static  final String MODIFIED_BY_ICON ="\n:speaking_head_in_silhouette:   *Modified By:* ";
    public static  final String TRANSITIONED_BY_ICON ="\n:speaking_head_in_silhouette:   *Transitioned By:* ";
    public static  final String DUE_DATE_ICON = "\n:hourglass_flowing_sand:   *Due Date:* ";
    public static  final String PREVIOUS_NAME_ICON = "\n:red_circle:   *Previous Name:* ";
    public static  final String MODIFIED_NAME_ICON = "\n:large_blue_circle:   *Modified Name:* ";
    public static  final String PREVIOUS_CONTENT_ICON = "\n:red_circle:   *Previous Content:* ";
    public static  final String MODIFIED_CONTENT_ICON = "\n:large_blue_circle:   *Modified Content:* ";
    public static  final String PREVIOUS_NOTES_ICON = "\n:red_circle:   *Previous Note Content:* ";
    public static  final String MODIFIED_NOTES_ICON =  "\n:large_blue_circle:   *Modified Note Content:* ";
    public static  final String PREVIOUS_DUE_DATE_ICON = "\n:red_circle:   *Previous DueDate:* ";
    public static  final String MODIFIED_DUE_DATE_ICON =  "\n:large_blue_circle:   *Modified DueDate:* ";


    //TaskGroup Tasks
    public static  final String TASKGROUP_TASK_ASSIGNMENT_TITLE = "New TaskGroup Task Assignment";
    public static  final String TASKGROUP_TASK_ASSIGNEE_UPDATE_TITLE ="Task Assignment Modification";
    public static  final String TASKGROUP_TASK_GREETING =" You have been assigned to the following TaskGroup Task";
    public static  final String TASKGROUP_TASK_MODIFICATION_GREETING =" A TaskGroup Task assigned to you has been modified";
    public static  final String TASKGROUP_ICON ="\n:briefcase:   *TaskGroup:* ";
    public static  final String TASKGROUP_TASK_ICON =":gear:   *TaskGroup Task:* ";
    public static final String TASKGROUP_TASK_REMINDER_TITLE = "Task Reminder";
    public static  final String TASKGROUP_TASK_REMINDER_GREETING =" A Reminder for the following TaskGroup Task";
    public static  final String TASKGROUP_TASK_DELETION_GREETING =" A Task Group Task assigned to you has been deleted";
    public static  final String TASKGROUP_TASK_DELETION_TITLE =" A Task assigned to you has been deleted";


    //Personal Tasks
    public static final String PERSONAL_TASK_REMINDER_TITLE = "Personal Task Reminder";
    public static  final String PERSONAL_TASK_REMINDER_GREETING =" A Reminder for the following Personal Task";
    public static  final String PERSONAL_TASK_ICON =":gear:   *Personal Task:* ";


    //Alt Texts
    public static  final String TASKGROUP_TASK_NAME_THUMBNAIL_TEXT ="TaskGroup Task Name Modification Thumbnail";
    public static  final String TASKGROUP_TASK_NOTE_THUMBNAIL_TEXT =  "TaskGroup Task Note Modification Thumbnail";
    public static  final String TASKGROUP_TASK_DATE_THUMBNAIL_TEXT = "TaskGroup Task Due Date Modification Thumbnail";
    public static  final String TASKGROUP_TASK_TRANSITION_THUMBNAIL_TEXT ="TaskGroup Task Transition Thumbnail";
    public static  final String TASKGROUP_TASK_FILE_TEXT ="TaskGroup Task File Thumbnail";
    public static  final String TASKGROUP_TASK_ASSIGNEE_UPDATE ="TaskGroup Task Assignee Update Thumbnail";
    public static  final String TASK_DELETE_THUMBNAIL_TEXT ="Task Delete Thumbnail";




    public static  final String TASK_NAME_THUMBNAIL_TEXT ="Task Name Modification Thumbnail";
    public static  final String TASK_NOTE_THUMBNAIL_TEXT ="Task Note Modification Thumbnail";
    public static  final String TASK_DUE_THUMBNAIL_TEXT ="Task Due Date Modification Thumbnail";
    public static  final String TASK_TRANSITION_THUMBNAIL_TEXT ="Task Transition Thumbnail";
    public static  final String REMINDER_THUMBNAIL_TEXT ="Reminder Thumbnail";


    public static  final String FRONTEND_URL = ENVConfig.BASE_URL;

    public static  final String CALENDER_THUMBNAIL = "https://api.slack.com/img/blocks/bkb_template_images/notifications.png";
    public static  final String ASSIGNMENT_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587030954303_assigned.png";
    public static  final String ASSIGNMENT_UPDATE_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587031448322_Changing%20the%20assignee.png";
    public static  final String UPDATE_TASK_NAME_THUMBNAIL  = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587035454442_Update%20the%20task%20name.png";
    public static  final String UPDATE_TASK_NOTE_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036006210_Update%20notes.png";
    public static  final String UPDATE_TASK_DUE_DATE_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036309205_Update%20the%20due%20date.png";
    public static  final String TRANSITION_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587038069361_changing%20the%20status%20of%20a%20task.png";
    public static  final String FILE_UPLOAD_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587037620456_Updates%20to%20files.png";

    public static  final String SUBTASK_CREATE_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036006210_Update%20notes.png";
    public static  final String SUBTASK_MODIFICATION_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036006210_Update%20notes.png";
    public static  final String SUBTASK_TRANSITION_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036006210_Update%20notes.png";
    public static  final String DELETED_BY_THUMBNAIL = "https://arimac-pmtool.s3-ap-southeast-1.amazonaws.com/profileImage_1587036006210_Update%20notes.png";



}
