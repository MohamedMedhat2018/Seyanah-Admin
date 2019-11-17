package com.beyond_tech.seyanah_admin.models;

import com.beyond_tech.seyanah_admin.constants.Constants;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;//Full name not user name
    private String userPassword;
    private String userEmail;
    private String userPhoneNumber;
    private String userType = Constants.USER_TYPE_FREE;
    private String createDate;
    //    private String addressOrCurruntLocation;
//    private String userPhoto = Constants.NULL;
    private String userPhoto = "https://firebasestorage.googleapis.com/v0/b/seyanah-uea.appspot.com/o/images%2Fuser.png?alt=media&token=f1e5aaa9-e548-4304-8672-7acf70d17aec";
    private String messageToken;
    private boolean login;

    private boolean activiationState = true;
    private String userId;
    private String messagingToken;

    public User(String userName, String userPassword, String userEmail, String userPhoneNumber, String userType, boolean userStatusActivation) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userType = userType;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public User() {
    }

    public String getMessageToken() {
        return messageToken;
    }

    public void setMessageToken(String messageToken) {
        this.messageToken = messageToken;
    }

    public boolean isActiviationState() {
        return activiationState;
    }

    public void setActiviationState(boolean activiationState) {
        this.activiationState = activiationState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVerified(boolean verified) {
    }

    public String getMessagingToken() {
        return messagingToken;
    }

    public void setMessagingToken(String messagingToken) {
        this.messagingToken = messagingToken;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

//    public String getAddressOrCurruntLocation() {
//        return addressOrCurruntLocation;
//    }

//    public void setAddressOrCurruntLocation(String addressOrCurruntLocation) {
//        this.addressOrCurruntLocation = addressOrCurruntLocation;
//    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

}
