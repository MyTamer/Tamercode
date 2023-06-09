package com.restfb.example;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Collections.singletonList;
import java.util.List;
import com.restfb.DefaultLegacyFacebookClient;
import com.restfb.Facebook;
import com.restfb.LegacyFacebookClient;
import com.restfb.Parameter;

/**
 * Examples of RestFB's Legacy REST API functionality.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
public class LegacyExample {

    /**
   * RestFB Legacy REST API client.
   */
    private LegacyFacebookClient facebookClient;

    /**
   * Entry point. You must provide a single argument on the command line: a
   * valid OAuth access token.
   * 
   * @param args
   *          Command-line arguments.
   * @throws IllegalArgumentException
   *           If no command-line arguments are provided.
   */
    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("You must provide an OAuth access token parameter. " + "See README for more information.");
        new LegacyExample(args[0]).runEverything();
    }

    LegacyExample(String accessToken) {
        facebookClient = new DefaultLegacyFacebookClient(accessToken);
    }

    void runEverything() {
        object();
        list();
        fql();
        publish();
    }

    void object() {
        Long uid = facebookClient.execute("users.getLoggedInUser", Long.class);
        System.out.println("My UID is " + uid);
    }

    void list() {
        out.println("* Call that returns a list *");
        List<LegacyUser> users = facebookClient.executeForList("Users.getInfo", LegacyUser.class, Parameter.with("uids", "220439, 7901103"), Parameter.with("fields", "last_name, first_name"));
        out.println("Users: " + users);
    }

    /**
   * Holds user information as retrieved in {@link #list()}.
   */
    public static class LegacyUser {

        @Facebook("first_name")
        String firstName;

        @Facebook("last_name")
        String lastName;

        /**
     * @see java.lang.Object#toString()
     */
        @Override
        public String toString() {
            return format("%s %s", firstName, lastName);
        }
    }

    void fql() {
        Long uid = facebookClient.execute("users.getLoggedInUser", Long.class);
        String query = "SELECT name, pic_big, affiliations FROM user " + "WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=" + uid + ")";
        List<LegacyFqlUser> users = facebookClient.executeForList("fql.query", LegacyFqlUser.class, Parameter.with("query", query), Parameter.with("return_ssl_resources", "true"));
        System.out.println("My friends and their affiliations:");
        for (LegacyFqlUser user : users) System.out.println(user);
    }

    /**
   * Holds user information as retrieved in {@link #list()}.
   */
    public static class LegacyFqlUser {

        @Facebook
        String name;

        @Facebook("pic_big")
        String pictureUrl;

        @Facebook
        List<Affiliation> affiliations;

        public String toString() {
            return format("Name: %s\nProfile Image URL: %s\nAffiliations: %s", name, pictureUrl, affiliations);
        }
    }

    public static class Affiliation {

        @Facebook
        String name;

        @Facebook
        String type;

        public String toString() {
            return format("%s (%s)", name, type);
        }
    }

    void publish() {
        out.println("* Publishes to your feed *");
        ActionLink category = new ActionLink();
        category.href = "http://bit.ly/KYbaN";
        category.text = "humor";
        Properties properties = new Properties();
        properties.category = category;
        properties.ratings = "5 stars";
        Medium medium = new Medium();
        medium.href = "http://bit.ly/187gO1";
        medium.src = "http://bit.ly/GaTlC";
        medium.type = "image";
        Attachment attachment = new Attachment();
        attachment.name = "i'm bursting with joy";
        attachment.href = "http://bit.ly/187gO1";
        attachment.caption = "{*actor*} rated the lolcat 5 stars";
        attachment.description = "a funny looking cat";
        attachment.properties = properties;
        attachment.media = singletonList(medium);
        String postId = facebookClient.execute("stream.publish", String.class, Parameter.with("attachment", attachment));
        out.println("ID of the post you just published: " + postId);
    }

    public static class ActionLink {

        @Facebook
        String text;

        @Facebook
        String href;
    }

    public static class Medium {

        @Facebook
        String type;

        @Facebook
        String src;

        @Facebook
        String href;
    }

    public static class Properties {

        @Facebook
        ActionLink category;

        @Facebook
        String ratings;
    }

    public static class Attachment {

        @Facebook
        String name;

        @Facebook
        String href;

        @Facebook
        String caption;

        @Facebook
        String description;

        @Facebook
        Properties properties;

        @Facebook
        List<Medium> media;
    }
}
