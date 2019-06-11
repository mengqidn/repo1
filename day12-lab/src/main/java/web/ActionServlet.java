package web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import entity.User;

public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		System.out.println("path:" + path);

		if ("/login".equals(path)) {
			// �����¼����
			processLogin(request, response);
		} else if ("/list".equals(path)) {
			// �����û��б�����
			processList(request, response);
		} else if ("/add".equals(path)) {
			// ��������û�����
			processAdd(request, response);
		} else if ("/del".equals(path)) {
			// ����ɾ���û�����
			processDel(request, response);
		}

	}

	private void processDel(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// ��ȡҪɾ�����û���id
		int id = Integer.parseInt(
				request.getParameter("id"));
		// �����ݿ���ɾ��ָ��id���û�
		UserDAO dao = new UserDAO();
		try {
			dao.delete(id);
			// �ض����û��б�
			response.sendRedirect("list.do");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * ��������û�����ķ���
	 */
	private void processAdd(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, ServletException {
		// ��������Ĳ���ֵ������
		request.setCharacterEncoding("utf-8");

		// ��ȡ�û���Ϣ
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");

		UserDAO dao = new UserDAO();
		try {
			/*
			 * �Ȳ鿴�û����Ƿ���ڣ�����Ѿ����ڣ� ��ת����addUser.jsp����ʾ"�û��� �Ѿ�����";���򣬽����û�����Ϣ���� �����ݿ⣬�ض����û��б�
			 */
			User user = dao.findByUsername(username);
			if (user != null) {
				request.setAttribute("msg", "�û����Ѿ�����");
				request.getRequestDispatcher("addUser.jsp").forward(request, response);
			} else {
				user = new User();
				user.setUsername(username);
				user.setPwd(pwd);
				user.setEmail(email);
				dao.save(user);
				response.sendRedirect("list.do");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * �����û��б�����ķ���
	 */
	private void processList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		/*
		 * ����session��֤, ���û�е�¼�����ض��򵽵�¼ ҳ�棬��������ִ�С�
		 */
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if (obj == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		// ��ѯ�������û�����Ϣ
		UserDAO dao = new UserDAO();
		try {
			List<User> users = dao.findAll();
			// ���ݲ�ѯ�����û���Ϣ��������
			/*
			 * ��ΪServlet���ó������ӵ�ҳ�棬 ���ԣ�����ʹ��ת�����ƣ������� �󶩵�request�����ϣ�Ȼ��ת���� jsp��չ�֡�
			 */
			// step1.�����ݰ󶩵�request�����ϡ�
			request.setAttribute("users", users);
			// step2.���ת������
			RequestDispatcher rd = request.getRequestDispatcher("listUser.jsp");
			// step3.ת��
			rd.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * �����¼����ķ���
	 */
	private void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, ServletException {
		request.setCharacterEncoding("utf-8");
		// ��ȡ�û���������
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		/*
		 * �����û����������ѯ���ݿ⣬����� ƥ��ļ�¼�����¼�ɹ����ض��� �û��б������¼ʧ�ܣ�ת������¼ ҳ�棬����ʾ���û������������
		 */
		UserDAO dao = new UserDAO();
		try {
			User user = dao.findByUsername(username);
			if (user != null && user.getPwd().equals(pwd)) {
				// ��¼�ɹ�
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect("list.do");
			} else {
				// ��¼ʧ��
				request.setAttribute("login_failed", "�û������������");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// ���쳣�׳���������������������
			throw new ServletException(e);
		}
	}

}
