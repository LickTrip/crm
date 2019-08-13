package com.michal.crm.service.Email;

import com.michal.crm.dao.EmailConfigRepository;
import com.michal.crm.dto.EmailDto;
import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.Email;
import com.michal.crm.model.UserEmailConfig;
import com.michal.crm.model.auxObjects.EmailOption;
import com.michal.crm.model.types.EmailTypes;
import com.michal.crm.model.types.ResultTypes;
import com.michal.crm.service.CompanyService;
import com.michal.crm.service.ContactsService;
import com.michal.crm.service.Email.Exception.EmailException;
import com.michal.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Override
    public EmailDto addItemToTable(int itemId, boolean isCont) {
        EmailDto emailItem = new EmailDto();
        emailItem.setCont(isCont);
        if (isCont) {
            emailItem.setContacts(contactsService.getContactById(itemId));
        } else {
            emailItem.setCompany(companyService.getCompanyById(itemId));
        }
        return emailItem;
    }

    @Override
    public int generateListId(List<EmailDto> emailDtoList) {
        if (emailDtoList.isEmpty()) {
            return 1;
        } else {
            return emailDtoList.get(emailDtoList.size() - 1).getId() + 1;
        }
    }

    @Override
    public List<EmailDto> removeItem(List<EmailDto> itemsList, int itemId) {
        List<EmailDto> newList = new ArrayList<>();
        for (EmailDto item : itemsList
        ) {
            if (item.getId() == itemId) {
                continue;
            }
            newList.add(item);
        }
        return newList;
    }

    @Override
    public void updateOutlookPath(String path) {
        UserEmailConfig oldEmailConfig = userService.getLoggedUser().getEmailConfig();
        if (!path.isEmpty())
            oldEmailConfig.setOutlookPath(path);
        emailConfigRepository.save(oldEmailConfig);
    }

    @Override
    public boolean openInOutLook(List<EmailDto> emailList) {
        String pathToOut = userService.getLoggedUser().getEmailConfig().getOutlookPath();
        if (pathToOut == null || pathToOut.equals("")) {
            return false;
        }
        String stringOfEmails = getEmails(emailList);
        ProcessBuilder pb = new ProcessBuilder(pathToOut, "/c", "ipm.note", "/m", stringOfEmails);
        try {
            pb.start();
        } catch (IOException e) {
            throw new EmailException("Unable to run outlook");
        }
        return true;
    }

    @Override
    public ResultTypes updateEmailInfo(ProfileDto profileDto) {
        UserEmailConfig oldEmailConfig = userService.getLoggedUser().getEmailConfig();
        UserEmailConfig newEmailConfig = profileDto.getUser().getEmailConfig();

        if (newEmailConfig.getEmail() != null)
            oldEmailConfig.setEmail(newEmailConfig.getEmail());

        if (newEmailConfig.getEmailHost() != null)
            oldEmailConfig.setEmailHost(newEmailConfig.getEmailHost());

        if (newEmailConfig.getEmailPort() > 0)
            oldEmailConfig.setEmailPort(newEmailConfig.getEmailPort());

        oldEmailConfig.setProtocolType(newEmailConfig.getProtocolType());

        ResultTypes resultTypes = ResultTypes.OK;
        if (!Objects.equals(profileDto.getPassNew(), "")) {
            if (Objects.equals(profileDto.getPassNew(), profileDto.getPassConf())) {
                oldEmailConfig.setEmailPassword(profileDto.getPassNew());
            } else {
                resultTypes = ResultTypes.PASS_NOT_MATCH;
            }
        }
        oldEmailConfig.setEmailPassword("not supported");

        emailConfigRepository.save(oldEmailConfig);
        return resultTypes;
    }

    @Override
    public String getEmails(List<EmailDto> emailDtoList) {
        if (emailDtoList.isEmpty()) {
            return "";
        }

        StringBuilder stringOfEmails = new StringBuilder();
        for (EmailDto emailD : emailDtoList
        ) {
            if (emailD.isCont()) {
                stringOfEmails.append(emailD.getContacts().getEmail()).append(";");
            } else {
                stringOfEmails.append(emailD.getCompany().getEmail()).append(";");
            }
        }
        return stringOfEmails.toString();
    }

    public void sendEmail(Email email) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        //mail.setTo(email.getSendTo());
        mail.setFrom("cmr666.tst@gmail.com");
        mail.setSubject(email.getSubject());
        mail.setText(email.getText());

        //javaMailSender.send(mail);
    }

    public List<Email> readEmails(EmailTypes types, Integer noEmailToDelete) {
        List<Email> emails = new ArrayList<>();
        EmailOption emailOption = getEmailOptions();
        Properties properties = getServerProperties(emailOption.getProtocol(), emailOption.getHost(), emailOption.getPort());
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(emailOption.getProtocol());
            store.connect(emailOption.getUserName(), emailOption.getPassword());
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);

            switch (types) {
                case ALL:
                    emails = seenEmails(folderInbox, true, true);
                    break;
                case SEEN:
                    emails = seenEmails(folderInbox, true, false);
                    break;
                case UNSEEN:
                    emails = seenEmails(folderInbox, false, false);
                    break;
                case DELETE:
                    emails = seenEmails(folderInbox, true, false);
                    break;
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + emailOption.getProtocol());
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        System.out.println("Read OK");
        return emails;
    }

    private EmailOption getEmailOptions() {
        EmailOption emailOption = new EmailOption();
        emailOption.setProtocol("imap");
        emailOption.setHost("imap.gmail.com");
        emailOption.setPort("993");
        emailOption.setUserName("cmr666.tst@gmail.com");
        emailOption.setPassword("hesloheslo123");
        return emailOption;
    }

    private List<Email> seenEmails(Folder folderInbox, boolean isSeen, boolean allMessage) throws MessagingException {
        // fetches new messages from server
        // search for all "unseen" messages
        Message[] messages;
        if (allMessage) {
            messages = folderInbox.getMessages();
        } else {
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm flagTerm = new FlagTerm(seen, isSeen);
            messages = folderInbox.search(flagTerm);
        }

        List<Email> emails = new ArrayList<>();
        for (Message message : messages) {
            Email email = new Email();
            email.setMsgNumber(message.getMessageNumber());
            email.setSendDate(message.getSentDate());
            setMailSendTo(message, email);
            email.setSubject(message.getSubject());
            try {
                String text = getTextFromMessage(message);
                email.setText(text);
                email.setPreview(text.length() < 100 ? text : text.substring(0, 100));

            } catch (IOException e) {
                System.out.println("Error reading content!!");
                e.printStackTrace();
            }
            emails.add(email);
        }

        return emails;
    }

    private void setMailSendTo(Message message, Email email) throws MessagingException {
        Address[] addresses = message.getFrom();
        List<String> names = new ArrayList<>();
        List<String> emailsAdd = new ArrayList<>();

        for (Address address : addresses) {
            String aux = address.toString();
            String[] auxSep = aux.split(" <");
            names.add(auxSep[0]);
            emailsAdd.add(auxSep[1].substring(0, auxSep[1].length() - 1));
        }

        email.setSendToA(addresses);
        email.setSendToNames(names);
        email.setSendToEmails(emailsAdd);
    }

    /**
     * Returns a Properties object which is configured for a POP3/IMAP server
     *
     * @param protocol either "imap" or "pop3"
     * @param host
     * @param port
     * @return a Properties object
     */
    private Properties getServerProperties(String protocol, String host,
                                           String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        // for gmail
        properties.setProperty(String.format("mail.%s.ssl.enable", protocol), "true");
        properties.setProperty(String.format("mail.%s.ssl.required", protocol), "true");
        properties.setProperty(String.format("mail.%s.ssl.ssl.socketFactory.port", protocol), "465");

        return properties;
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            // alternatives appear in an order of increasing
            // faithfulness to the original content. Customize as req'd.
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private String getTextFromBodyPart(
            BodyPart bodyPart) throws IOException, MessagingException {

        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }
}
