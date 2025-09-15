package com.ibm.wfm.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibm.wfm.beans.User;

@SuppressWarnings("deprecation")
@Component
public class UserDaoService {
	private static List<User> users = new ArrayList<>();
	private static int userCount=0;
	
	static {
		users.add(new User(1, "Steve", new Date(1962-1900,12-1,6)));
		users.add(new User(2, "Theresa", new Date(1969-1900,8-1,25)));
		users.add(new User(3, "Mike", new Date(1999-1900,8-1,23)));
		users.add(new User(4, "Liv", new Date(2003-1900,9-1,6)));
		userCount=4;
	}
	
	public List<User> findAll() {
		return users;
	}
	
	public User findOne(int id) {
		for (User user:users) {
			if (user.getId()==id) return user;
		}
		return null;
	}
	
	public User save(User user) {
		if (users==null) users = new ArrayList<>();
		if (user.getId()==null || user.getId()==0) user.setId(++userCount);
		users.add(user);
		return user;
	}
	
	public User deleteOne(int id) {
		User user = null;
		for (int i=0; i<users.size(); i++) {
			user = users.get(i);
			if (user.getId()==id) {
				users.remove(i);
				return user;
			}
		}
		return null;
	}

}
