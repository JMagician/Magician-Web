<br/>

<div align=center>
<img width="260px;" src="http://magician-io.com/img/logo-black.png"/>
</div>

<br/>

<div align=center>

<img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
<img src="https://img.shields.io/badge/jdk-11+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>

</div>
<br/>

<div align=center>
Magician的官方Web组件
</div>


## 项目简介

Magician-Web 是 Magician的官方Web组件，实现了以Controller的方式来进行参数的接收和响应

## 安装步骤

### 一、导入依赖

```xml
<!-- 这个是本项目打的jar包 -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician-Web</artifactId>
    <version>最新版</version>
</dependency>

<!-- 这个是Magician，一个网络编程包，属于项目核心 -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician</artifactId>
    <version>最新版</version>
</dependency>

<!-- 这个是日志包，支持任意可以跟slf4j桥接的包 -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>1.7.12</version>
</dependency>
```
### 二、创建Controller
```java
@Route("/demoController")
public class DemoController {

    @Route(value = "/demo", requestMethod = ReqMethod.POST)
    public DemoVO demo(DemoVO demoVO){
        return demoVO;
    }
}
```

### 三、创建服务
```java
Magician.createHttpServer().httpHandler("/", req -> {

                        MagicianWeb.createWeb()
                                .scan(传入Controller所在的包名)
                                .request(req);

                    }).bind(8080).start();
```
## 除此之外还实现了以下功能

1. 自定义拦截器
2. 注解式参数校验
3. 自带JWT管理类

## 开发资源
- 开发文档: [http://magician-io.com/docs/web/index.html](http://magician-io.com/docs/web/index.html)
- 使用示例: [https://github.com/yuyenews/Magician-Web-Example](https://github.com/yuyenews/Magician-Web-Example)