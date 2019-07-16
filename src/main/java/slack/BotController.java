package slack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BotController {
    private static final String APPLICATION_LINK = "https://www.redcross.or.kr/learn/edu/edu.do?orgcode=&educode1=02&educode2=02&edustatus=&pagesize=1000&edutypecode=01";
    private static final String ALARM_MESSAGE = "Somebody canceled !! \n Now available spots : ";
    private static final int MAXIMUM_NUMBER_OF_APPLICANTS = 60;
    private static final String DATE_TIME_FORMAT = "MM/dd HH:mm:ss";

    void runSlackBot() {
        try {
            System.out.println("Loading page..." + getCurrentTime());

            Document doc = Jsoup.connect(APPLICATION_LINK)
                    .referrer("https://google.com")
                    .userAgent("CHROME").get();

            String applicants = doc.getElementsContainingOwnText("/60").get(0).select(".dis-n-m > b").get(0).ownText();
            String stateMessage = doc.getElementsContainingOwnText("/60").get(1).ownText().substring(0, 4);
            int numberOfApplicants = Integer.parseInt(applicants);

            sendMessageIfAvailable(numberOfApplicants);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("문제가 발생했습니다. 현재 시간 : " + getCurrentTime());
        }
    }

    private void sendMessageIfAvailable(int numberOfApplicants) {
        if (numberOfApplicants != MAXIMUM_NUMBER_OF_APPLICANTS) {
            System.out.println(ALARM_MESSAGE + (MAXIMUM_NUMBER_OF_APPLICANTS - numberOfApplicants) + "\n" + getCurrentTime());
            sendMessage(numberOfApplicants);
        }else {
            sendState();
        }
    }

    private void sendState() {
        SlackMessage slackState = buildState();
        SlackUtils.sendMessage(slackState);
    }

    private SlackMessage buildState() {
        return SlackMessage.builder()
                .channel("heart_beat")
                .username("Doctor")
                .text("No one canceled yet :( \n but I'm watching up !! \n Current Time : " + getCurrentTime())
                .icon_emoji(":aww_yeah:")
                .build();
    }

    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return simpleDateFormat.format(new Date());
    }

    private void sendMessage(int numberOfApplicants) {
        SlackMessage slackMessage = buildMessage(numberOfApplicants, buildAttachments(APPLICATION_LINK));
        SlackUtils.sendMessage(slackMessage);
    }

    private SlackMessage buildMessage(int numberOfApplicants, SlackMessageAttachments attachments) {
        return SlackMessage.builder()
                .channel("notifier")
                .username("HARU")
                .text(ALARM_MESSAGE + (MAXIMUM_NUMBER_OF_APPLICANTS - numberOfApplicants))
                .attachments(new ArrayList<>(Arrays.asList(attachments)))
                .build();
    }

    private SlackMessageAttachments buildAttachments(String link) {
        return SlackMessageAttachments.builder()
                .title("Click here to APPLY!!")
                .title_link(link)
                .build();
    }
}
