package com.ebay.renzhang.testlinkProject;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebay.renzhang.testlinkProject.TestLinkClient;

/**
 * Servlet implementation class TestLinkServ
 */
@WebServlet("/TestLinkServ")
public class TestLinkServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestLinkServ() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String buildURL = request.getParameter("build_url");
		TestLinkClient tlClient = new TestLinkClient(buildURL);
		tlClient.processUrl();

		PrintWriter out = response.getWriter();
		out.println("Successfully!");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String buildURL = request.getParameter("build_url");
		TestLinkClient tlClient = new TestLinkClient(buildURL);
		tlClient.processUrl();

		PrintWriter out = response.getWriter();
		out.println("Successfully!");
		out.close();
	}
}

