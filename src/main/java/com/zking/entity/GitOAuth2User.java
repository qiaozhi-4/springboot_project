package com.zking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class GitOAuth2User implements OAuth2User, Serializable {
    private Integer id;
    private String login;
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("followers_url")
    private String followersUrl;
    @JsonProperty("following_url")
    private String followingUrl;
    @JsonProperty("gists_url")
    private String gistsUrl;
    @JsonProperty("starred_url")
    private String starredUrl;
    @JsonProperty("subscriptions_url")
    private String subscriptionsUrl;
    @JsonProperty("organizations_url")
    private String organizationsUrl;
    @JsonProperty("repos_url")
    private String reposUrl;
    @JsonProperty("events_url")
    private String eventsUrl;
    @JsonProperty("received_events_url")
    private String receivedEventsUrl;
    private String type;
    private String blog;
    private String weibo;
    private String bio;
    @JsonProperty("public_repos")
    private Integer publicRepos;
    @JsonProperty("public_gists")
    private Integer publicGists;
    private Integer followers;
    private Integer following;
    private Integer stared;
    private Integer watched;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private String email;
    private String remark;
    @JsonProperty("node_id")
    private String nodeId;
    @JsonProperty("gravatar_id")
    private String gravatarId;
    @JsonProperty("site_admin")
    private Boolean siteAdmin;
    private String company;
    private String location;
    private String hireable;
    @JsonProperty("twitter_username")
    private String twitterUsername;
    @JsonProperty("private_gists")
    private Integer privateGists;
    @JsonProperty("total_private_repos")
    private Integer totalPrivateRepos;

    @JsonProperty("owned_private_repos")
    private Integer ownedPrivateRepos;
    @JsonProperty("disk_usage")
    private String diskUsage;
    private String collaborators;
    @JsonProperty("two_factor_authentication")
    private Boolean twoFactorAuthentication;
    private LinkedHashMap plan;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
