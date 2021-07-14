<br/>

<div align=center>
<img width="260px;" src="http://magician-io.com/img/logo-white.png"/>
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
Magician's official web component
</div>


## Introduction

Magician-Web is the official Web component of Magician, which implements the interface and response of parameters in the way of Controller

## installation steps

### 1. import dependencies

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

### 2. CreateHandler

```java
@TCPHandler(path = "/")
public class MagicianWebHandler implements TCPBaseHandler<MagicianRequest> {

    @Override
    public void request(MagicianRequest magicianRequest) {
        try{
            MagicianWeb.createWeb()
                    .request(magicianRequest);
        } catch (Exception e){
        }
    }
}
```

### 3. Create Controller

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
		responseInputStream.setInputStream(file Stream);
		return responseInputStream;
	}
}
```

If the controller return is not a file stream, it will be converted to Json and returned, otherwise it will be processed as a file download

### 4. Create HTTP Server

```java
Magician.createTCPServer()
                .scan("The package name of the handler")
                .bind(8080);
```

## In addition, the following functions are also implemented

1. Custom Interceptor
2. Annotated parameter verification
3. Comes with JWT management class

## Documentation and examples
- Document: [http://magician-io.com/docs/en/web/index.html](http://magician-io.com/docs/en/web/index.html)
- Example: [https://github.com/yuyenews/Magician-Web-Example](https://github.com/yuyenews/Magician-Web-Example)