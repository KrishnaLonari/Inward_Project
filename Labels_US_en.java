/*
 * @(#)Labels_US_en.java	01-Jan-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenue.
 * Use is subject to license terms.
 *
 */


package com.api.remitGuru.component.util;

import java.util.ListResourceBundle;

/**
 * <BR> Used for retrieving the common properties for the project.
 * <BR>
 * <BR> This keeps the classes decoupled from the environment settings,
 *      for porting accross environments.
 * <BR> eg : Locale currentLocale = new Locale(""+user_info.getLanguage());
 *		//Locale currentLocale = new Locale("en");
 *		//ResourceBundle label = ResourceBundle.getBundle("/config/Labels", currentLocale);
 * 		ResourceBundle label = ResourceBundle.getBundle("com.remitGuru.app.component.util.Labels", currentLocale);
 * 		label.getString("addRole_roles");
 * @author		********
 * @version 	1.00, 01-Jan-2012
 */

public class Labels_US_en extends ListResourceBundle {
		
		public Object [  ]  [  ]  getContents (  )   
		{  
			return contents; 
		}  
		
		static final Object [  ]  [  ]  contents =  {  
			{ "ims_selectPartner", "Select Partner"} ,
			{ "lbl_yes", "Yes"} ,
			{ "lbl_no", "No"} ,
			{ "ims_delete", "Delete"} ,
			{ "validate_selectPartner", "Please Select a Partner"} ,

			{ "error_fileUpload", "Error in uploading File"} ,
			{ "error_notAdded", "Error: Record Not Added. "} ,
			{ "error_duplicate", "duplicate"} ,
			{ "error_fail", "fail"} ,
			{ "error_notAdded_pleaseTryAgain", "ERROR: Record Not Added.<br>Please try again later."} ,
			{ "error_couldNotGetRec", "ERROR: Could Not Get Records"} ,
			{ "error_delete", "ERROR: Record Not Deleted.<br>Please try again later."} ,

			{ "msg_fileUpload_successfully", "File uploaded successfully. <br>Path : "} ,
			{ "msg_success", "success"} ,
			{ "msg_add_success", "Record has been added successfully."} ,
			{ "msg_delete_confirm", "Do you want to delete this data?"} ,
			{ "msg_delete_icon", "Icon Deleted Successfully."} ,
			{ "msg_delete_success", "Record has been deleted successfully."} ,
			{ "msg_modify_success", "Record has been modified successfully."} ,


			/* 
			*  Files used: name of jsp page
			*/
			{ "ims_role", "Role"} ,
			{ "ims_roles", "Roles"} ,
			{ "ims_addRole", "Add Role"} ,
			{ "ims_modifyRole", "Modify Role"} ,
			{ "ims_deleteRole", "Delete Role"} ,
			{ "ims_roleWorksetMap", "Role Workset Mapping"} ,
			{ "ims_selectRole", "Select Role"} ,
			{ "ims_selectWorksetToMap", "Select Workset to Map"} ,
			{ "ims_selectedWorkset", "Selected Worksets for Mapping"} ,

			{ "role_name", "Name"} ,
			{ "role_description", "Description"} ,
			{ "role_url", "URL"} ,
			{ "role_icon", "Icon"} ,
			{ "role_entryPoint", "Entry Point"} ,
			{ "role_reportsTo", "Reports To"} ,

			{ "validate_role_roleName", "Please fill Role Name field!"} ,
			{ "validate_role_name", "Role Name"} ,
			{ "validate_role_description", "Please fill description field!"} ,
			{ "validate_role_url", "Please fill URL field!"} ,
			{ "validate_role_entryPoint", "Please fill Entrypoint field!"} ,
			{ "validate_role_selectRole", "Please Select a Role"} ,

			{ "error_duplicate_role", "This role name has been already used."} ,

			{ "ims_group", "Group"} ,
			{ "ims_groups", "Groups"} ,
			{ "ims_addGroup", "Add Group"} ,
			{ "ims_modifyGroup", "Modify Group"} ,
			{ "ims_deleteGroup", "Delete Group"} ,
			{ "ims_groupActivityMap", "Group Activity Mapping"} ,
			{ "ims_selectGroup", "Select Group"} ,
			{ "ims_selectActivityToMap", "Select Activity to Map"} ,
			{ "ims_selectedActivities", "Selected Activities for Mapping"} ,

			{ "group_name", "Name"} ,
			{ "group_description", "Description"} ,
			{ "group_url", "URL"} ,
			{ "group_icon", "Icon"} ,
			{ "group_entryPoint", "Entry Point"} ,

			{ "validate_group_groupName", "Please fill Group Name field!"} ,
			{ "validate_group_name", "Group Name"} ,
			{ "validate_group_description", "Please fill description field!"} ,
			{ "validate_group_url", "Please fill URL field!"} ,
			{ "validate_group_entryPoint", "Please fill Entrypoint field!"} ,
			{ "validate_group_selectGroup", "Please Select a Group"} ,

			{ "error_duplicate_group", "This group name has been already used."} ,
			

			//workset

			{ "ims_workset", "Workset"} ,
			{ "ims_worksets", "Worksets"} ,
			{ "ims_addWorkset", "Add Workset"} ,
			{ "ims_modifyWorkset", "Modify Workset"} ,
			{ "ims_deleteWorkset", "Delete Workset"} ,
			{ "ims_worksetActivityMap", "Workset Activity Mapping"} ,
			{ "ims_selectWorkset", "Select Workset"} ,
			{ "ims_selectActivityToMap", "Select Activity to Map"} ,
			{ "ims_selectedActivities", "Selected Activities for Mapping"} ,

			{ "workset_name", "Name"} ,
			{ "workset_description", "Description"} ,
			{ "workset_url", "URL"} ,
			{ "workset_icon", "Icon"} ,
			{ "workset_entryPoint", "Entry Point"} ,

			{ "validate_workset_worksetName", "Please fill Workset Name field!"} ,
			{ "validate_workset_name", "Workset Name"} ,
			{ "validate_workset_description", "Please fill description field!"} ,
			{ "validate_workset_url", "Please fill URL field!"} ,
			{ "validate_workset_entryPoint", "Please fill Entrypoint field!"} ,
			{ "validate_workset_selectWorkset", "Please Select a Workset"} ,

			{ "error_duplicate_workset", "This workset name has been already used."} ,
			{ "error_workset_noDataFound", "No Workset List Found"} ,


			/* 
			*  Files used:
			*
			*/

			{ "addProjectTask_generalDetails", "General Details"} ,
			{ "addProjectTask_cost", "Cost"} ,
			{ "addProjectTask_attachments", "Attachments"} ,
			{ "addProjectTask_referenceLinks", "Reference Links"} ,
			{ "addProjectTask_resources", "Resources"} ,
			{ "addProjectTask_addPrjTaskConf", "Add Project Task Confirmation"} ,


			{ "addProjectTask_createPrjTask", "Create Project Task"} ,
			{ "addProjectTask_addEditPrjAttchmnts", "Add/Edit Project Attachments"} ,
			{ "addProjectTask_addRefLink", "Add More Reference Link"} ,
			{ "addProjectTask_addWorkslips", "Add More Workslips"} ,
			{ "addProjectTask_addEditPrjWorkslip", "Add/Edit Project Workslip"} ,


			{ "addProjectTask_projectName", "Project Name"} ,
			{ "addProjectTask_selectProject", "Select Project"} ,
			{ "addProjectTask_taskName", "Task Name"} ,
			{ "addProjectTask_startDate", "Start Date"} ,
			{ "addProjectTask_endDate", "End Date"} ,
			{ "addProjectTask_taskDesc", "Task Description"} ,
			{ "addProjectTask_duration", "Duration"} ,
			{ "addProjectTask_effort", "Effort (Hrs.)"} ,
			{ "addProjectTask_completed", "% Completed"} ,
			{ "addProjectTask_resourceName", "Resource Name"} ,
			{ "addProjectTask_selectResource", "Select Resource"} ,
			{ "addProjectTask_hoursAssigned", "Hours Assigned"} ,
			{ "addProjectTask_resourceAssigned", "Resource % Assigned"} ,
			{ "addProjectTask_personalCost", "Personal Cost"} ,
			{ "addProjectTask_travelCost", "Travel Cost"} ,
			{ "addProjectTask_meterialCost", "Meterial Cost"} ,
			{ "addProjectTask_externalCost", "External Cost"} ,
			{ "addProjectTask_miscellaneousCost", "Miscellaneous Cost"} ,
			{ "addProjectTask_comments", "Comments"} ,
			{ "addProjectTask_attURL", "Attachment URL"} ,
			{ "addProjectTask_attName", "Attachment Name"} ,
			{ "addProjectTask_attDesc", "Description"} ,
			{ "addProjectTask_refLink", "Reference Link"} ,
			{ "addProjectTask_linkName", "Link Name"} ,
			{ "addProjectTask_linkDesc", "Link Description"} ,
			{ "addProjectTask_workslipName", "Workslip Name"} ,
			{ "addProjectTask_workslipDesc", "Workslip Description"} ,
			{ "addProjectTask_addResource", "Add Resource"} ,
			{ "addProjectTask_selectResource", "Select Resource"} ,
			{ "addProjectTask_downLoad", "Download"} ,
			{ "addProjectTask_addAtt", "Add Attachment"} ,


			{ "validate_addProjectTask_projectId", "Project Name cannot be left blank"} ,
			{ "validate_addProjectTask_taskName", "Task Name cannot be left blank"} ,
			{ "validate_addProjectTask_startDate", "Sart Name cannot be left blank"} ,
			{ "validate_addProjectTask_endDate", "End Name cannot be left blank"} ,
			{ "validate_addProjectTask_taskDescr", "Duration cannot be left blank"} ,
			{ "validate_addProjectTask_duration", "Duration cannot be left blank"} ,
			{ "validate_addProjectTask_effort", "Effort cannot be left blank"} ,
			{ "validate_addProjectTask_completed", "Percentage Completed cannot be left blank"} ,
			{ "validate_addProjectTask_resourceName", "Please select a Resource Name"} ,
			{ "validate_addProjectTask_hoursAssgn", "Hours Assigned field cannot be left blank!"} ,
			{ "validate_addProjectTask_resourcePrcntgAssigned", "Resource % Assigned field cannot be left blank!"} ,
			{ "validate_addProjectTask_deleteData", "Do you want to delete this Data?"} ,
			{ "validate_addProjectTask_refLink", "Reference Link cannot be blank"} ,
			{ "validate_addProjectTask_linkName", "Link Name cannot be blank"} ,
			{ "validate_addProjectTask_resourceName", "Please select a Resource Name!"} ,


			{ "error_addProjectTask_recNotAdded", "Error: Record Not Added. "} ,
			{ "error_addProjectTask_couldNotGetRecs", "ERROR: Could Not Get Records"} ,
			{ "error_addProjectTask_duplicate", "duplicate"} ,
			{ "error_addProjectTask_fail", "fail"} ,
			{ "error_addProjectTask_errUploadingFile", "Error in uploading File"} ,
			{ "error_addProjectTask_sucessUploadingFile", "File uploaded successfully."} ,
			{ "error_addProjectTask_unableCreateDir", "Unable to create dir"} ,
			{ "error_addProjectTask_success", "Success"} ,
			{ "error_addProjectTask_recAddedSuccess", "Record has been added successfully"} ,
			{ "error_addProjectTask_taskNameAlreadyUsed", "This Task name has been already used."} ,
			{ "error_addProjectTask_recordNotAdded", "ERROR: Record Not Added.<br>Please try again later"} ,
			{ "error_addProjectTask_recordNameAlreadyUsed", "This Resource Name has been already used."} ,
			{ "error_addProjectTask_attNameAlreadyUsed", "This Attachment name has been already used"} ,
			{ "error_addProjectTask_refLinkAlreadyUsed", "This Reference link has been already used"} ,
			{ "error_addProjectTask_workslipNameAlreadyUsed", "This workslip has been alreay used"} ,

			

			{ "addProject_GeneralDetails", "General Details"} ,
			{ "addProject_Attachments", "Attachments"} ,
			{ "addProject_References", "Reference"} ,
			{ "addProject_StakeHolder", "Stake Holder"} ,
			{ "addProject_Resources", "Resources"} ,
			{ "addProject_ProjectName", "Project Name"} ,
			{ "addProject_Client", "Client"} ,
			{ "addProject_SelectClient", "Select Client"} ,
			{ "addProject_StartDate", "Start Date"} ,
			{ "addProject_EndDate", "End Date"} ,
			{ "addProject_Budget", "Budget"} ,
			{ "addProject_Priority", "Priority"} ,
			{ "addProject_Status", "Status"} ,
			{ "addProject_SelectStatus", "Select Status"} ,
			{ "addProject_Probability", "Probability"} ,
			{ "addProject_Goals", "Goals"} ,
			{ "addProject_Todos", "To Dos"} ,
			{ "addProject_Portfolio", "Portfolio"} ,
			{ "addProject_SelectPortfolio", "Select Portfolio"} ,
			{ "addProject_Program", "Program"} ,
			{ "addProject_Selectprogram", "Select Program"} ,
			{ "addProject_Description", "Description"} ,
			{ "validate_addProject_projectName", "Project Name cannot be left blank"} ,
			{ "validate_modifyProjectDtls_clientId", "Please Select the Client"} ,
			{ "validate_modifyProjectDtls_priorityId", "Please Select the Prioroty"} ,
			{ "validate_modifyProjectDtls_statusId", "Please Select the Status"} ,
			{ "error_addProject_records", "ERROR: Could Not Get Records"} ,
			{ "error_addProject_add", "Error: Record Not Added."} ,
			{ "error_addProject_duplicate", "duplicate"} ,
			{ "error_addProject_fail", "fail"} ,
			{ "error_addProject_success", "success"} ,
			{ "error_addProject_added", "Record has been added successfully."} ,
			{ "error_addProject_notAdded", "ERROR: Record Not Added.<br>Please try again later."} ,
			{ "modifyProjectDtls_GeneralDetails", "General Details"} ,
			{ "modifyProjectDtls_Attachments", "Attachments"} ,
			{ "modifyProjectDtls_References", "Reference"} ,
			{ "modifyProjectDtls_StakeHolder", "Stake Holder"} ,
			{ "modifyProjectDtls_Resources", "Resources"} ,
			{ "modifyProjectDtls_CreateProject", "Create Project"} ,
			{ "addProject_ProjectName", "Project Name"} ,
			{ "addProject_Client", "Task Name"} ,
			{ "addProject_SelectClient", "Client"} ,
			{ "addProject_StartDate", "Start Date"} ,
			{ "addProject_EndDate", "End Date"} ,
			{ "addProject_Budget", "Budget"} ,
			{ "addProject_Priority", "Priority"} ,
			{ "modifyProjectDtls_Selectpriority", "Select Priority"} ,
			{ "addProject_Status", "Status"} ,
			{ "addProject_SelectStatus", "Probability"} ,
			{ "addProject_Probability", "Goals"} ,
			{ "addProject_Goals", "To Dos"} ,
			{ "addProject_Todos", "Fortfolio"} ,
			{ "addProject_Portfolio", "Program"} ,
			{ "addProject_SelectPortfolio", "Description"} ,
			{ "addPrjAttachment_AddEditProjectAttachments", "Add/Edit Project Attachments"} ,
			{ "addProject_Attachment", "Attachment URL"} ,
			{ "addProject_AttachmentName", "Attachment Name"} ,
			{ "addProject_Description", "Description"} ,
			{ "addProject_DeleteProject", "Delete Project"} ,
			{ "addProject_AttachmentName", "Attachment Name"} ,
			{ "addProject_Description", "Description"} ,
			{ "addProject_AddAttachment", "Add Attachment"} ,
			{ "addProject_Download", "Download"} ,
			{ "validate_modifyProjectDtls_projectName", "Project Name cannot be left blank"} ,
			{ "validate_modifyProjectDtls_clientId", "Please Select the Client"} ,
			{ "validate_modifyProjectDtls_priorityId", "Please Select the Prioroty"} ,
			{ "validate_modifyProjectDtls_statusId", "Please Select the Status"} ,
			{ "error_addProject_records", "ERROR: Could Not Get Records"} ,
			{ "error_addProject_add", "Error: Record Not Added."} ,
			{ "error_addProject_duplicate", "duplicate"} ,
			{ "error_addProject_fail", "fail"} ,
			{ "error_addProject_success", "success"} ,
			{ "error_addProject_added", "Record has been added successfully."} ,
			{ "error_addProject_notAdded", "ERROR: Record Not Added.<br>Please try again later."} ,
			{ "error_addProject_delete", "Record has been deleted successfully."} ,
			{ "error_addProject_Notdeleted", "ERROR: Record Not Deleted.<br>Please try again later."} ,
			{ "error_addProject_UploadError", "Error in uploading File"} ,
			{ "error_addProject_notUpdated", "Error: Record Not Updated."} ,
			{ "error_addProject_DuplicateAttchmentName", "This Attachment Name has been already used."} ,
			{ "error_addProject_notUpdated", "Error: Record Not Updated. Please Try Again"} ,
			{ "error_addProject_Updated", "Record has been Updated successfuly"} ,
			{ "DisplayPrjReferences_ReferenceLink", "Reference Link"} ,
			{ "DisplayPrjReferences_LinkName", "Link Name"} ,
			{ "DisplayPrjReferences_LinkDescription", "Link Description"} ,
			{ "DisplayPrjReferences_ReferenceLink", "Reference Link"} ,
			{ "addPrjReferences_ReferenceLink", "Link Name"} ,
			{ "addPrjReferences_LinkName", "Link Description"} ,
			{ "addPrjReferences_LinkDescription", "Add More Reference Link"} ,
			{ "addPrjReferences_AddEditProjectReferences", "Add/Edit Project References"} ,
			{ "validate_addPrjReferences_DeleteConfirm", "Do you want to delete this Data?"} ,
			{ "error_addPrjReferences_records", "ERROR: Could Not Get Records"} ,
			{ "error_addPrjReferences_notAdded", "Error: Record Not Added."} ,
			{ "error_addPrjReferences_notUpdated", "Error: Record Not Updated."} ,
			{ "error_addPrjReferences_NotDeleted", "Error: Record Not Deleted."} ,
			{ "error_addPrjReferences_duplicate", "Duplicate"} ,
			{ "error_addPrjReferences_fail", "Fail"} ,
			{ "error_addPrjReferences_success", "success"} ,
			{ "validate_addPrjReferences_referenceLink", "Please Fill the Reference Link"} ,
			{ "validate_addPrjReferences_linkName", "Please Fill the Reference Link Name"} ,
			{ "validate_addPrjReferences_DeleteConfirm", "Do you want to delete this Data?"} ,
			{ "DisplayPrjStakeHolders_StakeHolderName", "Stake Holder Name"} ,
			{ "DisplayPrjStakeHolders_Designation", "Designation"} ,
			{ "DisplayPrjStakeHolders_Role", "Role"} ,
			{ "DisplayPrjStakeHolders_AddMoreStakeHolders", "Add More Stake Holders"} ,
			{ "validate_addPrjStakeHolders_StakeholderName", "Please Fill the Attachment Name"} ,
			{ "validate_addPrjStakeHolders_DeleteConfirm", "Do you want to delete this Data?"} ,
			{ "error_addPrjReferences_records", "ERROR: Could Not Get Records"} ,
			{ "error_addPrjReferences_notUpdated", "Error: Record Not Updated."} ,
			{ "error_addPrjReferences_Updated", "Record has been Updated successfuly"} ,
			{ "error_addPrjReferences_duplicate", "Duplicate"} ,
			{ "error_addPrjReferences_fail", "Fail"} ,
			{ "error_addPrjReferences_success", "success"} ,
			{ "error_addPrjReferences_NotDeleted", "ERROR: Record Not Deleted."} ,
			{ "projectResourceMap_ProjectResourceMap", "Project Resource Map"} ,
			{ "projectResourceMap_group", "Group"} ,
			{ "projectResourceMap_SelectGroup", "Select Group"} ,
			{ "projectResourceMap_OR", "OR"} ,
			{ "projectResourceMap_Pool", "Pool"} ,
			{ "projectResourceMap_SelectPool", "Select Pool"} ,
			{ "projectResourceMap_Resources", "Resources"} ,
			{ "projectResourceMap_SelectResources", "Select Resources"} ,
			{ "projectResourceMap_PercentUsed", "Percent Used"} ,
			{ "projectResourceMap_Delete", "Delete"} ,
			{ "validate_projectResourceMap_resourceId", "Please Select Resources!"} ,
			{ "validate_projectResourceMap_percentUsedNumaric", "Percent Used Field Can not left Balnk!"} ,
			{ "validate_projectResourceMap_percentUsed", "Percent Used Field Can not left Balnk!"} ,
			{ "validate_projectResourceMap_gropuOrPool", "Please Select the Group or Pool"} ,
			{ "validate_projectResourceMap_DeleteConfirm", "Do you want to delete this Record?"} ,
			{ "error_projectResourceMap_rocords", "ERROR: Could Not Get Records"} ,
			{ "error_projectResourceMap_NotAdded", "ERROR: Could Not Add Records"} ,
			{ "error_projectResourceMap_Added", "Record has been added successfully"} ,
			{ "searchProject_ProjectSearch", "Project Search"} ,
			{ "searchProject_Project", "Project"} ,
			{ "searchProject_Client", "Client"} ,
			{ "searchProject_Selectclient", "Select client"} ,
			{ "searchProject_StartDate", "Start Date"} ,
			{ "searchProject_EndDate", "End Date"} ,
			{ "searchProject_Priority", "Priority"} ,
			{ "searchProject_Selectpriority", "Select priority"} ,
			{ "searchProject_Status", "Status"} ,
			{ "searchProject_Selectstatus", "Select status"} ,
			{ "searchProject_ProjectList", "ProjectList"} ,
			{ "searchProject_SrNo", "Sr.No."} ,
			{ "searchProject_Budget", "Budget"} ,
			{ "searchProject_Status", "Status"} ,
			{ "error_searchProject_records", "ERROR: Could Not Get Records"} ,
			{ "error_addProject_add", "Error: Record Not Added."} ,
			{ "error_addProject_duplicate", "duplicate"} ,
			{ "error_addProject_fail", "fail"} ,
			{ "error_addProject_success", "success"} ,
			{ "error_addProject_added", "Record has been added successfully."} ,
			{ "error_addProject_notAdded", "ERROR: Record Not Added.<br>Please try again later."} ,
			{ "error_searchProject_Nosearch", "No Search Result Found"} ,
			{ "error_searchProject_NoOfStatus", "No Of Status"} ,

		} ; 
	  
    /**
     * Used to print the String representation of this object.
     *
     */
    public String toString () {
    	
        return "This is Labels_US_en File -- labels.properties";
    }

}