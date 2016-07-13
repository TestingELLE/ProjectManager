/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

/**
 *
 * @author Yi
 */
public class ButtonsState {

    private boolean batchEditBtnVisible;
    private boolean addBtnVisible;
    private boolean uploadChangesBtnVisible;
    private boolean revertChangesBtnVisible;
    private boolean editMode;

    public ButtonsState() {

    }

    public ButtonsState(boolean addRecord, boolean batchEdit, boolean uploadChange, boolean revertChange, boolean editMode) {
        this.batchEditBtnVisible = batchEdit;
        this.addBtnVisible = addRecord;
        this.uploadChangesBtnVisible = uploadChange;
        this.revertChangesBtnVisible = revertChange;
        this.editMode = editMode;

    }
    
    //this is the common state change for enable or disable editing
    public void enableEdit(boolean canEdit) {
        if (canEdit) {
            this.addBtnVisible = false;
            this.editMode = true;
            this.uploadChangesBtnVisible = true;
            this.revertChangesBtnVisible = true;    
        }
        else{
            this.addBtnVisible = true;
            this.editMode = false;
            this.uploadChangesBtnVisible = false;
            this.revertChangesBtnVisible = false;    
            
        }
        
        
    }

    public boolean isBatchEditBtnVisible() {
        return batchEditBtnVisible;
    }

    public void setBatchEditBtnVisible(boolean batchEditBtnVisible) {
        this.batchEditBtnVisible = batchEditBtnVisible;
    }

    public boolean isAddBtnVisible() {
        return addBtnVisible;
    }

    public void setAddBtnVisible(boolean addBtnVisible) {
        this.addBtnVisible = addBtnVisible;
    }

    public boolean isUploadChangesBtnVisible() {
        return uploadChangesBtnVisible;
    }

    public void setUploadChangesBtnVisible(boolean uploadChangesBtnVisible) {
        this.uploadChangesBtnVisible = uploadChangesBtnVisible;
    }

    public boolean isRevertChangesBtnVisible() {
        return revertChangesBtnVisible;
    }

    public void setRevertChangesBtnVisible(boolean revertChangesBtnVisible) {
        this.revertChangesBtnVisible = revertChangesBtnVisible;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
    
    

}
