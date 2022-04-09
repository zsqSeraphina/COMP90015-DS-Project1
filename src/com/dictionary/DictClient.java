package com.dictionary;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictClient {
    // driver code
    public static void main(String[] args) {
        if (2 != args.length) {
            throw new IllegalArgumentException
                    ("Please enter server address and port number");
        }
        String address;
        int port;
        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
        } catch(Exception e) {
            throw new IllegalArgumentException
                    ("Error:" + e + ", Please enter correct server address and port number");
        }
        try {
            Socket socket = new Socket(address, port);

            PrintWriter output
                    = new PrintWriter(new OutputStreamWriter(
                            socket.getOutputStream(), StandardCharsets.UTF_8));

            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));

            Scanner sc = new Scanner(System.in);
            String line = null;

            while (!"exit".equalsIgnoreCase(line)) {

                line = sc.nextLine();

                output.println(line);
                System.out.println(in.readLine());
                output.flush();

            }
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
