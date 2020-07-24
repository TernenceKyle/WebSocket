package com.tk.prac4springboot.Domain;

import com.tk.prac4springboot.WebSocket.ShowLinkedCount;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Random;

@Component
public class BitCoinDataCenter  implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                System.out.println("BitCoin 初始化开始....");
                int bitprice = 10000;
                while(true){
                    int duration = 1000+ random.nextInt(2000);
                    try {
                        Thread.sleep(duration);
                        float factor =1+(float) (Math.random()-0.5);
                        int newbitprice =(int)(bitprice*factor);
                        String mformat = "{\"price\": \"%d\", \"total\": \"%d\"}";
                        System.out.println(newbitprice);
                        int num = Integer.parseInt(ShowLinkedCount.getTotalCount());
                        String message =String.format(mformat,newbitprice, num);
                        if (num>0)
                        {
                            ShowLinkedCount.broadCast(message);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        System.err.println("BitCoinPrice生成时间元素出错!");
                    }
                }
            }
        }).start();
    }
}
