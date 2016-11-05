package com.pb.lunchandlearn.service;

import com.pb.lunchandlearn.config.LDAPSettings;
import com.pb.lunchandlearn.config.ServiceAccountSettings;
import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.exception.ResourceNotFoundException;
import com.pb.lunchandlearn.exception.ServiceUnavailableException;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.pb.lunchandlearn.service.EmployeeServiceImpl.ADMIN_ROLE_LIST;
import static com.pb.lunchandlearn.service.EmployeeServiceImpl.MANAGER_ROLE_LIST;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Created by DE007RA on 8/4/2016.
 */
@Service
public class LDAPServiceImpl implements LDAPService {
	private static Map<String, String> USERS_DN = null;
	private static Map<String, Employee> AD_EMPLOYEES = null;
	private static List<String> guids = new ArrayList<>();
	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private LDAPSettings ldapSettings;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ServiceAccountSettings serviceAccountSettings;

	boolean isUpdatingEmployees = false;

	private String[] ldapMemberAttributes = {"member", "mail"};
	private String[] ldapUserAttributes = {"displayName", "cn", "mail", "manager"};
	private EmployeeAttributesMapper userMapper = null;
	private LDAPMemberDnAttributeMapper ldapMemberDnAttribMapper = null;
	public List<String> groupEmail = new ArrayList<>();

	@Override
	public Map<String, String> getUsersDn() {
		return USERS_DN;
	}

	@PostConstruct
	public void init() {
		setLdapAttributes();
		try {
//			updateEmployees();
		} catch (Exception e) {
			//TODO: log it
		}
	}

