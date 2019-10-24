package com.simple.rpc.client;

import com.simple.rpc.client.service.IProductService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author srh
 * @date 2019/10/23
 **/
public class ClientMainTest {

    public static void main(String[] args) {
        IProductService service = (IProductService) rpc(IProductService.class);
        System.out.println(service.create());
    }

    private static Object rpc(final Class<IProductService> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket("127.0.0.1", 8888);

                        String apiClassName = clazz.getName();
                        String methodName = method.getName();
                        Class<?>[] parameterTypes = method.getParameterTypes();

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeUTF(apiClassName);
                        objectOutputStream.writeUTF(methodName);
                        objectOutputStream.writeObject(parameterTypes);
                        objectOutputStream.writeObject(args);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        Object result = objectInputStream.readObject();
                        objectInputStream.close();
                        objectOutputStream.close();
                        socket.close();
                        return result;
                    }
                });
    }

}
