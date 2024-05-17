package com.eaproc.tutorials.librarymanagement.mail;

import lombok.Getter;

@Getter
public class PasswordResetMail implements IMailable{

    private final String userName;
    private final String resetCode;
    private final String subject;

    public PasswordResetMail(String userName, String resetCode) {
        this.userName = userName;
        this.resetCode = resetCode;
        this.subject = "Password Reset Request";
    }

    @Override
    public String toString() {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<div style='text-align: center;'>" +
                "<h1>Library Management Application</h1>" +
                "<div style='background-color: #f2f2f2; padding: 20px; border-radius: 10px; display: inline-block;'>" +
                "<h2>Password Reset Request</h2>" +
                "<p>Dear " + userName + ",</p>" +
                "<p>Your password reset code is:</p>" +
                "<h1 style='color: #333;'>" + resetCode + "</h1>" +
                "</div>" +
                "<p style='margin-top: 20px;'>If you didn't request this code, kindly ignore it or report to customer service.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
