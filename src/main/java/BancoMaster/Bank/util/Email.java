package BancoMaster.Bank.util;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class Email {

    private final JavaMailSender mailSender;


    @Async
    public void sendConfirmationCode(String toEmail, String userName, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("seu-email@gmail.com", "Banco Master");
            helper.setTo(toEmail);
            helper.setSubject("Código de Confirmação - Banco Master");

            String htmlContent = buildEmailTemplate(userName, code);
            helper.setText(htmlContent, true); // true ativa a renderização HTML

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Falha ao enviar e-mail de confirmação", e);
        }
    }

    private String buildEmailTemplate(String userName, String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f4f7f6;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 500px;
                        background-color: #ffffff;
                        margin: 0 auto;
                        padding: 30px;
                        border-radius: 8px;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
                    }
                    .header {
                        text-align: center;
                        padding-bottom: 20px;
                        border-bottom: 2px solid #eef2f5;
                    }
                    .header h1 {
                        color: #2c3e50;
                        margin: 0;
                        font-size: 24px;
                    }
                    .content {
                        padding: 20px 0;
                        text-align: center;
                    }
                    .content p {
                        color: #555555;
                        font-size: 16px;
                        line-height: 1.5;
                    }
                    .code-box {
                        display: inline-block;
                        background-color: #0056b3;
                        color: #ffffff;
                        font-size: 32px;
                        font-weight: bold;
                        letter-spacing: 6px;
                        padding: 15px 30px;
                        margin: 20px 0;
                        border-radius: 6px;
                    }
                    .footer {
                        text-align: center;
                        font-size: 12px;
                        color: #999999;
                        border-top: 1px solid #eef2f5;
                        padding-top: 15px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Banco Master</h1>
                    </div>
                    <div class="content">
                        <p>Olá, <strong>%s</strong>!</p>
                        <p>Obrigado por se cadastrar. Use o código de verificação abaixo para confirmar sua conta:</p>
                        <div class="code-box">%s</div>
                        <p>Este código expira em 10 minutos. Se você não solicitou este cadastro, desconsidere este e-mail.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2026 Banco Master. Todos os direitos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, code);
    }
}