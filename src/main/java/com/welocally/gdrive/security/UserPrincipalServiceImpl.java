package com.welocally.gdrive.security;

import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welocally.gdrive.util.DigestUtils;


@Service("userPrincipalService")
@Transactional(readOnly = false)
public class UserPrincipalServiceImpl implements UserDetailsService, UserPrincipalService, AuthenticationProvider {

	@Value("${authenticationProvider.useMD5Hash:true}")
	private Boolean useMD5Hash;
	
    @Autowired
	private UserPrincipalDao userPrincipalDao;
    
    @Autowired
	private RoleDao roleDao;
    
    @Autowired DigestUtils digestUtils;
	
	static Logger logger = Logger.getLogger(UserPrincipalServiceImpl.class);
		
	
	public Role findRole(String name) throws UserPrincipalServiceException {
		return roleDao.findByName(name);
	}
			
	
	public String makeMD5Hash(String unhashed) throws NoSuchAlgorithmException {	    
	    return digestUtils.makeMD5Hash(unhashed);
	}

	@Transactional(readOnly = false)
	public void updateAllPasswordsToMD5() {
		try {
			List<UserPrincipal> up = findAll();
			for (UserPrincipal userPrincipal : up) {
				String hashedPass = makeMD5Hash(userPrincipal.getPassword());
				System.out.println("UPDATE `user_principal` SET `password`='"+hashedPass+"' WHERE `user_name`='"+userPrincipal.getUsername()+"'");
				userPrincipal.setPassword(hashedPass);
				
				saveUserPrincipal(userPrincipal);				
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("no md5 hash", e);
		} catch (UserPrincipalServiceException e) {
			logger.error("problem saving user", e);
		}		
	}
	
    
    @Transactional(readOnly = false)
	public void activateWithRoles(UserPrincipal entity, List<String> roleNames)
			throws UserPrincipalServiceException {
        // create a role for the user so they can log in

        	
        	entity.setCredentialsExpired(false);
        	entity.setLocked(false);
        	entity.setEnabled(true);
        	
            saveUserPrincipalRoles(entity, roleNames);

            userPrincipalDao.save(entity);
        
		
	}
    
   

	
    public void removeRoles(UserPrincipal entity, List<String> roleNamesToRemove)
            throws UserPrincipalServiceException {
	    
	    Set<String> roleNamesOrig = new HashSet<String>();
        Set<Role> roles = entity.getRoles();
        for (Role role : roles) {
            
            //only add for save if not in list 
            if(!roleNamesToRemove.contains(role.getRole())){
                roleNamesOrig.add(role.getRole());
            }          
        }
        

        saveUserPrincipalRoles(entity, new ArrayList<String>(roleNamesOrig));
        
        
    }



    
    public void addRoles(UserPrincipal entity, List<String> roleNames)
            throws UserPrincipalServiceException {
	   
	    
	    Set<String> roleNamesOrig = new HashSet<String>();
	    Set<Role> roles = entity.getRoles();
	    for (Role role : roles) {
	        roleNamesOrig.add(role.getRole());
	    }
	    
	    for (String string : roleNames) {
	        roleNamesOrig.add(string);
        }
	    

        saveUserPrincipalRoles(entity, new ArrayList<String>(roleNamesOrig));
 
       
    }



    
	@Transactional(readOnly = false)
	public void deactivate(UserPrincipal entity)
			throws UserPrincipalServiceException {
    	entity.setCredentialsExpired(false);
    	entity.setLocked(true);
    	entity.setEnabled(true);
        userPrincipalDao.save(entity);
	}



	
    @Transactional(readOnly = false)
    public void signUp(UserPrincipal entity, List<String> roleNames) throws UserPrincipalServiceException {
        UserPrincipal user = userPrincipalDao.findByEmail(entity.getEmail());
        if (user != null) {
            throw new UserPrincipalServiceException("Email address already registered");
        }
        user = userPrincipalDao.findByUserName(entity.getUsername());
        if (user != null) {
            throw new UserPrincipalServiceException("Username already in use");
        }

        saveUserPrincipalRoles(entity, roleNames);

        userPrincipalDao.save(entity);
        
    }

    
    public UserPrincipal findUserByEmail(String email) {
        return userPrincipalDao.findByEmail(email);
    }

    
	public List<UserPrincipal> findByUserNameLike(String username) {
		return userPrincipalDao.findByUserNameLike(username);
	}

	
	public List<Role> findAllRoles() throws UserPrincipalServiceException {
		return roleDao.findAll();
	}

	
	public List<UserPrincipal> findAll()  {
		return userPrincipalDao.findAll();
	}
	
	

	
	@Transactional(readOnly = false)
	public void saveUserPrincipalRoles(UserPrincipal up, List<String> roles){
		
		try {
			//delete the old roles
			deleteUserPrincipalRoles(up);
			Set<Role> newRoles = new HashSet<Role>();			
			for (String role : roles) {
				Role r = new Role();
				r.setRole(role);
				r.setUser(up);
				r.setRoleGroup(role);
				newRoles.add(r);
			}
			if (newRoles.size() > 0)
				up.setRoles(newRoles);
			
		} catch (UserPrincipalServiceException e) {
			throw new RuntimeException(e);
		}
		
		
		
	}
	
	

	
	public Boolean hasUserRole(UserPrincipal up, String roleName)
			throws UserPrincipalServiceException {
		for (Role role : up.getRoles()) {
			if(role.getRole().equals(roleName))
				return true;
			
		}
		return false;
	}



	
	public UserPrincipal loadUserEmail(String email)
			throws UserPrincipalServiceException, UserNotFoundException {
		UserPrincipal principal = getUserPrincipalDao().findByEmail(email);
		if(principal == null)
			throw new UserNotFoundException("The email addr:"+email+
                	" was not found in our database.");
		
		return principal;
	}

	
    @Transactional(readOnly = false)
	public void deleteUserPrincipal(UserPrincipal up)
			throws UserPrincipalServiceException {
		userPrincipalDao.delete(up);
	}

	
	public boolean authenticate(String username, String password) 
			throws UserPrincipalServiceException,UserNotFoundException {
		

		try {
			UserDetails details =
				loadUserByUsername(username);
			
			String checkPassword = password;
			try {
				if(useMD5Hash)
					checkPassword = makeMD5Hash(checkPassword);
			} catch (NoSuchAlgorithmException e) {
				logger.error("cannot make hash of password", e);
			}
							
			if(details.getPassword().equals(checkPassword))
				return true;
			
		} catch (DataAccessException e) {
			throw new UserPrincipalServiceException(e);
		} catch (UsernameNotFoundException e) {
			throw new UserNotFoundException(e);
		}
		
		return false;
		
		
	}

	//this is for the UserPrincipalService interface
	public UserPrincipal loadUser(String username) 
	throws UserPrincipalServiceException,UserNotFoundException
	{
		UserPrincipal principal = 
			getUserPrincipalDao().findByUserName(username);
		
		if(principal==null )
			throw new UsernameNotFoundException("cannot find user:"+username);

		
		return principal;
		
		
	}
	
	
	
	
	public UserPrincipal findByUserName(String username)
			throws UserPrincipalServiceException {
		return getUserPrincipalDao().findByUserName(username);
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails details = null;
		UserPrincipal principal = 
			getUserPrincipalDao().findByUserName(username);
		
		if(principal==null ){
		    String[] sa = {"ROLE_USER"};
	        List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
	        for (int i = 0; i < sa.length; i++) {
	            GrantedAuthority ga = new GrantedAuthorityImpl(sa[i]);
	            authoritiesList.add(ga);
	        }  
	        
	        User u = new User(username,"",true,true,true,true,authoritiesList);
	        UserPrincipal up = new UserPrincipal();
	        up.setAuthenticated(true);
	        up.setEnabled(true);
	        up.setLocked(false);
	        up.setCredentialsExpired(false);
	        up.setUsername(username);
	        this.userPrincipalDao.save(up);
	        Set<Role> roles = new HashSet<Role>();
	        Role r = new Role();
	        r.setRole("ROLE_USER");
	        r.setRoleGroup("ROLE_USER");
	        r.setUser(up);
	        roles.add(r);
	        up.setRoles(roles);
	        
	        //up.set
	        this.userPrincipalDao.save(up);
	        
	        
	        return u;
		}
		else
		{
			Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();

	        for (Iterator<Role> iter = principal.getRoles().iterator(); iter.hasNext();) {
	          Role role = iter.next();
	          auths.add(new GrantedAuthorityImpl(role.getRole()));
	        }

	        if (auths.size() == 0) {
	          throw new UsernameNotFoundException("User has no GrantedAuthority");
	        }
	        
	        //Object[] objectArray = auths.toArray();
	        GrantedAuthority[] authArray = (GrantedAuthority[])auths.toArray(new GrantedAuthority[auths.size()]);
	        details = new DefaultUserDetails(
	        		authArray,
	        		principal.getCredentialsExpired(), 
	        		principal.getEnabled(), 
	        		principal.getCredentialsExpired(), 
	        		principal.getLocked(), 
	        		principal.getPassword(), 
	        		principal.getUsername());
			
		}
		
		return details;
	}
	
	
	public UserPrincipal loadUserByAuthId(String authId) throws UserNotFoundException, DataAccessException {
		

		UserPrincipal user = 
			getUserPrincipalDao().findByAuthId(authId);
		
		if(user==null)
			throw new UsernameNotFoundException("cannot find user with auth id:"+authId);
		
		return user;
	}

	
	public Authentication authenticate(Authentication arg0)
			throws AuthenticationException {
		try {
			boolean authenticated =
				authenticate(
						arg0.getPrincipal().toString(), 
						arg0.getCredentials().toString());
			
			//if authenticated load the principal
			if(authenticated)	
			{
				Authentication auth = loadUser(arg0.getPrincipal().toString());
				auth.setAuthenticated(true);
				return auth;
			}
			else
				throw new BadCredentialsException("Username/Password does not match for " + arg0.getPrincipal().toString());			
			
		} catch (UserPrincipalServiceException e) {
			logger.error("cannot access authentication provider");
			throw new ProviderException("cannot access authentication provider");
		} catch (UserNotFoundException e) {
			logger.error("principal was not found:"+arg0.getPrincipal().toString());
			throw new BadCredentialsException("cannot access authentication provider");
		}
		
	}

	
	public boolean supports(Class clazz) {
		return clazz.equals(UserPrincipal.class);
	}

	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalService#createUserPrincipal()
	 */
	public UserPrincipal createUserPrincipal() {
		return new UserPrincipal();
	}



	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalService#saveUserPrincipal(com.noi.utility.spring.UserPrincipal)
	 */
    @Transactional(readOnly = false)
	public Long saveUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException {
		//roles
		Set<Role> roles = up.getRoles();
		for (Role role : roles) {
			roleDao.save(role);
		}
		
		userPrincipalDao.save(up);
		return up.getId();
	}


	/**
	 * this will update the user principal roles to new ones
	 * 
	 */
	
    @Transactional(readOnly = false)
	public void saveUserPrincipalRoles(UserPrincipal up, Set<Role> roles)
			throws UserPrincipalServiceException {
		
		//remove the previous roles
		Set<Role> oldRoles = up.getRoles();
		for (Role role : oldRoles) {
			roleDao.delete(role);
		}
		
		//set the new ones
		up.setRoles(roles);
		
		//save
		saveUserPrincipal(up);
		

	}

	
    @Transactional(readOnly = false)
	public void deleteUserPrincipalRole(UserPrincipal up, Role role)
			throws UserPrincipalServiceException {
		if(role.getId() != null)
		{
			//Role rDel = roleDao.findByPrimaryKey(role.getId());
			up.getRoles().remove(role);
			userPrincipalDao.save(up);
			roleDao.delete(role);
		}
		
	}
	
	

	
	@Transactional(readOnly = false)
	public void deleteUserPrincipalRoles(UserPrincipal up)
			throws UserPrincipalServiceException {
			
		for (Role role : up.getRoles()) {
			roleDao.delete(role);
		}
		
		up.setRoles(null);
		userPrincipalDao.save(up);
	}

	/**
	 * @return Returns the userPrincipalDao.
	 */
	public UserPrincipalDao getUserPrincipalDao() {
		return userPrincipalDao;
	}

	/**
	 * @param userPrincipalDao The userPrincipalDao to set.
	 */
	public void setUserPrincipalDao(UserPrincipalDao userPrincipalDao) {
		this.userPrincipalDao = userPrincipalDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	
	public UserPrincipal findUserByPrimaryKey(Long id) {
		return userPrincipalDao.findByPrimaryKey(id);
	}

	


    
    /**
     * this is really bad that we have to do this but I cant figure 
     * out why it wont transact. its stupid really, there us no good reason
     * I tried a ton of different propagation types. pissed off really. 1 hour
     * wasted. 
     */
    public void saveSpreadsheetAuthInfo(
            UserPrincipal entity, 
            String key, 
            String authUrl, 
            String authToken,
            String authSecret,
            String accessToken,
            String accessSecret
            ) throws UserPrincipalServiceException {
        
        UserPrincipal bound = 
            userPrincipalDao.findByUserName(entity.getUsername());
        bound.setSsAccessToken(accessToken);
        bound.setSsAccessSecret(accessSecret);
        bound.setSsAuthToken(authToken);
        bound.setSsAuthSecret(authSecret);
        bound.setSsAuthUrl(authUrl);
        bound.setAuthKey(key);
        
        userPrincipalDao.save(bound);
    }

    
    public void saveGdocsAuthInfo(UserPrincipal entity, String key,
            String authUrl, String authToken, String authSecret,
            String accessToken, String accessSecret)
            throws UserPrincipalServiceException {
        
        UserPrincipal bound = 
            userPrincipalDao.findByUserName(entity.getUsername());
        bound.setGdocsAccessToken(accessToken);
        bound.setGdocsAccessSecret(accessSecret);
        bound.setGdocsAuthToken(authToken);
        bound.setGdocsAuthSecret(authSecret);
        bound.setGdocsAuthUrl(authUrl);
        bound.setAuthKey(key);
        
        userPrincipalDao.save(bound);
        

    }




	

}
