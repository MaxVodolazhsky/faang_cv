package org.faang.cv.bot.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.faang.cv.bot.jpa.model.states.ChatState;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class ChatEntity {

    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private ChatState state;

    private String fullName;

    private String mobileNumber;

    private String email;

    private String education;

    private String englishLevel;

    @Column(length = 1000)
    private String additionalInfo;

    private byte[] photo;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public ChatState getState() {
        return state;
    }

    public void setState(ChatState state) {
        this.state = state;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatEntity that = (ChatEntity) o;
        return Objects.equals(chatId, that.chatId) && state == that.state && Objects.equals(fullName, that.fullName) && Objects.equals(mobileNumber, that.mobileNumber) && Objects.equals(email, that.email) && Objects.equals(education, that.education) && Objects.equals(englishLevel, that.englishLevel) && Objects.equals(additionalInfo, that.additionalInfo) && Arrays.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(chatId, state, fullName, mobileNumber, email, education, englishLevel, additionalInfo);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
