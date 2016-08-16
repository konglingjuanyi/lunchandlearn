package com.pb.lunchandlearn.web;

/**
 * Created by DE007RA on 4/27/2016.
 */

import com.pb.lunchandlearn.config.SecuredUser;
import com.pb.lunchandlearn.domain.User;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.service.EmployeeService;
import com.pb.lunchandlearn.utils.CommonUtil;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	EmployeeService employeeService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject user(@AuthenticationPrincipal SecuredUser user) {
		if(user != null) {
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			List<String> authoritiesList = null;
			if(!CollectionUtils.isEmpty(authorities)) {
				authoritiesList = new ArrayList<>(user.getAuthorities().size());
				for (GrantedAuthority role : user.getAuthorities()) {
					authoritiesList.add(role.getAuthority());
				}
			}
			return CommonUtil.getUserInfo(new User(user.getUsername(), user.getEmailId(), authoritiesList), user.getGuid());
		}
		throw new ResourceNotFoundException();
	}
}