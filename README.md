## WebSocket接口

#### Tips

- SpringBoot会扫描项目目录下的Config文件夹中的文件，只要带@Configuration注解的类会被自动地加载。

- springboot中使用Websocket需要配置一个ServerEndpointExporter去管理所有的WebSocket接口。

  ```java
  /**
   * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
   * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
   */
  @ServerEndpoint("/url")
  @Component
  @OnOpen
  public void onOpen(Session session)
  {/*建立链接后所要运行的方法*/}
  @OnClose
  public void onClose(){
      /*断开链接后进行的逻辑*/
  }
  @OnMessage
  public void onMessage(){
      /*客户端向服务端发送消息后做的逻辑*/
  }
  @OnError/*发生错误/异常中断链接后的 逻辑*/
  ```

- 示例项目中利用Tomcat加载Servelt的机制，通过继承HttpServlet重写init方法的方法使一部分功能代码在Tomcat启动时就运行，并新建一个线程运行。代码如下

  ```java
  @WebServlet(name="BitCoinDataCenter",urlPatterns = "/BitCoinDataCenter",loadOnStartup=1) //标记为Servlet不是为了其被访问，而是为了便于伴随Tomcat一起启动
  public class BitCoinDataCenter extends HttpServlet implements Runnable{
  
  	public void init(ServletConfig config){
  		startup();
  	}
  	
  	public  void startup(){
  		new Thread(this).start();
  	}
  ```

  可是实际上并不支持，SpringBoot用此方法不可行，只能通过实现CommandLineRunner来实现相同的方法，该方法会在所有的Bean创建完成后执行，并且能获取到配置文件中的配置的属性。

  ```java
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
  ```

- 前端通过指定href url ws://localhost:port/ServerEndpoint 来访问ws接口

  ```javascript
  webSocket = new WebSocket("ws://localhost:1234/BitCoin");
  webSocket.onopen = function () {
              webSocket.send("客户端连接成功");
          }
          webSocket.onmessage= function (event) {
              setInnerHTML(event.data);
          }
          webSocket.onerror = function () {
              alert("WebSocket链接发生错误");
          }
          webSocket.onclose=function () {
              console.log("WebSocket连接关闭");
          }
          window.onbeforeunload = function () {
              closeWebSocket();
          }
  ```

  ```javascript
  /*
  	通过JavaScript中的方法来解析，串化JSON。
  */
  var parse = JSON.parse(innerHTML);
  var json = JSON.stringify(string);
  ```

  

