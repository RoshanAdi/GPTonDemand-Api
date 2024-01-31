package com.gpt.gptplus1.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class ChatMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @Column(nullable = false)
    private String role;

    @Column(nullable = false, length = 30000)
    private String content;
    @Column(nullable = true, columnDefinition = "int default 1000")
    private int maxTokens;
    @Column(nullable = true, columnDefinition = "int default 20000")
    private int maxHistory;

    @Column(nullable = true, length = 400)
    private  String systemMassage;
    @Column(nullable = true, columnDefinition = "VARCHAR(255) default 'gpt-4'")
    private String model;

    // Add any other necessary fields

    public ChatMsg() {
        // default constructor for JPA
    }

    public ChatMsg(String role, String content, int maxTokens, String systemMassage, int maxHistory, String model) {
        this.role = role;
        this.content = content;
        this.maxTokens = maxTokens;
        this.systemMassage = systemMassage;
        this.maxHistory = maxHistory;
        this.model = model;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getSystemMassage() {
        return systemMassage;
    }

    public void setSystemMassage(String systemMassage) {
        this.systemMassage = systemMassage;
    }

    public int getMaxHistory() {
        return maxHistory;
    }

    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "chatMsg{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", content='" + content + '\'' +
                ", maxTokens='" + maxTokens + '\'' +
                ", systemMassage='" + systemMassage + '\'' +
                ", maxHistory='" + maxHistory + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
