package com.welocally.gdrive.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.welocally.gdrive.domain.BaseEntity;

/**
 * 
 * 
 * @author cgraham
 * 
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
@Table(name="user_principal")
public class UserPrincipal extends BaseEntity implements Authentication, UserDetails {


    @Transient
	private Boolean authenticated;

    
    @Column(name = "user_name")
	private String username;
    
	private String password;
	
	private String email;
	
    @Column(name = "user_class")
	private String userClass;


    
    @Column(name = "credentials_expired", columnDefinition = "tinyint")
	private Boolean credentialsExpired = new Boolean(false);
    
    @Column(columnDefinition = "tinyint")
	private Boolean locked = new Boolean(false);
    
    @Column(columnDefinition = "tinyint")
	private Boolean enabled = new Boolean(true);
    
    
    /**
     * 
     *  * oid_id` BIGINT(20) NULL DEFAULT NULL ,
  `oid_username` VARCHAR(45) NULL DEFAULT NULL ,
  `oid_token` VARCHAR(255) NULL DEFAULT NULL ,
  `oid_secret` VARCHAR(255) NULL DEFAULT NULL ,
  `oid_verify` VARCHAR(255) NULL DEFAULT NULL ,
  `oid_profile_img` VARCHAR(255) NULL DEFAULT NULL ,
     * 
     */
    @Column(name = "oid_id")
    private Long oidId;
    
    @Column(name = "oid_username")
    private String oidUsername;
    
    @Column(name = "oid_profile_img")
    private String oidProfileImg;
    
    @Column(name = "auth_key")
    private String authKey;
  
    
    /*
     * ss_auth_url      text
ss_auth_token    varchar(255)
ss_auth_secret   varchar(255)
ss_access_token  varchar(255)
ss_access_secret varchar(255)
gdocs_auth_url   varchar(255)
gdocs_auth_token varchar(255)
gdocs_auth_secret varchar(255)
gdocs_access_token varchar(255)
gdocs_access_secret varchar(255)
     */
    @Column(name = "ss_auth_url")
    private String ssAuthUrl;
    
    @Column(name = "ss_auth_token")
    private String ssAuthToken;
    
    @Column(name = "ss_auth_secret")
    private String ssAuthSecret;   
    
    @Column(name = "ss_access_token")
    private String ssAccessToken;
    
    @Column(name = "ss_access_secret")
    private String ssAccessSecret;
    
    @Column(name = "gdocs_auth_url")
    private String gdocsAuthUrl;
    
    @Column(name = "gdocs_auth_token")
    private String gdocsAuthToken;
    
    @Column(name = "gdocs_auth_secret")
    private String gdocsAuthSecret;   
    
    @Column(name = "gdocs_access_token")
    private String gdocsAccessToken;
    
    @Column(name = "gdocs_access_secret")
    private String gdocsAccessSecret;

      
    @Column(name = "guid")
	private String authGuid;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles = new HashSet<Role>();

	public String getAuthGuid() {
		return authGuid;
	}

	public void setAuthGuid(String authGuid) {
		this.authGuid = authGuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	



	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param username
	 *            The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

    @JsonIgnore
	public Set<Role> getRoles() {
		return this.roles;
	}

    @JsonIgnore
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
    
    

	public Collection<GrantedAuthority> getAuthorities() {
		GrantedAuthority[] authorities = new GrantedAuthority[roles.size()];

		Role[] roleArray = (Role[]) roles.toArray(new Role[roles.size()]);
		for (int i = 0; i < roleArray.length; i++) {
			Role r = roleArray[i];
			GrantedAuthority ga = new GrantedAuthorityImpl(r.getRole());
			authorities[i] = ga;
		}

		return Arrays.asList(authorities);
	}

	public Object getCredentials() {
		return password;
	}


    
    public Object getDetails() {
        return new User(this.getUsername(), this.getPassword(), this
                .getEnabled(), !this.getCredentialsExpired(), this
                .getCredentialsExpired(), !this.getLocked(), this
                .getAuthorities());

    }

	
	public Object getPrincipal() {
		return username;
	}

	
	@JsonIgnore
	public boolean isAuthenticated() {
		return authenticated;
	}

	
	@JsonIgnore	
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		this.authenticated = arg0;

	}

	
	public String getName() {
		return username;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	@JsonIgnore
	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	@JsonIgnore
	public Boolean getEnabled() {
		return enabled;
	}

	@JsonIgnore
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	
	public boolean isAccountNonExpired() {
		return !getCredentialsExpired();
	}

	
	public boolean isAccountNonLocked() {
		return !getLocked();
	}

	
	public boolean isCredentialsNonExpired() {
		return !getCredentialsExpired();
	}

	
	public boolean isEnabled() {
		return getEnabled();
	}

	public Long getOidId() {
		return oidId;
	}

	public void setOidId(Long oidId) {
		this.oidId = oidId;
	}

	public String getOidUsername() {
		return oidUsername;
	}

	public void setOidUsername(String oidUsername) {
		this.oidUsername = oidUsername;
	}

	public String getOidProfileImg() {
		return oidProfileImg;
	}

	public void setOidProfileImg(String oidProfileImg) {
		this.oidProfileImg = oidProfileImg;
	}
   

    public String getSsAuthUrl() {
        return ssAuthUrl;
    }

    public void setSsAuthUrl(String ssAuthUrl) {
        this.ssAuthUrl = ssAuthUrl;
    }

    public String getSsAuthToken() {
        return ssAuthToken;
    }

    public void setSsAuthToken(String ssAuthToken) {
        this.ssAuthToken = ssAuthToken;
    }

    public String getSsAuthSecret() {
        return ssAuthSecret;
    }

    public void setSsAuthSecret(String ssAuthSecret) {
        this.ssAuthSecret = ssAuthSecret;
    }

    public String getSsAccessToken() {
        return ssAccessToken;
    }

    public void setSsAccessToken(String ssAccessToken) {
        this.ssAccessToken = ssAccessToken;
    }

    public String getSsAccessSecret() {
        return ssAccessSecret;
    }

    public void setSsAccessSecret(String ssAccessSecret) {
        this.ssAccessSecret = ssAccessSecret;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getGdocsAuthUrl() {
        return gdocsAuthUrl;
    }

    public void setGdocsAuthUrl(String gdocsAuthUrl) {
        this.gdocsAuthUrl = gdocsAuthUrl;
    }

    public String getGdocsAuthToken() {
        return gdocsAuthToken;
    }

    public void setGdocsAuthToken(String gdocsAuthToken) {
        this.gdocsAuthToken = gdocsAuthToken;
    }

    public String getGdocsAuthSecret() {
        return gdocsAuthSecret;
    }

    public void setGdocsAuthSecret(String gdocsAuthSecret) {
        this.gdocsAuthSecret = gdocsAuthSecret;
    }

    public String getGdocsAccessToken() {
        return gdocsAccessToken;
    }

    public void setGdocsAccessToken(String gdocsAccessToken) {
        this.gdocsAccessToken = gdocsAccessToken;
    }

    public String getGdocsAccessSecret() {
        return gdocsAccessSecret;
    }

    public void setGdocsAccessSecret(String gdocsAccessSecret) {
        this.gdocsAccessSecret = gdocsAccessSecret;
    }


	

}
