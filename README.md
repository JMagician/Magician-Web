<h1> 
    <a href="https://magician-io.com">Magician-Web</a> ·
    <img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/jdk-11+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>
</h1>

Magician-Web 是Magician的官方 web组件，可以很方便的管理Controller，支持拦截器，会话管理，注解式参数校验，实体类接收参数等

## 文档

这个版本的文档还没出，jar也暂时还没传到中央库，尽请期待，不过可以自己编译，跟着示例玩一下试试
[https://magician-io.com](https://magician-io.com)

## 示例

### 导入依赖

```xml
<!-- This is the jar package build by this project -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician-Web</artifactId>
    <version>last version</version>
</dependency>

<!-- This is Magician -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician</artifactId>
    <version>last version</version>
</dependency>

<!-- This is the log package, which supports any package that can be bridged with slf4j -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>1.7.12</version>
</dependency>
```

### 创建核心handler

```java
@HttpHandler(path="/")
public class DemoHandler implements HttpBaseHandler {

    @Override
    public void request(MagicianRequest magicianRequest, MagicianResponse response) {
        try{
            // 主要是这句
            MagicianWeb.createWeb()
                    .request(magicianRequest);
        } catch (Exception e){
        }
    }
}
```

### 创建 Controller

```java
@Route("/demoController")
public class DemoController {

	// You can use entity classes to receive parameters
	@Route(value = "/demo", requestMethod = ReqMethod.POST)
	public DemoVO demo(DemoVO demoVO){
		return demoVO;
	}

	// You can also directly use MagicianRequest to get parameters
	@Route(value = "/demob", requestMethod = ReqMethod.POST)
	public String demob(MagicianRequest request){
		return "ok";
	}

	// Download file
	@Route(value = "/demob", requestMethod = ReqMethod.POST)
	public ResponseInputStream demob(){
		ResponseInputStream responseInputStream = new ResponseInputStream();
		responseInputStream.setName("file name");
		responseInputStream.setBytes(file bytes);
		return responseInputStream;
	}
}
```


### 启动 HTTP 服务

```java
Magician.createHttp()
                .scan("handler和controller所在的包名")
                .bind(8080);
```