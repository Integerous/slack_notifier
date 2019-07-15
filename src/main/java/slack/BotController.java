package slack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class BotController {
    private static final String APPLICATION_LINK = "https://www.redcross.or.kr/learn/edu/edu.do?orgcode=&educode1=02&educode2=02&edustatus=&pagesize=1000&edutypecode=01";
    private static final String ALARM_MESSAGE = "Somebody canceled!! Now available spots : ";
    private static final int MAXIMUM_NUMBER_OF_APPLICANTS = 60;

    void runSlackBot() {
        try {
            System.out.println("Loading page...");

            Document doc = Jsoup.connect(APPLICATION_LINK)
                    .referrer("https://google.com")
                    .userAgent("CHROME").get();

            String applicants = doc.getElementsContainingOwnText("/60").get(0).select(".dis-n-m > b").get(0).ownText();
            String stateMessage = doc.getElementsContainingOwnText("/60").get(1).ownText().substring(0, 4);
            int numberOfApplicants = Integer.parseInt(applicants);

            sendMessageIfAvailable(numberOfApplicants);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured.... going to retry...  \n");
        }
    }

    private void sendMessageIfAvailable(int numberOfApplicants) {
        if (numberOfApplicants < MAXIMUM_NUMBER_OF_APPLICANTS) {
            System.out.println(ALARM_MESSAGE + (MAXIMUM_NUMBER_OF_APPLICANTS - numberOfApplicants));
            sendMessage(numberOfApplicants);
        }
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
                .icon_emoji(":twice:")
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
