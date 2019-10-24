package com.simple.rpc.service;

import com.simple.rpc.client.service.IProductService;
import com.simple.rpc.service.impl.ProductServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author srh
 * @date 2019/10/23
 **/
public class ServerMainTest {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            String apiClassName = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class[] parameterTypes = (Class[]) objectInputStream.readObject();
            Object[] args4Method = (Object[]) objectInputStream.readObject();

            Class clazz = null;
            if (apiClassName.equals(IProductService.class.getName())) {
                clazz = ProductServiceImpl.class;
            }
            assert clazz != null;
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object result = method.invoke(clazz.newInstance(), args4Method);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
