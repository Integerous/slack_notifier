package slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Getter
@Setter
public class SlackMessage implements Serializable {

    private String channel;
    private String username;
    private String text;
    private String icon_emoji;
    private List<SlackMessageAttachments> attachments;
}
