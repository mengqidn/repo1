package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import util.DBUtils;

public class UserDAO {
	
	/**
	 * �����û���(uname)��ѯ���û�����Ϣ��
	 * ����Ҳ���������null;���򣬽����û�
	 * ����Ϣ��ӵ�User�������棬Ȼ�󷵻ء�
	 */
	public User findByUsername(String uname) throws Exception {
		User user = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConn();
			String sql = "SELECT * FROM t_user "
					+ "WHERE username=?";
			stat = conn.prepareStatement(sql);
			stat.setString(1, uname);
			rs = stat.executeQuery();
			if(rs.next()) {
				
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(
						rs.getString("username"));
				user.setPwd(
						rs.getString("password"));
				user.setEmail(rs.getString("email"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, rs);
		}
		return user;
	}
	
	
	public void delete(int id) throws Exception {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = DBUtils.getConn();
			String sql = "DELETE FROM t_user "
					+ "WHERE id=?";
			stat = conn.prepareStatement(sql);
			stat.setInt(1, id);
			stat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, null);
		}
	}
	
	public void save(User user) throws Exception {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = DBUtils.getConn();
			String sql = "INSERT INTO "
				+ "t_user VALUES(null,?,?,?)";
			stat = conn.prepareStatement(sql);
			stat.setString(1, user.getUsername());
			stat.setString(2, user.getPwd());
			stat.setString(3, user.getEmail());
			stat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, null);
		}
	}
	/**
	 * ��ѯ�������û�����Ϣ��
	 * ע��
	 * ��ϵ���ݿ������ŵ���һ������¼��
	 * ��java�������������ԡ������
	 * DAOʱ�����Ǿ�������ѯ���ļ�¼ת����
	 * һ����Ӧ��java����
	 */
	public List<User> findAll() 
			throws Exception{
		List<User> users = 
				new ArrayList<User>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConn();
			String sql = 
					"SELECT * FROM t_user";
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = 
						rs.getString("username");
				String pwd = 
						rs.getString("password");
				String email = 
						rs.getString("email");
				User user = new User();
				user.setId(id);
				user.setUsername(username);
				user.setPwd(pwd);
				user.setEmail(email);
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, rs);
		}
		return users;
	}
}






