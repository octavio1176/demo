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
    @Async
    public void sendCode(String toEmail, String userName , String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("seu-email@gmail.com", "Banco Master");
            helper.setTo(toEmail);
            helper.setSubject("Código da sua conta ");

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
    @Async
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("seu-email@gmail.com", "Milestone");
            helper.setTo(toEmail);
            helper.setSubject("Bem-vindo(a) ao Milestone — sua jornada financeira em grupo começa agora");

            String htmlContent = buildWelcomeEmailTemplate(userName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Falha ao enviar e-mail de boas-vindas", e);
        }
    }

    private String buildWelcomeEmailTemplate(String userName) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    background-color: #e9ebee;
                    margin: 0;
                    padding: 20px;
                }
                .email-container {
                    max-width: 540px;
                    background-color: #ffffff;
                    margin: 0 auto;
                    border-radius: 10px;
                    overflow: hidden;
                    box-shadow: 0 6px 20px rgba(15,32,55,0.12);
                }
                .header {
                    position: relative;
                    background: linear-gradient(135deg, #10233f 0%%, #1c3a63 55%%, #2c5486 100%%);
                    padding: 44px 34px 38px 34px;
                }
                .brand-row {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    margin-bottom: 22px;
                }
                .brand-mark {
                    width: 30px;
                    height: 30px;
                    border-radius: 8px;
                    background: #d4af5a;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 16px;
                    font-weight: 900;
                    color: #10233f;
                }
                .brand-name {
                    color: #ffffff;
                    font-size: 14px;
                    font-weight: 700;
                    letter-spacing: 2px;
                    text-transform: uppercase;
                }
                .header h1 {
                    color: #ffffff;
                    margin: 0;
                    font-size: 34px;
                    line-height: 1.15;
                    font-weight: 800;
                    letter-spacing: -0.5px;
                }
                .header h1 span {
                    color: #d4af5a;
                }
                .header .sub {
                    color: #a9bcd6;
                    margin: 12px 0 0 0;
                    font-size: 14.5px;
                    font-weight: 400;
                }
                .arrows {
                    color: #d4af5a;
                    font-weight: 700;
                    letter-spacing: 3px;
                    font-size: 13px;
                    margin-top: 18px;
                }
                .content {
                    padding: 34px 34px 6px 34px;
                }
                .content p {
                    color: #4a5568;
                    font-size: 15.5px;
                    line-height: 1.65;
                    margin: 0 0 14px 0;
                }
                .content p.lead {
                    color: #10233f;
                    font-size: 17px;
                    font-weight: 600;
                }
                .divider {
                    height: 1px;
                    background: #edf0f4;
                    margin: 8px 34px 0 34px;
                }
                .features {
                    padding: 26px 34px 10px 34px;
                }
                .feature-item {
                    display: flex;
                    align-items: flex-start;
                    gap: 14px;
                    margin-bottom: 18px;
                }
                .feature-icon {
                    flex: 0 0 auto;
                    width: 34px;
                    height: 34px;
                    border-radius: 8px;
                    background: #f0f4fa;
                    color: #1c3a63;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 16px;
                    font-weight: 700;
                }
                .feature-text strong {
                    display: block;
                    color: #10233f;
                    font-size: 14.5px;
                    margin-bottom: 2px;
                }
                .feature-text span {
                    color: #78828a;
                    font-size: 13.5px;
                    line-height: 1.5;
                }
                .cta-wrap {
                    text-align: center;
                    padding: 10px 34px 36px 34px;
                }
                .cta-button {
                    display: inline-block;
                    background: #10233f;
                    color: #ffffff !important;
                    text-decoration: none;
                    font-size: 15px;
                    font-weight: 700;
                    letter-spacing: 0.3px;
                    padding: 15px 38px;
                    border-radius: 7px;
                }
                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #a0a8b3;
                    border-top: 1px solid #edf0f4;
                    padding: 20px 30px;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <div class="brand-row">
                        <div class="brand-mark">M</div>
                        <div class="brand-name">Milestone</div>
                    </div>
                    <h1>Welcome to<br><span>your next Milestone.</span></h1>
                    <p class="sub">A forma mais simples de juntar dinheiro em grupo, com propósito.</p>
                    <div class="arrows">&gt;&gt;&gt;&gt;&gt; &nbsp;&nbsp;&nbsp; &lt;&lt;&lt;&lt;&lt;</div>
                </div>

                <div class="content">
                    <p class="lead">Olá, %s 👋</p>
                    <p>Sua conta foi criada com sucesso. A partir de agora, você pode criar grupos, definir metas e acompanhar cada contribuição até alcançar o seu próximo marco financeiro.</p>
                </div>

                <div class="divider"></div>

                <div class="features">
                    <div class="feature-item">
                        <div class="feature-icon">01</div>
                        <div class="feature-text">
                            <strong>Crie um grupo</strong>
                            <span>Defina um objetivo e um valor alvo em minutos.</span>
                        </div>
                    </div>
                    <div class="feature-item">
                        <div class="feature-icon">02</div>
                        <div class="feature-text">
                            <strong>Convide participantes</strong>
                            <span>Adicione amigos, família ou colegas para contribuir junto com você.</span>
                        </div>
                    </div>
                    <div class="feature-item">
                        <div class="feature-icon">03</div>
                        <div class="feature-text">
                            <strong>Acompanhe o progresso</strong>
                            <span>Veja cada contribuição em tempo real, rumo à meta.</span>
                        </div>
                    </div>
                </div>

                <div class="cta-wrap">
                    <a href="#" class="cta-button">Criar meu primeiro grupo</a>
                </div>

                <div class="footer">
                    <p>&copy; 2026 Milestone. Todos os direitos reservados.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(userName);
    }
}