	private List<String> getEmployeesDn(String baseDN) {
		List<List<String>> dns = ldapTemplate.search(baseDN, "(objectClass=group)",
				SearchControls.OBJECT_SCOPE, ldapMemberAttributes, ldapMemberDnAttribMapper);
		if (!CollectionUtils.isEmpty(dns)) {
			return dns.get(0);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public Employee addEmployee(String guid) {
		if(StringUtils.isEmpty(guid)) {
			return null;
		}
		List<String> dnsList = null;
		guid = guid.toUpperCase();
		for (String baseDN : ldapSettings.getArrayBaseDNs()) {
			if(CollectionUtils.isEmpty(dnsList)) {
				dnsList = getEmployeesDn(baseDN);
			}
			else {
				dnsList.addAll(getEmployeesDn(baseDN));
			}
		}
		if(!CollectionUtils.isEmpty(dnsList)) {
			for(String userDn : dnsList) {
				String id = getGuid(userDn);
				if(StringUtils.isEmpty(id)) {
					throw new UsernameNotFoundException(MessageFormat.format("Guid: {0}", guid));
				}
				if(guid.equals(id)) {
					return fetchEmployee(id);
				}
			}
		}
		throw new UsernameNotFoundException(MessageFormat.format("Guid: {0}", guid));
	}

	private String getGuid(String userDn) {
		if(!StringUtils.isEmpty(userDn)) {
			int cnIndex = userDn.indexOf("CN=");
			if(cnIndex > -1) {
				String guid = userDn.substring(cnIndex + 3, userDn.indexOf(",", cnIndex));
				return StringUtils.trim(guid).toUpperCase();
			}
		}
		return null;
	}

	private Employee fetchEmployee(String userDn) {
		List<Employee> empInfo = ldapTemplate.search(userDn, "(objectClass=person)",
				SearchControls.OBJECT_SCOPE, ldapUserAttributes, userMapper);
		if (!CollectionUtils.isEmpty(empInfo)) {
			Employee employee = empInfo.get(0);
			AD_EMPLOYEES.put(employee.getGuid(), employee);
			if (USERS_DN == null) {
				USERS_DN = new ConcurrentHashMap<>();
			}
			USERS_DN.put(employee.getGuid().toUpperCase(), userDn);
			return employee;
		}
		return null;
	}

	@Override
	public void updateEmployees() throws Exception {
		if (!isUpdatingEmployees) {
			try {
				isUpdatingEmployees = true;
				// load AD_EMPLOYEES list
				if (AD_EMPLOYEES != null) {
					AD_EMPLOYEES.clear();
				}
				for (String baseDN : ldapSettings.getArrayBaseDNs()) {
					List<String> dnsList = getEmployeesDn(baseDN);
					if (!CollectionUtils.isEmpty(dnsList)) {
						if (AD_EMPLOYEES == null) {
							AD_EMPLOYEES = new ConcurrentHashMap<>(dnsList.size());
						}
						for (String dnsStr : dnsList) {
							fetchEmployee(dnsStr);
						}
					}
//					isEmployeesLoadedOnce = true;
					setEmployeesManagerName();
				}
				removeEmployees();
			} catch (Exception e) {
//				if (isEmployeesLoadedOnce) {
				e.printStackTrace();
					throw new ServiceUnavailableException("Error while accessing LDAP Server");
//				}
			} finally {
				isUpdatingEmployees = false;
				guids.clear();
			}
		}
	}

	private void removeEmployees() {
		if(AD_EMPLOYEES != null && AD_EMPLOYEES.size() > 0) {
			List guids = new ArrayList(AD_EMPLOYEES.keySet());
			employeeRepository.removeEmployees(guids);
		}
	}

	private void setServiceAccount() {
		Employee employee = AD_EMPLOYEES.get(StringUtils.upperCase(serviceAccountSettings.getGuid()));
		if(employee == null) {
			employee = new Employee();
			employee.setGuid(serviceAccountSettings.getGuid().toUpperCase());
			employee.setName(serviceAccountSettings.getGuid());
			AD_EMPLOYEES.put(employee.getGuid(), employee);
			USERS_DN.put(employee.getGuid(), ldapSettings.getServiceDN());
		}
		employee.setRoles(ADMIN_ROLE_LIST);
		employeeRepository.updateAllDifferentFields(employee);
	}

	private void setRoleAsManager() {
		for (String guid : guids) {
			Employee manager = AD_EMPLOYEES.get(guid);
			if(manager != null) {
				manager.setRoles(MANAGER_ROLE_LIST);
			}
		}
	}

	private void setEmployeesManagerName() {
		setServiceAccount();
		setRoleAsManager();
		if (AD_EMPLOYEES != null && AD_EMPLOYEES.size() > 0) {
			for (Employee emp : AD_EMPLOYEES.values()) {
				Map<String, String> managers = emp.getManagers();
				if (managers != null) {
					for (Map.Entry<String, String> manager : managers.entrySet()) {
						if(manager.getKey().equals(manager.getValue())) {
							Employee employee = AD_EMPLOYEES.get(manager.getKey());
							if (employee != null) {
								manager.setValue(employee.getName());
							}
						}
					}
				}
				employeeRepository.updateAllDifferentFields(emp);
			}
		}
	}

	private void setLdapAttributes() {
		if (userMapper == null) {
			userMapper = new EmployeeAttributesMapper();
		}
		if (ldapMemberDnAttribMapper == null) {
			ldapMemberDnAttribMapper = new LDAPMemberDnAttributeMapper();
		}
	}

	@Override
	public void updateEmployee(String guid) {
		String userDn = USERS_DN.get(guid);
		if(StringUtils.isEmpty(userDn)) {
			throw new ResourceNotFoundException(MessageFormat.format("User with Guid '{0}' is not found", guid));
		}
		fetchEmployee(userDn);
	}

	private class EmployeeAttributesMapper implements AttributesMapper<Employee> {
		@Override
		public Employee mapFromAttributes(Attributes attrs) throws NamingException {
			Employee employee = new Employee();
			employee.setGuid(((String) attrs.get("cn").get()).toUpperCase());
			employee.setName((String) attrs.get("displayName").get());
			employee.setEmpId((String) attrs.get("displayName").get());
			if (attrs.get("mail") != null) {
				employee.setEmailId((String) attrs.get("mail").get());
			}
			if (attrs.get("manager") != null) {
				String managerGuid = getGuid((String) attrs.get("manager").get());
				Map<String, String> managers = null;
				Employee e = AD_EMPLOYEES.get(managerGuid);
				if (e != null) {
					managers = Collections.singletonMap(managerGuid, e.getName());
				} else {
					guids.add(managerGuid);
					managers = new HashMap<String, String>(1);
					managers.put(managerGuid, managerGuid);
				}
				employee.setManagers(managers);
			}
			return employee;
		}
	}

	private class LDAPMemberDnAttributeMapper implements AttributesMapper {
		@Override
		public List<String> mapFromAttributes(Attributes attributes)
				throws NamingException {
			List<String> dns = null;
			Attribute attribute = attributes.get("mail");
			if (attribute != null) {
				String email = ((String) attribute.get());
				if (!StringUtils.isEmpty(email)) {
					groupEmail.add(email);
				}
			}
			Attribute attribMembers = attributes.get("member");
			if (attribMembers != null && attribMembers.size() > 0) {
				dns = new ArrayList<>();
				for (int count = 0; count < attribMembers.size(); ++count) {
					dns.add((String) attribMembers.get(count));
				}
			}
			return dns;
		}
	}

	@Override
	public Employee getEmployee(String guid) {
		if (AD_EMPLOYEES == null) {
			return null;
		}
		return AD_EMPLOYEES.get(guid);
	}

	@Override
	public void authenticateEmployee(String guid, String pwd) {
		if (USERS_DN == null || USERS_DN.isEmpty()) {
			throw new UsernameNotFoundException(MessageFormat.format("User Name: {0}", guid));
		}
		String baseDn = USERS_DN.get(guid);
		if (StringUtils.isEmpty(baseDn)) {
			throw new UsernameNotFoundException(MessageFormat.format("User Name: {0}", guid));
		}

		try {
			ldapTemplate.authenticate(query().base(baseDn).where("sAMAccountName").is(guid), pwd);
		} catch (AuthenticationException ex) {
			throw new UsernameNotFoundException(MessageFormat.format("User Name: {0}", guid));
		}
	}
}