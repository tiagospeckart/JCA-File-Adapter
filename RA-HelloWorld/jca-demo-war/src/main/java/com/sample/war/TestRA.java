package com.sample.war;

import com.sample.adapter.HelloWorldConnection;
import com.sample.adapter.HelloWorldConnectionFactory;
import jakarta.annotation.Resource;
import jakarta.resource.ResourceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/test")
public class TestRA extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(mappedName = "java:/eis/HelloWorld")
    private HelloWorldConnectionFactory connectionFactory;

    public TestRA() {
        super();

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = null;
        HelloWorldConnection connection = null;
        try {
            connection = connectionFactory.getConnection();
            result = connection.helloWorld();

        } catch (ResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        PrintWriter out = response.getWriter();
        out.println(result);

        out.flush();
        //  connection.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}